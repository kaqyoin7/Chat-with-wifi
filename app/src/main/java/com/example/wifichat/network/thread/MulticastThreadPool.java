package com.example.wifichat.network.thread;

import com.example.wifichat.api.ChatRecordsApi;
import com.example.wifichat.network.multicast.MulticastReceiver;
import com.example.wifichat.network.multicast.MulticastSender;
import com.example.wifichat.constant.NetMessageUtil;
import com.example.wifichat.service.LocalStorageService;
import com.example.wifichat.service.impl.LocalStorageServiceImpl;
import com.example.wifichat.util.ContextHolderUtil;
import com.example.wifichat.util.GeneralUtil;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class MulticastThreadPool {

    private static ExecutorService executorService;
    private static Logger logger = Logger.getLogger("MulticastThreadPool");
    private static LocalStorageService localStorageService = LocalStorageServiceImpl.getInstance(ContextHolderUtil.getContext());
    private static ChatRecordsApi chatRecordsApi = new ChatRecordsApi(ContextHolderUtil.getContext());
    // 初始化线程池
    public static void init() {
        executorService = Executors.newCachedThreadPool();
    }

    // 接收组播消息
    public static void submitReceiverTask(final MulticastReceiver receiver) {
        if (executorService == null) {
            init();
        }
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                //TODO：回调过滤本地IP
                receiver.receiveMulticastMessage(new MulticastReceiver.MessageCallback() {
                    @Override
                    public void onMessageReceived(Map<String, String> message) {

                        if (message.get(NetMessageUtil.IP).equals(GeneralUtil.getLocalIpAddress())){
                            logger.info("Received from self, ignore");
                        }else {

                            storeFriendInfo(message);
                            System.out.println("Received from multicast: " + message);
                            handleOnAndOffLine(message);
                        }
                        //本地调试，线上环境需要使用上述IP过滤方案
//                            System.out.println("Received from multicast: " + message);
//                            handleOnAndOffLine(message);
                    }
                });

            }
        });
    }


    // 发送组播消息
    public static void submitSenderTask(final MulticastSender sender, final String message) {
        if (executorService == null) {
            init();
        }
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                sender.sendMulticastMessage(message);
            }
        });
    }

    // 关闭线程池
    public static void shutdown() {
        if (executorService != null) {
            executorService.shutdown();
        }
    }

    private static void handleOnAndOffLine(Map<String,String> message) {
        String ip = message.get(NetMessageUtil.IP);

        if (message.get(NetMessageUtil.IS_ONLINE).equals(NetMessageUtil.SIG_ONLINE)) {

            SocketThread.startClient(message.get(NetMessageUtil.USER_ID), ip, NetMessageUtil.SERVER_PORT);
            System.out.println("收到上线通知");

        } else if (message.get(NetMessageUtil.IS_ONLINE).equals(NetMessageUtil.SIG_OFFLINE)) {

            System.out.println("收到下线通知");
        }
    }

    /**
     * 本地存储好友信息
     * @param message:ip-port-userId-userName-message
     */
    private static void storeFriendInfo(Map<String,String> message){
        String friendId = message.get(NetMessageUtil.USER_ID);
        String friendName = message.get(NetMessageUtil.USER_NAME);
        String friendIp = message.get(NetMessageUtil.IP);
        if (localStorageService.readFileInternalStorage(friendId) == null){
            logger.info("Received from friend, ignore");
            String greeting = message.get(NetMessageUtil.USER_NAME) + "： Heh low, I'm " + message.get(NetMessageUtil.USER_NAME + " gooood to see you");
            //本地存储首次连接欢迎消息
            chatRecordsApi.appendChatRecord(friendId,greeting);

            localStorageService.appendFileInternalStorage(NetMessageUtil.FRIEND_LIST,friendIp + "!-!" + friendId + "!@!" + friendName + "!~!");
        }

        //FIXME 尝试补充若检测到同ID，IP不同的用户上线，更新IP----应该需要用到getFriendList()


    }
}

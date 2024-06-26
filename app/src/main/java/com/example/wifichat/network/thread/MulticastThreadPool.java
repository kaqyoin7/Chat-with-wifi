package com.example.wifichat.network.thread;

import com.example.wifichat.network.multicast.MulticastReceiver;
import com.example.wifichat.network.multicast.MulticastSender;
import com.example.wifichat.constant.NetMessageUtil;
import com.example.wifichat.util.GeneralUtil;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class MulticastThreadPool {

    private static ExecutorService executorService;

    private static Logger logger = Logger.getLogger("MulticastThreadPool");

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

                        //TODO：第二个判断条件为本地服务器启动地址

                        if (message.get(NetMessageUtil.IP).equals(GeneralUtil.getLocalIpAddress())){
                            logger.info("Received from self, ignore");
                        }else {
                            System.out.println("Received from multicast: " + message);
                            handleOnAndOffLine(message);
                        }
                        //本地调试，线上环境需要使用上述IP过滤方案
//                            System.out.println("Received from multicast: " + message);
//                            handleOnAndOffLine(message);
                    }
                });
                //FIXME: 考虑在此更新组播源上线状态
                //向组播源发送在线通知
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

            SocketThread.startClient(ip, NetMessageUtil.SERVER_PORT);
            System.out.println("收到上线通知");

        } else if (message.get(NetMessageUtil.IS_ONLINE).equals(NetMessageUtil.SIG_OFFLINE)) {

            //FIXME: 处理下线通知
            System.out.println("收到下线通知");
        }
    }
}

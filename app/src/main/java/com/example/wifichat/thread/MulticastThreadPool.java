package com.example.wifichat.thread;

import com.example.wifichat.multicast.MulticastReceiver;
import com.example.wifichat.multicast.MulticastSender;
import com.example.wifichat.consts.NetMessageUtil;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MulticastThreadPool {

    private static ExecutorService executorService;

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
                Map<String,String> message = receiver.receiveMulticastMessage();
                //FIXME: 在此处向组播源请求连接
                handleOnAndOffLine(message);
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
        int port = Integer.parseInt(message.get(NetMessageUtil.PORT));

        if (message.get("IS_ONLINE").equals(NetMessageUtil.SIG_ONLINE)) {

            SocketThread.startClient(ip,port);
            System.out.println("收到上线通知");


        } else if (message.get("IS_ONLINE").equals(NetMessageUtil.SIG_OFFLINE)) {

            //FIXME: 处理下线通知

            System.out.println("收到下线通知");
        }
    }
}

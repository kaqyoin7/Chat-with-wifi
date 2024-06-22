package com.example.wifichat.thread;

import com.example.wifichat.multicast.MulticastReceiver;
import com.example.wifichat.multicast.MulticastSender;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MulticastThreadPool {

    private static ExecutorService executorService;

    // 初始化线程池
    public static void init() {
        executorService = Executors.newCachedThreadPool();
    }

    // 提交接收组播消息任务给线程池执行
    public static void submitReceiverTask(final MulticastReceiver receiver) {
        if (executorService == null) {
            init();
        }
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                receiver.receiveMulticastMessage();
            }
        });
    }

    // 提交发送组播消息任务给线程池执行
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
}

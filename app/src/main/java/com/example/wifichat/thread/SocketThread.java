package com.example.wifichat.thread;


import com.example.wifichat.network.Client;
import com.example.wifichat.network.Server;

/**
 * @Author kaqyoin
 * @program: Multicast
 * @description:
 * @Date 2024-6-22 20:11
 */
public class SocketThread {

    public static void startServer(int port) {
        new Thread(() -> {
            Server server = new Server();
            server.startServer(port);
        }).start();
    }

    /**
     * 连接到Server，向Server端发送上线消息
     * @param ipAddress
     * @param port
     */
    public static void startClient(String ipAddress, int port) {
        new Thread(() -> {
            Client client = new Client();
            client.connectToServer(ipAddress, 8088); // 替换为你的服务器IP和端口号

        }).start();

    }
}

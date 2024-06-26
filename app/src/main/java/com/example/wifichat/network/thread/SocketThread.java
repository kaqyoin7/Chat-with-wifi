package com.example.wifichat.network.thread;


import com.example.wifichat.network.socket.Client;
import com.example.wifichat.network.socket.Server;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * @Author kaqyoin
 * @program: Multicast
 * @description:
 * @Date 2024-6-22 20:11
 */
public class SocketThread {

    private static Socket socket;
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
//            client.connectToServer(ipAddress, 8088);
            socket = client.connectToServer(ipAddress, port);
            if(socket == null) {
                System.err.println("Failed to connect to server at " + ipAddress + ":" + port);
            }
        }).start();
    }

    public static void sendToServer(String msg){
        new Thread(() -> {
            if (socket == null || socket.isClosed()) {
                System.err.println("Socket is not connected or already closed.");
                return;
            }
            try {
                 PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
                out.println(socket.getLocalSocketAddress()+" : "+ msg);
                System.out.println("send msg to server: "+ socket.getInetAddress() + ":" + socket.getPort() + " " +msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

    }
}

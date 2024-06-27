package com.example.wifichat.network.thread;


import com.example.wifichat.MainActivity;
import com.example.wifichat.api.ChatRecordsApi;
import com.example.wifichat.api.FriendsApi;
import com.example.wifichat.constant.NetMessageUtil;
import com.example.wifichat.network.socket.Client;
import com.example.wifichat.network.socket.Server;
import com.example.wifichat.service.LocalStorageService;
import com.example.wifichat.service.impl.LocalStorageServiceImpl;
import com.example.wifichat.util.ContextHolderUtil;
import com.example.wifichat.util.GeneralUtil;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;

/**
 * @Author kaqyoin
 * @program: Multicast
 * @description:
 * @Date 2024-6-22 20:11
 */
public class SocketThread {

    private static Socket socket;
    private static ChatRecordsApi chatRecordsApi = new ChatRecordsApi(ContextHolderUtil.getContext());
    private static FriendsApi friendsApi = new FriendsApi(ContextHolderUtil.getContext());
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
    public static void startClient(String userId, String ipAddress, int port) {
        new Thread(() -> {
            Client client = new Client();
            socket = client.connectToServer(ipAddress, port);
            if(socket == null) {
                System.err.println("Failed to connect to server at " + ipAddress + ":" + port);
            }
            //存储socket连接
            MainActivity.addToSocketMap(userId,socket);
        }).start();
    }

    public static void sendToServer(Socket serverSocket,String msg){
        new Thread(() -> {
            if ((serverSocket == null) || serverSocket.isClosed()) {
                System.err.println("Socket is not connected or already closed.");
                return;
            }
            try {

                PrintWriter out = new PrintWriter(new OutputStreamWriter(serverSocket.getOutputStream()), true);
                out.println(GeneralUtil.constructMessage(serverSocket, msg));

                //本地存储发送消息
                Map<String, String> map = friendsApi.getFriendInfoByIp(String.valueOf(serverSocket.getInetAddress()));
                String userId = map.get(NetMessageUtil.USER_ID);
                chatRecordsApi.appendChatRecord(userId, msg);

                System.out.println("send msg to server: "+ serverSocket.getInetAddress() + ":" + serverSocket.getPort() + " " +msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

    }



}

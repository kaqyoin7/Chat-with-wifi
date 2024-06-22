package com.example.wifichat.network;

import util.NetMsgUtil;

import java.io.*;
import java.net.Socket;
import java.util.logging.Logger;

/**
 * @Author kaqyoin
 * @program: Multicast
 * @description:
 * @Date 2024-6-20 16:41
 */
public class Client {

    private Logger logger = Logger.getLogger("Client");
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    /**
     * 连接到Server，向Server端发送上线消息
     * @param ipAddress 服务器IP地址
     * @param port 服务器端口
     */
    public void connectToServer(String ipAddress, int port) {
        try {
            // IP、PORT
            socket = new Socket(ipAddress, port);

            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));



            //FIXME: 连同发送自身ip端口

            // 连接日志
            printConnectLog(ipAddress, port);
            // 发送消息到服务器
//            out.println("Hello from client!");
            out.println(NetMsgUtil.SIG_ONLINE);

            // 接收服务器的响应
            String response = in.readLine();
            System.out.println("Server response: " + response);

            // 关闭连接
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void printConnectLog(String ipAddress, int port){

        logger.info("client connect on :" + socket.getLocalAddress() + ":" + socket.getLocalPort());
        logger.info("client connect to serve socket:" + ipAddress + ":" + port);
    }
}

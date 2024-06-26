package com.example.wifichat.network.socket;


import com.example.wifichat.constant.NetMessageUtil;

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
    public Socket connectToServer(String ipAddress, int port) {
        try {
            // IP、PORT
            socket = new Socket(ipAddress, port);

            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // 连接日志
            printConnectLog();
            // 发送消息到服务器
            out.println(socket.getLocalSocketAddress()+" is "+ NetMessageUtil.SIG_ONLINE);

            // 接收服务器的响应
            String response = in.readLine();
            System.out.println("Server response: " + response);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return socket;
    }

    private void printConnectLog(){
        logger.info("client connect on :" + socket.getLocalAddress() + ":" + socket.getLocalPort());
        logger.info("client connect to serve socket:" + socket.getInetAddress() + ":" + socket.getPort());

    }
}

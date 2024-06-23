package com.example.wifichat.network;


import com.example.wifichat.consts.NetMessageUtil;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

/**
 * @Author kaqyoin
 * @program: Multicast
 * @description:
 * @Date 2024-6-20 16:41
 */
public class Server {
    private ServerSocket serverSocket;

    private Logger logger = Logger.getLogger("Server");
    /**
     * 启动服务器
     * @param port 服务器端口
     */
    public void startServer(int port) {
        try {
            //PORT
            serverSocket = new ServerSocket(port);
            logger.info("Server started on port " + port);

            while (true) {
                //TODO: 阻塞，等待客户端连接
                Socket clientSocket = serverSocket.accept();
                logger.info("Accepted connection from " + clientSocket.getInetAddress()+":"+clientSocket.getPort());

                // 启动新线程处理客户端请求
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clientHandler.start();
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler extends Thread {
        private Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;

        private Logger logger = Logger.getLogger("ClientHandler");
        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try {
                out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                // 读取客户端发送的消息
                String clientMessage = in.readLine();

                if (clientMessage.equals(NetMessageUtil.SIG_ONLINE)) {
                    logger.info("Received online Message");
                    //FIXME: 更新用户状态，使用TOAST通知xxx上线，同时设置UI为在线状态
                }

                System.out.println("Received from client: " + clientMessage);

                // 发送响应给客户端
                out.println("Now I know you are online!");

                // 关闭连接
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //FIXME: 尝试在这里处理下线逻辑
    public void stopServer() {
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

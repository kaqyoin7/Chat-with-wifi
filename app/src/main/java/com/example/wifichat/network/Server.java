package com.example.wifichat.network;

import com.example.wifichat.consts.NetMessageUtil;
import com.example.wifichat.util.GeneralUtil;

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
     * 启动服务器，等待client连接
     * @param port 服务器端口
     */
    public void startServer(int port) {
        try {
            //PORT
            serverSocket = new ServerSocket(port);
            logger.info("Server started on  " + serverSocket.getLocalSocketAddress());

            while (true) {
                //TODO: 阻塞，等待客户端连接
                // 获取向server发起连接的clientSocket
                Socket clientSocket = serverSocket.accept();
                logger.info("Accepted connection from " + clientSocket.getInetAddress()+":"+clientSocket.getPort());

                // FIXME: 启动新线程处理客户端请求
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clientHandler.start();
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 子线程处理客户端请求
     */
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

                // 读取客户端发送的消息，阻塞
                String clientMessage = in.readLine();

                //Keep thread
                while (clientMessage != null) {
                    System.out.println("Received from client: " + clientMessage);
                    if (GeneralUtil.getIdentifier(clientMessage).equals(NetMessageUtil.SIG_ONLINE)) {
                        logger.info("Received online Message");
                        out.println("Now I know you are online!");

                        // FIXME: 更新用户状态，使用TOAST通知xxx上线，同时设置UI为在线状态
                    }
                    // 继续读取下一个消息
                    clientMessage = in.readLine();
                }


            } catch (IOException e) {
                //处理客户端断开连接异常
                //FIXME: 尝试在这里处理下线逻辑
                if (e instanceof java.net.SocketException && "Connection reset".equals(e.getMessage())) {
                    logger.warning("Client connection reset: " + clientSocket.getInetAddress() + ":" + clientSocket.getPort());
                } else {
                    e.printStackTrace();
                }
            } finally {
                try {
                    if (in != null) in.close();
                    if (out != null) out.close();
                    if (clientSocket != null && !clientSocket.isClosed()) clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
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

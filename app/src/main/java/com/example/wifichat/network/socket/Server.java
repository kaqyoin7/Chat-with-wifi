package com.example.wifichat.network.socket;

import com.example.wifichat.MainActivity;
import com.example.wifichat.api.ChatRecordsApi;
import com.example.wifichat.api.FriendsApi;
import com.example.wifichat.constant.NetMessageUtil;
import com.example.wifichat.service.LocalStorageService;
import com.example.wifichat.service.impl.LocalStorageServiceImpl;
import com.example.wifichat.util.ContextHolderUtil;
import com.example.wifichat.util.GeneralUtil;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
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
        private ChatRecordsApi chatRecordsApi = new ChatRecordsApi(ContextHolderUtil.getContext());
        private FriendsApi friendsApi = new FriendsApi(ContextHolderUtil.getContext());
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
                    //收到上线回复
                    if (GeneralUtil.getIdentifier(clientMessage).equals(NetMessageUtil.SIG_ONLINE)) {
                        logger.info("Received online Message");
                        out.println("Now I know you are online!");
                        // FIXME: 更新用户状态，使用TOAST通知xxx上线，同时设置UI为在线状态
                        //接收消息
                    } else{
                        Map<String, String> msgParsed = GeneralUtil.parseMessageUserInfo(clientMessage);
                        //本地存储接收消息
                        chatRecordsApi.appendChatRecord(msgParsed.get(NetMessageUtil.USER_ID),msgParsed.get(NetMessageUtil.IS_ONLINE));
                    }
                    clientMessage = in.readLine();
                }

            } catch (IOException e) {
                //处理客户端断开连接异常
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

                    //TODO 下线逻辑
                    Map<String,String> friendInfo = friendsApi.getFriendInfoByIp(String.valueOf(clientSocket.getInetAddress()));
//                    MainActivity.socketMap.remove(friendInfo.get(NetMessageUtil.USER_ID));
                    MainActivity.removeSocketMapChangeListener(friendInfo.get(NetMessageUtil.USER_ID));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

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

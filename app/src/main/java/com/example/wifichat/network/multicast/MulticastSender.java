package com.example.wifichat.network.multicast;

import com.example.wifichat.constant.NetMessageUtil;
import com.example.wifichat.service.LocalStorageService;
import com.example.wifichat.service.impl.LocalStorageServiceImpl;
import com.example.wifichat.util.ContextHolderUtil;
import com.example.wifichat.util.GeneralUtil;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.logging.Logger;

/**
 * @Author kaqyoin
 * @program: MulticastTest
 * @description:
 * @Date 2024-6-21 11:10
 */
public class MulticastSender {

    //多播组地址
    private static final String MULTICAST_GROUP = "239.0.0.1";
    // 多播端口
    private static final int PORT = 5000;
    private Logger logger = Logger.getLogger("MulticastSender");
    private static LocalStorageService localStorageService = LocalStorageServiceImpl.getInstance(ContextHolderUtil.getContext());
    public void sendMulticastMessage(String message) {
        try {
            //获取多播组地址
            InetAddress group = InetAddress.getByName(MULTICAST_GROUP);
            //多播Socket
            MulticastSocket socket = new MulticastSocket();

            // 获取本机IP
            String localIp = GeneralUtil.getLocalIpAddress();
            if (localIp == null) {
                logger.warning("Failed to get local IP address");
            }
            String userName = localStorageService.readFileInternalStorage(NetMessageUtil.USER_NAME);
            String userID = localStorageService.readFileInternalStorage(NetMessageUtil.USER_ID);

//            String messageSocket = localIp + "-" + socket.getLocalPort()+ "-" + message;
            String messageSocket = localIp + "-" + socket.getLocalPort()+ "-" + userID + "-" + userName + "-" + message;

            // 将消息转换为字节数组
            byte[] msg = messageSocket.getBytes();
            DatagramPacket packet = new DatagramPacket(msg, msg.length, group,PORT);
            //发送数据包到多播组
            socket.send(packet);
//            logger.info("message send success");

            //关闭Socket
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package com.example.wifichat.multicast;

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

            String messageSocket = localIp + "-" + PORT+ "-" + message;
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

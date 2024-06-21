package com.example.wifichat.multicast;

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

            // 将消息转换为字节数组
            byte[] msg = message.getBytes();
            // 创建数据报文包 -> 数据、数据长、组播地址、端口
            DatagramPacket packet = new DatagramPacket(msg, msg.length, group,PORT);
            //发送数据包到多播组
            socket.send(packet);
            logger.info("message send success");
            logger.info("Sent multicast message: " + message);

            //关闭Socket
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

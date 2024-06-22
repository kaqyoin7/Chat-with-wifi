package com.example.wifichat.multicast;

import com.example.wifichat.util.GeneralUtil;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Map;
import java.util.logging.Logger;

/**
 * @Author kaqyoin
 * @program: MulticastTest
 * @description:
 * @Date 2024-6-21 11:10
 */
public class MulticastReceiver {
    //多播组地址
    private static final String MULTICAST_GROUP = "239.0.0.1";
    //多播端口
    private static int PORT = 5000;
    private Logger logger = Logger.getLogger("MulticastReceiver");

    /**
     * 接收多播消息，返回多播源ip-port-isOnline
     */
    public Map<String,String> receiveMulticastMessage() {
        MulticastSocket socket = null;

        try {
            // 获取多播组的地址
            InetAddress group = InetAddress.getByName(MULTICAST_GROUP);
            //绑定端口
            socket = new MulticastSocket(PORT);
            // 加入多播组 -> 组播地址、端口
            socket.joinGroup(group);
            logger.info("Joined multicast group: " + MULTICAST_GROUP);
            // 创建缓冲区用于存储接收的数据
            byte[] buffer = new byte[256];
            // 创建数据报文包
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

            while (true) {
                // 接收数据包
                socket.receive(packet);
                // 将数据包内容转换为字符串
                String message = new String(packet.getData(), 0, packet.getLength());

                //ip-port-isOnline
                Map<String,String> msgParsed = GeneralUtil.parseMessage(message);
                // 打印接收到的消息
                System.out.println("Received: " + message);
                return msgParsed;
            }


        } catch (Exception e) {
            e.printStackTrace();  // 打印异常堆栈信息
            return null;
        } finally {
            if (socket != null) {
                try {
                    // 离开多播组
                    InetAddress group = InetAddress.getByName(MULTICAST_GROUP);
                    socket.leaveGroup(group);
                    // 关闭多播套接字
                    socket.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

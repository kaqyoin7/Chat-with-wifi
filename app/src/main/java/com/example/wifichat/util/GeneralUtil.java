package com.example.wifichat.util;

import com.example.wifichat.constant.NetMessageUtil;
import com.example.wifichat.service.LocalStorageService;
import com.example.wifichat.service.impl.LocalStorageServiceImpl;

import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.Socket;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author kaqyoin
 * @program: Multicast
 * @description:
 * @Date 2024-6-22 14:58
 */
public class GeneralUtil {

    private static LocalStorageService localStorageService = LocalStorageServiceImpl.getInstance(ContextHolderUtil.getContext());

    // 获取本机IP地址的方法
    public static String getLocalIpAddress() {
        try {
            //获取所有网络接口
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            // 遍历网络接口
            while (interfaces.hasMoreElements()) {
                NetworkInterface iface = interfaces.nextElement();
                // 过滤掉回环接口、虚拟接口及未启用的网络接口
                if (iface.isLoopback() || !iface.isUp()) {
                    continue;
                }

                // 获取该网络接口的所有IP地址
                Enumeration<InetAddress> addresses = iface.getInetAddresses();

                while (addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();
                    if (addr.isSiteLocalAddress()) {
                        return addr.getHostAddress();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 获取一个可用的本地端口
    public static int getAvailablePort() {
        try (MulticastSocket socket = new MulticastSocket(0)) {
            return socket.getLocalPort();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }


    /**
     * 消息格式：ip-port-msg_content
     * @param message
     * @return
     */
    public static Map<String,String> parseMessage(String message){
        // 分割字符串
        String[] parts = message.split("-");

        // 确保分割后的数组有三个部分
        if (parts.length == 3) {
            // 创建Map并将字段存入Map
            Map<String, String> map = new HashMap<>();
            map.put(NetMessageUtil.IP, parts[0]);
            map.put(NetMessageUtil.PORT, parts[1]);
            map.put(NetMessageUtil.IS_ONLINE, parts[2]);

            return map;
        } else {
            System.out.println("Message format is incorrect.");
            return null;
        }
    }


    /**
     * 消息格式：ip-port-msg_content
     * @param message
     * @return
     */
    public static Map<String,String> parseMessageUserInfo(String message){
        // 分割字符串
        String[] parts = message.split("-");

        // 确保分割后的数组有三个部分
        if (parts.length == 5) {
            // 创建Map并将字段存入Map
            Map<String, String> map = new HashMap<>();
            map.put(NetMessageUtil.IP, parts[0]);
            map.put(NetMessageUtil.PORT, parts[1]);
            map.put(NetMessageUtil.USER_ID, parts[2]);
            map.put(NetMessageUtil.USER_NAME, parts[3]);
            map.put(NetMessageUtil.IS_ONLINE, parts[4]);

            return map;
        } else {
            System.out.println("Message format is incorrect.");
            return null;
        }
    }


    public static String getIdentifier(String input) {
        if (input == null || input.isEmpty()) {
            return "";
        }
        int lastSpaceIndex = input.lastIndexOf(' ');
        if (lastSpaceIndex == -1) {
            return input;
        }
        return input.substring(lastSpaceIndex + 1);
    }

    //生成用户ID
    public static String generateUserId() {
        long lastTimestamp = 0L;
        Lock lock = new ReentrantLock();
        lock.lock();
        try {
            long currentTimestamp = System.currentTimeMillis();
            // 如果当前时间戳与上次生成的时间戳相同，需要在时间戳后面加上一个随机数，保证唯一性
            if (currentTimestamp == lastTimestamp) {
                currentTimestamp += 1; // 这里可以根据具体需求进行调整
            }
            lastTimestamp = currentTimestamp;
            return String.valueOf(currentTimestamp);
        } finally {
            lock.unlock();
        }
    }

    public static String constructMessage(Socket socket, String msg){
        String ip = String.valueOf(socket.getLocalAddress());
        String port = String.valueOf(socket.getLocalPort());
        String userId = localStorageService.readFileInternalStorage(NetMessageUtil.USER_ID);
        String userName = localStorageService.readFileInternalStorage(NetMessageUtil.USER_NAME);
        String message = ip + "-" + port + "-" + userId + "-" + userName + "-" + msg;
        return message;
    }


}

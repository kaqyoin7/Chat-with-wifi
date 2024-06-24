package com.example.wifichat.util;

import android.content.Context;

import com.example.wifichat.consts.NetMessageUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author kaqyoin
 * @program: Multicast
 * @description:
 * @Date 2024-6-22 14:58
 */
public class GeneralUtil {

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



}

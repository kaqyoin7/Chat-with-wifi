package com.example.wifichat.service;

import java.net.Socket;
import java.util.Map;

public interface SocketMapChangeListener {
    void onSocketMapChanged(Map<String, Socket> socketMap);
}
package com.example.wifichat.api;

import android.content.Context;

import com.example.wifichat.service.LocalStorageService;
import com.example.wifichat.service.impl.LocalStorageServiceImpl;

import java.util.Arrays;
import java.util.List;

public class ChatRecordsApi {

    private LocalStorageService localStorageService;

    public ChatRecordsApi(Context context){
        this.localStorageService = LocalStorageServiceImpl.getInstance(context);
    }

    /**
     * 获取历史聊天记录
     * @param friendId
     */
    public List<String> getChatHistories(String friendId){
        String content = localStorageService.readFileInternalStorage(friendId);
        String[] chatHistories = content.split("!~!");
        return Arrays.asList(chatHistories);
    }

    /**
     * 追加聊天记录
     * @param friendId
     * @param content
     */
    public void appendChatRecord(String friendId, String content){
        localStorageService.appendFileInternalStorage(friendId, content + "!~!");
    }

}

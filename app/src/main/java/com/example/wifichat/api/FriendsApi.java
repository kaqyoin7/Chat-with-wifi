package com.example.wifichat.api;

import static com.example.wifichat.constant.NetMessageUtil.FRIEND_LIST;

import android.content.Context;

import com.example.wifichat.constant.NetMessageUtil;
import com.example.wifichat.service.LocalStorageService;
import com.example.wifichat.service.impl.LocalStorageServiceImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FriendsApi {
    private LocalStorageService localStorageService;

    public FriendsApi(Context context){
        this.localStorageService = LocalStorageServiceImpl.getInstance(context);
    }

    /**
     * Map-userId, userName
     * @return
     */
    public Map<String,String> getFriendInfoByIp(String friendIp) {
        String content = localStorageService.readFileInternalStorage(FRIEND_LIST);
        if (content == null || content.isEmpty()) {
            return null;
        }
        // 按 !~! 分割字符串
        String[] friendsListArray = content.split("!~!");
        Map<String,Map<String,String>> ipMap = new HashMap<>();
        for (String friendInfo : friendsListArray) {
            if (!friendInfo.isEmpty()) {
                // 按 !@! 分割每个朋友信息
                String[] infoParts = friendInfo.split("!-!");
                String ip = infoParts[0];
                String id_name = infoParts[1];

                String[] id_name_parts = id_name.split("!@!");
                String userId = id_name_parts[0];
                String userName = id_name_parts[1];
                Map<String, String> friendMap = new HashMap<>();
                friendMap.put(NetMessageUtil.USER_ID, userId);
                friendMap.put(NetMessageUtil.USER_NAME, userName);
                ipMap.put(ip, friendMap);

            }
        }
        return ipMap.get(friendIp);
    }


    /**
     * map-userId, userName
     * @return
     */
    public List<Map<String,String>> getFriendsIdList() {

        String content = localStorageService.readFileInternalStorage(FRIEND_LIST);
        if (content == null || content.isEmpty()) {
            return null;
        }
        List<Map<String,String>> friendsList = new ArrayList<>();
        String[] friendsListArray = content.split("!~!");
        for (String friendInfo : friendsListArray) {
            if (!friendInfo.isEmpty()) {
                String[] infoParts = friendInfo.split("!-!");
                String id_name = infoParts[1];

                String[] id_name_parts = id_name.split("!@!");
                String userId = id_name_parts[0];
                String userName = id_name_parts[1];

                Map<String,String> friendMap = new HashMap<>();
                friendMap.put(NetMessageUtil.USER_ID, userId);
                friendMap.put(NetMessageUtil.USER_NAME, userName);

                friendsList.add(friendMap);
            }
        }
        return friendsList;

    }
}

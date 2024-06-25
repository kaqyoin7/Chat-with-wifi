package com.example.wifichat.api;

import static com.example.wifichat.constant.NetMessageUtil.FRIEND_LIST;

import android.content.Context;

import com.example.wifichat.service.LocalStorageService;
import com.example.wifichat.service.impl.LocalStorageServiceImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FriendsApi {
    private LocalStorageService localStorageService;

    public FriendsApi(Context context){
        this.localStorageService = LocalStorageServiceImpl.getInstance(context);
    }

//    /**
//     * 返回用户列表ID
//     * @param
//     */
//    public List<String> getFriendsIdList(){
//       String content = localStorageService.readFileInternalStorage(FRIEND_LIST);
//       String[] friendsIdList = content.split("\\^");
//       return Arrays.asList(friendsIdList);
//    }


}

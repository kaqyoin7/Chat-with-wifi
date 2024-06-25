package com.example.wifichat.api;

import static com.example.wifichat.constant.NetMessageUtil.USER_ID;
import static com.example.wifichat.constant.NetMessageUtil.USER_NAME;

import android.content.Context;

import com.example.wifichat.constant.NetMessageUtil;
import com.example.wifichat.service.LocalStorageService;
import com.example.wifichat.service.impl.LocalStorageServiceImpl;
import com.example.wifichat.util.GeneralUtil;

import java.util.HashMap;
import java.util.Map;

public class UserApi {
    private LocalStorageService localStorageService;

    public UserApi(Context context){
        this.localStorageService = LocalStorageServiceImpl.getInstance(context);
    }

    /**
     * 获取用户名、id；若不存在则创建用户
     * @param userName
     * @return {"user_name": {#userName} ,"user_id": {#userId}}
     */
    public Map<String, String> getUser(String userName) {
        // 读取用户名
        String storedUserName = localStorageService.readFileInternalStorage(USER_NAME);
        // 读取用户ID
        String storedUserId = localStorageService.readFileInternalStorage(USER_ID);

        Map<String, String> userInfo = new HashMap<>();

        if (storedUserName != null && storedUserId != null) {
            userInfo.put(USER_NAME, storedUserName);
            userInfo.put(USER_ID, storedUserId);
        } else {
            // 重新生成 userId 和 userName 文件
            String newUserId = GeneralUtil.generateUserId();
            localStorageService.overwriteFileInternalStorage(USER_NAME,userName);
            //TODO: ^作为读friend_list时的分割符
//            localStorageService.overwriteFileInternalStorage(USER_ID,newUserId + "^");
            localStorageService.overwriteFileInternalStorage(USER_ID,newUserId);

            userInfo.put(USER_NAME, userName);
            userInfo.put(USER_ID, newUserId);
        }

        return userInfo;
    }

    /**
     * 修改用户名
     * @param userName
     */
    public void updateUser(String userName){
        localStorageService.overwriteFileInternalStorage(USER_NAME,userName);
    }

}

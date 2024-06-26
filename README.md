#  :speech_balloon: WifiChat  



​	 ***:sparkles:***  项目使用 **FireBase** 实现持久化：

>  请确保在运行项目前您已注册服务并做好下列准备：[将 Firebase 添加到您的 Android 项目  | Firebase for Android (google.com)](https://firebase.google.com/docs/android/setup?hl=zh-cn#analytics-enabled)



##### 	<img src="https://i.gifer.com/ZAbi.gif" width="29" height="29" style="display:inline-block;" />Dependency:

```
implementation platform('com.google.firebase:firebase-bom:33.1.0')
implementation 'com.google.firebase:firebase-analytics'
implementation 'com.google.firebase:firebase-database'  
```



#### DS

```
     online:
        └─  root
             └─chatroom_test(**root_chatroom**)
                	└─ key(**root_msg**)
                    	└─ Msg
                    	└─Name 
                    	
​     local:
        └─  user_id
        └─  user_name
        └─  friend_id1
        └─  friend_id2
        └─  .....
        └─  friend_list
                └─  friend_id1
                        └─  {#user_name}:msg
                        └─  ......
                └─  friend_id1
                        └─  ......
                └─  ......
```



### FS

```
com
└─example
    └─wifichat
        │  MainActivity.java
        │
        ├─ adapter
        │      └─ FriendsAdapter.java
        │
        ├─ api
        │      └─ ChatRecordsApi.java
        │      └─ FriendsApi.java
        │      └─ UserApi.java
        │
        ├─ chatroom
        │      └─ ChatRoomGroup.java
        │      └─ ChatRoomPrivate.java
        │
        ├─ constant
        │      └─ NetMessageUtil.java
        │
        ├─ model
        │      └─ User.java
        │
        ├─ network
        │  ├─ multicast
        │  │      └─ MulticastReceiver.java
        │  │      └─ MulticastSender.java
        │  │
        │  ├─ socket
        │  │      └─ Client.java
        │  │      └─ Server.java
        │  │
        │  └─ thread
        │          └─ MulticastThreadPool.java
        │          └─ SocketThread.java
        │
        ├─ observer
        │      └─ FileChangeObserver.java
        │      └─ UserViewModel.java
        │
        ├─ service
        │  │  └─ LocalStorageService.java
        │  │  └─ SocketMapChangeListener.java
        │  │
        │  └─ impl
        │     └─ LocalStorageServiceImpl.java
        │
        └─ util
                └─ ContextHolderUtil.java
                └─ GeneralUtil.java


```



## Version Compatibility

| Gradle Version | Plugin Version |
| -------------- | -------------- |
| >= 8.0         | >= 8.11        |



🍃 kaquoin#163.com (# -> @)


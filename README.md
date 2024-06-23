#  :speech_balloon: WifiChat  

***:sparkles:持续更新中~***

WifiChat 为一个基于`FireBase`与`MulticastSocket`实现的`Android` Wifi 局域网聊天App

* FireBase：https://console.firebase.google.com/
* MulticastSocket：https://developer.android.com/reference/java/net/MulticastSocket



#### 上手指南

​	 :exclamation: 项目使用 **FireBase** 实现持久化：

>  请确保在运行项目前您已注册服务并做好下列准备：[将 Firebase 添加到您的 Android 项目  | Firebase for Android (google.com)](https://firebase.google.com/docs/android/setup?hl=zh-cn#analytics-enabled)

##### Dependency:

```
implementation platform('com.google.firebase:firebase-bom:33.1.0')
implementation 'com.google.firebase:firebase-analytics'
implementation 'com.google.firebase:firebase-database'  
```



#### 存储结构

```
└─root
​   └─ users
   	|   └─ userId
  	| 		└─ name
	|   	└─ profilePicUrl
	└─ friendships	
            └─ userId
             	 └─ userId(friend)
               			└─ secretkey
               				  └─ msg
                              └─ name
```



#### 文件目录说明

eg：

```
com
└─example
    └─wifichat
        ├─ ChatRoom.java
        ├─ MainActivity.java
        │
        ├─ multicast
        │   ├─ MulticastReceiver.java
        │   ├─ MulticastSender.java
        │
        ├─ network
        │   ├─ Client.java
        │   ├─ Server.java
        │
        ├─ thread
        │   ├─ MulticastThreadPool.java
        │   ├─ SocketThread.java
        │
        └─ util
            ├─ GeneralUtil.java
            └─ NetMsgUtil.java
```



##### Verison

```
Android Gradle Plugin Version: 8.1.1
Gradle Version: 8.0
```

#### 作者  

🍃 kaquoin#163.com (# -> @)


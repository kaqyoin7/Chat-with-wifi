package com.example.wifichat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.wifichat.adapter.FriendsAdapter;
import com.example.wifichat.api.FriendsApi;
import com.example.wifichat.api.UserApi;
import com.example.wifichat.chatroom.ChatRoomGroup;
import com.example.wifichat.constant.NetMessageUtil;
import com.example.wifichat.model.User;
import com.example.wifichat.observer.UserViewModel;
import com.example.wifichat.network.multicast.MulticastReceiver;
import com.example.wifichat.network.multicast.MulticastSender;
import com.example.wifichat.network.thread.MulticastThreadPool;
import com.example.wifichat.network.thread.SocketThread;
import com.example.wifichat.service.LocalStorageService;
import com.example.wifichat.service.SocketMapChangeListener;
import com.example.wifichat.service.impl.LocalStorageServiceImpl;
import com.example.wifichat.util.ContextHolderUtil;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;



public class MainActivity extends AppCompatActivity {

    private Button add_room;
    private EditText room_name;
    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> list_of_rooms = new ArrayList<>();
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot();
    private RecyclerView recyclerView;
    private ArrayList<User> usersData;
    private FriendsAdapter friendsAdapter;
    private UserViewModel userViewModel;
    private static SocketMapChangeListener socketMapChangeListener;
    //用户名
    private String name;
    private UserApi userApi;
    private LocalStorageService localStorageService;
    private FriendsApi friendsApi;
    public static Map<String, Socket> socketMap = new HashMap<>();
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar(findViewById(R.id.toolbar));


        userApi = new UserApi(this);
        localStorageService = LocalStorageServiceImpl.getInstance(this);
        friendsApi = new FriendsApi(this);
        ContextHolderUtil.init(this);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        usersData = new ArrayList<>();
        friendsAdapter = new FriendsAdapter(this, usersData);
        recyclerView.setAdapter(friendsAdapter);

        //监听数据
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.getUsers().observe(this, new Observer<ArrayList<User>>() {
            @Override
            public void onChanged(ArrayList<User> users) {
                friendsAdapter.setUsersData(users);
            }
        });

        setSocketMapChangeListener(new SocketMapChangeListener() {
            @Override
            public void onSocketMapChanged(Map<String, Socket> socketMap) {
                System.out.println("socketMap 发生变化：" + socketMap);

                initializeData();
            }
        });

        if (friendsApi.getFriendsIdList()!= null){
            initializeData();
        }


        add_room = (Button) findViewById(R.id.btn_add_room);
        room_name = (EditText) findViewById(R.id.room_name_edittext);
        listView = (ListView) findViewById(R.id.listview);

        arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list_of_rooms);
        listView.setAdapter(arrayAdapter);

        if (localStorageService.readFileInternalStorage(NetMessageUtil.USER_NAME) == null){
            requestUserName();
        }
        else {
            name = localStorageService.readFileInternalStorage(NetMessageUtil.USER_NAME);
        }

        groupChatEventHandler();

        onLineSimulation();
    }

    // 注册监听器方法
    public static void setSocketMapChangeListener(SocketMapChangeListener l) {
        socketMapChangeListener = l;
    }

    public static void removeSocketMapChangeListener(String userId) {
        socketMap.remove(userId);
        if (socketMapChangeListener != null) {
            socketMapChangeListener.onSocketMapChanged(socketMap);
        }
    }

    public static void addToSocketMap(String userId, Socket socket) {
        socketMap.put(userId, socket);
        if (socketMapChangeListener != null) {
            socketMapChangeListener.onSocketMapChanged(socketMap);
        }
    }

    //设备上线模拟
    private static void onLineSimulation() {
        //固定server启动端口
        SocketThread.startServer(NetMessageUtil.SERVER_PORT);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        MulticastThreadPool.submitReceiverTask(new MulticastReceiver());

        //上线通知
        MulticastThreadPool.submitSenderTask(new MulticastSender(), NetMessageUtil.SIG_ONLINE);
    }

    private void groupChatEventHandler() {

        //TODO：添加聊天室
        add_room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> map = new HashMap<>();
                map.put(room_name.getText().toString(),"");
                root.updateChildren(map);
                Log.i(TAG,room_name.getText()+" created!!!!!!!!");
            }
        });

        root.addValueEventListener(new ValueEventListener() {
            //初始数据加载、数据更新、子节点变化时调用
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Set<String> set = new HashSet<>();

                //TODO: 读一级聊天室名
                Iterator iterator = snapshot.getChildren().iterator();

                //聊天室名不可重复
                while(iterator.hasNext()){
                    set.add(((DataSnapshot)iterator.next()).getKey());
                }

                list_of_rooms.clear();
                list_of_rooms.addAll(set);

                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        //TODO: 跳转聊天室
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, ChatRoomGroup.class);
                intent.putExtra("room_name",((TextView)view).getText().toString());
                intent.putExtra("user_name",name);
                startActivity(intent);
            }
        });
    }


    private void initializeData() {
       List<Map<String,String>> friendsInfo = friendsApi.getFriendsIdList();
       if (friendsInfo == null) {
           return;
       }

        TypedArray userImageResources = getResources().obtainTypedArray(R.array.user_images);
       usersData.clear();

        for (Map<String, String> friendInfo : friendsInfo) {
            User user = new User(friendInfo.get(NetMessageUtil.USER_ID)
                    ,friendInfo.get(NetMessageUtil.USER_NAME)
                    , userImageResources.getResourceId((int) (Math.random() * userImageResources.length()), -1)
                    ,NetMessageUtil.SIG_ONLINE);

        usersData.add(user);
    }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                userViewModel.setUsers(usersData);
            }
        });}

    }

        /**
         * 输入用户名，考虑在此处分配ID
         */
    public void requestUserName(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter name:");

        EditText input_field = new EditText(this);
        builder.setView(input_field);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                name = input_field.getText().toString();
                //本地存储用户数据
                userApi.getUser(name);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
                requestUserName();
            }
        });

        builder.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        //noinspection SimplifiableIfStatement
        if (item.getItemId() == android.R.id.home) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



}
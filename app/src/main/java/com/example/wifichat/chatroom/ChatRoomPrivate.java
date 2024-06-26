package com.example.wifichat.chatroom;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.wifichat.MainActivity;
import com.example.wifichat.R;
import com.example.wifichat.api.ChatRecordsApi;
import com.example.wifichat.constant.NetMessageUtil;
import com.example.wifichat.network.thread.SocketThread;
import com.example.wifichat.observer.FileChangeObserver;
import com.example.wifichat.service.LocalStorageService;
import com.example.wifichat.service.impl.LocalStorageServiceImpl;

import java.net.Socket;
import java.util.List;

public class ChatRoomPrivate extends AppCompatActivity {

    private TextView textView;
    private Button btn_send_msg;
    private EditText input_msg;
    private static ChatRecordsApi chatRecordsApi;
    private FileChangeObserver fileChangeObserver;
    private static LocalStorageService localStorageService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room_private);
        setSupportActionBar(findViewById(R.id.toolbar_chat_room_private));
        setTitle(getIntent().getStringExtra(NetMessageUtil.USER_NAME));

        textView = findViewById(R.id.textView_private);
        btn_send_msg = findViewById(R.id.btn_send_private);
        input_msg = findViewById(R.id.msg_input_private);

        chatRecordsApi = new ChatRecordsApi(this);
        localStorageService = LocalStorageServiceImpl.getInstance(this);

        fileChangeObserver = new FileChangeObserver(getIntent().getStringExtra(NetMessageUtil.USER_ID), textView, getIntent().getStringExtra(NetMessageUtil.USER_ID));
        fileChangeObserver.startWatching();

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        //加载历史聊天记录
        List<String> chatRecords = chatRecordsApi.getChatHistories(getIntent().getStringExtra(NetMessageUtil.USER_ID));
        for (String chatRecord : chatRecords) {
            textView.append(chatRecord + "\n");
        }

        String userId = getIntent().getStringExtra(NetMessageUtil.USER_ID);
        Socket socket = MainActivity.socketMap.get(userId);
        btn_send_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = input_msg.getText().toString();
                SocketThread.sendToServer(socket, message);
                input_msg.setText("");
            }
        });


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //处理返回按钮事件，返回主屏幕
        if (item.getItemId() == android.R.id.home){
            //关闭当前Activity，返回上一个Activity
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
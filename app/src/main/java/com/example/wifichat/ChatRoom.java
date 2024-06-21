package com.example.wifichat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ChatRoom extends AppCompatActivity {

    private Button btn_send_msg;
    private EditText input_msg;
    private TextView chat_conversation;
    private String user_name,room_name;
    private String chat_msg,chat_user_name;
    private DatabaseReference root_chatroom;
    //TODO：密钥 -> 标明唯一消息与发送人
    private String temp_key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        setSupportActionBar(findViewById(R.id.toolbar_chat_room));
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        btn_send_msg = findViewById(R.id.btn_send);
        input_msg = findViewById(R.id.msg_input);
        chat_conversation = findViewById(R.id.textView);

        user_name = getIntent().getExtras().get("user_name").toString();
        room_name = getIntent().getExtras().get("room_name").toString();

        setTitle("Room - "+room_name);

        //聊天室名下一级数据
        root_chatroom = FirebaseDatabase.getInstance().getReference().child(room_name);

        //保存消息，生成唯一密钥
        btn_send_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String,Object> map = new HashMap<>();
                //TODO: 生成新子节点，生成返回密钥 -> 时间戳 -> 考虑依靠时间戳返回有序聊天记录
                temp_key = root_chatroom.push().getKey();
                root_chatroom.updateChildren(map);
                //消息数据
                DatabaseReference root_msg = root_chatroom.child(temp_key);

                Map<String,Object> mapMsg = new HashMap<>();
                mapMsg.put("name",user_name);
                mapMsg.put("msg",input_msg.getText().toString());

                root_msg.updateChildren(mapMsg);
            }
        });

        root_chatroom.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                append_chat_conversation(snapshot);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                append_chat_conversation(snapshot);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    /**
     * 显示发送消息到TextView
     * @param dataSnapshot
     */
    private void append_chat_conversation(DataSnapshot dataSnapshot){
        Iterator iterator = dataSnapshot.getChildren().iterator();
        while (iterator.hasNext()){
            //读 msg
            chat_msg = (String)((DataSnapshot)iterator.next()).getValue();
            //读 username
            chat_user_name = (String)((DataSnapshot)iterator.next()).getValue();

            chat_conversation.append(chat_user_name + ":" + chat_msg + "\n");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        //noinspection SimplifiableIfStatement
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
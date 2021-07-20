package com.example.chattogether;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.chattogether.Models.Message;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    String receiverName;
    String receiverUID;
    String userID;

    public static String userProfile;
    public static String receiverProfile;

    CircleImageView recProfile;
    TextView recName;
    EditText msgView;
    CardView SendMsgBtn;
    RecyclerView msgList;

    private String senderRoom;
    private String receiverRoom;


    private FirebaseAuth auth;
    private FirebaseDatabase database;

    List<Message> messageList;
    MessageAdapter messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        setInitialState();

        DatabaseReference databaseReference = database.getReference().child("user").child(auth.getUid());
        DatabaseReference chatReference =  database.getReference().child("chats").child(senderRoom)
                .child("messages");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        //linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        msgList.setLayoutManager(linearLayoutManager);
        messageAdapter = new MessageAdapter(ChatActivity.this,messageList);
        msgList.setAdapter(messageAdapter);

        msgList.smoothScrollToPosition(msgList.getBottom());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userProfile = snapshot.child("profileUri").getValue().toString();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        chatReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageList.clear();
                for(DataSnapshot snap:snapshot.getChildren())
                {
                    Message message = snap.getValue(Message.class);
                    messageList.add(message);
                }
                messageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        SendMsgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = msgView.getText().toString();
                if(msg.isEmpty())
                {
                    return;
                }
                msgView.setText("");
                Date date = new Date();

                Message message = new Message(userID,msg,date.getTime());
                database.getReference()
                        .child("chats")
                        .child(senderRoom)
                        .child("messages")
                        .push().setValue(message)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                database.getReference()
                                        .child("chats")
                                        .child(receiverRoom)
                                        .child("messages")
                                        .push().setValue(message);
                            }
                        });
            }
        });

    }

    private void setInitialState() {
        Intent intent = getIntent();
        receiverName = intent.getStringExtra(HomeActivity.CHAT_USER_NAME);
        receiverUID = intent.getStringExtra(HomeActivity.CHAT_USER_ID);
        receiverProfile = intent.getStringExtra(HomeActivity.CHAT_USER_PROFILE);

        recProfile = findViewById(R.id.recProfile);
        recName = findViewById(R.id.recName);
        msgView = findViewById(R.id.msgView);
        SendMsgBtn = findViewById(R.id.sendMsgBtn);
        msgList = findViewById(R.id.msgList);

        Picasso.get().load(receiverProfile).into(recProfile);
        recName.setText(""+receiverName);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance("https://chattogether-19397-default-rtdb.firebaseio.com/");
        userID = auth.getUid();

        senderRoom = userID+receiverUID;
        receiverRoom = receiverUID+userID;

        messageList = new ArrayList<>();
    }
}
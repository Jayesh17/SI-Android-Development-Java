package com.example.chattogether.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.chattogether.Adapters.MessageAdapter;
import com.example.chattogether.Models.Message;
import com.example.chattogether.NotificationManagement.FcmNotificationsSender;
import com.example.chattogether.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.pd.chocobar.ChocoBar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    String receiverName;
    String receiverUID;
    String userID;

    String userName;
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

    String senderToken;
    String receiverToken;

    public static final String USER_ID = "com.example.chattogether.Activities.ChatActivity.USERID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        setInitialState();

        DatabaseReference databaseReference = database.getReference().child("user").child(auth.getUid());
        DatabaseReference chatReference =  database.getReference().child("chats").child(senderRoom)
                .child("messages");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        //linearLayoutManager.setReverseLayout(true);
        msgList.setLayoutManager(linearLayoutManager);
        messageAdapter = new MessageAdapter(ChatActivity.this,messageList);
        msgList.setAdapter(messageAdapter);

        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (((LinearLayoutManager) msgList.getLayoutManager())
                        .findLastVisibleItemPosition() != messageAdapter.getItemCount() - 1) {
                    msgList.scrollToPosition(messageAdapter.getItemCount() - 1);
                    handler.postDelayed(this, 200);
                }
            }
        }, 200 /* change it if you want*/);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userName = snapshot.child("name").getValue().toString();
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
                    msgList.smoothScrollToPosition(msgList.getBottom());
                    //messageAdapter.notifyDataSetChanged();
                    //msgList.smoothScrollToPosition(msgList.getBottom());
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



                                FcmNotificationsSender notificationsSender = new FcmNotificationsSender(receiverToken,userName,message.getMessage(),getApplicationContext(),ChatActivity.this);

                                notificationsSender.SendNotifications();
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

        FirebaseMessaging.getInstance().subscribeToTopic("all");

        DatabaseReference tokenReference = database.getReference().child("tokens");
        tokenReference.child(auth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                senderToken =snapshot.getValue(String.class);
                Log.d("TOKENS",senderToken);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        tokenReference.child(receiverUID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                receiverToken = snapshot.getValue(String.class);
               // Log.d("TOKENS",receiverToken);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ChatActivity.this,HomeActivity.class);
        overridePendingTransition(R.anim.zoom_enter,R.anim.zoom_exit);
        startActivity(intent);
        finish();
    }
    public void clearChats(View v)
    {
        Dialog dialog = new Dialog(ChatActivity.this,R.style.clearChatDialog);
        dialog.setContentView(R.layout.layout_clear_chat_dialog);

        TextView yesBtn, noBtn;
        yesBtn = dialog.findViewById(R.id.clearChatYesBtn);
        noBtn = dialog.findViewById(R.id.clearChatNoBtn);

        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.d("USERCURRENT",auth.getCurrentUser()+"");
                DatabaseReference chat = database.getReference().child("chats").child(senderRoom);
                chat.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        ChocoBar.builder().setActivity(ChatActivity.this)
                                .setText("chat Deleted Successfully.")
                                .setDuration(ChocoBar.LENGTH_LONG)
                                .green()  // in built green ChocoBar
                                .show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        ChocoBar.builder().setActivity(ChatActivity.this)
                                .setText("something went wrong.")
                                .setDuration(ChocoBar.LENGTH_LONG)
                                .red()  // in built green ChocoBar
                                .show();
                    }
                });
                dialog.dismiss();
            }
        });

        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void viewProfile(View view)
    {
        Intent intent = new Intent(ChatActivity.this,userProfileActivity.class);
        intent.putExtra(USER_ID,receiverUID);
        startActivity(intent);
    }
}
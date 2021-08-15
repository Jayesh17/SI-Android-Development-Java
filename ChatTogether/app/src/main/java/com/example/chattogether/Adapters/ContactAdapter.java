package com.example.chattogether.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chattogether.Activities.AddFriendActivity;
import com.example.chattogether.Models.Contact;
import com.example.chattogether.NotificationManagement.FcmNotificationsSender;
import com.example.chattogether.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter {

    Context context;
    List<Contact> contactList;
    FirebaseAuth auth;
    FirebaseDatabase database;


    public ContactAdapter(Context context, List<Contact> contactList) {
        this.context = context;
        this.contactList = contactList;
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance("https://chattogether-19397-default-rtdb.firebaseio.com/");
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.add_friend_item,parent,false);

        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Contact contact = contactList.get(position);
        ContactViewHolder contactViewHolder = (ContactViewHolder) holder;
        contactViewHolder.contactName.setText(contact.getName());
        contactViewHolder.contactNumber.setText(contact.getPhone());
        contactViewHolder.addAsFriendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference databaseReference = database.getReference().child("FriendList").child(auth.getUid());
                databaseReference.child(auth.getUid()+contact.getUserID()).setValue(contact.getUserID()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {

                            database.getReference().child("FriendList").child(contact.getUserID()).child(contact.getUserID()+auth.getUid()).setValue(auth.getUid()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText(AddFriendActivity.context,"Friend Added Successfully",Toast.LENGTH_LONG).show();
                                        contactViewHolder.addAsFriendBtn.setClickable(false);
                                        contactViewHolder.addAsFriendBtn.setEnabled(false);

                                        database.getReference().child("tokens").child(contact.getUserID()).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                String token = snapshot.getValue(String.class);
                                                database.getReference().child("user").child(auth.getUid()).child("name").addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        String username = snapshot.getValue(String.class);
                                                        FcmNotificationsSender notificationsSender = new FcmNotificationsSender(token,username+" has added you as a friend.","Tap to chat!",AddFriendActivity.app_context,AddFriendActivity.act);
                                                        notificationsSender.SendNotifications();
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {
                                                        Toast.makeText(AddFriendActivity.context,"Something Went Wrong while adding friend.",Toast.LENGTH_LONG).show();
                                                    }
                                                });
                                            }
                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                                Toast.makeText(AddFriendActivity.context,"Something Went Wrong while adding friend.",Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    }
                                    else {
                                        Toast.makeText(AddFriendActivity.context,"Something Went Wrong while adding friend.",Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                        else {
                            Toast.makeText(AddFriendActivity.context,"Something Went Wrong while adding friend.",Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    class ContactViewHolder extends RecyclerView.ViewHolder {

        ImageView addAsFriendBtn;
        TextView contactName;
        TextView contactNumber;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);

            addAsFriendBtn = itemView.findViewById(R.id.addAsFriendBtn);
            contactName = itemView.findViewById(R.id.contactName);
            contactNumber = itemView.findViewById(R.id.contactNumber);
        }
    }
}

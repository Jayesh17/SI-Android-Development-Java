package com.example.chattogether;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chattogether.Models.Contact;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
                databaseReference.push().setValue(contact.getUserID()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(AddFriendActivity.context,"Friend Added Successfully",Toast.LENGTH_LONG).show();
                            contactViewHolder.addAsFriendBtn.setClickable(false);
                            contactViewHolder.addAsFriendBtn.setEnabled(false);
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

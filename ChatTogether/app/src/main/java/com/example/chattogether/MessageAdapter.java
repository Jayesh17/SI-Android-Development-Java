package com.example.chattogether;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chattogether.Models.Message;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter {
    @NonNull

    Context context;
    List<Message> messageList;
    int MSG_SEND=1;
    int MSG_RECEIVE=2;

    public MessageAdapter(@NonNull Context context, List<Message> messageList) {
        this.context = context;
        this.messageList = messageList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==MSG_SEND)
        {
            View view = LayoutInflater.from(context).inflate(R.layout.layout_sender_message_item,parent,false);
            return new SenderViewHolder(view);
        }
        else{
            View view = LayoutInflater.from(context).inflate(R.layout.layout_receiver_message_item,parent,false);
            return new ReceiverViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Message message = messageList.get(position);
        if(holder.getClass()==SenderViewHolder.class)
        {
            SenderViewHolder senderViewHolder = (SenderViewHolder) holder;
            senderViewHolder.msg.setText(message.getMessage());
        }
        else {
            ReceiverViewHolder receiverViewHolder = (ReceiverViewHolder) holder;
            receiverViewHolder.msg.setText(message.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    @Override
    public int getItemViewType(int position) {
            Message message = messageList.get(position);
            if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(message.getSenderID()))
            {
                return MSG_SEND;
            }
            else {
                return MSG_RECEIVE;
            }
    }

    class SenderViewHolder extends RecyclerView.ViewHolder{

        TextView msg;

        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            msg = itemView.findViewById(R.id.senderMSG);
        }
    }

    class ReceiverViewHolder extends RecyclerView.ViewHolder{

        TextView msg;

        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            msg = itemView.findViewById(R.id.receiverMSG);
        }
    }
}

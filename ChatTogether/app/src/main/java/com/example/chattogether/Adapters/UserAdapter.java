package com.example.chattogether.Adapters;

import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chattogether.Activities.ChatActivity;
import com.example.chattogether.Activities.HomeActivity;
import com.example.chattogether.Models.User;
import com.example.chattogether.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    HomeActivity homeActivity;
    List<User> users;

    public UserAdapter(HomeActivity homeActivity, List<User> users) {
        this.homeActivity = homeActivity;
        this.users=users;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(homeActivity).inflate(R.layout.chat_row_item,parent,false);

        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        //users.removeIf(user -> user.getUID()==FirebaseAuth.getInstance().getCurrentUser().getUid());
        User user = users.get(position);

            holder.userName.setText(user.getName());
            holder.userStatus.setText(user.getStatus());
            Picasso.get().load(user.getProfileUri()).into(holder.userProfile);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(homeActivity, ChatActivity.class);
                    intent.putExtra(homeActivity.CHAT_USER_NAME,user.getName());
                    intent.putExtra(homeActivity.CHAT_USER_PROFILE,user.getProfileUri());
                    intent.putExtra(homeActivity.CHAT_USER_ID,user.getUID());

                    homeActivity.startActivity(intent);
                    homeActivity.finish();
                }
            });
        }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CircleImageView userProfile;
        TextView userName;
        TextView userStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.userName);
            userStatus = itemView.findViewById(R.id.userStatus);
            userProfile = itemView.findViewById(R.id.userImage);

        }
    }
}

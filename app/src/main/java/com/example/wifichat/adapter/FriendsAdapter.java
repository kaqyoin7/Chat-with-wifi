package com.example.wifichat.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.wifichat.R;
import com.example.wifichat.chatroom.ChatRoomPrivate;
import com.example.wifichat.constant.NetMessageUtil;
import com.example.wifichat.model.User;

import java.util.ArrayList;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.ViewHolder>{
    // Member variables.
    private ArrayList<User> usersData;
    private Context mContext;
    /**
     * Constructor that passes in the users data and the context.
     *
     * @param usersData ArrayList containing the users data.
     * @param context Context of the application.
     */
    public FriendsAdapter(Context context, ArrayList<User> usersData) {
        this.usersData = usersData;
        this.mContext = context;
    }
    /**
     * Required method for creating the viewholder objects.
     *
     * @param parent The ViewGroup into which the new View will be added
     * after it is bound to an adapter position.
     * @param viewType The view type of the new View.
     * @return The newly created ViewHolder.
     */
    @Override
    public FriendsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).
                inflate(R.layout.list_item, parent, false));
    }
    /**
     * Required method that binds the data to the viewholder.
     *
     * @param holder The viewholder into which the data should be put.
     * @param position The adapter position.
     */
    @Override
    public void onBindViewHolder(FriendsAdapter.ViewHolder holder,
                                 int position) {
// Get current users.
        User currentUser = usersData.get(position);
// Populate the textviews with data.
        holder.bindTo(currentUser);
    }
    /**
     * Required method for determining the size of the data set.
     *
     * @return Size of the data set.
     */
    @Override
    public int getItemCount() {
        return usersData.size();
    }


    public void setUsersData(ArrayList<User> newUsersData){
        usersData = newUsersData;
        notifyDataSetChanged();
    }
    /**
     * ViewHolder class that represents each row of data in the RecyclerView.
     */
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // Member Variables for the TextViews
        private TextView nickNameText;
        private TextView isOnlineText;
        private ImageView picImage;
        /**
         * Constructor for the ViewHolder, used in onCreateViewHolder().
         *
         * @param itemView The rootview of the list_item.xml layout file.
         */
        ViewHolder(View itemView) {
            super(itemView);
            nickNameText = itemView.findViewById(R.id.nick_name);
            isOnlineText = itemView.findViewById(R.id.is_online);
            picImage = itemView.findViewById(R.id.pic);
            itemView.setOnClickListener(this);
        }
        void bindTo(User currentUser){
            nickNameText.setText(currentUser.getUserName());
            isOnlineText.setText(currentUser.getIsOnline());
            // Load the images into the ImageView using the Glide library.
            Glide.with(mContext).load(currentUser.getPicUrl()).into(picImage);
        }


        @Override
        public void onClick(View view) {
            User currentUser = usersData.get(getAdapterPosition());
            Intent detailIntent = new Intent(mContext, ChatRoomPrivate.class);
            detailIntent.putExtra(NetMessageUtil.USER_ID, currentUser.getUserId());
            detailIntent.putExtra(NetMessageUtil.PIC_URL, currentUser.getPicUrl());
//            detailIntent.putExtra(NetMessageUtil.USER_NAME, currentUser.getUserName());
            mContext.startActivity(detailIntent);
        }

    }
}

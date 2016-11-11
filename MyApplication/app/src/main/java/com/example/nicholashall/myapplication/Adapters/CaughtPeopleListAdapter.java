package com.example.nicholashall.myapplication.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.nicholashall.myapplication.Models.User;
import com.example.nicholashall.myapplication.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by nicholashall on 11/10/16.
 */

public class CaughtPeopleListAdapter extends RecyclerView.Adapter<CaughtPeopleListAdapter.UserHolder> {
    public ArrayList<User> users;
    private Context context;

    public CaughtPeopleListAdapter(ArrayList<User> users, Context context) {
        this.users = users;
        this.context = context;
    }


    @Override
    public UserHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflateView = LayoutInflater.from(context).inflate(R.layout.caught_people_item_view, parent, false);
        return new UserHolder(inflateView);
    }



    @Override
    public void onBindViewHolder(UserHolder holder, int position) {
            User user = users.get(position);
            holder.bindUser(user);
    }

    @Override
    public int getItemCount() {
        return users == null ? 0 : users.size();
    }


    class UserHolder extends RecyclerView.ViewHolder {


        @Bind(R.id.caught_username)
        TextView caughtUsername;

//        @Bind(R.id.caught_avatar)
//        ImageView caughtAvatar;


        public UserHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        //        Lets put our data in our UI
        public void bindUser(final User user){
            caughtUsername.setText(user.getUserName());
//            caughtAvatar.setText
        }

    }
}
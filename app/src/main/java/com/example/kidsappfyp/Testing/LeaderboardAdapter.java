package com.example.kidsappfyp.Testing;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.kidsappfyp.HelperClasses.UserHelperClass;
import com.example.kidsappfyp.R;
import com.example.kidsappfyp.databinding.RowLeaderboardsBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;

import java.util.ArrayList;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.LeaderboardViewHolder>{
    Context context;
    ArrayList<UserHelperClass> users;

    public LeaderboardAdapter(Context context, ArrayList<UserHelperClass> users) {
        this.context = context;
        this.users = users;
    }

    @NonNull
    @Override
    public LeaderboardAdapter.LeaderboardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_leaderboards, parent, false);
        return new LeaderboardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LeaderboardAdapter.LeaderboardViewHolder holder, int position) {
        UserHelperClass user = users.get(position);

        holder.binding.namePerson.setText(user.getUserName());
        holder.binding.coins.setText(String.valueOf(user.getScore()));
        holder.binding.index.setText(String.format("#%d", position+1));

        Glide.with(context)
                .load(user.getProfile())
                .placeholder(R.drawable.boy)
                .into(holder.binding.imageView7);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class LeaderboardViewHolder extends RecyclerView.ViewHolder{
        RowLeaderboardsBinding binding;
        public LeaderboardViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = RowLeaderboardsBinding.bind(itemView);
        }
    }
}

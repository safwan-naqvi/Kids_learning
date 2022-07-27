package com.example.kidsappfyp.Activities.VideoLearning;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.kidsappfyp.Constants.Constants;
import com.example.kidsappfyp.HelperClasses.ModelVideo;
import com.example.kidsappfyp.R;

import java.util.ArrayList;

public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.ViewHolder>{

    Context context;
    ArrayList<ModelVideo> arrOfVideoList;

    public VideoListAdapter(Context context, ArrayList<ModelVideo> arrOfVideoList) {
        this.context = context;
        this.arrOfVideoList = arrOfVideoList;
    }

    @NonNull
    @Override
    public VideoListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.card_item_video_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VideoListAdapter.ViewHolder holder, int position) {
        Glide.with(context)
                .load(arrOfVideoList.get(position).getVideoThumb())
                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
                .into(holder.imgThumbnail);

        holder.txtVideoTitle.setText(arrOfVideoList.get(position).getVideoTitle());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Constants.VIDEO_ID = arrOfVideoList.get(position).getVideoId();
                context.startActivity(new Intent(context, VideoPlayActivity.class).putExtra("Position",position).putExtra("ArrayOfVideo",arrOfVideoList));
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrOfVideoList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView imgThumbnail;
        TextView txtVideoTitle;

        public ViewHolder(@NonNull View view) {
            super(view);
            txtVideoTitle = view.findViewById(R.id.txtVideoDescription);
            cardView = view.findViewById(R.id.cardViewMain);
            imgThumbnail = view.findViewById(R.id.ivThumbnailView);
        }
    }
}

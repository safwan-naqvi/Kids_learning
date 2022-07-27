package com.example.kidsappfyp.Activities.VideoLearning;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.kidsappfyp.Constants.Constants;
import com.example.kidsappfyp.R;

public class VideoCategoryAdapter extends RecyclerView.Adapter<VideoCategoryAdapter.ViewHolder>{

    Context context;
    String[] videocategory;
    int[] tumbnailList;

    public VideoCategoryAdapter(Context context, String[] videocategory, int[] tumbnailList) {
        this.context = context;
        this.videocategory = videocategory;
        this.tumbnailList = tumbnailList;
    }


    @NonNull
    @Override
    public VideoCategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.card_list_home, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VideoCategoryAdapter.ViewHolder holder, int position) {
        Glide.with(context).load(tumbnailList[position]).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)).into(holder.imgHomeCategory);
        holder.lloutHome.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Constants.VIDEO_CATEGORY_ID=String.valueOf(position);
                //Toast.makeText(context, "Clicked on position"+ Constants.VIDEO_CATEGORY_ID, Toast.LENGTH_SHORT).show();
                context.startActivity(new Intent(context, ListVideoActivity.class).putExtra("Category",videocategory[position]));
            }
        });
    }

    @Override
    public int getItemCount() {
        return videocategory.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgHomeCategory;
        LinearLayout lloutHome;
        public ViewHolder(@NonNull View view) {
            super(view);
            imgHomeCategory = view.findViewById(R.id.imgHomeCategory);
            lloutHome = view.findViewById(R.id.lloutCardHome);
        }
    }

}

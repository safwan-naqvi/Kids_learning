package com.example.kidsappfyp.Activities.VideoLearning;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kidsappfyp.HelperClasses.DatabaseHelper;
import com.example.kidsappfyp.HelperClasses.ModelVideo;
import com.example.kidsappfyp.R;

import java.util.ArrayList;

public class ListVideoActivity extends AppCompatActivity {
    Context context;
    DatabaseHelper databaseHelper;
    RecyclerView rvVideoList;
    ArrayList<ModelVideo> arrOfVideoList;
    VideoListAdapter videoListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_list_video);

        //region InitWidget
        context = this;
        rvVideoList = findViewById(R.id.rvVideoList);
        TextView txtTitleSubHome = findViewById(R.id.txtTitleSubHome);
        Intent intent = getIntent();
        txtTitleSubHome.setText(intent.getStringExtra("Category"));
        setRvVideoListAdapter();
        //endregion


    }

    private void setRvVideoListAdapter() {
        databaseHelper=new DatabaseHelper(context);
        arrOfVideoList=databaseHelper.getVideoDetails();
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rvVideoList.setLayoutManager(staggeredGridLayoutManager);
        videoListAdapter = new VideoListAdapter(context, arrOfVideoList);
        rvVideoList.setAdapter(videoListAdapter);
        videoListAdapter.notifyDataSetChanged();
    }

    public void onClickBack(View view) {
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
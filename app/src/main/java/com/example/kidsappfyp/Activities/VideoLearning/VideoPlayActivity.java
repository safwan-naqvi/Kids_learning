package com.example.kidsappfyp.Activities.VideoLearning;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import android.widget.TextView;
import android.widget.Toast;
import com.example.kidsappfyp.Constants.Constants;
import com.example.kidsappfyp.HelperClasses.ModelVideo;
import com.example.kidsappfyp.R;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;


import java.util.ArrayList;

public class VideoPlayActivity extends YouTubeBaseActivity {

    ArrayList<ModelVideo> arrOfVideoList;
    YouTubePlayerView youTubePlayerView;
    int POSITION;
    TextView videoTitleOfVideo;
    RecyclerView rvVideoList;
    Context context;
    public String API = "AIzaSyACdEdup8o9LKXHTm-yq3MBMuWt82SMCZI";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_video_play);
        context = this;
        initDefine();

    }


    private void initDefine() {
        rvVideoList = findViewById(R.id.rvVideoList);
        youTubePlayerView = findViewById(R.id.youTubePlayerView);
        videoTitleOfVideo = findViewById(R.id.videoTitleOfVideo);
        Intent intent = getIntent();
        arrOfVideoList = (ArrayList<ModelVideo>) intent.getSerializableExtra("ArrayOfVideo");
        POSITION = intent.getIntExtra("Position", 0);
        initVideoPlayer();
    }

    private void initVideoPlayer() {
        YouTubePlayer.OnInitializedListener listener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                String videoId = Constants.VIDEO_ID;
                youTubePlayer.loadVideo(videoId);
                videoTitleOfVideo.setText(arrOfVideoList.get(POSITION).getVideoTitle());
                youTubePlayer.play();
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Toast.makeText(VideoPlayActivity.this, "Initialization Failed" +youTubeInitializationResult, Toast.LENGTH_SHORT).show();
            }
        };



        youTubePlayerView.initialize(API, listener);
    }

    public void onClickBack(View view) {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
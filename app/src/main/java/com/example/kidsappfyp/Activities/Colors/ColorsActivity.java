package com.example.kidsappfyp.Activities.Colors;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.example.kidsappfyp.HelperClasses.CenterZoomLayoutManager;
import com.example.kidsappfyp.R;

public class ColorsActivity extends AppCompatActivity {
    private RecyclerView colorRecycler;
    private ColorsAdapter adapter;
    private CenterZoomLayoutManager centerZoomLayoutManager;

    private Button previous, play, next;
    private int counter = 0;
    private int[] sounds;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_colors);

        sounds = new int[]{R.raw.red, R.raw.pink, R.raw.purple, R.raw.indigo, R.raw.blue, R.raw.sky_blue, R.raw.cyan, R.raw.teal,
                R.raw.green, R.raw.lime, R.raw.yellow, R.raw.amber, R.raw.orange, R.raw.brown, R.raw.grey, R.raw.black, R.raw.white};

        final int[] colors = getApplicationContext().getResources().getIntArray(R.array.colors);
        adapter = new ColorsAdapter(getApplicationContext());
        centerZoomLayoutManager = new CenterZoomLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        colorRecycler = (RecyclerView) findViewById(R.id.colors_recycler);
        previous = (Button) findViewById(R.id.previous_colors);
        play = (Button) findViewById(R.id.play_colors);
        next = (Button) findViewById(R.id.next_colors);

        colorRecycler.setLayoutManager(centerZoomLayoutManager);
        colorRecycler.setItemAnimator(new DefaultItemAnimator());
        colorRecycler.setAdapter(adapter);

        counter = Integer.MAX_VALUE / 2;

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counter = centerZoomLayoutManager.findLastCompletelyVisibleItemPosition();
                counter--;
                if (counter < 0) {
                    counter = colors.length - 1;
                    colorRecycler.scrollToPosition(counter);
                }
                colorRecycler.smoothScrollToPosition(counter);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counter = centerZoomLayoutManager.findLastCompletelyVisibleItemPosition();
                counter++;
                colorRecycler.smoothScrollToPosition(counter);
            }
        });
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(colorRecycler);

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counter = centerZoomLayoutManager.findLastCompletelyVisibleItemPosition();
                int pos = counter % colors.length;
                if (mediaPlayer != null) {
                    mediaPlayer.release();
                }
                mediaPlayer = MediaPlayer.create(getApplicationContext(), sounds[pos]);
                mediaPlayer.start();
            }
        });

    }

    public void onClickBack(View view) {
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
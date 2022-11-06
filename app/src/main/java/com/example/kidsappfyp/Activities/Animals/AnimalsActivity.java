package com.example.kidsappfyp.Activities.Animals;

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

import com.example.kidsappfyp.Activities.Colors.ColorsAdapter;
import com.example.kidsappfyp.Activities.Vegetables.ImageAdapter;
import com.example.kidsappfyp.Activities.Vegetables.ImageItem;
import com.example.kidsappfyp.HelperClasses.CenterZoomLayoutManager;
import com.example.kidsappfyp.R;

import java.util.ArrayList;
import java.util.List;

public class AnimalsActivity extends AppCompatActivity {

    private RecyclerView animalRecycler;
    private List<ImageItem> imageItemList;
    private ImageAdapter adapter;
    private CenterZoomLayoutManager centerZoomLayoutManager;

    private Button previous, play, next;
    private int counter = 0;

    private MediaPlayer mediaPlayer;

    private int[] sounds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_animals);

        sounds = new int[]{R.raw.alligator, R.raw.bear, R.raw.elephant, R.raw.lion, R.raw.monkey, R.raw.panda, R.raw.rabbit,
                R.raw.snake, R.raw.squirrel, R.raw.tiger, R.raw.zebra};


        imageItemList = new ArrayList<>();
        initList();
        adapter = new ImageAdapter(this, imageItemList);

        animalRecycler = (RecyclerView) findViewById(R.id.animals_recycler);
        centerZoomLayoutManager = new CenterZoomLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        animalRecycler.setLayoutManager(centerZoomLayoutManager);
        animalRecycler.setItemAnimator(new DefaultItemAnimator());
        animalRecycler.setAdapter(adapter);

        previous = (Button) findViewById(R.id.previous_animals);
        play = (Button) findViewById(R.id.play_animals);
        next = (Button) findViewById(R.id.next_animals);

        counter = Integer.MAX_VALUE / 2;
//2147483647
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counter = centerZoomLayoutManager.findLastCompletelyVisibleItemPosition();
                counter--;
                animalRecycler.smoothScrollToPosition(counter);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counter = centerZoomLayoutManager.findLastCompletelyVisibleItemPosition();
                counter++;
                animalRecycler.smoothScrollToPosition(counter);
            }
        });

        animalRecycler.scrollToPosition(counter);
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(animalRecycler);

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counter = centerZoomLayoutManager.findLastCompletelyVisibleItemPosition();
                int pos = counter % imageItemList.size();
                if (mediaPlayer != null) {
                    mediaPlayer.release();
                }
                mediaPlayer = MediaPlayer.create(getApplicationContext(), sounds[pos]);
                mediaPlayer.start();
            }
        });
    }

    private void initList() {
        imageItemList.add(new ImageItem("Alligator", R.drawable.alligator));
        imageItemList.add(new ImageItem("Bear", R.drawable.bear));
        imageItemList.add(new ImageItem("Elephant", R.drawable.elephant));
        imageItemList.add(new ImageItem("Lion", R.drawable.lion));
        imageItemList.add(new ImageItem("Monkey", R.drawable.monkey));
        imageItemList.add(new ImageItem("Panda", R.drawable.panda));
        imageItemList.add(new ImageItem("Rabbit", R.drawable.rabbit));
        imageItemList.add(new ImageItem("Snake", R.drawable.snake));
        imageItemList.add(new ImageItem("Squirrel", R.drawable.squirrel));
        imageItemList.add(new ImageItem("Tiger", R.drawable.tiger));
        imageItemList.add(new ImageItem("Zebra", R.drawable.zebra));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void onClickBack(View view) {
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
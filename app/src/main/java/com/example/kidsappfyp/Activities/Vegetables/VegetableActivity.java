package com.example.kidsappfyp.Activities.Vegetables;

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

import java.util.ArrayList;
import java.util.List;

public class VegetableActivity extends AppCompatActivity {

    private RecyclerView vegetablesRecycler;
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
        setContentView(R.layout.activity_vegetable);


        sounds = new int[]{R.raw.artichoke, R.raw.asparagus, R.raw.beans, R.raw.beetroot, R.raw.blue_cabbage, R.raw.broccoli,
                R.raw.brussel_sprouts, R.raw.carrot, R.raw.cauliflower, R.raw.celery, R.raw.chili_peppers, R.raw.chinese_cabbage,
                R.raw.corn, R.raw.cucumbers, R.raw.dil, R.raw.eggplant, R.raw.garlic, R.raw.green_cabbage, R.raw.lemon, R.raw.lettuce,
                R.raw.onion, R.raw.parsley, R.raw.peppers, R.raw.potatoe, R.raw.radish, R.raw.red_cabbage, R.raw.squash,
                R.raw.tomatoe, R.raw.zuchinni};

        imageItemList = new ArrayList<>();
        initList();
        adapter = new ImageAdapter(this, imageItemList);

        vegetablesRecycler = (RecyclerView) findViewById(R.id.vegetables_recycler);
        centerZoomLayoutManager = new CenterZoomLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        vegetablesRecycler.setLayoutManager(centerZoomLayoutManager);
        vegetablesRecycler.setItemAnimator(new DefaultItemAnimator());
        vegetablesRecycler.setAdapter(adapter);

        previous = (Button) findViewById(R.id.previous_vegetables);
        play = (Button) findViewById(R.id.play_vegetables);
        next = (Button) findViewById(R.id.next_vegetables);

        counter = Integer.MAX_VALUE / 2;

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counter = centerZoomLayoutManager.findLastCompletelyVisibleItemPosition();
                counter--;
                vegetablesRecycler.smoothScrollToPosition(counter);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counter = centerZoomLayoutManager.findLastCompletelyVisibleItemPosition();
                counter++;
                vegetablesRecycler.smoothScrollToPosition(counter);
            }
        });

        vegetablesRecycler.scrollToPosition(counter);
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(vegetablesRecycler);

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

    public void onClickBack(View view) {
    }

    private void initList() {
        imageItemList.add(new ImageItem("Artichokes", R.drawable.artichokes));
        imageItemList.add(new ImageItem("Asparagus", R.drawable.asparagus));
        imageItemList.add(new ImageItem("Beans & Peas", R.drawable.beansandpeas));
        imageItemList.add(new ImageItem("Beetroot", R.drawable.beet));
        imageItemList.add(new ImageItem("Blue Cabbage", R.drawable.bluecabbage));
        imageItemList.add(new ImageItem("Broccoli", R.drawable.broccolli));
        imageItemList.add(new ImageItem("Brussels Sprouts", R.drawable.brusselssprout));
        imageItemList.add(new ImageItem("Carrot", R.drawable.carrot));
        imageItemList.add(new ImageItem("Cauliflower", R.drawable.cauli));
        imageItemList.add(new ImageItem("Celery", R.drawable.celery));
        imageItemList.add(new ImageItem("Chili Peppers", R.drawable.chilipeppers));
        imageItemList.add(new ImageItem("Chinese Cabbage", R.drawable.chinesecabbage));
        imageItemList.add(new ImageItem("Corn", R.drawable.corn));
        imageItemList.add(new ImageItem("Cucumbers", R.drawable.cucumbers));
        imageItemList.add(new ImageItem("Dil", R.drawable.dil));
        imageItemList.add(new ImageItem("Eggplant", R.drawable.eggplant));
        imageItemList.add(new ImageItem("Garlic", R.drawable.garlic));
        imageItemList.add(new ImageItem("Green Cabbage", R.drawable.greencabbage));
        imageItemList.add(new ImageItem("Lemon", R.drawable.lemon));
        imageItemList.add(new ImageItem("Lettuce", R.drawable.lettuce));
        imageItemList.add(new ImageItem("Onions", R.drawable.onions));
        imageItemList.add(new ImageItem("Parsley", R.drawable.parseley));
        imageItemList.add(new ImageItem("Peppers", R.drawable.peppers));
        imageItemList.add(new ImageItem("Potatoes", R.drawable.potatoes));
        imageItemList.add(new ImageItem("Radish", R.drawable.radish));
        imageItemList.add(new ImageItem("Red Cabbage", R.drawable.redcabbage));
        imageItemList.add(new ImageItem("Squashes", R.drawable.squashes));
        imageItemList.add(new ImageItem("Tomatoes", R.drawable.tomatoes));
        imageItemList.add(new ImageItem("Zuccini", R.drawable.zuccini));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}

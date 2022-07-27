package com.example.kidsappfyp.Activities.Urdu;

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

import com.example.kidsappfyp.Activities.Vegetables.ImageAdapter;
import com.example.kidsappfyp.Activities.Vegetables.ImageItem;
import com.example.kidsappfyp.HelperClasses.CenterZoomLayoutManager;
import com.example.kidsappfyp.R;

import java.util.ArrayList;
import java.util.List;

public class UrduActivity extends AppCompatActivity {

    private RecyclerView urduRecycler;
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
        setContentView(R.layout.activity_urdu);

        sounds = new int[]{R.raw.alif_madd, R.raw.alif, R.raw.be, R.raw.pe, R.raw.tey, R.raw.te, R.raw.se,
                R.raw.jeem, R.raw.che, R.raw.baree_he, R.raw.khe,
                R.raw.ddaal, R.raw.daal, R.raw.zaal, R.raw.rey, R.raw.re, R.raw.ze,
                R.raw.zhe, R.raw.seen, R.raw.sheen, R.raw.svaad, R.raw.zaad,
                R.raw.toy, R.raw.zoy, R.raw.ain, R.raw.gain, R.raw.fe,
                R.raw.qaaf, R.raw.kaaf, R.raw.gaaf, R.raw.laam, R.raw.meem,
                R.raw.noon, R.raw.vaao, R.raw.chotee_he, R.raw.ye, R.raw.baree_ye};


        imageItemList = new ArrayList<>();
        initList();
        adapter = new ImageAdapter(this, imageItemList);

        urduRecycler = (RecyclerView) findViewById(R.id.urdu_recycler);
        centerZoomLayoutManager = new CenterZoomLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        urduRecycler.setLayoutManager(centerZoomLayoutManager);
        urduRecycler.setItemAnimator(new DefaultItemAnimator());
        urduRecycler.setAdapter(adapter);

        previous = (Button) findViewById(R.id.previous_urdu);
        play = (Button) findViewById(R.id.play_urdu);
        next = (Button) findViewById(R.id.next_urdu);

        //counter = Integer.MAX_VALUE;


        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (counter > 0) {
                    counter = centerZoomLayoutManager.findLastCompletelyVisibleItemPosition();
                    counter--;
                    urduRecycler.smoothScrollToPosition(counter);
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counter = centerZoomLayoutManager.findLastCompletelyVisibleItemPosition();
                counter++;
                urduRecycler.smoothScrollToPosition(counter);
            }
        });

        urduRecycler.scrollToPosition(counter);
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(urduRecycler);

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
        imageItemList.add(new ImageItem("Alif Madda", R.drawable.alif_madda));
        imageItemList.add(new ImageItem("Alif", R.drawable.alif));
        imageItemList.add(new ImageItem("bey", R.drawable.bey));
        imageItemList.add(new ImageItem("pe", R.drawable.pey));
        imageItemList.add(new ImageItem("tey", R.drawable.tey));
        imageItemList.add(new ImageItem("ttey", R.drawable.ttey));
        imageItemList.add(new ImageItem("se", R.drawable.sey));
        imageItemList.add(new ImageItem("jeem", R.drawable.jeem));
        imageItemList.add(new ImageItem("Che", R.drawable.chay));
        imageItemList.add(new ImageItem("Baree Hy", R.drawable.hy));
        imageItemList.add(new ImageItem("Khay", R.drawable.khay));
        imageItemList.add(new ImageItem("daal", R.drawable.daal));
        imageItemList.add(new ImageItem("Ddaal", R.drawable.ddal));
        imageItemList.add(new ImageItem("zaal", R.drawable.zaal));
        imageItemList.add(new ImageItem("re", R.drawable.rrey));
        imageItemList.add(new ImageItem("arry", R.drawable.array));
        imageItemList.add(new ImageItem("ze", R.drawable.ze));
        imageItemList.add(new ImageItem("seyy", R.drawable.seyy));
        imageItemList.add(new ImageItem("seen", R.drawable.seeen));
        imageItemList.add(new ImageItem("sheen", R.drawable.sheen));
        imageItemList.add(new ImageItem("suad", R.drawable.swaad));
        imageItemList.add(new ImageItem("zuad", R.drawable.zuaad));
        imageItemList.add(new ImageItem("toyen", R.drawable.toyen));
        imageItemList.add(new ImageItem("zoyen", R.drawable.zoyen));
        imageItemList.add(new ImageItem("ein", R.drawable.ain));
        imageItemList.add(new ImageItem("ghain", R.drawable.ghain));
        imageItemList.add(new ImageItem("fey", R.drawable.fey));
        imageItemList.add(new ImageItem("quaf", R.drawable.quaaf));
        imageItemList.add(new ImageItem("kaaf", R.drawable.kaaf));
        imageItemList.add(new ImageItem("gaaf", R.drawable.gaaf));
        imageItemList.add(new ImageItem("laam", R.drawable.laam));
        imageItemList.add(new ImageItem("meem", R.drawable.meem));
        imageItemList.add(new ImageItem("noon", R.drawable.noon));
        imageItemList.add(new ImageItem("waow", R.drawable.wao));
        imageItemList.add(new ImageItem(" choti hey", R.drawable.hey));
        imageItemList.add(new ImageItem("yeh", R.drawable.cyah));
        imageItemList.add(new ImageItem("barri yeh", R.drawable.byah));
    }

    public void onClickBack(View view) {
    }
}
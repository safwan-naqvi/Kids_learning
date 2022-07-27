package com.example.kidsappfyp.Activities.GeoShapes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.airbnb.lottie.LottieAnimationView;
import com.example.kidsappfyp.Activities.Numbers.NumberSecondaryAdapter;
import com.example.kidsappfyp.Activities.Numbers.NumbersAdapter;
import com.example.kidsappfyp.Activities.Numbers.NumbersSecondaryModel;
import com.example.kidsappfyp.R;

import java.util.ArrayList;
import java.util.List;

public class ShapesActivity extends AppCompatActivity {
    Activity context;
    private RecyclerView primary, secondary;
    int[] shapes;
    public static int SHAPE = 0;
    private MediaPlayer mediaPlayer;
    private List<ShapesModel> ShapesList;
    ShapesAdapterSecondary shapesAdapterSecondary;
    LottieAnimationView lottieAnimationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_shapes);
        //region Inits
        primary = findViewById(R.id.shapes_recycler_primary);
        secondary = findViewById(R.id.shapes_recycler_secondary);
        lottieAnimationView = findViewById(R.id.welcomeScreen);
        context = this;
        shapes = new int[]{R.drawable.circle,R.drawable.triangle,R.drawable.rhombus,R.drawable.heart
        ,R.drawable.pentagon,R.drawable.star,R.drawable.square,R.drawable.rectangle};
        //endregion
        //region Setup Display panda animation for once

        // Storing data into SharedPreferences
        SharedPreferences sh = getSharedPreferences("DisplayPanda", MODE_PRIVATE);
        // The value will be default as empty string because for
        // the very first time when the app is opened, there is nothing to show
        Boolean flag = sh.getBoolean("display", false);
        mediaPlayer = MediaPlayer.create(this, R.raw.numbers);
        mediaPlayer.setLooping(false);
        mediaPlayer.setVolume(1, 1);
        if (flag) {
            SharedPreferences.Editor myEdit = sh.edit();
            myEdit.putBoolean("display", false);
            // Once the changes have been made,
            // we need to commit to apply those changes made,
            // otherwise, it will throw an error
            myEdit.commit();
            lottieAnimationView.setVisibility(View.VISIBLE);
            //Init Welcome Screen and sound//
            mediaPlayer.start();
            //end-------------------------
        } else {
            lottieAnimationView.setVisibility(View.GONE);
        }


        //endregion
        setRvAdapter();
        setRvAdapterForNumbers();

    }


    private void setRvAdapter() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 5, LinearLayoutManager.VERTICAL, false);
        primary.setLayoutManager(gridLayoutManager);
        ShapesAdapterPrimary shapesAdapterPrimary = new ShapesAdapterPrimary(context, shapes);
        primary.setAdapter(shapesAdapterPrimary);
    }

    public void setRvAdapterForNumbers() {
        ShapesList = new ArrayList<>();
        switch (SHAPE) {
            case 0:
                ShapesList.add(new ShapesModel(R.drawable.circle,"Circle", getString(R.string.circle)));
                break;
            case 1:
                ShapesList.add(new ShapesModel(R.drawable.triangle,"Triangle",getString(R.string.triangle)));
                break;
            case 2:
                ShapesList.add(new ShapesModel(R.drawable.rhombus,"Rhombus",getString(R.string.rhombus)));
                break;
            case 3:
                ShapesList.add(new ShapesModel(R.drawable.heart,"Heart",getString(R.string.heart)));
                break;
            case 4:
                ShapesList.add(new ShapesModel(R.drawable.pentagon,"Pentagon",getString(R.string.pentagon)));
                break;
            case 5:
                ShapesList.add(new ShapesModel(R.drawable.star,"Star",getString(R.string.star)));
                break;
            case 6:
                ShapesList.add(new ShapesModel(R.drawable.square,"Square",getString(R.string.square)));
                break;
            case 7:
                ShapesList.add(new ShapesModel(R.drawable.rectangle,"Rectangle",getString(R.string.rectangle)));
                break;
        }
        shapesAdapterSecondary = new ShapesAdapterSecondary(ShapesList,context);
        LinearLayoutManager catLayoutManager = new LinearLayoutManager(this);
        catLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        secondary.setLayoutManager(catLayoutManager);
        secondary.setAdapter(shapesAdapterSecondary);
        shapesAdapterSecondary.notifyDataSetChanged();
    }

    //region Life Cycle Remaining Functions
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayer.pause();
    }
//endregion

    public void onClickBack(View view) {
        finish();
    }
}
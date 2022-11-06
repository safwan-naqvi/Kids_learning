package com.example.kidsappfyp.Activities.GeoShapes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.kidsappfyp.Activities.Numbers.NumberSecondaryAdapter;
import com.example.kidsappfyp.Activities.Numbers.NumbersAdapter;
import com.example.kidsappfyp.Activities.Numbers.NumbersSecondaryModel;
import com.example.kidsappfyp.R;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;

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

    private SharedPreferences sharedPreferences;
    SharedPreferences.Editor myEdit;

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
        mediaPlayer = MediaPlayer.create(this, R.raw.shape_learn);
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


        sharedPreferences = getSharedPreferences("ShapesWalkThrough",MODE_PRIVATE);
        myEdit = sharedPreferences.edit();
        String walk = sharedPreferences.getString("show","on");
        //region Shapes WalkThrough
        if(walk.equals("on")){
            myEdit.putString("show", "off");
            myEdit.commit();
            new TapTargetSequence(this)
                    .targets(
                            TapTarget.forView(findViewById(R.id.button_shape), "Firstly Choose Shape", "You need to select the Shape You Want to know about.")
                                    // All options below are optional
                                    .outerCircleColor(R.color.colorButtonSecondary)      // Specify a color for the outer circle
                                    .outerCircleAlpha(0.90f)            // Specify the alpha amount for the outer circle
                                    .targetCircleColor(R.color.white)   // Specify a color for the target circle
                                    .titleTextSize(20)                  // Specify the size (in sp) of the title text
                                    .titleTextColor(R.color.white)      // Specify the color of the title text
                                    .descriptionTextSize(10)            // Specify the size (in sp) of the description text
                                    .descriptionTextColor(R.color.white)  // Specify the color of the description text
                                    .textColor(R.color.colorMain)            // Specify a color for both the title and description text
                                    .textTypeface(Typeface.SANS_SERIF)  // Specify a typeface for the text
                                    .dimColor(R.color.black)
                                    .titleTextSize(24)
                                    .descriptionTextSize(18)// If set, will dim behind the view with 30% opacity of the given color
                                    .drawShadow(true)                   // Whether to draw a drop shadow or not
                                    .cancelable(false)                  // Whether tapping outside the outer circle dismisses the view
                                    .tintTarget(true)
                                    .icon(getDrawable(R.drawable.one))  // Whether to tint the target view's color
                                    .transparentTarget(false)           // Specify whether the target is transparent (displays the content underneath)
                                    .targetRadius(60),                  // Specify the target radius (in dp)
                            TapTarget.forView(findViewById(R.id.button_shape_Name), "Secondly Click Here", "Now Clicking here will speak out the Description of Shapes.")
                                    // All options below are optional
                                    .outerCircleColor(R.color.appBlueTrans)      // Specify a color for the outer circle
                                    .outerCircleAlpha(0.96f)            // Specify the alpha amount for the outer circle
                                    .targetCircleColor(R.color.white)   // Specify a color for the target circle
                                    .titleTextSize(20)                  // Specify the size (in sp) of the title text
                                    .titleTextColor(R.color.white)      // Specify the color of the title text
                                    .descriptionTextSize(10)
                                    .titleTextSize(24)
                                    .descriptionTextSize(18)// Specify the size (in sp) of the description text
                                    .descriptionTextColor(R.color.white)  // Specify the color of the description text
                                    .textColor(R.color.colorWhite)            // Specify a color for both the title and description text
                                    .textTypeface(Typeface.SANS_SERIF)  // Specify a typeface for the text
                                    .dimColor(R.color.black)            // If set, will dim behind the view with 30% opacity of the given color
                                    .drawShadow(true)                   // Whether to draw a drop shadow or not
                                    .cancelable(false)                  // Whether tapping outside the outer circle dismisses the view
                                    .tintTarget(true)                   // Whether to tint the target view's color
                                    .transparentTarget(false)           // Specify whether the target is transparent (displays the content underneath)
                                    .icon(getDrawable(R.drawable.two))                     // Specify a custom drawable to draw as the target
                                    .targetRadius(60)).listener(new TapTargetSequence.Listener() {
                        @Override
                        public void onSequenceFinish() {
                            Toast.makeText(context, "Well done", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {
                            Toast.makeText(context, "One Step More", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onSequenceCanceled(TapTarget lastTarget) {

                        }
                    }).start();


        }
        //endregion

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void onClickBack(View view) {
        finish();
    }
}
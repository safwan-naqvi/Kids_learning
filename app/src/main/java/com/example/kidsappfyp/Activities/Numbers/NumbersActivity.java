package com.example.kidsappfyp.Activities.Numbers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.kidsappfyp.Activities.VocalsAlphabet.AlphabetThingsAdapter;
import com.example.kidsappfyp.Activities.VocalsAlphabet.AlphabetThingsModel;
import com.example.kidsappfyp.Activities.VocalsAlphabet.VocalsAndLetterAdapter;
import com.example.kidsappfyp.R;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.getkeepsafe.taptargetview.TapTargetView;

import java.util.ArrayList;
import java.util.List;

public class NumbersActivity extends AppCompatActivity {
    Activity context;
    private RecyclerView primary, secondary;
    int[] numbers;
    public static int NUMBER = 0;
    private MediaPlayer mediaPlayer;
    private List<NumbersSecondaryModel> NumbersSecondaryList;
    NumberSecondaryAdapter numbersAdapter;

    Button step_one, step_two;

    LottieAnimationView lottieAnimationView;
    private SharedPreferences sharedPreferences;
    SharedPreferences.Editor myEdit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_numbers);

        //region Inits
        primary = findViewById(R.id.numbers_recycler_primary);
        secondary = findViewById(R.id.numbers_recycler_secondary);
        lottieAnimationView = findViewById(R.id.welcomeScreen);
        context = this;
        numbers = new int[]{R.drawable.zero, R.drawable.one, R.drawable.two, R.drawable.three, R.drawable.four, R.drawable.five
                , R.drawable.six, R.drawable.seven, R.drawable.eight, R.drawable.nine};
        //endregion


        sharedPreferences = getSharedPreferences("NumberWalkThrough",MODE_PRIVATE);
        myEdit = sharedPreferences.edit();
        String walk = sharedPreferences.getString("show","on");

        //region Although for first time
        if(walk.equals("on")){
            myEdit.putString("show", "off");
            myEdit.commit();
            new TapTargetSequence(this)
                    .targets(
                            TapTarget.forView(findViewById(R.id.button_Number), "Firstly Choose Number", "You need to select the digit you want to go with.")
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
                            TapTarget.forView(findViewById(R.id.button_Number_Name), "Secondly Click Here", "Now Clicking here will speak out the Pronunciation of digits.")
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
        NumbersAdapter numbersAdapter = new NumbersAdapter(context, numbers);
        primary.setAdapter(numbersAdapter);
    }

    public void setRvAdapterForNumbers() {
        NumbersSecondaryList = new ArrayList<>();
        switch (NUMBER) {
            case 0:
                NumbersSecondaryList.add(new NumbersSecondaryModel(R.drawable.zero, "Zero", R.drawable.ic_zero));
                break;
            case 1:
                NumbersSecondaryList.add(new NumbersSecondaryModel(R.drawable.one, "one", R.drawable.ic_one));
                break;
            case 2:
                NumbersSecondaryList.add(new NumbersSecondaryModel(R.drawable.two, "two", R.drawable.ic_two));
                break;
            case 3:
                NumbersSecondaryList.add(new NumbersSecondaryModel(R.drawable.three, "three", R.drawable.ic_three));
                break;
            case 4:
                NumbersSecondaryList.add(new NumbersSecondaryModel(R.drawable.four, "four", R.drawable.ic_four));
                break;
            case 5:
                NumbersSecondaryList.add(new NumbersSecondaryModel(R.drawable.five, "five", R.drawable.ic_five));
                break;
            case 6:
                NumbersSecondaryList.add(new NumbersSecondaryModel(R.drawable.six, "six", R.drawable.ic_six));
                break;
            case 7:
                NumbersSecondaryList.add(new NumbersSecondaryModel(R.drawable.seven, "seven", R.drawable.ic_seven));
                break;
            case 8:
                NumbersSecondaryList.add(new NumbersSecondaryModel(R.drawable.eight, "eight", R.drawable.ic_eight));
                break;
            case 9:
                NumbersSecondaryList.add(new NumbersSecondaryModel(R.drawable.nine, "nine", R.drawable.ic_nine));
                break;
        }
        numbersAdapter = new NumberSecondaryAdapter(context, NumbersSecondaryList);
        LinearLayoutManager catLayoutManager = new LinearLayoutManager(this);
        catLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        secondary.setLayoutManager(catLayoutManager);
        secondary.setAdapter(numbersAdapter);
        numbersAdapter.notifyDataSetChanged();
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
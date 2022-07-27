package com.example.kidsappfyp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.example.kidsappfyp.Activities.VideoLearning.VideoCategoryAdapter;
import com.example.kidsappfyp.R;

public class VideoLearnActivity extends AppCompatActivity {

    String[] videocategory;
    int[] tumbnailList;
    VideoCategoryAdapter videoCategoryAdapter;
    RecyclerView rvVideoCategory;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_video_learn);
        //region InitWidgets
        rvVideoCategory = findViewById(R.id.rvVideoCategory);
        context = this;
        //endregion
        videocategory = new String[]{"ABC Songs", "Number Songs", "Color Songs", "Animal Songs", "Shape Songs", "Vehicle Songs", "Fruit Songs", "Vegetable Songs", "Day Songs", "Month Songs", "Clothes Songs"};
        tumbnailList = new int[]{R.drawable.vt_abc, R.drawable.vt_number, R.drawable.vt_color, R.drawable.vt_animal, R.drawable.vt_shape, R.drawable.vt_vehicle, R.drawable.vt_fruit, R.drawable.vt_vegetable, R.drawable.vt_day, R.drawable.vt_month, R.drawable.vt_clothes};
        setRvAdapter();

    }

    public void onClickBack(View view) {
        finish();
    }

    private void setRvAdapter() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2, LinearLayoutManager.VERTICAL, false);
        rvVideoCategory.setLayoutManager(gridLayoutManager);
        videoCategoryAdapter = new VideoCategoryAdapter(context, videocategory, tumbnailList);
        rvVideoCategory.setAdapter(videoCategoryAdapter);
    }


}
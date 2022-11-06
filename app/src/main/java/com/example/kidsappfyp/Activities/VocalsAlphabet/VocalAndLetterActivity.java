package com.example.kidsappfyp.Activities.VocalsAlphabet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kidsappfyp.Activities.VideoLearning.VideoCategoryAdapter;
import com.example.kidsappfyp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class VocalAndLetterActivity extends AppCompatActivity {
    Context context;
    private RecyclerView primary, secondary;
    private TextView letter;
    public VocalsAndLetterAdapter vocalsAndLetterAdapter;
    private AlphabetThingsAdapter alphabetThingsAdapter;
    int[] alphabets;
    public static String ALPHABET = "A";
    FirebaseFirestore firebaseFirestore;
    private List<AlphabetThingsModel> alphabetThingsList;
    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_vocal_and_letter);
        //region InitWidgets
        primary = findViewById(R.id.vocals_recycler_primary);
        secondary = findViewById(R.id.vocals_recycler_secondary);
        context = this;
        activity = this;

        letter = findViewById(R.id.letter_tv);
        //endregion
        //region Input to Alphabets List
        letter.setText("Letter "+ALPHABET);
        alphabets = new int[]{R.drawable.alpha1, R.drawable.alpha2, R.drawable.alpha3, R.drawable.alpha4, R.drawable.alpha5, R.drawable.alpha6
                , R.drawable.alpha7, R.drawable.alpha8, R.drawable.alpha9, R.drawable.alpha10, R.drawable.alpha12, R.drawable.alpha13
                , R.drawable.alpha14, R.drawable.alpha15, R.drawable.alpha16, R.drawable.alpha17, R.drawable.alpha18, R.drawable.alpha19
                , R.drawable.alpha20, R.drawable.alpha21, R.drawable.alpha22, R.drawable.alpha23, R.drawable.alpha24, R.drawable.alpha25
                , R.drawable.alpha26, R.drawable.alpha27};
        //endregion
        setRvAdapter();

        //region GetRecyclerViewData
        firebaseFirestore = FirebaseFirestore.getInstance();

        setRvAdapterForAlphabets();
        firebaseFirestore.collection(ALPHABET).orderBy("index").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                alphabetThingsList.add(new AlphabetThingsModel(documentSnapshot.get("icon").toString(), documentSnapshot.get("speak").toString()));
                                Log.i("works", alphabetThingsList.get(0).getAlpha_img());
                            }
                            alphabetThingsAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getApplicationContext(), "Error " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Issue " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        //endregion

    }

    public void setRvAdapterForAlphabets() {
        alphabetThingsList = new ArrayList<>();
        alphabetThingsAdapter = new AlphabetThingsAdapter(activity, alphabetThingsList);
        LinearLayoutManager catLayoutManager = new LinearLayoutManager(this);
        catLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        secondary.setLayoutManager(catLayoutManager);
        secondary.setAdapter(alphabetThingsAdapter);
    }

    private void setRvAdapter() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2, LinearLayoutManager.VERTICAL, false);
        primary.setLayoutManager(gridLayoutManager);
        vocalsAndLetterAdapter = new VocalsAndLetterAdapter(activity, alphabets);
        primary.setAdapter(vocalsAndLetterAdapter);
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
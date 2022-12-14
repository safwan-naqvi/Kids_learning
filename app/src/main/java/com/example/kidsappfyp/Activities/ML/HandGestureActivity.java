package com.example.kidsappfyp.Activities.ML;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.kidsappfyp.R;
import com.example.kidsappfyp.Testing.Question;
import com.example.kidsappfyp.Testing.QuizActivity;
import com.example.kidsappfyp.Testing.ResultsActivity;
import com.example.kidsappfyp.Testing.TestingMainActivity;
import com.example.kidsappfyp.databinding.ActivityHandGestureBinding;
import com.example.kidsappfyp.ml.Model;
import com.github.drjacky.imagepicker.ImagePicker;
import com.github.drjacky.imagepicker.constant.ImageProvider;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

public class HandGestureActivity extends AppCompatActivity {
    ActivityHandGestureBinding binding;
    int imageSize = 224;
    private MediaPlayer mediaPlayer;
    ArrayList<QuestionsModel> questions;
    QuestionsModel question;
    int index = 0;
    int correctAnswers = 0;
    CountDownTimer timer;
    FirebaseFirestore database;
    String DownloadImageUrl;
    String url = "http://192.168.100.110:5000/predict";
    private Uri imageUri;
    private StorageReference UserImagesRef;
    String answer = "";
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHandGestureBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        mediaPlayer = MediaPlayer.create(this, R.raw.wrong_answer);
        mediaPlayer.setLooping(false);
        mediaPlayer.setVolume(1, 1);

        progressDialog = new ProgressDialog(this);
        UserImagesRef = FirebaseStorage.getInstance().getReference().child("ANSWERS");



        questions = new ArrayList<>();
        database = FirebaseFirestore.getInstance();
        Random random = new Random();
        final int rand = random.nextInt(10);
        final String catId = getIntent().getStringExtra("catId");
        //region Questions Startup
        database.collection("categories")
                .document(catId)
                .collection("questions")
                .whereGreaterThanOrEqualTo("index", rand)
                .orderBy("index")
                .limit(5).get().addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots.getDocuments().size() < 5) {
                        database.collection("categories")
                                .document(catId)
                                .collection("questions")
                                .whereLessThanOrEqualTo("index", rand)
                                .orderBy("index")
                                .get().addOnSuccessListener(queryDocumentSnapshots1 -> {
                                    for (DocumentSnapshot snapshot : queryDocumentSnapshots1) {
//                                                QuestionsModel question = snapshot.toObject(QuestionsModel.class);
//                                                questions.add(question);
                                        questions.add(new QuestionsModel(snapshot.getString("question"),
                                                snapshot.getString("answer"),
                                                snapshot.getString("questionImage")));
                                    }
                                    setNextQuestion();
                                });
                    } else {
                        for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                            QuestionsModel question = snapshot.toObject(QuestionsModel.class);
                            questions.add(question);
                        }
                        setNextQuestion();
                    }
                });

        //endregion
        //region Launcher
        ActivityResultLauncher<Intent> launcher =
                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), (ActivityResult result) -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Log.e("Hand", "Activity Launcher For Image: Checking If Data is Coming or not " + result.getData());
                        imageUri = result.getData().getData();
                        if(imageUri!=null){
                            saveImageInFirebase();
                        }
                        // Use the uri to load the image
                    }else{
                        Log.e("Hand", "Activity Launcher For Image" + result.getData());
                    }
                });
        //endregion
        binding.answerGesture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.Companion.with(HandGestureActivity.this)
                        .crop()
                        .cropFreeStyle()
                        .maxResultSize(1080, 1080, true)
                        .provider(ImageProvider.BOTH) //Or bothCameraGallery()
                        .createIntentFromDialog((Function1)(new Function1(){
                            public Object invoke(Object var1){
                                this.invoke((Intent)var1);
                                return Unit.INSTANCE;
                            }

                            public final void invoke(@NotNull Intent it){
                                Intrinsics.checkNotNullParameter(it,"it");
                                launcher.launch(it);
                            }
                        }));
            }
        });
        resetTimer();
        binding.quit.setOnClickListener(view1 -> {
            startActivity(new Intent(HandGestureActivity.this, TestingMainActivity.class));
            finish();
        });



    }

    void resetTimer() {
        timer = new CountDownTimer(20000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                binding.timer.setText(String.valueOf(millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                if (index <= questions.size()) {
                    index++;
                    setNextQuestion();
                }
            }
        };
    }

    void setNextQuestion() {
        if (timer != null)
            timer.cancel();

        timer.start();
        if (index < questions.size()) {
            if(index == 0){
                binding.questionCounter.setText(String.format("%d/%d", (index + 1), questions.size()));
                question = questions.get(index);
                binding.question.setText(question.getQuestion());
                Glide.with(getApplicationContext())
                        .load(question.getQuestionImage())
                        .into(binding.questionImg);
            }else{
                new Handler().postDelayed(() -> {
                    binding.questionCounter.setText(String.format("%d/%d", (index + 1), questions.size()));
                    question = questions.get(index);
                    binding.question.setText(question.getQuestion());
                    Glide.with(getApplicationContext())
                            .load(question.getQuestionImage())
                            .into(binding.questionImg);
                },2000);
            }
        } else {
            Intent intent = new Intent(HandGestureActivity.this, ResultsActivity.class);
            intent.putExtra("correct", correctAnswers);
            intent.putExtra("total", questions.size());
            startActivity(intent);
            Toast.makeText(this, "Quiz Finished.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    void checkAnswer(String answer) {
        if (answer.equals(question.getAnswer())) {
            correctAnswers++;
            binding.gestureCorrectAnswer.setVisibility(View.VISIBLE);
            binding.gestureCorrectAnswer.setAnimation(R.raw.correct);
            mediaPlayer = MediaPlayer.create(this, R.raw.correct_answer);
            mediaPlayer.setLooping(false);
            mediaPlayer.setVolume(1, 1);
            if(!mediaPlayer.isPlaying()){
                mediaPlayer.start();
            }else{
                mediaPlayer.stop();
                mediaPlayer.start();
            }
        } else {
            showAnswer();
        }
        new Handler().postDelayed(() -> {
            if (index < questions.size()) {
                index++;
                reset();
                setNextQuestion();
            } else {
                Intent intent = new Intent(HandGestureActivity.this, ResultsActivity.class);
                intent.putExtra("correct", correctAnswers);
                intent.putExtra("total", questions.size());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                Toast.makeText(HandGestureActivity.this, "Quiz Finished.", Toast.LENGTH_SHORT).show();
            }
        }, 1500);

    }

    private void showAnswer() {
        binding.gestureCorrectAnswer.setVisibility(View.VISIBLE);
        if (question.getAnswer().equals(answer)) {
            binding.gestureCorrectAnswer.setVisibility(View.VISIBLE);
            binding.gestureCorrectAnswer.setAnimation(R.raw.correct);
            mediaPlayer = MediaPlayer.create(this, R.raw.correct_answer);
            mediaPlayer.setLooping(false);
            mediaPlayer.setVolume(1, 1);
            if(!mediaPlayer.isPlaying()){
                mediaPlayer.start();
            }else{
                mediaPlayer.stop();
                mediaPlayer.start();
            }
        } else {
            binding.gestureCorrectAnswer.setAnimation(R.raw.wrong);
            binding.gestureCorrectAnswer.setVisibility(View.VISIBLE);
            mediaPlayer = MediaPlayer.create(this, R.raw.wrong_answer);
            mediaPlayer.setLooping(false);
            mediaPlayer.setVolume(1, 1);
            if(!mediaPlayer.isPlaying()){
                mediaPlayer.start();
            }else{
                mediaPlayer.stop();
                mediaPlayer.start();
            }
            binding.correctAnswer.setText("You Are Wrong\nCorrect Answer is " + question.getAnswer());
        }
        new Handler().postDelayed(() -> binding.gestureCorrectAnswer.setVisibility(View.GONE),1000);
    }

    void reset() {
        binding.correctAnswer.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        if(mediaPlayer.isPlaying()){
            mediaPlayer.pause();
        }
    }

    private void saveImageInFirebase() {
        progressDialog.setMessage("Processing");
        progressDialog.setCancelable(false);
        progressDialog.show();
        //region Making Unique name for image
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd,yyyy");
        String saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        String saveCurrentTime = currentTime.format(calendar.getTime());
        //endregion

        //region Making Unique key for image
        String productRandomKey = saveCurrentDate + saveCurrentTime;

        StorageReference filePath = UserImagesRef.child(imageUri.getLastPathSegment() + productRandomKey);

        //region Uploading Image to Database

        final UploadTask uploadTask = filePath.putFile(imageUri);
        uploadTask.addOnFailureListener(e -> progressDialog.dismiss()).addOnSuccessListener(taskSnapshot -> {
            //if image is uploaded it will get that link of image to be stored in database

            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {

                        progressDialog.dismiss();
                        throw task.getException();
                    }
                    DownloadImageUrl = filePath.getDownloadUrl().toString();
                    return filePath.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        DownloadImageUrl = task.getResult().toString();
                        getData(DownloadImageUrl);
                    }
                    progressDialog.dismiss();
                }
            });
        }).addOnProgressListener(snapshot -> {
            double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
            progressDialog.setMessage("Progress "+progress);
        });

        //endregion

    }

    private void getData(String downloadImageUrl) {
        RequestQueue queue = Volley.newRequestQueue(HandGestureActivity.this);
        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    answer = jsonResponse.getString("gesture");
                    checkAnswer(answer);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(HandGestureActivity.this, "Error " + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("url", downloadImageUrl);
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }


}
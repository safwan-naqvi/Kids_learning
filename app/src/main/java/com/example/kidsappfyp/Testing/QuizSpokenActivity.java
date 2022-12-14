package com.example.kidsappfyp.Testing;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.kidsappfyp.R;
import com.example.kidsappfyp.databinding.ActivityQuizBinding;
import com.example.kidsappfyp.databinding.ActivityQuizSpokenBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

public class QuizSpokenActivity extends AppCompatActivity {

    ActivityQuizSpokenBinding binding;

    ArrayList<Question> questions;
    int index = 0;
    Question question;
    CountDownTimer timer;
    FirebaseFirestore database;
    int correctAnswers = 0;
    String answerSpoken;
    private MediaPlayer mediaPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = ActivityQuizSpokenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mediaPlayer = MediaPlayer.create(this, R.raw.wrong_answer);
        mediaPlayer.setLooping(false);
        mediaPlayer.setVolume(1, 1);
        questions = new ArrayList<>();
        database = FirebaseFirestore.getInstance();

        final String catId = getIntent().getStringExtra("catId");

        Random random = new Random();
        final int rand = random.nextInt(12);

        database.collection("categories")
                .document(catId)
                .collection("questions")
                .whereGreaterThanOrEqualTo("index", rand)
                .orderBy("index")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.getDocuments().size() < 5) {
                            database.collection("categories")
                                    .document(catId)
                                    .collection("questions")
                                    .whereLessThanOrEqualTo("index", rand)
                                    .orderBy("index")
                                    .limit(5).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                            for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                                                Question question = snapshot.toObject(Question.class);
                                                questions.add(question);
                                            }
                                            setNextQuestion();
                                        }
                                    });
                        } else {
                            for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                                Question question = snapshot.toObject(Question.class);
                                questions.add(question);
                            }
                            setNextQuestion();
                        }
                    }
                });


        resetTimer();

        binding.quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(QuizSpokenActivity.this, TestingMainActivity.class));
                finish();
            }
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

    void showAnswer() {
        if (!question.getAnswer().equals(answerSpoken)) {
            binding.correctSpokenAnswer.setVisibility(View.VISIBLE);
            binding.correctSpokenAnswer.setText("Correct Answer: " + question.getAnswer());
        } else {
            binding.correctSpokenAnswer.setVisibility(View.GONE);
        }
    }

    void setNextQuestion() {
        if (timer != null)
            timer.cancel();

        timer.start();
        if (index < questions.size()) {
            binding.questionCounter.setText(String.format("%d/%d", (index + 1), questions.size()));
            question = questions.get(index);
            binding.question.setText(question.getQuestion());
            Glide.with(getApplicationContext())
                    .load(question.getQuestionImage())
                    .into(binding.questionImg);
        } else {
            Intent intent = new Intent(QuizSpokenActivity.this, ResultsActivity.class);
            intent.putExtra("correct", correctAnswers);
            intent.putExtra("total", questions.size());
            startActivity(intent);
            Toast.makeText(this, "Quiz Finished.", Toast.LENGTH_SHORT).show();
            finishAffinity();
        }
    }

    void reset() {
        binding.animationViewQuizAnswer.setVisibility(View.GONE);
        binding.correctSpokenAnswer.setVisibility(View.GONE);
    }

    void checkAnswer(String answer) {
        binding.animationViewQuizAnswer.setVisibility(View.VISIBLE);
        if (answer.equals(question.getAnswer())) {
            correctAnswers++;
            binding.animationViewQuizAnswer.setAnimation(R.raw.correct);
            mediaPlayer = MediaPlayer.create(this, R.raw.correct_answer);
        } else {
            showAnswer();
            binding.animationViewQuizAnswer.setAnimation(R.raw.wrong);
            mediaPlayer = MediaPlayer.create(this, R.raw.wrong_answer);
        }
        mediaPlayer.setLooping(false);
        mediaPlayer.setVolume(1, 1);
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        } else {
            mediaPlayer.stop();
            mediaPlayer.start();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (index < questions.size()) {
                    index++;
                    reset();
                    setNextQuestion();
                } else {
                    Intent intent = new Intent(QuizSpokenActivity.this, ResultsActivity.class);
                    intent.putExtra("correct", correctAnswers);
                    intent.putExtra("total", questions.size());
                    Toast.makeText(QuizSpokenActivity.this, "Quiz Finished.", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                    finishAffinity();

                }
            }
        }, 2500);
    }

    public void onClick(View view) {
        if (view.getId() == R.id.answerSpeak) {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(intent, 72);
            } else {
                Toast.makeText(this, "Your Device is not supported by Speech Input Feature", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 72:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    answerSpoken = result.get(0).trim().toLowerCase();
                    checkAnswer(answerSpoken);
                }
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }
}
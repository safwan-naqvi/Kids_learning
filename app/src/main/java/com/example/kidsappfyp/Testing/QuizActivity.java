package com.example.kidsappfyp.Testing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.kidsappfyp.R;
import com.example.kidsappfyp.databinding.ActivityQuizBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Random;

public class QuizActivity extends AppCompatActivity {

    ActivityQuizBinding binding;
    private MediaPlayer mediaPlayer;
    ArrayList<Question> questions;
    Question question;
    int index = 0;
    int correctAnswers = 0;
    CountDownTimer timer;
    FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = ActivityQuizBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mediaPlayer = MediaPlayer.create(this, R.raw.wrong_answer);
        mediaPlayer.setLooping(false);
        mediaPlayer.setVolume(1, 1);
        questions = new ArrayList<>();
        database = FirebaseFirestore.getInstance();

        final String catId = getIntent().getStringExtra("catId");

        Random random = new Random();
        final int rand = random.nextInt(10);

        database.collection("categories")
                .document(catId)
                .collection("questions")
                .whereGreaterThanOrEqualTo("index", 0)
                .orderBy("index")
                .get().addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots.getDocuments().size() < 5) {
                        database.collection("categories")
                                .document(catId)
                                .collection("questions")
                                .whereLessThanOrEqualTo("index", rand)
                                .orderBy("index")
                                .limit(5).get().addOnSuccessListener(queryDocumentSnapshots1 -> {
                                    for (DocumentSnapshot snapshot : queryDocumentSnapshots1) {
                                        Question question = snapshot.toObject(Question.class);
                                        questions.add(question);
                                    }
                                    setNextQuestion();
                                });
                    } else {
                        for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                            Question question = snapshot.toObject(Question.class);
                            questions.add(question);
                        }
                        setNextQuestion();
                    }
                });


        resetTimer();

        binding.quizBtn.setOnClickListener(view -> {
            startActivity(new Intent(QuizActivity.this, TestingMainActivity.class));
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

    void showAnswer() {
        if (question.getAnswer().equals(binding.option1.getText().toString()))
            binding.option1.setBackground(getResources().getDrawable(R.drawable.option_right));
        else if (question.getAnswer().equals(binding.option2.getText().toString()))
            binding.option2.setBackground(getResources().getDrawable(R.drawable.option_right));
        else if (question.getAnswer().equals(binding.option3.getText().toString()))
            binding.option3.setBackground(getResources().getDrawable(R.drawable.option_right));
        else if (question.getAnswer().equals(binding.option4.getText().toString()))
            binding.option4.setBackground(getResources().getDrawable(R.drawable.option_right));
    }

    void setNextQuestion() {
        if (timer != null)
            timer.cancel();

        timer.start();
        if (index < questions.size()) {
            binding.questionCounter.setText(String.format("%d/%d", (index + 1), questions.size()));
            question = questions.get(index);
            binding.question.setText(question.getQuestion());
            binding.option1.setText(question.getOption1());
            binding.option2.setText(question.getOption2());
            binding.option3.setText(question.getOption3());
            binding.option4.setText(question.getOption4());
            Glide.with(getApplicationContext())
                    .load(question.getQuestionImage())
                    .into(binding.questionImg);
        } else {
            Intent intent = new Intent(QuizActivity.this, ResultsActivity.class);
            intent.putExtra("correct", correctAnswers);
            intent.putExtra("total", questions.size());
            startActivity(intent);
            Toast.makeText(this, "Quiz Finished.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    void checkAnswer(TextView textView) {
        String selectedAnswer = textView.getText().toString();
        if (selectedAnswer.equals(question.getAnswer())) {
            correctAnswers++;
            mediaPlayer = MediaPlayer.create(this, R.raw.correct_answer);
            textView.setBackground(getResources().getDrawable(R.drawable.option_right));
        } else {
            showAnswer();
            mediaPlayer = MediaPlayer.create(this, R.raw.wrong_answer);
            textView.setBackground(getResources().getDrawable(R.drawable.option_wrong));
        }
        mediaPlayer.setLooping(false);
        mediaPlayer.setVolume(1, 1);
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        } else {
            mediaPlayer.stop();
            mediaPlayer.start();
        }
        new Handler().postDelayed(() -> {
            if (index < questions.size()) {
                index++;
                reset();
                setNextQuestion();
            } else {
                Intent intent = new Intent(QuizActivity.this, ResultsActivity.class);
                intent.putExtra("correct", correctAnswers);
                intent.putExtra("total", questions.size());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                Toast.makeText(QuizActivity.this, "Quiz Finished.", Toast.LENGTH_SHORT).show();
            }
        }, 1500);

    }

    void reset() {
        binding.option1.setBackground(getResources().getDrawable(R.drawable.option_unselected));
        binding.option2.setBackground(getResources().getDrawable(R.drawable.option_unselected));
        binding.option3.setBackground(getResources().getDrawable(R.drawable.option_unselected));
        binding.option4.setBackground(getResources().getDrawable(R.drawable.option_unselected));
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.option_1:
            case R.id.option_2:
            case R.id.option_3:
            case R.id.option_4:
                if (timer != null)
                    timer.cancel();
                TextView selected = (TextView) view;
                checkAnswer(selected);

                break;
            case R.id.nextBtn:
                reset();
                if (index <= questions.size()) {
                    index++;
                    setNextQuestion();
                } else {
                    Intent intent = new Intent(QuizActivity.this, ResultsActivity.class);
                    intent.putExtra("correct", correctAnswers);
                    intent.putExtra("total", questions.size());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }

}
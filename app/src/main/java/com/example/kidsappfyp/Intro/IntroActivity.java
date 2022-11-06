package com.example.kidsappfyp.Intro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.kidsappfyp.Dashboard.DashboardActivity;
import com.example.kidsappfyp.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class IntroActivity extends AppCompatActivity {

    Button signInWithGoogle;

    private GoogleSignInClient mGoogleSignInClient;

    private final static int RC_SIGN_IN = 786;
    private FirebaseAuth mAuth;
    private MediaPlayer mediaPlayer;
    private LottieAnimationView tipsAnim;
    //List for daily tips
    private List<String> tips_list;
    private TextView tips;

    @Override
    protected void onStart() {
        super.onStart();
        //If User Already in firebase so we will not start again login procedure
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            Intent intent = new Intent(getApplicationContext(), SelectionActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_intro);
        //Hooking Elements
        signInWithGoogle = findViewById(R.id.sign_in_with_google_btn);

        tipsAnim = findViewById(R.id.tips_creator_avatar);
        mediaPlayer = MediaPlayer.create(this, R.raw.intro_bg);
        mediaPlayer.setLooping(true);
        mediaPlayer.setVolume(0.4f, 0.4f);
        tips = findViewById(R.id.tip_of_the_day);
        //end of hooking

        //region Added New Tips of the Day
        tips_list = new ArrayList<>();
        tips_list.add("Tip: No one is perfect – that’s why pencils have erasers.");
        tips_list.add("Tip: Work Smarter Not Harder.");
        tips_list.add("Tip: Click On Avater For new Tip.");
        tips_list.add("Tip: For More points on Leader board pass more tests.");
        tips_list.add("Tip: Fall seven times, stand up eight.");
        tips_list.add("Tip: You always pass failure on the way to success.");
        //endregion


        createRequestForLogin();
        //Authenticating Firebase
        mAuth = FirebaseAuth.getInstance();

        signInWithGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        //When Click on avater new Tip Generated
        tipsAnim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeTip();
            }
        });

    }

    private void changeTip() {
        Random rand = new Random();
        tips.setText(tips_list.get(rand.nextInt(tips_list.size())));
    }

    private void createRequestForLogin() {

        // Configure Google Sign In and give a popup
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        //Building SignInClient with options specified by geo this will help us to
        //create request from app to google to access gmails.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                //Logic
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {

            }
        }

    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = new Intent(getApplicationContext(), SelectionActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(IntroActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    @Override
    protected void onResume() {
        super.onResume();
        mediaPlayer.start();
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
}
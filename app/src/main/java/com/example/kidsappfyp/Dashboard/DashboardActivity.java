package com.example.kidsappfyp.Dashboard;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.example.kidsappfyp.Fragments.HomeFragment;
import com.example.kidsappfyp.HelperClasses.CommonHelper;
import com.example.kidsappfyp.HelperClasses.DatabaseHelper;
import com.example.kidsappfyp.HelperClasses.UserHelperClass;
import com.example.kidsappfyp.Intro.IntroActivity;
import com.example.kidsappfyp.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.HashMap;
import java.util.Map;

public class DashboardActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;


    private MediaPlayer mediaPlayer;
    private SharedPreferences sharedPreferences;
    //region Navigation Drawer
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ActionBarDrawerToggle toggle;
    ImageView soundMode;
    //endregion
    //region Global Variables
    Dialog dialog;
    String userID;
    GoogleSignInAccount signInAccount;
    private FirebaseFirestore firebaseFirestore;
    private String userGender = "";
    int score = 100;

    //endregion
    //region Database Inits
    Context context;
    DatabaseHelper databaseHelper;
    //endregion
    SharedPreferences.Editor myEdit;
    String music;
    boolean flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_dashboard);

        //region Shared Preference for handling Music mode
        sharedPreferences = getSharedPreferences("BGMusic",MODE_PRIVATE);
        myEdit = sharedPreferences.edit();
        //endregion


        //region Setting Up Database
        context = this;
        databaseHelper = new DatabaseHelper(context);
        try {
            databaseHelper.createDataBase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //endregion

        //Generating Request to Google
        createRequest();

        //region Elements Inits
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mediaPlayer = MediaPlayer.create(this, R.raw.bg);
        mediaPlayer.setLooping(true);
        mediaPlayer.setVolume(0.5f, 0.5f);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        signInAccount = GoogleSignIn.getLastSignedInAccount(this);
        firebaseFirestore = FirebaseFirestore.getInstance();
        dialog = new Dialog(this);

        //endregion

        //region Action Toolbar
        navigationView.setItemIconTintList(null);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        drawerLayout.bringToFront();
        toggle.syncState();
        navigationView.setCheckedItem(R.id.menu_home);
        //endregion

        //region Accessing Navigation Drawer Header
        View header = navigationView.getHeaderView(0);
        soundMode = header.findViewById(R.id.app_sound_mode);
        //endregion



        //region TurnSound On/Off
        soundMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 //
                if (String.valueOf(soundMode.getTag()) == "Sound") {
                    soundMode.setImageResource(R.drawable.mute);
                    mediaPlayer.pause();
                    soundMode.setTag("Mute");
                    myEdit.putString("mode", "off");
                } else {
                    soundMode.setImageResource(R.drawable.speaker);
                    mediaPlayer.start();
                    soundMode.setTag("Sound");
                    myEdit.putString("mode", "on");
                }
                myEdit.commit();
                //Commit

            }
        });

        //endregion

        //region Check If User Already Exists in Database
        userID = mAuth.getCurrentUser().getUid();
        DocumentReference documentReference = firebaseFirestore.collection("USERS").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value.exists() && error == null) {
                    Toast.makeText(DashboardActivity.this, "Welcome!", Toast.LENGTH_SHORT).show();
                } else {
                    openDialog(); //For Future
                }
            }
        });
        //endregion

        //region Navigation Transfers
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.menu_home:
                        replaceFragment(new HomeFragment());
                        break;
                    case R.id.nav_leaderboard:
                        Toast.makeText(getApplicationContext(), "LeaderBoard Activity", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_logout:
                        mAuth.signOut();
                        mGoogleSignInClient.signOut();
                        startActivity(new Intent(DashboardActivity.this, IntroActivity.class));
                        finish();
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;

            }
        });
        navigationView.bringToFront();
        //endregion


        //endregion


        replaceFragment(new HomeFragment()); //as default Dashboard
    }


    private void createRequest() {
        mAuth = FirebaseAuth.getInstance();
        // Configure Google Sign In and give a popup
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        //Building SignInClient with options specified by geo this will help us to
        //create request from app to google to access gmails.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }


    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        fragmentTransaction.replace(R.id.main_container, fragment);
        fragmentTransaction.commit();
    }


    @Override
    protected void onResume() {
        super.onResume();
        music = sharedPreferences.getString("mode", "off");
        if(music.equals("off")){
            mediaPlayer.pause();
        }else{
            mediaPlayer.start();
        }
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

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    private void openDialog() {
        dialog.setContentView(R.layout.gender_dialog_box);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        LinearLayout male = dialog.findViewById(R.id.gender_male);
        LinearLayout female = dialog.findViewById(R.id.gender_female);
        dialog.show();


        male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userGender = "Male";
                dialog.dismiss();
                storeNewUsersData();
            }
        });

        female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userGender = "Female";
                dialog.dismiss();
                storeNewUsersData();
            }
        });
    }

    //region Logics to Check if user is new or not if new then store information to realtime database for more usability
    private void storeNewUsersData() {
        //region Storing Data of User to Firestore
        userID = mAuth.getCurrentUser().getUid();

        String pUrl = GoogleSignIn.getLastSignedInAccount(context).getPhotoUrl().toString();

        DocumentReference documentReference = firebaseFirestore.collection("USERS").document(userID);
        Map<String, Object> user = new HashMap<>();
        user.put("userName", signInAccount.getDisplayName());
        user.put("email", signInAccount.getEmail());
        user.put("gender", userGender);
        user.put("score", score);
        user.put("profile",pUrl);
        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                CommonHelper.userName = signInAccount.getDisplayName();
                CommonHelper.userGender = userGender;
                CommonHelper.userEmail = signInAccount.getEmail();
                CommonHelper.userScore = Integer.parseInt(String.valueOf(score));
                replaceFragment(new HomeFragment());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
        //endregion


    }


}
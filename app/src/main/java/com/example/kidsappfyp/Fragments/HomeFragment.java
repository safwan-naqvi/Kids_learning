package com.example.kidsappfyp.Fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.kidsappfyp.Activities.Animals.AnimalsActivity;
import com.example.kidsappfyp.Activities.Colors.ColorsActivity;
import com.example.kidsappfyp.Activities.GeoShapes.ShapesActivity;
import com.example.kidsappfyp.Activities.Numbers.NumbersActivity;
import com.example.kidsappfyp.Activities.Urdu.UrduActivity;
import com.example.kidsappfyp.Activities.Vegetables.FruitsActivity;
import com.example.kidsappfyp.Activities.Vegetables.VegetableActivity;
import com.example.kidsappfyp.Activities.VideoLearnActivity;
import com.example.kidsappfyp.Activities.VocalsAlphabet.VocalAndLetterActivity;
import com.example.kidsappfyp.HelperClasses.CommonHelper;
import com.example.kidsappfyp.R;
import com.example.kidsappfyp.Testing.QuizActivity;
import com.example.kidsappfyp.Testing.TestingMainActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.HashMap;
import java.util.Map;

public class HomeFragment extends Fragment implements View.OnClickListener {

    //region Initializing
    TextView userName, points;
    CircularImageView userProfileImage;
    CardView optionAlphabet, optionNumber, optionShape, optionAnimal, optionFnV, optionVeg, optionColor, optionVideoLearn, optionUrdu;


    //Firebase and bg
    FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    FirebaseFirestore firebaseFirestore;
    String userID;
    GoogleSignInAccount signInAccount;
    //endregion


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        createRequest();
        firebaseFirestore = FirebaseFirestore.getInstance();
        userName = view.findViewById(R.id.user_profile_name);
        userProfileImage = view.findViewById(R.id.user_profile_image);
        points = view.findViewById(R.id.user_points);
        //region init CardViews

        optionAlphabet = view.findViewById(R.id.option_letters);
        optionNumber = view.findViewById(R.id.option_numbers);
        optionShape = view.findViewById(R.id.option_shapes);
        optionAnimal = view.findViewById(R.id.option_animal);
        optionVideoLearn = view.findViewById(R.id.option_video_learning);
        optionFnV = view.findViewById(R.id.option_fruits);
        optionVeg = view.findViewById(R.id.option_vegetable);
        optionColor = view.findViewById(R.id.option_color);
        optionUrdu = view.findViewById(R.id.option_urdu);

        //endregion

        signInAccount = GoogleSignIn.getLastSignedInAccount(getContext());

        userID = mAuth.getCurrentUser().getUid();
        DocumentReference documentReference = firebaseFirestore.collection("USERS").document(userID);
        documentReference.addSnapshotListener(getActivity(), new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value.exists()) {

                    if (error != null) {
                        Log.i("appCheck", "Listen failed.", error);
                        return;
                    }

                    if (value != null && value.exists()) {
                        CommonHelper.userName = value.getString("userName");
                        CommonHelper.userGender = value.getString("gender");
                        CommonHelper.userEmail = value.getString("email");
                        CommonHelper.userScore = (long) value.get("score");
                        changeData(value.getString("profile"));
                    } else {
                        Log.i("appCheck", "Current data: null ");
                    }
                    if (value.getMetadata().hasPendingWrites()) {
                        changeData(value.getString("profile"));
                    }

                }
            }
        });


        optionAlphabet.setOnClickListener(this::onClick);
        optionAnimal.setOnClickListener(this::onClick);
        optionNumber.setOnClickListener(this::onClick);
        optionShape.setOnClickListener(this::onClick);
        optionFnV.setOnClickListener(this::onClick);
        optionVideoLearn.setOnClickListener(this::onClick);
        optionVeg.setOnClickListener(this::onClick);
        optionColor.setOnClickListener(this::onClick);
        optionUrdu.setOnClickListener(this::onClick);



        return view;
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
        mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);
    }

    private void changeData(String url) {
        if (signInAccount != null) {
            userName.setText(CommonHelper.userName);
            if (getActivity() != null) {
                Glide.with(getContext()).load(url) //6 - 11 - 2022 change this from signin user current image
                        .apply(new RequestOptions()
                                .fitCenter()
                                .format(DecodeFormat.PREFER_ARGB_8888)
                                .override(Target.SIZE_ORIGINAL))
                        .placeholder(R.drawable.boy)
                        .into(userProfileImage);
            }
        }
        points.setText("Points: " + CommonHelper.userScore);
    }

    Intent intent;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.option_animal:
                Intent intent2 = new Intent(getActivity(), AnimalsActivity.class);
                startActivity(intent2);
                ((Activity) getActivity()).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;
            case R.id.option_numbers:
                refreshSharedPreferenceForWelcome();
                intent = new Intent(getActivity(), NumbersActivity.class);
                startActivity(intent);
                ((Activity) getActivity()).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;
            case R.id.option_letters:
                intent = new Intent(getActivity(), VocalAndLetterActivity.class);
                startActivity(intent);
                ((Activity) getActivity()).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;
            case R.id.option_shapes:
                refreshSharedPreferenceForWelcome();
                intent = new Intent(getActivity(), ShapesActivity.class);
                startActivity(intent);
                ((Activity) getActivity()).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;
            case R.id.option_urdu:
                Intent intentx2 = new Intent(getActivity(), UrduActivity.class);
                startActivity(intentx2);
                ((Activity) getActivity()).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;
            case R.id.option_fruits:
                Intent i2 = new Intent(getActivity(), FruitsActivity.class);
                startActivity(i2);
                ((Activity) getActivity()).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;
            case R.id.option_vegetable:
                Intent i3 = new Intent(getActivity(), VegetableActivity.class);
                startActivity(i3);
                ((Activity) getActivity()).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;
            case R.id.option_color:
                Intent i4 = new Intent(getActivity(), ColorsActivity.class);
                startActivity(i4);
                ((Activity) getActivity()).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;
            case R.id.option_video_learning:
                Intent i5 = new Intent(getActivity(), VideoLearnActivity.class);
                startActivity(i5);
                ((Activity) getActivity()).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

            default:
                break;
        }
    }

    private void refreshSharedPreferenceForWelcome() {
        // Storing data into SharedPreferences
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("DisplayPanda", getContext().MODE_PRIVATE);

        // Creating an Editor object to edit(write to the file)
        SharedPreferences.Editor myEdit = sharedPreferences.edit();

        // Storing the key and its value as the data fetched from edittext
        myEdit.putBoolean("display", true);

        // Once the changes have been made,
        // we need to commit to apply those changes made,
        // otherwise, it will throw an error
        myEdit.commit();
    }


}


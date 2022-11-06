package com.example.kidsappfyp.Activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.kidsappfyp.Dashboard.DashboardActivity;
import com.example.kidsappfyp.R;
import com.github.drjacky.imagepicker.ImagePicker;
import com.github.drjacky.imagepicker.constant.ImageProvider;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

public class ProfileActivity extends AppCompatActivity {

    private Uri imageUri;
    ConstraintLayout progress_bar;
    TextView uploadImageText, userEmail, progressIndication;
    ImageView back;
    CircularImageView userDP;
    TextInputLayout username;
    Button updateBtn;
    FirebaseFirestore firebaseFirestore;
    private StorageReference UserImagesRef;
    String userName, url, DownloadImageUrl, _email;
    String _username;
    private String checker = "null";
    public static final String TAG = "ProfileEditActivity";
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_profile);

        initWidget();

        //region Launcher
        ActivityResultLauncher<Intent> launcher =
                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), (ActivityResult result) -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Log.e(TAG, "Activity Launcher For Image: Checking If Data is Coming or not " + result.getData());
                        imageUri = result.getData().getData();
                        userDP.setImageURI(imageUri);
                        // Use the uri to load the image
                    }else{
                        Log.e(TAG, "Activity Launcher For Image" + result.getData());
                    }
                });
        //endregion

        //region Fetch Data from Fire store if available otherwise get from Google Please change for user current
        DocumentReference documentReference = firebaseFirestore.collection("USERS").document(uid);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value.exists()) {

                    if (error != null) {
                        Log.i("appCheck", "Listen failed.", error);
                        return;
                    }

                    if (value != null) {
                        changeData(value);
                    } else {
                        Log.i("appCheck", "Current data: null ");
                    }
                }
            }
        });
        //endregion

        uploadImageText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checker = "checked";
                ImagePicker.Companion.with(ProfileActivity.this)
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

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _username = username.getEditText().getText().toString().trim();
                if (checker.equals("checked")) {
                    saveImageInFirebase();
                } else {
                    storeInfoInFirebase();
                }

            }
        });

    }

    private void saveImageInFirebase() {
        progress_bar.setVisibility(View.VISIBLE);
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
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progress_bar.setVisibility(View.INVISIBLE);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //if image is uploaded it will get that link of image to be stored in database

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            progressIndication.setVisibility(View.INVISIBLE);
                            progress_bar.setVisibility(View.INVISIBLE);
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
                            storeInfoInFirebase();
                        }
                        progress_bar.setVisibility(View.INVISIBLE);
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                progressIndication.setVisibility(View.VISIBLE);
                progressIndication.setText((int) progress + "% Uploaded");
            }
        });

        //endregion

    }

    private void storeInfoInFirebase() {
        //region Updating Database of admin
        HashMap<String, Object> profile = new HashMap<>();
        profile.put("userName", _username);
        if (checker.equals("checked")) {
            profile.put("profile", DownloadImageUrl);
        }
        firebaseFirestore.collection("USERS").document(uid).update(profile);
        //endregion
        //recreate();
        progress_bar.setVisibility(View.VISIBLE);
        progressIndication.setVisibility(View.VISIBLE);
        progressIndication.setText("Updated Successfully");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progress_bar.setVisibility(View.INVISIBLE);
                progressIndication.setVisibility(View.INVISIBLE);
            }
        }, 1000);
        startActivity(new Intent(ProfileActivity.this, DashboardActivity.class));
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    private void changeData(DocumentSnapshot value) {
        userName = value.getString("userName");
        url = value.getString("profile");
        _email = value.getString("email");

        username.getEditText().setText(userName);
        userEmail.setText(_email);
        Glide.with(this).load(url)
                .apply(new RequestOptions()
                        .fitCenter()
                        .format(DecodeFormat.PREFER_ARGB_8888)
                        .override(Target.SIZE_ORIGINAL))
                .into(userDP);
    }

    private void initWidget() {
        back = findViewById(R.id.goBackToUserActivity4);
        UserImagesRef = FirebaseStorage.getInstance().getReference().child("USERS IMAGE");
        firebaseFirestore = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        progress_bar = findViewById(R.id.admin_saving_info);
        progressIndication = findViewById(R.id.progress_percentage);
        userEmail = findViewById(R.id.user_email_tv);
        uploadImageText = findViewById(R.id.upload_user_image_tv);
        userDP = findViewById(R.id.userEditProfileImage);
        username = findViewById(R.id.et_user_profile_name);
        updateBtn = findViewById(R.id.user_update_profile);
    }

}
package com.quaser.edtechapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.quaser.edtechapp.Auth.AuthUtils;
import com.quaser.edtechapp.rest.api.APIMethods;
import com.quaser.edtechapp.rest.api.interfaces.APIResponseListener;
import com.quaser.edtechapp.rest.response.DataRp;
import com.quaser.edtechapp.utils.FileUtils;
import com.quaser.edtechapp.utils.Method;
import com.quaser.edtechapp.utils.Transformations.CircleTransform;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.File;

public class NameActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private EditText nameEt;
    private MaterialButton continueBtn;
    private MaterialButton skipBtn;

    private ConstraintLayout dpLayout;
    private ImageView dpImg;
    private ProgressBar dpProgressBar;

    private boolean isUpdatingDP = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);

        findViews();
        setListeners();
    }



    private void setListeners() {
        skipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startMainActivity();
            }
        });

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyName();
            }
        });

        dpLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isUpdatingDP){
                    selectPicture();
                }
            }
        });
    }

    private void selectPicture() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");

        startActivityForResult(Intent.createChooser(intent, "Select Profile Photo from"), 100);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        if (requestCode == 100
                && resultCode == Activity.RESULT_OK) {
            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();
                isUpdatingDP = true;
                dpProgressBar.setVisibility(View.VISIBLE);


                Picasso.get()
                        .load(uri)
                        .transform(new CircleTransform())
                        .into(dpImg);
                startUpload(uri);
            }
        }
    }
    
    String prevImage = "";
    boolean updatesMade = false;

    boolean uploadedToFB = false;
    boolean uploadedToServer = false;
    private void startUpload(Uri uri) {
        uploadedToFB = false;
        uploadedToServer = false;

        //Uploading to firebase
        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                .setPhotoUri(uri)
                .build();

        FirebaseAuth.getInstance().getCurrentUser().updateProfile(request)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            uploadedToFB = true;
                        } else {
                            Toast.makeText(NameActivity.this, "Auth change DP Failed!", Toast.LENGTH_SHORT).show();
                            dpProgressBar.setVisibility(View.GONE);
                        }
                        hideProgress();
                    }
                });


        //Uploading to our servers
        String extension = FileUtils.getExtension(uri, this);
        String encodedDP = FileUtils.getEncodedImage(uri, this);
        
        APIMethods.updateDP(encodedDP, extension, new APIResponseListener<DataRp>() {
            @Override
            public void success(DataRp response) {
                updatesMade = true;
                uploadedToServer = true;
                dpProgressBar.setVisibility(View.GONE);
                isUpdatingDP = false;
                prevImage = response.getLink();
                hideProgress();
            }

            @Override
            public void fail(String code, String message, String redirectLink, boolean retry, boolean cancellable) {
                dpProgressBar.setVisibility(View.GONE);
                isUpdatingDP = false;
                if (prevImage != null && !prevImage.isEmpty()){
                    Picasso.get()
                            .load(prevImage)
                            .into(dpImg);
                } else {
                    Picasso.get()
                            .load(R.drawable.ic_display_picture)
                            .into(dpImg);
                }
                Toast.makeText(NameActivity.this, "DP update failed!", Toast.LENGTH_SHORT).show();
                Method.showFailedAlert(NameActivity.this
                , code + " - " + message);
            }
        });
    }

    private void hideProgress() {
        if (uploadedToServer && uploadedToFB){
            dpProgressBar.setVisibility(View.GONE);
            Toast.makeText(NameActivity.this, "Profile Picture updated successfully!", Toast.LENGTH_SHORT).show();
        }
    }

    boolean changedOnServer = false;
    boolean changedOnFirebase = false;

    private void verifyName() {
        if (nameEt.getText().toString().isEmpty())
            nameEt.setError("This is required");
        else {

            if (isUpdatingDP){
                Toast.makeText(this, "Please wait which photo is being uploaded!", Toast.LENGTH_SHORT).show();
                return;
            }

            startProgress();
            nameEt.setError(null);

            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(nameEt.getText().toString())
                    .build();
            FirebaseAuth
                    .getInstance()
                    .getCurrentUser()
                    .updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                changedOnFirebase = true;
                                completeNameChange();
                            }else{
                                stopProgress();
                                Method.showFailedAlert(NameActivity.this, "Unable to update name on auths");
                            }
                        }
                    });
            APIMethods.changeName(nameEt.getText().toString(), new APIResponseListener<JSONObject>() {
                @Override
                public void success(JSONObject response) {
                    changedOnServer = true;
                    completeNameChange();
                }

                @Override
                public void fail(String code, String message, String redirectLink, boolean retry, boolean cancellable) {
                    stopProgress();
                    Method.showFailedAlert(NameActivity.this, code + " - " + message);
                }
            });
        }
    }

    private void completeNameChange() {
        if (changedOnFirebase && changedOnServer)
            startMainActivity();
    }

    private void startMainActivity() {
        AuthUtils.nameAdded(this);
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("isNewUser", true);
        startActivity(i);
        this.finish();
    }

    private void findViews() {
        progressBar = findViewById(R.id.progressBar);
        nameEt = findViewById(R.id.nameEt);
        continueBtn = findViewById(R.id.continueBtn);
        skipBtn = findViewById(R.id.skipBtn);

        dpLayout = findViewById(R.id.dpLayout);
        dpImg = findViewById(R.id.display_picture);
        dpProgressBar = findViewById(R.id.dp_progress);
    }

    private void startProgress(){
        progressBar.setVisibility(View.VISIBLE);
        disable();
    }

    private void stopProgress(){
        progressBar.setVisibility(View.GONE);
        enable();
    }


    private void enable(){
        nameEt.setEnabled(true);
        continueBtn.setEnabled(true);
        skipBtn.setEnabled(true);
    }

    private void disable(){
        nameEt.setEnabled(false);
        continueBtn.setEnabled(false);
        skipBtn.setEnabled(false);
    }

}
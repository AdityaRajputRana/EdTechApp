package com.quaser.edtechapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.quaser.edtechapp.Auth.AuthUtils;
import com.quaser.edtechapp.rest.api.APIMethods;
import com.quaser.edtechapp.rest.api.interfaces.APIResponseListener;
import com.quaser.edtechapp.utils.Method;

import org.json.JSONObject;

public class NameActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private EditText nameEt;
    private MaterialButton continueBtn;
    private MaterialButton skipBtn;

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
    }

    boolean changedOnServer = false;
    boolean changedOnFirebase = false;

    private void verifyName() {
        if (nameEt.getText().toString().isEmpty())
            nameEt.setError("This is required");
        else {

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
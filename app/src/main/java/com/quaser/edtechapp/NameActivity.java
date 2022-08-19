package com.quaser.edtechapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.material.button.MaterialButton;
import com.quaser.edtechapp.Auth.AuthUtils;

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

    private void verifyName() {
        if (nameEt.getText().toString().isEmpty())
            nameEt.setError("This is required");
        else {
            nameEt.setError(null);
            startProgress();

        }
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
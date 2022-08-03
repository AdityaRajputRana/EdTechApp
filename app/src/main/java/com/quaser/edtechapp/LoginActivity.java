package com.quaser.edtechapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.quaser.edtechapp.rest.api.API;
import com.quaser.edtechapp.rest.api.HashUtils;
import com.quaser.edtechapp.rest.api.VolleyClient;
import com.quaser.edtechapp.rest.api.interfaces.AnonymousResListener;
import com.quaser.edtechapp.rest.response.AnonymousRP;
import com.quaser.edtechapp.utils.Method;

public class LoginActivity extends AppCompatActivity {

    private ConstraintLayout phoneLayout;
    private ConstraintLayout otpLayout;

    private EditText phoneEt;
    private TextView countryCodeTxt;
    private EditText otpEt;

    private ProgressBar progressBar;
    private TextView messageTxt;

    private int screenState = 0;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViews();
        setVisibility();
        setUpBackBtn();
        setOnClickListener();
        initialiseVolley();
    }

    private void initialiseVolley() {
        requestQueue = VolleyClient.getRequestQueue();
    }

    private void setOnClickListener() {
        findViewById(R.id.continueBtn)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        confirmPhoneNumber();
                    }
                });

        findViewById(R.id.skipBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginAnonymously();
            }
        });
    }

    private void loginAnonymously() {
        startProgress("Signing you in anonymously");
        phoneLayout.setVisibility(View.GONE);
        API.signInAnonymously(new AnonymousResListener() {
            @Override
            public void success(AnonymousRP response) {
                Toast.makeText(LoginActivity.this, "Signed in anonymously", Toast.LENGTH_SHORT).show();
                startMainActivity(response.getUserId(), "User", "anonymous");
            }

            @Override
            public void fail(String code, String message, String redirectLink, boolean retry, boolean cancellable) {
                Method.showFailedAlert(LoginActivity.this, message);
                phoneLayout.setVisibility(View.VISIBLE);
                stopProgress();
            }
        }, HashUtils.getHashedData("{}"));
    }

    private void startMainActivity(String userId, String name, String userType) {
        SharedPreferences.Editor editor = this.getSharedPreferences("EdTech", MODE_PRIVATE).edit();
        editor.putBoolean("user/isLoggedIn", true);
        editor.putString("user/id", userId);
        editor.putString("user/name", name);
        editor.putString("user/type", userType);
        editor.commit();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }

    private void confirmPhoneNumber() {
        String phoneNum = phoneEt.getText().toString();
        if (phoneNum.length() != 10){
            phoneEt.setError("Phone number should be of 10 digits");
        } else {
            phoneEt.setError(null);
            sendOtp();
        }
    }

    private void sendOtp() {
        startProgress();
        phoneLayout.setVisibility(View.GONE);

    }

    private void startProgress() {
        startProgress("");
    }

    private void startProgress(String s) {
        if (!s.isEmpty()){
            messageTxt.setText(s);
        }
        messageTxt.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void stopProgress() {
        messageTxt.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
    }

    private void setUpBackBtn() {
        findViewById(R.id.backBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        switch (screenState){
            case 1:
                phoneLayout.setVisibility(View.VISIBLE);
                otpLayout.setVisibility(View.GONE);
                screenState--;
                break;
            default:
                super.onBackPressed();
        }
    }

    private void findViews() {
        phoneLayout = findViewById(R.id.phoneLayout);
        otpLayout = findViewById(R.id.otpLayout);

        countryCodeTxt = findViewById(R.id.countryCodeTxt);
        phoneEt = findViewById(R.id.phoneNumberEt);
        //Todo: find otp et

        messageTxt = findViewById(R.id.messageTxt);
        progressBar = findViewById(R.id.progressBar);
    }

    private void setVisibility() {
        phoneLayout.setVisibility(View.VISIBLE);
        otpLayout.setVisibility(View.GONE);
    }

    public void showWhyPopUp(View view) {
        //Todo: improve UI for popup

        new AlertDialog.Builder(this)
                .setTitle("Why login?")
                .setMessage("Login is required to sync your course progress across devices, payments and subscribe to events that are tagged to your name.")
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setCancelable(true)
                .show();
    }
}
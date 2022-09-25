package com.quaser.edtechapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;
import com.quaser.edtechapp.Auth.AuthUtils;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);

        super.onCreate(savedInstanceState);
        SharedPreferences preferences =  this.getSharedPreferences("EdTech", MODE_PRIVATE);
        boolean isOnboarded = preferences.getBoolean("isOnboarded",false);

        FirebaseMessaging.getInstance().subscribeToTopic("all");
        if (isOnboarded){
            boolean isLoggedIn = AuthUtils.getInstance().isLoggedIn();
            if (isLoggedIn) {
                if (!AuthUtils.isNameAdded(this))
                    startActivity(new Intent(this, NameActivity.class));
                else
                    startActivity(new Intent(this, MainActivity.class));
            } else {
                startActivity(new Intent(this, LoginActivity.class));
            }
        } else {
            startActivity(new Intent(this, OnBoardingActivity.class));
        }
        this.finish();
    }
}
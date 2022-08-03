package com.quaser.edtechapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);

        super.onCreate(savedInstanceState);
        SharedPreferences preferences =  this.getSharedPreferences("EdTech", MODE_PRIVATE);
        Boolean isOnboarded = preferences.getBoolean("isOnboarded",false);

        if (isOnboarded){
            Boolean isLoggedIn = preferences.getBoolean("user/isLoggedIn", false);
            if (isLoggedIn) {
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
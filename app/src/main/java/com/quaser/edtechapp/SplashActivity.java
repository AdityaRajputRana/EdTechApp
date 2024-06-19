package com.quaser.edtechapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.splashscreen.SplashScreen;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import com.framgia.android.emulator.EmulatorDetector;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;
import com.quaser.edtechapp.Auth.AuthUtils;
import com.quaser.edtechapp.utils.EmulatorUtil;
import com.quaser.edtechapp.utils.RootUtil;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        runApp();

        super.onCreate(savedInstanceState);

    }

    private void runApp() {
        SharedPreferences preferences =  this.getSharedPreferences("EdTech", MODE_PRIVATE);
        boolean isOnboarded = preferences.getBoolean("isOnboarded",false);

        FirebaseMessaging.getInstance().subscribeToTopic("all");
        if (isOnboarded){
            boolean isLoggedIn = AuthUtils.getInstance().isLoggedIn();
            if (isLoggedIn) {
                if (!AuthUtils.isDeviceIdVerified(this)){
                    startDeviceChangeActivity();
                    return;
                }
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

    private void startDeviceChangeActivity() {
        startActivity(new Intent(
                this, DeviceChangeActivity.class
        ));
        this.finish();
    }
}
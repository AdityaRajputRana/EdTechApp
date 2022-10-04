package com.quaser.edtechapp.PersonalityTest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.quaser.edtechapp.R;


public class PersonalityActivity extends AppCompatActivity {

    LinearLayout appBarLayout;
    FrameLayout frameLayout;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personality);

        findViews();
        setListeners();
        fetchPersonality();
    }

    private void fetchPersonality() {

    }

    private void findViews() {
        appBarLayout = findViewById(R.id.appBarLayout);
        frameLayout = findViewById(R.id.frameLayout);
        progressBar = findViewById(R.id.progressBar);
    }

    private void setListeners() {
        findViewById(R.id.backBtn).setOnClickListener(view -> onBackPressed());
    }
}
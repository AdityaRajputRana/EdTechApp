package com.quaser.edtechapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.google.gson.Gson;
import com.quaser.edtechapp.rest.response.UnitRP;

public class LessonActivity extends AppCompatActivity {

    private UnitRP unitRP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);

        if (getIntent().getBooleanExtra("isUnitAttached", false)){
            showLessonUI();
            fetchCompletedLesson();
        } else {
            Toast.makeText(this, "Unit not attached!", Toast.LENGTH_SHORT).show(); //Todo handle this
        }
    }

    private void showLessonUI() {
        String unitStr = getIntent().getStringExtra("unitRP");
        unitRP = new Gson().fromJson(unitStr, UnitRP.class);

    }

    private void fetchCompletedLesson() {

    }
}
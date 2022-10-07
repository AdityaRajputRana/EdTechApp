package com.quaser.edtechapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.quaser.edtechapp.Interface.LessonListener;
import com.quaser.edtechapp.Interface.RevLessonInterface;
import com.quaser.edtechapp.LessonFragments.AssignmentFragment;
import com.quaser.edtechapp.models.ShortUnit;
import com.quaser.edtechapp.rest.response.AssignmentRP;

public class FrameActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frame);

        if (getIntent().getStringExtra("type").equals("assignment")){
            setUpAssignmentLayout();
        }
    }


    private void setUpAssignmentLayout() {
        setUpAppBar("Your Assignment");
        AssignmentRP assignmentRP = new Gson().fromJson(getIntent().getStringExtra("assignment"), AssignmentRP.class);
        LessonListener listener = new LessonListener() {
            @Override
            public void nextLesson() {
                launchUnitActivity(assignmentRP.getUnit_id(), assignmentRP.getUnit_title());
            }

            @Override
            public void fullScreen(RevLessonInterface listener) {

            }

            @Override
            public void revFullScreen() {

            }
        };
        AssignmentFragment fragment = new AssignmentFragment(assignmentRP.getUnit_id(), assignmentRP, true, listener);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.frameLayout, fragment)
                .commit();
    }

    private void launchUnitActivity(String unit_id, String unit_title) {
        Intent intent = new Intent(this, UnitActivity.class);
        ShortUnit shortUnit = new ShortUnit();
        shortUnit.set_id(unit_id);
        shortUnit.setUnit_title(unit_title);
        intent.putExtra("SHORT_UNIT", new Gson().toJson(shortUnit));
        startActivity(intent);
    }


    private void setUpAppBar(String title){
        TextView titleTxt = findViewById(R.id.titleTxt);
        titleTxt.setText(title);
        findViewById(R.id.appBarLayout).setVisibility(View.VISIBLE);
        findViewById(R.id.backBtn).setOnClickListener(view -> onBackPressed());
    }
}
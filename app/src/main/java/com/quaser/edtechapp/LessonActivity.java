package com.quaser.edtechapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.quaser.edtechapp.Interface.LessonListener;
import com.quaser.edtechapp.LessonFragments.VideoFragment;
import com.quaser.edtechapp.models.ShortLesson;
import com.quaser.edtechapp.rest.response.UnitRP;
import com.warkiz.tickseekbar.TickSeekBar;

public class LessonActivity extends AppCompatActivity implements LessonListener {

    //Todo: Don't let user open locked items.

    private UnitRP unitRP;
    private TextView unitTitleTxt;
    private TickSeekBar seekBar;

    int currentIndex = 0;
    ShortLesson shortLesson;
    String TAG = "lessonFragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);

        findViews();
        if (getIntent().getBooleanExtra("isUnitAttached", false)){
            showLessonUI();
            setUpLesson();
            fetchCompletedLesson();
        } else {
            Toast.makeText(this, "Unit not attached!", Toast.LENGTH_SHORT).show(); //Todo handle this
        }
    }

    private void setUpLesson() {
        currentIndex = unitRP.getCompleted_lessons();
        shortLesson = unitRP.getLesson().get(currentIndex);

        //Todo handle pre-reqs here
        switch (shortLesson.getType()){
            case "video":
                VideoFragment videoFragment = new VideoFragment(this, shortLesson);
                getSupportFragmentManager().beginTransaction().add(R.id.frameLayout, videoFragment, TAG)
                        .commit();
                break;
            default:
                Toast.makeText(this, "Unidentified type.", Toast.LENGTH_SHORT).show();
        }
    }

    private void findViews() {
        unitTitleTxt = findViewById(R.id.titleTxt);
        seekBar = findViewById(R.id.lessonProgressBar);
        findViewById(R.id.backBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void showLessonUI() {
        String unitStr = getIntent().getStringExtra("unitRP");
        unitRP = new Gson().fromJson(unitStr, UnitRP.class);

        unitTitleTxt.setText(unitRP.getUnit_title());
        seekBar.setTickCount(unitRP.getTotal_lessons());
        seekBar.setMax(100f);
        int totalDivs = unitRP.getTotal_lessons() - 1;
        if (totalDivs <= 0)
            totalDivs=1;
        float progress = (float) ((unitRP.getCompleted_lessons()*100)/totalDivs);
        //Todo: if progress 100 then change thumb to tick icon
        //Todo: if some new lessons are added/moved in between then show them as incomplete. Handle that.
        Log.i("seekbarProgress", String.valueOf(progress));
        seekBar.setProgress(progress);
        Log.i("seekbar", String.valueOf(seekBar.getProgress()));
    }

    private void fetchCompletedLesson() {

    }

    @Override
    public void nextLesson() {
        
    }

    @Override
    public void fullScreen() {

    }
}
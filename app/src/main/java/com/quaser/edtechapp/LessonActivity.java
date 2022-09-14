package com.quaser.edtechapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.quaser.edtechapp.Helpers.PaymentHelper;
import com.quaser.edtechapp.Interface.LessonListener;
import com.quaser.edtechapp.Interface.RevLessonInterface;
import com.quaser.edtechapp.LessonFragments.*;
import com.quaser.edtechapp.models.ShortLesson;
import com.quaser.edtechapp.rest.requests.VerifyLessonPaymentReq;
import com.quaser.edtechapp.rest.response.UnitRP;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultListener;
import com.razorpay.PaymentResultWithDataListener;
import com.warkiz.tickseekbar.TickSeekBar;

public class LessonActivity extends AppCompatActivity implements LessonListener, PaymentResultWithDataListener {

    //Todo: Don't let user open locked items.

    private UnitRP unitRP;
    private TextView unitTitleTxt;
    private TickSeekBar seekBar;

    int currentIndex = 0;
    ShortLesson shortLesson;
    String TAG = "lessonFragment";

    private LinearLayout appBar;

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
                VideoFragment videoFragment = new VideoFragment(this, shortLesson, unitRP.get_id());
                getSupportFragmentManager().beginTransaction().add(R.id.frameLayout, videoFragment, TAG)
                        .commit();
                break;
            case "article":
                ArticleFragment articleFragment = new ArticleFragment(unitRP.get_id(), shortLesson, this);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.frameLayout, articleFragment, TAG)
                        .commit();
                break;
            case "test":
                TestFragment testFragment
                        = new TestFragment(unitRP.get_id(), shortLesson, this);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.frameLayout, testFragment, TAG)
                        .commit();
                break;
            case "assignment":
                AssignmentFragment assignmentFragment =
                        new AssignmentFragment(unitRP.get_id(), shortLesson, this);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.frameLayout, assignmentFragment, TAG)
                        .commit();
                break;
            case "event":
                EventsFragment eventsFragment =
                        new EventsFragment(unitRP.get_id(), shortLesson, this);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.frameLayout, eventsFragment, TAG)
                        .commit();
                break;
            case "payment":
                PaymentFragment paymentFragment =
                        new PaymentFragment(unitRP.get_id(), shortLesson, this, getApplicationContext());
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.frameLayout, paymentFragment, TAG)
                        .commit();
                break;
            default:
                Toast.makeText(this, "Unidentified type.", Toast.LENGTH_SHORT).show();
        }
    }


    private void findViews() {
        unitTitleTxt = findViewById(R.id.titleTxt);
        seekBar = findViewById(R.id.lessonProgressBar);
        appBar = findViewById(R.id.appBar);
        findViewById(R.id.backBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private int SCREEN_STATE = 0;

    @Override
    public void onBackPressed() {
        switch (SCREEN_STATE){
            case 1:
                reverseFullScreen();
                break;
            default:
                super.onBackPressed();
        }
    }

    @Override
    public void revFullScreen(){
        SCREEN_STATE = 0;
        appBar.setVisibility(View.VISIBLE);
        seekBar.setVisibility(View.VISIBLE);
        toggleFullScreen(false);
    }

    private void reverseFullScreen() {
        SCREEN_STATE = 0;
        appBar.setVisibility(View.VISIBLE);
        seekBar.setVisibility(View.VISIBLE);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        toggleFullScreen(false);
        if (lessonInterface != null)
            lessonInterface.reverseFullScreen();
    }

    public void toggleFullScreen(boolean goFullScreen){
        WindowInsetsControllerCompat windowInsetsController =
                ViewCompat.getWindowInsetsController(getWindow().getDecorView());
        if (windowInsetsController == null) {
            return;
        }

        if (goFullScreen) {
            windowInsetsController.setSystemBarsBehavior(
                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            );
            windowInsetsController.hide(WindowInsetsCompat.Type.systemBars());
        }
        else
            windowInsetsController.show(WindowInsetsCompat.Type.systemBars());

    }

    private void showLessonUI() {
        if (unitRP == null) {
            String unitStr = getIntent().getStringExtra("unitRP");
            unitRP = new Gson().fromJson(unitStr, UnitRP.class);
        }

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
        //Todo: Instead of indexes, make use of last lesson and make a mechanism to do newly added lesson in between
        Log.i("LessonOldCompleted", String.valueOf(unitRP.getCompleted_lessons()));
        unitRP.setCompleted_lessons(unitRP.getCompleted_lessons()+1);
        Log.i("LessonNewCompleted", String.valueOf(unitRP.getCompleted_lessons()));
        getSupportFragmentManager()
                .beginTransaction()
                .remove(getSupportFragmentManager().findFragmentByTag(TAG))
                .commit();
        showLessonUI();
        setUpLesson();
    }

    RevLessonInterface lessonInterface;

    @Override
    public void fullScreen(RevLessonInterface lessonInterface) {
        toggleFullScreen(true);
        appBar.setVisibility(View.GONE);
        seekBar.setVisibility(View.GONE);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        this.lessonInterface = lessonInterface;
        SCREEN_STATE=1;
    }



    @Override
    public void onPaymentSuccess(String s, PaymentData paymentData) {
        PaymentHelper.getInstance().success(new Gson().toJson(
                new VerifyLessonPaymentReq(paymentData)

        ));
    }

    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {
        PaymentHelper.getInstance().fail(s);
    }
}
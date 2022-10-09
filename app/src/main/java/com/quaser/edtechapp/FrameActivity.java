package com.quaser.edtechapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.quaser.edtechapp.Helpers.PaymentHelper;
import com.quaser.edtechapp.Interface.LessonListener;
import com.quaser.edtechapp.Interface.RevLessonInterface;
import com.quaser.edtechapp.LessonFragments.ArticleFragment;
import com.quaser.edtechapp.LessonFragments.AssignmentFragment;
import com.quaser.edtechapp.LessonFragments.EventsFragment;
import com.quaser.edtechapp.LessonFragments.PaymentFragment;
import com.quaser.edtechapp.LessonFragments.TestFragment;
import com.quaser.edtechapp.LessonFragments.VideoFragment;
import com.quaser.edtechapp.models.ShortLesson;
import com.quaser.edtechapp.models.ShortUnit;
import com.quaser.edtechapp.rest.requests.VerifyLessonPaymentReq;
import com.quaser.edtechapp.rest.response.AssignmentRP;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultListener;
import com.razorpay.PaymentResultWithDataListener;

public class FrameActivity extends AppCompatActivity implements LessonListener, PaymentResultWithDataListener {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frame);

        if (getIntent().getStringExtra("type").equals("assignment")){
            setUpAssignmentLayout();
        } else if (getIntent().getStringExtra("type").equals("SHORT_LESSON")){
            setUpShortLesson();
        }
    }

    final String TAG = "FrameActivityFrag";

    private void setUpShortLesson() {
        ShortLesson shortLesson = new Gson().fromJson(
                getIntent().getStringExtra("SHORT_LESSON"), ShortLesson.class
        );
        
        String unitID = getIntent().getStringExtra("unit_id");

        //Todo handle pre-reqs here
        switch (shortLesson.getType()){
            case "video":
                VideoFragment videoFragment = new VideoFragment(this, shortLesson, unitID);
                getSupportFragmentManager().beginTransaction().add(R.id.frameLayout, videoFragment, TAG)
                        .commit();
                break;
            case "article":
                ArticleFragment articleFragment = new ArticleFragment(unitID, shortLesson, this);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.frameLayout, articleFragment, TAG)
                        .commit();
                break;
            case "test":
                TestFragment testFragment
                        = new TestFragment(unitID, shortLesson, this);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.frameLayout, testFragment, TAG)
                        .commit();
                break;
            case "assignment":
                AssignmentFragment assignmentFragment =
                        new AssignmentFragment(unitID, shortLesson, this);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.frameLayout, assignmentFragment, TAG)
                        .commit();
                break;
            case "event":
                EventsFragment eventsFragment =
                        new EventsFragment(unitID, shortLesson, this);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.frameLayout, eventsFragment, TAG)
                        .commit();
                break;
            case "payment":
                PaymentFragment paymentFragment =
                        new PaymentFragment(unitID, shortLesson, this, getApplicationContext());
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.frameLayout, paymentFragment, TAG)
                        .commit();
                break;
            default:
                Toast.makeText(this, "Unidentified type.", Toast.LENGTH_SHORT).show();
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

    @Override
    public void nextLesson() {
        getSupportFragmentManager()
                .beginTransaction()
                .remove(getSupportFragmentManager().findFragmentByTag(TAG))
                .commit();
        FrameActivity.this.finish();
    }

    RevLessonInterface lessonInterface;



    @Override
    public void fullScreen(RevLessonInterface lessonInterface) {
        toggleFullScreen(true);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        this.lessonInterface = lessonInterface;
        SCREEN_STATE=1;
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
        toggleFullScreen(false);
    }

    private void reverseFullScreen() {
        SCREEN_STATE = 0;
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
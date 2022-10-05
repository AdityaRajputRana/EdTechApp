package com.quaser.edtechapp.PersonalityTest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.quaser.edtechapp.Fragments.ProfileFrament;
import com.quaser.edtechapp.LessonFragments.TestFragment;
import com.quaser.edtechapp.R;
import com.quaser.edtechapp.models.ShortLesson;
import com.quaser.edtechapp.rest.api.APIMethods;
import com.quaser.edtechapp.rest.api.interfaces.APIResponseListener;
import com.quaser.edtechapp.utils.Method;


public class PersonalityActivity extends AppCompatActivity {

    LinearLayout appBarLayout;
    FrameLayout frameLayout;
    ProgressBar progressBar;
    ResultFragment resultFragment;
    TestFragment testFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personality);

        findViews();
        setListeners();
        fetchPersonality();
    }

    private void fetchPersonality() {
        APIMethods.getPersonalityTest(new APIResponseListener<PersonalityTestRP>() {
            @Override
            public void success(PersonalityTestRP response) {
                processResponse(response);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void fail(String code, String message, String redirectLink, boolean retry, boolean cancellable) {
                progressBar.setVisibility(View.GONE);
                Method.showFailedAlert(PersonalityActivity.this, code + " - " + message);
            }
        });
    }

    private void processResponse(PersonalityTestRP response) {
        if (response.is_test_attempted){
            showResultFragments(response.getPersonality());
        } else {
            showStartTestFragment(response);
        }
    }

    private void showStartTestFragment(PersonalityTestRP res) {
        removeFragments();
        appBarLayout.setVisibility(View.GONE);
        frameLayout.setVisibility(View.VISIBLE);
        ShortLesson shortLesson = new ShortLesson();
        shortLesson.setType("PersonalityTest");
        shortLesson.setName("Personality Test");
        if (res.title != null && !res.title.isEmpty())
            shortLesson.setName(res.title);
        if (res.message != null && !res.message.isEmpty())
            shortLesson.setShort_description(res.message);
        testFragment = new TestFragment(shortLesson, this::showResultFragments);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.frameLayout, testFragment)
                .commit();
    }

    private void showResultFragments(EndPersonalityTestRP personalityTestRP){
        removeFragments();
        resultFragment = new ResultFragment(personalityTestRP);
        appBarLayout.setVisibility(View.VISIBLE);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.frameLayout, resultFragment)
                .commit();
        frameLayout.setVisibility(View.VISIBLE);
    }

    private void removeFragments() {
        if (resultFragment != null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .remove(resultFragment)
                    .commit();
            resultFragment = null;
        }
        if (testFragment != null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .remove(testFragment)
                    .commit();
            testFragment = null;
        }
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
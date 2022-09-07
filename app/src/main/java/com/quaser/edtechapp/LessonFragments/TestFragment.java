package com.quaser.edtechapp.LessonFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.quaser.edtechapp.Interface.LessonListener;
import com.quaser.edtechapp.R;
import com.quaser.edtechapp.models.ShortLesson;
import com.quaser.edtechapp.rest.api.API;
import com.quaser.edtechapp.rest.api.APIMethods;
import com.quaser.edtechapp.rest.api.interfaces.APIResponseListener;
import com.quaser.edtechapp.rest.response.TestLessonRP;
import com.quaser.edtechapp.utils.Method;

public class TestFragment extends Fragment {

    TestLessonRP testLesson;

    String unitId;
    ShortLesson shortLesson;
    LessonListener listener;

    //BriefLayout
    LinearLayout briefLayout;
    TextView bTitleTxt;
    TextView bHeadTxt;
    TextView bBodyTxt;
    TextView bQuestionsTxt;
    TextView bTimeTxt;
    LinearLayout bQuestionsLayout;
    LinearLayout bTimeLayout;
    MaterialButton startBtn;
    ProgressBar bProgressBar;




    public TestFragment(String unitId, ShortLesson shortLesson, LessonListener listener){
        this.unitId = unitId;
        this.shortLesson = shortLesson;
        this.listener = listener;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_test, container, false);
        findViews(view);
        setUpTitles();
        fetchTest();
        return view;
    }

    private void fetchTest() {
        APIMethods.getLesson(shortLesson.getId(), TestLessonRP.class, new APIResponseListener<TestLessonRP>() {

            @Override
            public void success(TestLessonRP response) {
                bProgressBar.setVisibility(View.GONE);
                testLesson = response;
                showFullLesson();
            }

            @Override
            public void fail(String code, String message, String redirectLink, boolean retry, boolean cancellable) {
                Method.showFailedAlert(getActivity(), "Failed: "
                        + code+" - " + message);
                bProgressBar.setVisibility(View.GONE);
            }
        });
    }

    private void showFullLesson() {
        bHeadTxt.setText(testLesson.getTitle());
        if (testLesson.getDescription() != null
        && !testLesson.getDescription().isEmpty()){
            bBodyTxt.setText(testLesson.getDescription());
        }
        bQuestionsTxt.setText(testLesson.getNum_questions()
         + " Questions");
        bTimeTxt.setText(getTime(testLesson.getTime_allowed()));
        
        bQuestionsLayout.setVisibility(View.VISIBLE);
        bTimeLayout.setVisibility(View.VISIBLE);
        startBtn.setVisibility(View.VISIBLE);
        
        startBtn.setOnClickListener(view -> startTest());
    }

    private void startTest() {
        briefLayout.setVisibility(View.GONE);
    }

    private String getTime(int seconds) {
        int minutes = (int) (seconds/60);
        int hours = (int) (minutes/60);

        int days = (int) (hours/24);
        int months = (int) (days/30.5);
        int years = (int) (months/12);

        if (seconds<60){
            return String.valueOf(seconds) + " seconds";
        } else if (minutes<60){
            return String.valueOf(minutes) + " minutes";
        } else if (hours<24){
            return String.valueOf(hours) + " hours";
        } else if (days<31){
            return String.valueOf(days) + " days";
        } else if (months<12){
            return String.valueOf(months) + " months";
        } else {
            return String.valueOf(years) + " years";
        }


    }

    private void setUpTitles() {
        if (shortLesson.getName() != null
                && !shortLesson.getName().isEmpty()) {
            bHeadTxt.setText(shortLesson.getName());
            bHeadTxt.setVisibility(View.VISIBLE);
            bHeadTxt.setText(shortLesson.getName());
        }

        if (shortLesson.getShort_description() != null
                && !shortLesson.getShort_description().isEmpty()) {
            bBodyTxt.setText(shortLesson.getShort_description());
            bBodyTxt.setVisibility(View.VISIBLE);
        }
    }

    private void findViews(View view) {
        briefLayout = view.findViewById(R.id.briefLayout);
        bTitleTxt = view.findViewById(R.id.testHead);
        bHeadTxt = view.findViewById(R.id.head);
        bBodyTxt = view.findViewById(R.id.descriptionTxt);
        bQuestionsLayout = view.findViewById(R.id.questionLL);
        bTimeLayout = view.findViewById(R.id.timeLL);
        bTimeTxt = view.findViewById(R.id.numTimeTxt);
        bQuestionsTxt = view.findViewById(R.id.numQuestionTxt);
        bProgressBar = view.findViewById(R.id.briefProgress);
        startBtn = view.findViewById(R.id.startTestBtn);
    }
}
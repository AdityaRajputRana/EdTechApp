package com.quaser.edtechapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.gson.Gson;
import com.quaser.edtechapp.Adapter.ViewQuestionRVAdapter;
import com.quaser.edtechapp.rest.api.APIMethods;
import com.quaser.edtechapp.rest.api.interfaces.APIResponseListener;
import com.quaser.edtechapp.rest.response.QuestionRP;
import com.quaser.edtechapp.utils.Method;

public class ViewQuestionActivity extends AppCompatActivity {

    LinearLayout appBar;
    RecyclerView recyclerView;
    LinearLayoutManager manager;
    ViewQuestionRVAdapter adapter;
    TextView titleTxt;
    ExtendedFloatingActionButton addBtn;
//    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_question);

        findViews();
        if (getIntent().getBooleanExtra("hasQuestionAttached", false))
            showQuestion();
        else
            fetchQuestion();

        attachScrollListenerToRV();
    }

    private void attachScrollListenerToRV() {
        findViewById(R.id.backBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        recyclerView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                Log.i("Scroll", "happened");
                if (manager.findFirstVisibleItemPosition() == 0){
                    if (appBar.getVisibility() == View.VISIBLE){
                        Log.i("Scroll", "0");
                        appBar.setVisibility(View.INVISIBLE);
                    }
                }

                if (manager.findFirstVisibleItemPosition() == 1){
                    Log.i("Scroll", "1");
                    if (appBar.getVisibility() != View.VISIBLE){
                        appBar.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    private void fetchQuestion() {
        //Todo fetch question
        String questionId ="";
        if (getIntent().getBooleanExtra("hasQuestionAttached", false)){
            QuestionRP questionRP = new Gson().fromJson(getIntent().getStringExtra("question"), QuestionRP.class);
            questionId = questionRP.get_id();
        } else {
            questionId = getIntent().getStringExtra("questionId");
        }
        APIMethods.getQuestion(this, questionId, new APIResponseListener<QuestionRP>() {
            @Override
            public void success(QuestionRP response) {
                addResponse(response);
            }

            @Override
            public void fail(String code, String message, String redirectLink, boolean retry, boolean cancellable) {
                Method.showFailedAlert(ViewQuestionActivity.this, code + "-" + message);
                if (adapter!=null){
                    adapter.showNoAnswerYetTxt();
                }
            }
        });
    }

    private void addResponse(QuestionRP response) {
        if (adapter == null){
            showFetchedQuestion(response);
        } else {
            addAnswers(response);
        }
    }

    private void showFetchedQuestion(QuestionRP questionRP){
        manager = new LinearLayoutManager(this);
        adapter = new ViewQuestionRVAdapter(questionRP, this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        titleTxt.setText(questionRP.getHead());
        addAnswers(questionRP);
    }

    private void addAnswers(QuestionRP questionRP) {
        adapter.addAnswers(questionRP.getAnswers());
        addBtn.extend();
    }

    private void showQuestion() {
        QuestionRP questionRP = new Gson().fromJson(getIntent().getStringExtra("question"), QuestionRP.class);
        manager = new LinearLayoutManager(this);
        adapter = new ViewQuestionRVAdapter(questionRP, this
        );
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        titleTxt.setText(questionRP.getHead());
        if (getIntent().getBooleanExtra("isNewQuestion", false)) {
            adapter.showNoAnswerYetTxt();
            addBtn.extend();
        }
        else
            fetchQuestion();
    }

    private void findViews() {
        appBar = findViewById(R.id.appBar);
        recyclerView = findViewById(R.id.recyclerView);
        titleTxt = findViewById(R.id.titleTxt);
        addBtn = findViewById(R.id.addBtn);
        addBtn.hide();
//        progressBar = findViewById(R.id.progressBar);
    }
}
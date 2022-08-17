package com.quaser.edtechapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.quaser.edtechapp.Adapter.ViewQuestionRVAdapter;
import com.quaser.edtechapp.rest.response.QuestionRP;

public class ViewQuestionActivity extends AppCompatActivity {

    LinearLayout appBar;
    RecyclerView recyclerView;
    LinearLayoutManager manager;
    ViewQuestionRVAdapter adapter;
    ProgressBar progressBar;


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
        recyclerView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                if (manager.findFirstVisibleItemPosition() == 0){
                    if (appBar.getVisibility() == View.VISIBLE){
                        appBar.setVisibility(View.INVISIBLE);
                    }
                }

                if (manager.findFirstVisibleItemPosition() == 1){
                    if (appBar.getVisibility() != View.VISIBLE){
                        appBar.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    private void fetchQuestion() {
    }

    private void showQuestion() {
        QuestionRP questionRP = new Gson().fromJson(getIntent().getStringExtra("question"), QuestionRP.class);
        manager = new LinearLayoutManager(this);
        adapter = new ViewQuestionRVAdapter(questionRP);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
    }

    private void findViews() {
        appBar = findViewById(R.id.appBar);
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
    }
}
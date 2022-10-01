package com.quaser.edtechapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.quaser.edtechapp.rest.api.APIMethods;
import com.quaser.edtechapp.rest.api.interfaces.APIResponseListener;
import com.quaser.edtechapp.rest.response.RanklistRes;
import com.quaser.edtechapp.utils.Method;

public class LeaderboardActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LinearLayout topBar;
    private ImageButton backBtn;
    private LinearLayoutManager manager;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        findViews();
        setScrollListener();
        loadRankList();
    }

    private void loadRankList() {
        APIMethods.getRanklist(new APIResponseListener<RanklistRes>() {
            @Override
            public void success(RanklistRes response) {
                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void fail(String code, String message, String redirectLink, boolean retry, boolean cancellable) {
                progressBar.setVisibility(View.GONE);
                Method.showFailedAlert(LeaderboardActivity.this, code + " - " + message);
            }
        });
    }

    private void findViews() {
        recyclerView = findViewById(R.id.recyclerView);
        topBar = findViewById(R.id.topBar);
        backBtn = findViewById(R.id.backBtn);
        progressBar = findViewById(R.id.progressBar);
    }

    private void setScrollListener() {
        recyclerView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                if (manager.findFirstCompletelyVisibleItemPosition() == 0){
                    if (topBar.getVisibility() == View.VISIBLE){
                        topBar.setVisibility(View.INVISIBLE);
                    }
                }

                if (manager.findFirstCompletelyVisibleItemPosition() == 1){
                    if (topBar.getVisibility() != View.VISIBLE){
                        topBar.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }
}
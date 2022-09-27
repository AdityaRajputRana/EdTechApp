package com.quaser.edtechapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.quaser.edtechapp.Adapter.ForumHomeRVAdapter;
import com.quaser.edtechapp.rest.api.APIMethods;
import com.quaser.edtechapp.rest.api.interfaces.APIResponseListener;
import com.quaser.edtechapp.rest.response.ForumHomeRP;
import com.quaser.edtechapp.utils.Method;

public class SearchActivity extends AppCompatActivity {

    String keyword;
    ImageButton backBtn;
    RecyclerView recyclerView;
    ForumHomeRVAdapter adapter;
    ProgressBar progressBar;
    TextView searchTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        findViews();
        keyword = getIntent().getStringExtra("KEYWORD");
        searchTxt.setText(keyword);
        fetchForumQuestions();

    }

    private void fetchForumQuestions() {
        //Todo handle pagination logic
        APIMethods.getForumQuestion(keyword, new APIResponseListener<ForumHomeRP>() {
            @Override
            public void success(ForumHomeRP response) {
                progressBar.setVisibility(View.GONE);
                showRecyclerView(response);
            }

            @Override
            public void fail(String code, String message, String redirectLink, boolean retry, boolean cancellable) {
                Method.showFailedAlert(SearchActivity.this, code+"-"+message);
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void showRecyclerView(ForumHomeRP res) {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        if (res.areMorePagesAvailable()){
            res.getQuestions().add(null);
        }
        adapter = new ForumHomeRVAdapter(res, this);
        recyclerView.setLayoutManager(manager);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int visibleItem = manager.getChildCount();
                int totalItem = manager.getItemCount();
                int firstVisibleItem = manager.findFirstVisibleItemPosition();

                if ((visibleItem + firstVisibleItem) >= totalItem
                        && firstVisibleItem >= 0){
                    res.paginate(adapter, SearchActivity.this);
                    if (!res.areMorePagesAvailable()){
                        recyclerView.removeOnScrollListener(this);
                    }
                }
            }
        });
        recyclerView.setAdapter(adapter);
    }


    private void findViews() {
        backBtn  = findViewById(R.id.backBtn);
        recyclerView = findViewById(R.id.questionRV);
        progressBar = findViewById(R.id.progressBar);
        searchTxt = findViewById(R.id.searchTxt);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchActivity.this.finish();
            }
        });
    }
}
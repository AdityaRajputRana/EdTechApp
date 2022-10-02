package com.quaser.edtechapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.quaser.edtechapp.Adapter.UnitRVAdapter;
import com.quaser.edtechapp.LiveData.UnitData;
import com.quaser.edtechapp.models.ShortUnit;
import com.quaser.edtechapp.rest.api.APIMethods;
import com.quaser.edtechapp.rest.api.interfaces.APIResponseListener;
import com.quaser.edtechapp.rest.response.UnitRP;
import com.quaser.edtechapp.utils.Method;

public class UnitActivity extends AppCompatActivity {

    //Todo: If Item is locked show a pre-req text to complete another unit.

    TextView titleTxt;
    LinearLayout topBar;
    RecyclerView recyclerView;
    MaterialButton continueBtn;
    UnitRVAdapter adapter;
    LinearLayoutManager manager;
    ProgressBar progressBar;

    UnitRP unitRP;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit);

        setUpBackBtn();
        findViews();
        fetchUnit();
        setUpTopBar();
    }

    ShortUnit shortUnit;

    private void fetchUnit() {
        String unitStr = getIntent().getStringExtra("SHORT_UNIT");
        shortUnit = new Gson().fromJson(unitStr, ShortUnit.class);
        titleTxt.setText(shortUnit.getUnit_title());
        APIMethods.getUnit(shortUnit.get_id(), this, new APIResponseListener<UnitRP>() {
            @Override
            public void success(UnitRP response) {
                progressBar.setVisibility(View.GONE);
                unitRP = response;
                shortRecyclerView();
            }

            @Override
            public void fail(String code, String message, String redirectLink, boolean retry, boolean cancellable) {
                progressBar.setVisibility(View.GONE);
                Method.showFailedAlert(UnitActivity.this, code+ "-" + message);
            }
        });
    }

    private void setUpContinueBtn(){
        continueBtn.setVisibility(View.GONE);
        Log.i("UnitLD", "setting cont btn");
        if (unitRP.getCompleted_lessons() >= unitRP.getLesson().size()){
            continueBtn.setVisibility(View.VISIBLE);
            continueBtn.setText("Go Back");
            continueBtn.setOnClickListener(view -> onBackPressed());
            Log.i("UnitLD", "go back");
        } else if (unitRP.isHas_user_started() || unitRP.getCompleted_lessons() > 0){
            continueBtn.setVisibility(View.VISIBLE);
            continueBtn.setText("Continue Learning");
            continueBtn.setOnClickListener(view -> startLessonActivity());
        } else {
            if (unitRP.isIs_paid()){
                if (unitRP.isHas_user_purchased()){
                    continueBtn.setVisibility(View.VISIBLE);
                    continueBtn.setText("Begin Learning");
                    continueBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startLessonActivity();
                        }
                    });
                } else {
                    continueBtn.setText("Buy Now");
                    continueBtn.setVisibility(View.GONE);
                    //Todo: Buy Unit in if not free here.
                }
            } else {
                continueBtn.setText("Begin Learning");
                continueBtn.setVisibility(View.VISIBLE);
                continueBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startLessonActivity();
                    }
                });
            }
        }
    }

    private void shortRecyclerView() {
        //Todo: handle if a new lesson is added in between
        //Todo: if new lesson is added and old is deleted then also unit is marked complete
        UnitData.setUnitData(unitRP);
        UnitData.getUnitRPMutableLiveData().observe(this, newRp -> {
            unitRP = newRp;
            setUpContinueBtn();
        });
        if (unitRP.getUnit_title() == null
        || unitRP.getUnit_title().isEmpty()){
            unitRP.setUnit_title(shortUnit.getUnit_title());
        }

        manager = new LinearLayoutManager(this);
        adapter = new UnitRVAdapter(unitRP, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(manager);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void startLessonActivity() {
        Intent intent = new Intent(this, LessonActivity.class);
        intent.putExtra("isUnitAttached", true);
        String unitStr = new Gson().toJson(unitRP);
        intent.putExtra("unitRP", unitStr);
        startActivity(intent);
    }

    private void setUpTopBar() {
        recyclerView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                if (manager.findFirstVisibleItemPosition() == 0){
                    if (topBar.getVisibility() == View.VISIBLE){
                        topBar.setVisibility(View.INVISIBLE);
                        titleTxt.setVisibility(View.INVISIBLE);
                    }
                }

                if (manager.findFirstVisibleItemPosition() == 1){
                    if (topBar.getVisibility() != View.VISIBLE){
                        topBar.setVisibility(View.VISIBLE);
                        titleTxt.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    private void findViews() {
        titleTxt = findViewById(R.id.titleTxt);
        topBar = findViewById(R.id.topBar);
        recyclerView = findViewById(R.id.recyclerView);
        continueBtn = findViewById(R.id.continueBtn);
        progressBar = findViewById(R.id.progressBar);
    }

    private void setUpBackBtn() {
        findViewById(R.id.backBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}
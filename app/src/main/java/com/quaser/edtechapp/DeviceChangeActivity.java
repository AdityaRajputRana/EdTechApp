package com.quaser.edtechapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.quaser.edtechapp.rest.api.APIMethods;
import com.quaser.edtechapp.rest.api.interfaces.APIResponseListener;
import com.quaser.edtechapp.rest.response.ChangeDeviceRP;
import com.quaser.edtechapp.rest.response.DeviceChangeRP;
import com.quaser.edtechapp.utils.Method;

public class DeviceChangeActivity extends AppCompatActivity {

    private TextView titleTxt;
    private EditText bodyEt;
    private MaterialButton logoutBtn;
    private MaterialButton submitBtn;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_change);

        findViews();
        setUpLogout();
        fetchDetails();
    }

    private void fetchDetails() {
        APIMethods.isDeviceChanged(new APIResponseListener<DeviceChangeRP>() {
            @Override
            public void success(DeviceChangeRP response) {
                progressBar.setVisibility(View.GONE);
                if (response == null || response.isNew()){
                    showSubmitLayout();
                } else {
                    showDetails(response);
                }
            }

            @Override
            public void fail(String code, String message, String redirectLink, boolean retry, boolean cancellable) {
                showSubmitLayout();
            }
        });
    }

    private void showDetails(DeviceChangeRP response) {
        titleTxt.setText(response.getStatus());
        if (response.showPlaceholder()){
            bodyEt.setText(response.getPlaceholder());
            bodyEt.setVisibility(View.VISIBLE);
            bodyEt.setEnabled(false);
        } else {
            if (response.getMessage() != null
            && !response.getMessage().isEmpty()) {
                bodyEt.setText(response.getMessage());
                bodyEt.setEnabled(false);
                bodyEt.setVisibility(View.VISIBLE);
            } else {
                bodyEt.setVisibility(View.GONE);
            }
        }
        if (response.showResubmit()){
            submitBtn.setVisibility(View.VISIBLE);
            submitBtn.setText("Re-Submit");
            submitBtn.setOnClickListener(view -> showSubmitLayout());
        } else {
            submitBtn.setVisibility(View.GONE);
        }
    }

    private void showSubmitLayout() {
        bodyEt.setText("");
        bodyEt.setHint("Explain the reason for logging into here. Our team will reach out to you soon");
        bodyEt.setEnabled(true);
        bodyEt.setVisibility(View.VISIBLE);

        submitBtn.setText("Submit");
        submitBtn.setOnClickListener(view -> submitReq());
        submitBtn.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    private void submitReq() {
        if (bodyEt.getText().toString().isEmpty()){
            bodyEt.setError("This is required");
            return;
        }
        bodyEt.setError(null);
        progressBar.setVisibility(View.VISIBLE);
        bodyEt.setVisibility(View.GONE);
        submitBtn.setVisibility(View.GONE);
        APIMethods.changeDevice(this, bodyEt.getText().toString(), new APIResponseListener<ChangeDeviceRP>() {
            @Override
            public void success(ChangeDeviceRP response) {
                progressBar.setVisibility(View.GONE);
                bodyEt.setEnabled(false);
                bodyEt.setVisibility(View.VISIBLE);
                submitBtn.setVisibility(View.GONE);
                if (response != null && (!response.isIs_old_request_pending()
                || response.isIs_new_request_created())) {
                    titleTxt.setText("Request submitted! Our admins will review it soon.");
                } else {
                    titleTxt.setText("Previously requested device change is still in progress! Please wait till we review that.");
                }
            }

            @Override
            public void fail(String code, String message, String redirectLink, boolean retry, boolean cancellable) {
                progressBar.setVisibility(View.GONE);
                bodyEt.setVisibility(View.VISIBLE);
                submitBtn.setVisibility(View.VISIBLE);
                Method.showFailedAlert(DeviceChangeActivity.this, code + " - " + message);
            }
        });
    }

    private void setUpLogout() {
        logoutBtn.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(
                    this, LoginActivity.class
            ));
            this.finish();
        });
    }

    private void findViews() {
        progressBar = findViewById(R.id.progressBar);
        titleTxt = findViewById(R.id.titleTxt);
        bodyEt = findViewById(R.id.bodyEt);
        logoutBtn = findViewById(R.id.logOutBtn);
        submitBtn = findViewById(R.id.submitBtn);
    }
}
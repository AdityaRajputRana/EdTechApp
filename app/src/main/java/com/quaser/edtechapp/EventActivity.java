package com.quaser.edtechapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.quaser.edtechapp.rest.api.APIMethods;
import com.quaser.edtechapp.rest.api.interfaces.APIResponseListener;
import com.quaser.edtechapp.rest.response.EventDetailsRP;
import com.quaser.edtechapp.utils.Method;

public class EventActivity extends AppCompatActivity {

    ProgressBar progressBar;
    EventDetailsRP event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        progressBar = findViewById(R.id.progressBar);
        findViewById(R.id.backBtn).setOnClickListener(view -> onBackPressed());
        String eventId = getIntent().getStringExtra("event_id");
        fetchEventDetails(eventId);
    }

    private void fetchEventDetails(String eventID) {
        APIMethods.getEventDetails(eventID, new APIResponseListener<EventDetailsRP>() {
            @Override
            public void success(EventDetailsRP response) {
                progressBar.setVisibility(View.GONE);
                event = response;
                showTicket();
            }

            @Override
            public void fail(String code, String message, String redirectLink, boolean retry, boolean cancellable) {
                progressBar.setVisibility(View.GONE);
                Method.showFailedAlert(EventActivity.this, code + " - " + message);
            }
        });
    }

    private void showTicket() {
        View view = findViewById(R.id.ticketView);
        view.setVisibility(View.VISIBLE);
        TextView typeTxt = view.findViewById(R.id.typeTxt);
        TextView titleTxt = view.findViewById(R.id.titleTxt);
        TextView locationTxt = view.findViewById(R.id.locationTxt);
        TextView dateTxt = view.findViewById(R.id.dateTxt);
        TextView timeTxt = view.findViewById(R.id.timeTxt);
        TextView sectionTxt = view.findViewById(R.id.sectionTxt);
        TextView seatTxt = view.findViewById(R.id.seatTxt);
        TextView priceTxt = view.findViewById(R.id.priceTxt);
        TextView orderTxt = view.findViewById(R.id.orderTxt);
        ImageView qrImg = view.findViewById(R.id.qrImage);
        TextView ticketTxt = view.findViewById(R.id.ticketTxt);
        MaterialButton continueBtn = view.findViewById(R.id.continueBtn);

        titleTxt.setText(event.getTitle());
        typeTxt.setText(event.getType());
        locationTxt.setText(event.getLocation());
        dateTxt.setText(event.getTime().getDate_full());
        if (event.getTime().getTime() == null)
            timeTxt.setText("-");
        else
            timeTxt.setText(event.getTime().getTime());


        if (event.getIs_paid() || event.getPrice() > 0){
            priceTxt.setText(String.valueOf(event.getPrice()));
            if (event.getOrderId() != null)
                orderTxt.setText(event.getOrderId());
        }
        if (!event.isOnline()){
            sectionTxt.setText(event.getSid());
            seatTxt.setText(event.getSno());
            ticketTxt.setVisibility(View.VISIBLE);
        } else {
            if (event.getSdk() != null &&
            event.getId() != null){
                continueBtn.setVisibility(View.VISIBLE);
                continueBtn.setText("Launch Meeting");

            } else if (event.getMeet_link() != null
            && !event.getMeet_link().isEmpty()){
                continueBtn.setText("Open Meeting");
                continueBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(event.getMeet_link()));
                        startActivity(intent);
                    }
                });
                continueBtn.setVisibility(View.VISIBLE);
            }
        }

    }
}
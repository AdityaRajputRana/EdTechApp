package com.quaser.edtechapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.quaser.edtechapp.rest.api.APIMethods;
import com.quaser.edtechapp.rest.api.interfaces.APIResponseListener;
import com.quaser.edtechapp.rest.response.QuestionRP;
import com.quaser.edtechapp.utils.Method;

import java.util.ArrayList;
import java.util.Arrays;

public class AddQuestionActivity extends AppCompatActivity {
    
    private MaterialButton continueBtn;
    EditText headEt;
    EditText imgEt;
    EditText tagsEt;
    EditText bodyEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question);
        
        
        findViews();
        setListeners();
    }

    private void setListeners() {
        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (areFieldsFilled()){
                    processData();
                }
            }
        });
    }

    private void processData() {
        disable();
        ArrayList<String> tagsList = new ArrayList<>();
        if (!tagsEt.getText().toString().isEmpty()){
            String[] tags = tagsEt.getText().toString().split(",");
            tagsList= new ArrayList<String>(Arrays.asList(tags));
        }

        postQuestion(tagsList);
    }

    private void postQuestion(ArrayList<String> tagsList) {
        APIMethods.postQuestion(headEt.getText().toString(),
                bodyEt.getText().toString(), imgEt.getText().toString(), tagsList,
                this, new APIResponseListener<QuestionRP>() {
                    @Override
                    public void success(QuestionRP response) {
                        Toast.makeText(AddQuestionActivity.this, "Question Added Successfully!", Toast.LENGTH_SHORT).show();
                        launchViewQuestionActivity(response);
                    }

                    @Override
                    public void fail(String code, String message, String redirectLink, boolean retry, boolean cancellable) {
                        enable();
                        Method.showFailedAlert(AddQuestionActivity.this, code+"-"+message);
                    }
                });
    }

    private void launchViewQuestionActivity(QuestionRP response) {
        String questionRP = new Gson().toJson(response);
        Intent intent = new Intent(this, ViewQuestionActivity.class);
        intent.putExtra("hasQuestionAttached", true);
        intent.putExtra("isNewQuestion", true);
        intent.putExtra("question", questionRP);
        startActivity(intent);
        this.finish();
    }

    private void disable(){
        continueBtn.setEnabled(false);
        headEt.setEnabled(false);
        bodyEt.setEnabled(false);
        imgEt.setEnabled(false);
        tagsEt.setEnabled(false);
    }

    private void enable(String message){
        continueBtn.setEnabled(true);
        headEt.setEnabled(true);
        bodyEt.setEnabled(true);
        imgEt.setEnabled(true);
        tagsEt.setEnabled(true);
        if (!message.isEmpty()){
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

    private void enable(){
        enable("");
    }

    private boolean areFieldsFilled() {
        boolean areFilled = true;
        if (headEt.getText().toString().isEmpty()){
            headEt.setError("This is required");
            areFilled = false;
        } else {
            headEt.setError(null);
        }

        if (bodyEt.getText().toString().isEmpty()){
            bodyEt.setError("This is required");
            areFilled = false;
        } else {
            bodyEt.setError(null);
        }
        return areFilled;
    }

    private void findViews() {
        continueBtn = findViewById(R.id.continueBtn);
        headEt = findViewById(R.id.headEt);
        imgEt = findViewById(R.id.imgEt);
        tagsEt = findViewById(R.id.tagsEt);
        bodyEt = findViewById(R.id.bodyEt);
    }
}
package com.quaser.edtechapp.rest.requests;

import android.app.Activity;

import com.quaser.edtechapp.Auth.AuthUtils;

import java.util.ArrayList;

public class AddQuestionRq {
    String user_id;
    String image_url;
    ArrayList<String> tags;
    String head;
    String body;
    String user_name;

    public AddQuestionRq() {
    }

    public AddQuestionRq(String image_url, ArrayList<String> tags, String head, String body,
                         Activity context) {
        this.image_url = image_url;
        this.tags = tags;
        this.head = head;
        this.body = body;
        this.user_id = AuthUtils.getInstance().getUserId();
        this.user_name = AuthUtils.getInstance().getUserName();
    }
}

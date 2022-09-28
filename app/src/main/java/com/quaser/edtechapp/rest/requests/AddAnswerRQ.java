package com.quaser.edtechapp.rest.requests;

import android.app.Activity;

import com.google.android.gms.auth.api.Auth;
import com.quaser.edtechapp.Auth.AuthUtils;

import java.util.ArrayList;

public class AddAnswerRQ {
    String user_id;
    String image_url;
    ArrayList<String> tags;
    String head;
    String body;
    String user_name;
    String html;
    String serialized;
    ArrayList<String> media;
    String question_id;
    String user_display_picture;

    public AddAnswerRQ() {
    }

    public AddAnswerRQ(String image_url, String head, String body, String html, String serialized, ArrayList<String> media, String question_id) {
        this.user_id = AuthUtils.getUserId();
        this.image_url = image_url;
        this.head = head;
        this.body = body;
        this.user_name = AuthUtils.getUserName();
        this.html = html;
        this.serialized = serialized;
        this.media = media;
        this.question_id = question_id;
    }
}

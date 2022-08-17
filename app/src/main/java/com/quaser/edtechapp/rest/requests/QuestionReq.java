package com.quaser.edtechapp.rest.requests;

import android.app.Activity;

import com.quaser.edtechapp.Auth.AuthUtils;

public class QuestionReq {
    String user_id;
    String question_id;

    public QuestionReq() {
    }

    public QuestionReq(Activity activity, String question_id) {
        this.user_id = AuthUtils.getInstance(activity).getUserId();
        this.question_id = question_id;
    }
}

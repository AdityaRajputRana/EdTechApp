package com.quaser.edtechapp.rest.requests;

import android.app.Activity;

import com.quaser.edtechapp.Auth.AuthUtils;

public class ForumQAReq {
    String user_id;
    String question_id;
    String answer_id;

    public ForumQAReq() {
    }

    public ForumQAReq(Activity activity, String question_id) {
        this.user_id = AuthUtils.getInstance().getUserId();
        this.question_id = question_id;
    }

    public ForumQAReq(String question_id){
        this.question_id = question_id;
        this.user_id = AuthUtils.getInstance().getUserId();
    }

    public ForumQAReq(String question_id, String answer_id){
        this.question_id = question_id;
        this.answer_id = answer_id;
        this.user_id = AuthUtils.getInstance().getUserId();
    }
}

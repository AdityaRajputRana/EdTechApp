package com.quaser.edtechapp.rest.requests;

import android.app.Activity;

import com.quaser.edtechapp.Auth.AuthUtils;

public class ForumReq {
    int page;
    String user_id;
    public ForumReq(){
        user_id = AuthUtils.getInstance().getUserId();
    }

    public String getUserId() {
        return user_id;
    }

    String keyword;

    public void setUserId(String userId) {
        this.user_id = userId;
    }
    public ForumReq(int page){
        user_id = AuthUtils.getUserId();
        this.page = page;
    }

    public ForumReq(String keyword, int page){
        user_id = AuthUtils.getUserId();
        this.page = page;
        this.keyword = keyword;
    }

    public ForumReq(String keyword){
        user_id = AuthUtils.getUserId();
        this.keyword = keyword;
    }
}

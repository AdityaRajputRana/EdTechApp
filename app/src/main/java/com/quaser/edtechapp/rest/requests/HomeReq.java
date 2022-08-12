package com.quaser.edtechapp.rest.requests;

import android.app.Activity;

import com.quaser.edtechapp.Auth.AuthUtils;

public class HomeReq {
    String user_id;
    public HomeReq(Activity activity){
        user_id = AuthUtils.getInstance(activity).getUserId();
    }

    public HomeReq() {
    }

    public String getUserId() {
        return user_id;
    }

    public void setUserId(String userId) {
        this.user_id = userId;
    }
}

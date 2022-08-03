package com.quaser.edtechapp.rest.requests;

import android.app.Activity;

import com.quaser.edtechapp.Auth.AuthUtils;

public class HomeReq {
    String userId;
    public HomeReq(Activity activity){
        userId = AuthUtils.getInstance(activity).getUserId();
    }

    public HomeReq() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}

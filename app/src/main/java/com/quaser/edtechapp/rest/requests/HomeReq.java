package com.quaser.edtechapp.rest.requests;

import android.app.Activity;

import com.google.firebase.auth.FirebaseAuth;
import com.quaser.edtechapp.Auth.AuthUtils;

public class HomeReq {
    String user_id;
    int page;
    public HomeReq(Activity activity){
        user_id = AuthUtils.getInstance().getUserId();
    }

    public HomeReq() {
        this.user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        this.page = 1;
    }

    public HomeReq(int page) {
        this.page = page;
        this.user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public String getUserId() {
        return user_id;
    }

    public void setUserId(String userId) {
        this.user_id = userId;
    }
}

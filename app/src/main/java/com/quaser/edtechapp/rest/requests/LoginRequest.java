package com.quaser.edtechapp.rest.requests;

import android.app.Activity;

import com.google.firebase.auth.FirebaseAuth;
import com.quaser.edtechapp.Auth.AuthUtils;

public class LoginRequest {
    String user_id;
    String phone_number;
    String uid;
    public LoginRequest(){
        user_id = AuthUtils.getUserId();
        this.phone_number = FirebaseAuth.getInstance().getCurrentUser()
                .getPhoneNumber();
        uid = FirebaseAuth.getInstance().getCurrentUser()
                .getUid();
    }

    public String getUserId() {
        return user_id;
    }

    public void setUserId(String userId) {
        this.user_id = userId;
    }
}

package com.quaser.edtechapp.rest.requests;

import android.app.Activity;

import com.google.firebase.auth.FirebaseAuth;
import com.quaser.edtechapp.Auth.AuthUtils;

public class LoginRequest {
    String user_id;
    String phone_number;
    public LoginRequest(){
        user_id = AuthUtils.getUserId();
        this.phone_number = FirebaseAuth.getInstance().getCurrentUser()
                .getPhoneNumber();
    }

    public String getUserId() {
        return user_id;
    }

    public void setUserId(String userId) {
        this.user_id = userId;
    }
}

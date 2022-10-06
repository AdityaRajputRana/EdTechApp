package com.quaser.edtechapp.rest.requests;

import android.app.Activity;

import com.google.firebase.auth.FirebaseAuth;
import com.quaser.edtechapp.Auth.AuthUtils;

public class LoginRequest {
    String user_id;
    String phone_number;
    String name;
    String device_id; //Todo:SL inflate this
    public LoginRequest(){
        user_id = AuthUtils.getUserId();
        this.phone_number = FirebaseAuth.getInstance().getCurrentUser()
                .getPhoneNumber();
    }

    public LoginRequest(boolean ano){
        user_id = AuthUtils.getUserId();
        name = AuthUtils.getUserName();
    }

    public LoginRequest(String name){
        user_id = AuthUtils.getUserId();
        this.phone_number = FirebaseAuth.getInstance().getCurrentUser()
                .getPhoneNumber();
        this.name = name;
    }

    public String getUserId() {
        return user_id;
    }

    public void setUserId(String userId) {
        this.user_id = userId;
    }
}

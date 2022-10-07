package com.quaser.edtechapp.rest.requests;

import android.app.Activity;

import com.google.firebase.auth.FirebaseAuth;
import com.quaser.edtechapp.Auth.AuthUtils;
import com.quaser.edtechapp.utils.Tools;

public class LoginRequest {
    String user_id;
    String phone_number;
    String name;
    String device_id;
    public LoginRequest(Activity activity){
        user_id = AuthUtils.getUserId();
        this.phone_number = FirebaseAuth.getInstance().getCurrentUser()
                .getPhoneNumber();
        this.device_id = Tools.getUniqueDeviceId(activity);
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

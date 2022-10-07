package com.quaser.edtechapp.rest.requests;

import com.google.firebase.auth.FirebaseAuth;

public class ChangeDeviceReq {
    String user_id = FirebaseAuth.getInstance()
            .getCurrentUser().getUid();
    String new_device_id;
    String message;

    public ChangeDeviceReq(String new_device_id, String message) {
        this.new_device_id = new_device_id;
        this.message = message;
    }
}

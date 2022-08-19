package com.quaser.edtechapp.rest.requests;

import android.app.Activity;

import com.quaser.edtechapp.Auth.AuthUtils;

public class UnitReq {
    String user_id;
    String unit_id;

    public UnitReq() {
    }

    public UnitReq(Activity activity, String unit_id) {
        this.user_id = AuthUtils.getInstance().getUserId();
        this.unit_id = unit_id;
    }
}

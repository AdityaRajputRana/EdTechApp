package com.quaser.edtechapp.rest.requests;

import com.google.gson.Gson;

public class VerifyLessonPaymentReq {
    String order_id;
    String razorpay_order_id;
    String razorpay_payment_id;
    String razorpay_signature;

    public static VerifyLessonPaymentReq getInstance(String s, String id){
        VerifyLessonPaymentReq req = new Gson().fromJson(s, VerifyLessonPaymentReq.class);
        req.order_id = id;
        return req;
    }
}

package com.quaser.edtechapp.models;

public class PaymentModel {
    String user_id;
    String unit_id;
    String lesson_id;
    String event_id;
    String unit_title;
    String lesson_title;
    String event_title;
    String payment_type;
    String pay_for;

    String order_id;
    String razorpay_order_id;
    String razorpay_payment_id;
    String razorpay_signature;

    long created_at;

    String currency;
    int amount;
    String status;

    public String getStatus() {
        return status;
    }

    public PaymentModel() {
    }

    public String getUnit_id() {
        return unit_id;
    }

    public String getLesson_id() {
        return lesson_id;
    }

    public String getEvent_id() {
        return event_id;
    }

    public String getUnit_title() {
        return unit_title;
    }

    public String getLesson_title() {
        return lesson_title;
    }

    public String getEvent_title() {
        return event_title;
    }

    public String getPayment_type() {
        return payment_type;
    }

    public String getOrder_id() {
        return order_id;
    }

    public String getCurrency() {
        return currency;
    }

    public int getAmount() {
        return amount;
    }
}

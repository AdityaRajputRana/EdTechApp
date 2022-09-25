package com.quaser.edtechapp.rest.response;

import com.google.gson.JsonObject;
import com.quaser.edtechapp.models.ShortEvent;

import java.util.ArrayList;

public class SubscribeEventRP extends ShortEvent {
    String razorpay_order_id;
    ArrayList<JsonObject> users_subscribed;
    String action;
    String html;

    public SubscribeEventRP() {
    }

    public String getRazorpay_order_id() {
        return razorpay_order_id;
    }

    public void setRazorpay_order_id(String razorpay_order_id) {
        this.razorpay_order_id = razorpay_order_id;
    }

    public ArrayList<JsonObject> getUsers_subscribed() {
        return users_subscribed;
    }

    public void setUsers_subscribed(ArrayList<JsonObject> users_subscribed) {
        this.users_subscribed = users_subscribed;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }
}

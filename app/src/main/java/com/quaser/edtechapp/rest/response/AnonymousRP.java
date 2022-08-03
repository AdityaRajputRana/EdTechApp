package com.quaser.edtechapp.rest.response;

public class AnonymousRP {
    String userId;

    public AnonymousRP() {
    }

    public AnonymousRP(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}

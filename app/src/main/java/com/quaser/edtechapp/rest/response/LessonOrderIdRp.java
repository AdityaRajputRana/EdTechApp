package com.quaser.edtechapp.rest.response;

import com.quaser.edtechapp.models.Order;

public class LessonOrderIdRp {
    String api_key;
    Order order;

    public String getApi_key() {
        return api_key;
    }

    public Order getOrder() {
        return order;
    }

    public LessonOrderIdRp() {
    }
}

package com.quaser.edtechapp.rest.requests;

import com.quaser.edtechapp.Auth.AuthUtils;

public class SubscribeEventReq extends LessonReq{
    String event_id;
    public SubscribeEventReq(String lesson_id, String unit_id, String event_id) {
        super(lesson_id, unit_id);
        this.event_id = event_id;
    }
}

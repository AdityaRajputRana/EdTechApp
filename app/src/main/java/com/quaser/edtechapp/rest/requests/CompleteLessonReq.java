package com.quaser.edtechapp.rest.requests;

import com.quaser.edtechapp.Auth.AuthUtils;

public class CompleteLessonReq {
    String user_id = AuthUtils.getUserId();
    String lesson_id;
    String unit_id;

    public CompleteLessonReq(String lesson_id, String unit_id){
        this.lesson_id = lesson_id;
        this.unit_id = unit_id;
        user_id = AuthUtils.getUserId();
    }

}

package com.quaser.edtechapp.rest.requests;

import com.google.firebase.auth.FirebaseAuth;
import com.quaser.edtechapp.Auth.AuthUtils;

public class LessonReq {
    String user_id = AuthUtils.getUserId();
    String lesson_id;
    String unit_id;

    public LessonReq(String lesson_id) {
        this.lesson_id = lesson_id;
    }
    public LessonReq(String lesson_id, String unit_id){
        this.lesson_id = lesson_id;
        this.unit_id = unit_id;
    }

}

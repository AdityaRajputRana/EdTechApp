package com.quaser.edtechapp.rest.requests;

import com.quaser.edtechapp.Auth.AuthUtils;

public class SubmitAssignmentReq {
    String user_id = AuthUtils.getUserId();
    String lesson_id;
    String unit_id;
    String file;

    public SubmitAssignmentReq(String lesson_id) {
        this.lesson_id = lesson_id;
    }
    public SubmitAssignmentReq(String lesson_id, String unit_id, String file){
        this.lesson_id = lesson_id;
        this.unit_id = unit_id;
        this.file = file;
    }

}

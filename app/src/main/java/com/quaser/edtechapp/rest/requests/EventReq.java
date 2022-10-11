package com.quaser.edtechapp.rest.requests;

import com.google.firebase.auth.FirebaseAuth;

public class EventReq {
    String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
    String event_id;
    public EventReq(String event_id){
        this.event_id =event_id;
    }
}

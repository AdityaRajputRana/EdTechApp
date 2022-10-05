package com.quaser.edtechapp.PersonalityTest;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class EndTestReq {
    String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
    ArrayList<EndTestModel> tests;

    public ArrayList<EndTestModel> getTests(){
        if (tests == null){
            tests = new ArrayList<EndTestModel>() ;
        }
        return tests;
    }
}

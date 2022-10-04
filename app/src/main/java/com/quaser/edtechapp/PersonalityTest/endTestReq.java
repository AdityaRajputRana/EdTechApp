package com.quaser.edtechapp.PersonalityTest;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class endTestReq {
    String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
    ArrayList<EndTestModel> tests;
}

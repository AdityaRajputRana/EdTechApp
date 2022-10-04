package com.quaser.edtechapp.PersonalityTest;

import java.util.ArrayList;

public class PersonalityTestModel {
    String title;
    String message;
    ArrayList<TestModel> tests;

    public PersonalityTestModel() {
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public ArrayList<TestModel> getTests() {
        return tests;
    }
}

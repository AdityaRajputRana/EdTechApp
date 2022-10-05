package com.quaser.edtechapp.PersonalityTest;

import java.util.ArrayList;

public class TestModel {
    int index;
    String _id;
    String title;
    PersonalityAlphabetModel positive;
    PersonalityAlphabetModel negative;
    String message;
    int num_question;
    int time_allowed;
    ArrayList<QuestionModel> questions;

    public TestModel() {
    }

    public int getIndex() {
        return index;
    }

    public String getId() {
        return _id;
    }

    public ArrayList<QuestionModel> getQuestions() {
        return questions;
    }
}

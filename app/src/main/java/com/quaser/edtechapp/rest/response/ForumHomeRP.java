package com.quaser.edtechapp.rest.response;

import java.util.ArrayList;

public class ForumHomeRP {
    ArrayList<QuestionRP> questions;

    public ArrayList<QuestionRP> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<QuestionRP> questions) {
        this.questions = questions;
    }

    public ForumHomeRP() {
    }
}

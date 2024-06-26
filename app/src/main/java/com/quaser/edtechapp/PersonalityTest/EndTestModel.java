package com.quaser.edtechapp.PersonalityTest;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class EndTestModel {
    int index;
    String test_id;
    ArrayList<EndAnswerModel> answers;

    public EndTestModel(int index, @Nullable String id) {
        this.test_id = id;
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getId() {
        return test_id;
    }

    public void setId(String id) {
        this.test_id = id;
    }

    public ArrayList<EndAnswerModel> getAnswers() {
        if (answers == null){
            answers = new ArrayList<EndAnswerModel>();
        }
        return answers;
    }

    public void setAnswers(ArrayList<EndAnswerModel> answers) {
        this.answers = answers;
    }
}

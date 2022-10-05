package com.quaser.edtechapp.PersonalityTest;

import java.util.ArrayList;

public class QuestionModel {
    int index;
    String _id;
    String question;
    String image;
    String video;
    ArrayList<AnswerModel> options;

    public int getIndex() {
        return index;
    }

    public String getId() {
        return _id;
    }

    public String getQuestion() {
        return question;
    }

    public String getImage() {
        return image;
    }

    public String getVideo() {
        return video;
    }

    public ArrayList<AnswerModel> getOptions() {
        return options;
    }

    public QuestionModel() {
    }
}

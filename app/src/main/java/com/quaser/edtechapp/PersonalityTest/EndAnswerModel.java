package com.quaser.edtechapp.PersonalityTest;

public class EndAnswerModel {
    String question_id;
    int question_index;
    String option_id;
    int option_index;

    public EndAnswerModel(String question_id, int question_index, String option_id, int option_index) {
        this.question_id = question_id;
        this.question_index = question_index;
        this.option_id = option_id;
        this.option_index = option_index;
    }

    public EndAnswerModel() {
    }

    public String getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(String question_id) {
        this.question_id = question_id;
    }

    public int getQuestion_index() {
        return question_index;
    }

    public void setQuestion_index(int question_index) {
        this.question_index = question_index;
    }

    public String getOption_id() {
        return option_id;
    }

    public void setOption_id(String option_id) {
        this.option_id = option_id;
    }

    public int getOption_index() {
        return option_index;
    }

    public void setOption_index(int option_index) {
        this.option_index = option_index;
    }
}

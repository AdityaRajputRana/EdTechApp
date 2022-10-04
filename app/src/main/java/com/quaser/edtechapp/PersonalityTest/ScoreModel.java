package com.quaser.edtechapp.PersonalityTest;

public class ScoreModel {
    int test_index;
    String test_id;
    String test_title;
    String positive_score;
    String positive_title;
    String negative_score;
    String negative_title;


    public ScoreModel() {
    }

    public int getTest_index() {
        return test_index;
    }

    public String getTest_id() {
        return test_id;
    }

    public String getTest_title() {
        return test_title;
    }

    public String getPositive_score() {
        return positive_score;
    }

    public String getPositive_title() {
        return positive_title;
    }

    public String getNegative_score() {
        return negative_score;
    }

    public String getNegative_title() {
        return negative_title;
    }

    public int getPositiveScore(){
        String ps = positive_score.replace("%","");
        return Integer.parseInt(ps);
    }

    public int getNegativeScore(){
        String ps = negative_score.replace("%","");
        return Integer.parseInt(ps);
    }
}

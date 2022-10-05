package com.quaser.edtechapp.PersonalityTest;

import java.util.ArrayList;

public class EndPersonalityTestRP {
    String title;
    String head;
    String body;
    ArrayList<String> traits;
    ArrayList<String> weakness;
    ArrayList<String> strengths;
    ArrayList<String> career_options;
    ArrayList<ScoreModel> scores;

    public EndPersonalityTestRP() {
    }

    public String getTitle() {
        return title;
    }

    public String getHead() {
        return head;
    }

    public String getBody() {
        return body;
    }

    public ArrayList<String> getTraits() {
        return traits;
    }

    public ArrayList<String> getWeakness() {
        return weakness;
    }

    public ArrayList<String> getStrengths() {
        return strengths;
    }

    public ArrayList<String> getCareer_options() {
        return career_options;
    }

    public ArrayList<ScoreModel> getScores() {
        return scores;
    }
}

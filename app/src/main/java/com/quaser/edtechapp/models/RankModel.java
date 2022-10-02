package com.quaser.edtechapp.models;

public class RankModel {
    String user_id;
    String id;
    String user_name;
    int rank;
    int score;
    String display_picture;

    public String getUser_id() {
        return user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public int getRank() {
        return rank;
    }

    public int getScore() {
        return score;
    }

    public String getDisplay_picture() {
        return display_picture;
    }

    public RankModel() {
    }
}

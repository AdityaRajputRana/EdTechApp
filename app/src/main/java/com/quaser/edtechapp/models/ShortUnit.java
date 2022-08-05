package com.quaser.edtechapp.models;

import java.util.ArrayList;

public class ShortUnit {
    String unitId;
    String unitTitle;
    ArrayList<String> tags;
    int totalLessons;
    int completedLessons;

    public String getUnitId() {
        return unitId;
    }

    public String getUnitTitle() {
        return unitTitle;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public int getTotalLessons() {
        return totalLessons;
    }

    public int getCompletedLessons() {
        return completedLessons;
    }

    public ShortUnit() {
    }
}

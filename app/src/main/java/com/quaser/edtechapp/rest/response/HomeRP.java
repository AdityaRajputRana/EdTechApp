package com.quaser.edtechapp.rest.response;

import com.quaser.edtechapp.models.Prerequisite;

import java.util.ArrayList;

public class HomeRP {
    boolean isAnonymous;
    String quote;
    String name;
    int completedLessons;
    int totalLessons;
    String courseTitle;
    String courseId;
    String courseDescription;
    ArrayList<String> tags;
    Prerequisite prerequisite;
    boolean isLocked;
    boolean isPaid;

    public boolean isAnonymous() {
        return isAnonymous;
    }

    public String getQuote() {
        return quote;
    }

    public String getName() {
        return name;
    }

    public int getCompletedLessons() {
        return completedLessons;
    }

    public int getTotalLessons() {
        return totalLessons;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public String getCourseId() {
        return courseId;
    }

    public String getCourseDescription() {
        return courseDescription;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public Prerequisite getPrerequisite() {
        return prerequisite;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public HomeRP() {
    }
}

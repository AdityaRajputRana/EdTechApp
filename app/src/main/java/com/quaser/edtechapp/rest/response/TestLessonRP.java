package com.quaser.edtechapp.rest.response;

public class TestLessonRP {

    String lesson_id;
    String title;
    String short_description;
    String description;
    String type;
    boolean is_complete;
    String completion;
    boolean is_locked;

    int num_questions;

    public String getLesson_id() {
        return lesson_id;
    }

    public String getTitle() {
        return title;
    }

    public String getShort_description() {
        return short_description;
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }

    public boolean isIs_complete() {
        return is_complete;
    }

    public String getCompletion() {
        return completion;
    }

    public boolean isIs_locked() {
        return is_locked;
    }

    public int getNum_questions() {
        return num_questions;
    }

    public int getTime_allowed() {
        return time_allowed;
    }

    int time_allowed;



    public TestLessonRP() {
    }
}

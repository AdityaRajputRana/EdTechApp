package com.quaser.edtechapp.rest.response;

import com.quaser.edtechapp.models.ShortAssignment;
import com.quaser.edtechapp.models.ShortLesson;

import java.util.ArrayList;

public class UnitRP {
    String unit_title;
    String completion;
    int total_lessons = 0;
    int total_vids = 0;
    int total_tests = 0;
    int total_articles = 0;
    int completed_lessons = 0;
    ArrayList<String> tags = new ArrayList<String>();
    boolean is_paid;
    boolean has_user_purchased = false;
    boolean has_user_started = false;
    ArrayList<ShortAssignment> assignments = new ArrayList<ShortAssignment>();
    LastLesson start_at;

    public String getUnit_title() {
        return unit_title;
    }

    public void setUnit_title(String unit_title) {
        this.unit_title = unit_title;
    }

    public String getCompletion() {
        return completion;
    }

    public void setCompletion(String completion) {
        this.completion = completion;
    }

    public int getTotal_lessons() {
        return total_lessons;
    }

    public void setTotal_lessons(int total_lessons) {
        this.total_lessons = total_lessons;
    }

    public int getTotal_vids() {
        return total_vids;
    }

    public void setTotal_vids(int total_vids) {
        this.total_vids = total_vids;
    }

    public int getTotal_tests() {
        return total_tests;
    }

    public void setTotal_tests(int total_tests) {
        this.total_tests = total_tests;
    }

    public int getTotal_articles() {
        return total_articles;
    }

    public void setTotal_articles(int total_articles) {
        this.total_articles = total_articles;
    }

    public int getCompleted_lessons() {
        return completed_lessons;
    }

    public void setCompleted_lessons(int completed_lessons) {
        this.completed_lessons = completed_lessons;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public boolean isIs_paid() {
        return is_paid;
    }

    public void setIs_paid(boolean is_paid) {
        this.is_paid = is_paid;
    }

    public boolean isHas_user_purchased() {
        return has_user_purchased;
    }

    public void setHas_user_purchased(boolean has_user_purchased) {
        this.has_user_purchased = has_user_purchased;
    }

    public boolean isHas_user_started() {
        return has_user_started;
    }

    public void setHas_user_started(boolean has_user_started) {
        this.has_user_started = has_user_started;
    }

    public ArrayList<ShortAssignment> getAssignments() {
        return assignments;
    }

    public void setAssignments(ArrayList<ShortAssignment> assignments) {
        this.assignments = assignments;
    }

    public LastLesson getStart_at() {
        return start_at;
    }

    public void setStart_at(LastLesson start_at) {
        this.start_at = start_at;
    }

    public ArrayList<ShortLesson> getLesson() {
        return lesson;
    }

    public void setLesson(ArrayList<ShortLesson> lesson) {
        this.lesson = lesson;
    }

    public UnitRP() {
    }

    ArrayList<ShortLesson> lesson = new ArrayList<ShortLesson>();

    class LastLesson{
        String lesson_id;

        public String getLesson_id() {
            return lesson_id;
        }

        public void setLesson_id(String lesson_id) {
            this.lesson_id = lesson_id;
        }

        public String getLesson_name() {
            return lesson_name;
        }

        public void setLesson_name(String lesson_name) {
            this.lesson_name = lesson_name;
        }

        public String getLesson_type() {
            return lesson_type;
        }

        public void setLesson_type(String lesson_type) {
            this.lesson_type = lesson_type;
        }

        String lesson_name;
        String lesson_type;

        public LastLesson() {
        }
    }
}

package com.quaser.edtechapp.models;

import java.util.ArrayList;

public class ShortUnit {
    Prerequisite prerequisite;
    String _id;
    String unit_title;
    ArrayList<String> tags;
    int total_lessons;
    int completed_lessons = 0; //Todo: Not present in api
    int index;
    boolean is_paid, is_locked;

    public ShortUnit() {
    }

    public Prerequisite getPrerequisite() {
        return prerequisite;
    }

    public void setPrerequisite(Prerequisite prerequisite) {
        this.prerequisite = prerequisite;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getUnit_title() {
        return unit_title;
    }

    public void setUnit_title(String unit_title) {
        this.unit_title = unit_title;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public int getTotal_lessons() {
        return total_lessons;
    }

    public void setTotal_lessons(int total_lessons) {
        this.total_lessons = total_lessons;
    }

    public int getCompleted_lessons() {
        return completed_lessons;
    }

    public void setCompleted_lessons(int completed_lessons) {
        this.completed_lessons = completed_lessons;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean isIs_paid() {
        return is_paid;
    }

    public void setIs_paid(boolean is_paid) {
        this.is_paid = is_paid;
    }

    public boolean isIs_locked() {
        return is_locked;
    }

    public void setIs_locked(boolean is_locked) {
        this.is_locked = is_locked;
    }
}

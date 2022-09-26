package com.quaser.edtechapp.models;

public class ShortLesson {
    String lesson_id;
    String title;
    String description;
    Prerequisite pre_requisite;
    String type;
    boolean is_completed;
    boolean is_locked;

    public String getName() {
        return title;
    }

    public void setName(String name) {
        this.title = name;
    }

    public String getShort_description() {
        return description;
    }

    public void setShort_description(String short_description) {
        this.description = short_description;
    }

    public Prerequisite getPre_requisite() {
        return pre_requisite;
    }

    public void setPre_requisite(Prerequisite pre_requisite) {
        this.pre_requisite = pre_requisite;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isIs_complete() {
        return is_completed;
    }

    public void setIs_complete(boolean is_complete) {
        this.is_completed = is_complete;
    }

    public boolean isIs_locked() {
        return is_locked;
    }

    public void setIs_locked(boolean is_locked) {
        this.is_locked = is_locked;
    }

    public String getId() {
        return lesson_id;
    }

    public void setId(String id) {
        this.lesson_id = id;
    }

    public ShortLesson() {
    }
}

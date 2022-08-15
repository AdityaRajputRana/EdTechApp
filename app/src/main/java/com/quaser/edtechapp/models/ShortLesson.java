package com.quaser.edtechapp.models;

public class ShortLesson {
    String id;
    String name;
    String short_description;
    Prerequisite pre_requisite;
    String type;
    boolean is_complete = false;
    boolean is_locked;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShort_description() {
        return short_description;
    }

    public void setShort_description(String short_description) {
        this.short_description = short_description;
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
        return is_complete;
    }

    public void setIs_complete(boolean is_complete) {
        this.is_complete = is_complete;
    }

    public boolean isIs_locked() {
        return is_locked;
    }

    public void setIs_locked(boolean is_locked) {
        this.is_locked = is_locked;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ShortLesson() {
    }
}

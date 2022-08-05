package com.quaser.edtechapp.models;

public class Prerequisite {
    boolean has_prerequisite;
    String on;
    long time;
    String type;
    String message;

    public boolean isHas_prerequisite() {
        return has_prerequisite;
    }

    public String getOn() {
        return on;
    }

    public long getTime() {
        return time;
    }

    public String getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public Prerequisite() {
    }
}

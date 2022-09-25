package com.quaser.edtechapp.rest.response;

import com.quaser.edtechapp.models.ShortEvent;

import java.util.ArrayList;

public class EventsListRP {

    String lesson_id;
    String title;
    String short_description;
    String description;
    String type;
    boolean is_complete;
    String completion;
    boolean is_locked;

    ArrayList<ShortEvent> events;

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

    public ArrayList<ShortEvent> getEvents() {
        return events;
    }

    public EventsListRP() {
    }
}

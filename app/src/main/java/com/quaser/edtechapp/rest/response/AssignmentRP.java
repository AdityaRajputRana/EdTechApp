package com.quaser.edtechapp.rest.response;

public class AssignmentRP {

    String lesson_id;
    String title;
    String short_description;
    String description;
    String type;
    boolean is_complete;
    String completion;
    boolean is_locked;

    String intro_vid;
    String body;
    String sample_url;

    String submitted_url;
    String placeholder;
    String status;

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

    public String getIntro_vid() {
        return intro_vid;
    }

    public String getBody() {
        return body;
    }

    public String getSample_url() {
        return sample_url;
    }

    public String getSubmitted_url() {
        return submitted_url;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public String getStatus() {
        return status;
    }

    public AssignmentRP() {
    }
}

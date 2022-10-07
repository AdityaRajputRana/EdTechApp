package com.quaser.edtechapp.rest.response;

public class AssignmentRP {

    String user_id;
    String unit_id;
    long submitted_on;


    private String assignment_type;

    private String bucket_name;
    private String file_path;
    String _id;
    String created_at;
    String uploaded_at;
    int __v;

    String lesson_id;
    String title;
    String short_description;
    String description;
    String type;
    boolean is_completed;
    String completion;
    boolean is_locked;

    String intro_vid;
    String body;
    String sample;

    String assignment_url;
    String prev_assignment_url;
    String placeholder;
    String status;

    public String getUnit_title() {
        return unit_title;
    }

    String unit_title;

    public String getUnit_id() {
        return unit_id;
    }

    public boolean is_next_btn_enabled(){
        if (is_completed){
            return true;
        } else {
            return assignment_type != null
                    && assignment_type.equals("auto")
                    && status != null
                    && status.equals("Submitted");
        }
    }

    public String getSample() {
        return sample;
    }

    public String getAssignment_url() {
        return assignment_url;
    }

    public String getPrev_assignment_url() {
        return prev_assignment_url;
    }


    public boolean show_submit_btn(){
        if (status == null || status.equals("Not Submitted")
        || status.equals("Rejected"))
            return true;
        else
            return false;
    }
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
        return is_completed;
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
        return sample;
    }

    public String getSubmitted_url() {
        return assignment_url;
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

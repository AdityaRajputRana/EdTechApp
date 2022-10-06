package com.quaser.edtechapp.models;

import java.util.ArrayList;

public class Answer {
    String _id;
    String question_id;
    String user_id;
    String body;
    boolean is_liked;
    boolean liked;
    boolean accepted_answers;
    String createdAt;
    String updatedAt;
    ArrayList<String> upvotes;
    int total_upvotes;
    String head;
    boolean unliked;
    String user_name;
    String display_picture;

    public String getDisplay_picture(){
        return display_picture;
    }
    public String getUser_name(){return user_name;}


    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(String question_id) {
        this.question_id = question_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public boolean isIs_liked() {
        return is_liked || liked;
    }

    public void setIs_liked(boolean is_liked) {
        this.is_liked = is_liked;
        this.liked = is_liked;
        this.unliked = !is_liked;
    }

    public boolean isAccepted_answers() {
        return accepted_answers;
    }

    public void setAccepted_answers(boolean accepted_answers) {
        this.accepted_answers = accepted_answers;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public ArrayList<String> getUpvotes() {
        return upvotes;
    }

    public void setUpvotes(ArrayList<String> upvotes) {
        this.upvotes = upvotes;
    }

    public int getTotal_upvotes() {
        return total_upvotes;
    }

    public void setTotal_upvotes(int total_upvotes) {
        this.total_upvotes = total_upvotes;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public Answer(String _id, String question_id, String user_id, String body, boolean is_liked, boolean accepted_answers, String createdAt, String updatedAt, ArrayList<String> upvotes, int total_upvotes, String head) {
        this._id = _id;
        this.question_id = question_id;
        this.user_id = user_id;
        this.body = body;
        this.is_liked = is_liked;
        this.accepted_answers = accepted_answers;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.upvotes = upvotes;
        this.total_upvotes = total_upvotes;
        this.head = head;
        this.liked = is_liked;
        this.unliked = !is_liked;
    }

    public Answer() {
    }
}

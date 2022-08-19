package com.quaser.edtechapp.rest.response;

import com.google.firebase.auth.FirebaseAuth;
import com.quaser.edtechapp.Auth.AuthUtils;
import com.quaser.edtechapp.models.Prerequisite;
import com.quaser.edtechapp.models.ShortUnit;

import java.util.ArrayList;

public class HomeRP {
    boolean is_anonymous_login;
    String active_quote;
    Object last_lesson; //Todo Make this
    Object last_unit; //Todo Make this

    String name = FirebaseAuth.getInstance()
            .getCurrentUser().getDisplayName(); //Todo: this is not present in request
    int completed_lessons;
    int total_lessons;
    String course_title;
    String course_description;
    ArrayList<String> tags;
    Prerequisite prerequisite;
    boolean isLocked; //Todo: Not present in api
    boolean isPaid;//Todo: Not present in api
    String headline;

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    ArrayList<ShortUnit> units;

    public boolean isIs_anonymous_login() {
        return is_anonymous_login;
    }

    public void setIs_anonymous_login(boolean is_anonymous_login) {
        this.is_anonymous_login = is_anonymous_login;
    }

    public String getActive_quote() {
        return active_quote;
    }

    public void setActive_quote(String active_quote) {
        this.active_quote = active_quote;
    }

    public Object getLast_lesson() {
        return last_lesson;
    }

    public void setLast_lesson(Object last_lesson) {
        this.last_lesson = last_lesson;
    }

    public Object getLast_unit() {
        return last_unit;
    }

    public void setLast_unit(Object last_unit) {
        this.last_unit = last_unit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCompleted_lessons() {
        return completed_lessons;
    }

    public void setCompleted_lessons(int completed_lessons) {
        this.completed_lessons = completed_lessons;
    }

    public int getTotal_lessons() {
        return total_lessons;
    }

    public void setTotal_lessons(int total_lessons) {
        this.total_lessons = total_lessons;
    }

    public String getCourse_title() {
        return course_title;
    }

    public void setCourse_title(String course_title) {
        this.course_title = course_title;
    }

    public String getCourse_description() {
        return course_description;
    }

    public void setCourse_description(String course_description) {
        this.course_description = course_description;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public Prerequisite getPrerequisite() {
        return prerequisite;
    }

    public void setPrerequisite(Prerequisite prerequisite) {
        this.prerequisite = prerequisite;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }

    public ArrayList<ShortUnit> getUnits() {
        return units;
    }

    public void setUnits(ArrayList<ShortUnit> units) {
        this.units = units;
    }

    public HomeRP() {
    }
}

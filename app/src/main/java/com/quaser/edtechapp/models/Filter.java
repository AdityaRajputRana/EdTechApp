package com.quaser.edtechapp.models;

public class Filter {
    String keyword;
    int page;
    String user_id;
    String tags;

    public Filter() {
    }

    public String getTag(){
        return tags;
    }

    public boolean isTagged(){
        return (tags != null) && !tags.isEmpty();
    }

    public String getKeyword() {
        return keyword;
    }

    public void setTag(String tag){
        this.tags = tag;
    }

    public int getPage() {
        return page;
    }

    public String getUser_id() {
        return user_id;
    }
}

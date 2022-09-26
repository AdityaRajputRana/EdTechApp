package com.quaser.edtechapp.rest.response;

public class DataRp {
    String link;
    String data;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public DataRp() {
    }

    public DataRp(String link, String data) {
        this.link = link;
        this.data = data;
    }
}

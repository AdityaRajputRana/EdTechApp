package com.quaser.edtechapp.models;

public class DateTime {
    String date_full;
    int date = 0;
    String month;
    int year;
    String day;
    String time;

    public DateTime() {
    }


    public String getDate_full() {
        return date_full;
    }
    public String getTime() {
        return time;
    }

    public int getDate() {
        return date;
    }

    public String getMonth() {
        return month;
    }
}

package com.quaser.edtechapp.models;

public class ShortEvent {
    String title;
    DateTime time;
    String description;
    String type;
    SeatInformation seats;
    String venue;
    Boolean is_paid;
    int price;
    Prerequisite prerequisite;
    Boolean is_locked;
    Boolean is_subscribed;

    public ShortEvent() {
    }

    public String getTitle() {
        return title;
    }

    public DateTime getTime() {
        return time;
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }

    public SeatInformation getSeats() {
        return seats;
    }

    public String getVenue() {
        return venue;
    }

    public Boolean getIs_paid() {
        return is_paid;
    }

    public int getPrice() {
        return price;
    }

    public Prerequisite getPrerequisite() {
        return prerequisite;
    }

    public Boolean getIs_locked() {
        return is_locked;
    }

    public Boolean getIs_subscribed() {
        return is_subscribed;
    }
}

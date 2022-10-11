package com.quaser.edtechapp.rest.response;

import com.quaser.edtechapp.models.ShortEvent;

public class EventDetailsRP extends ShortEvent {
    String location;
    String meet_link;
    String sdk;
    String key;
    String id;
    String pw;
    String sid;
    String sno;
    String orderId;

    public String getOrderId() {
        return orderId;
    }

    public EventDetailsRP() {
    }

    public boolean isOnline(){
        return  (getType() != null
        && getType().equals("online"));
    }

    public String getLocation() {
        if (getType() != null
        && getType().equals("online")){
            if (location != null && !location.isEmpty())
                return location;
            if (meet_link == null || meet_link.isEmpty())
                return getVenue();
            return meet_link;
        } else {
            if (location != null && !location.isEmpty())
                return location;
            else
                return getVenue();
        }
    }

    public String getMeet_link() {
        return meet_link;
    }

    public String getSdk() {
        return sdk;
    }

    public String getKey() {
        return key;
    }

    public String getId() {
        return id;
    }

    public String getPw() {
        return pw;
    }

    public String getSid() {
        return sid;
    }

    public String getSno() {
        return sno;
    }
}

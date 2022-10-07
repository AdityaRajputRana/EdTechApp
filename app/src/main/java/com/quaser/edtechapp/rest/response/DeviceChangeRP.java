package com.quaser.edtechapp.rest.response;

public class DeviceChangeRP {
    String user_id;
    String old_device_id;
    String new_device_id;
    String message;
    String status;
    String placeholder;
    long created_at;

    public String getMessage() {
        return message;
    }

    public boolean isNew(){
        return status != null && status.equals("nothing");
    }

    public String getStatus() {
        if (status == null){
            return "Looks like you are on a new device.";
        }
        if (status.equals("pending")){
            return "Your request has been submitted and would be soon reviewed by admins";
        } else if (status.equals("rejected") || status.equals("false")){
            return "Your request to change device was rejected";
        } else if (status.equals("accepted")  || status.equals("true")) {
            return "Your device was changed successfully. Log out from here to log into your account";
        }
        return "Looks like you logged into a new device. Please fill the form below.";
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public boolean showResubmit(){
        return status != null && (status.equals("rejected") || status.equals("false"));
    }

    public boolean showPlaceholder(){
        return status != null && (status.equals("rejected") || status.equals("false"))
                && placeholder != null && !placeholder.isEmpty();
    }

    public DeviceChangeRP() {
    }
}

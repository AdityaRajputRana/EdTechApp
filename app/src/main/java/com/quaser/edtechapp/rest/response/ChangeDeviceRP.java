package com.quaser.edtechapp.rest.response;

public class ChangeDeviceRP {
    boolean is_old_request_pending;
    boolean is_new_request_created;
    String message;
    String  device_id;

    public boolean isIs_old_request_pending() {
        return is_old_request_pending;
    }

    public boolean isIs_new_request_created() {
        return is_new_request_created;
    }

    public String getMessage() {
        return message;
    }

    public String getDevice_id() {
        return device_id;
    }

    public ChangeDeviceRP() {
    }
}

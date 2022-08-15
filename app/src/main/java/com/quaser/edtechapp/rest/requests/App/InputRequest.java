package com.quaser.edtechapp.rest.requests.App;

public class InputRequest {
    String random_string;
    Object input;
    long timestamp;
    String hash;

    public InputRequest(String random_string, Object input, long timestamp, String hash) {
        this.random_string = random_string;
        this.input = input;
        this.timestamp = timestamp;
        this.hash = hash;
    }
}

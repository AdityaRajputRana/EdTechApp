package com.quaser.edtechapp.rest.response;

public class PaymentRP {

    String lesson_id;
    String title;
    String short_description;
    String description;
    String type;
    boolean is_complete;
    String completion;
    boolean is_locked;

    int amount;
    int sub_amount;
    String price;
    String currency;
    String price_description;


    public String getLesson_id() {
        return lesson_id;
    }

    public String getTitle() {
        return title;
    }

    public String getShort_description() {
        return short_description;
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }

    public boolean isIs_complete() {
        return is_complete;
    }

    public String getCompletion() {
        return completion;
    }

    public boolean isIs_locked() {
        return is_locked;
    }

    public int getAmount() {
        return amount;
    }

    public int getSub_amount() {
        return sub_amount;
    }

    public String getPrice() {
        return price;
    }

    public String getCurrency() {
        return currency;
    }

    public String getPrice_description() {
        return price_description;
    }

    public PaymentRP() {
    }
}

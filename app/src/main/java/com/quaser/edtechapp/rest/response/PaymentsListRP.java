package com.quaser.edtechapp.rest.response;

import com.quaser.edtechapp.models.PaymentModel;

import java.util.ArrayList;

public class PaymentsListRP {
    int page;
    int count;
    int pages;
    ArrayList<PaymentModel> payHistory;

    public int getPage() {
        return page;
    }

    public int getCount() {
        return count;
    }

    public int getPages() {
        return pages;
    }

    public ArrayList<PaymentModel> getPayHistory() {
        return payHistory;
    }

    public PaymentsListRP() {
    }
}

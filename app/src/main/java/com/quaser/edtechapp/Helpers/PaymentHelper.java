package com.quaser.edtechapp.Helpers;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.quaser.edtechapp.R;
import com.quaser.edtechapp.models.Order;
import com.quaser.edtechapp.rest.response.LessonOrderIdRp;
import com.razorpay.Checkout;
import com.razorpay.PayloadHelper;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.util.Locale;

public class PaymentHelper{

    FirebaseUser user;

    interface Listener{
    }
    private Context applicationContext;
    private String apiKey;
    private Checkout checkout;

    private Listener listener;
    private LessonOrderIdRp orderIdRp;

    private Order order;
    private Activity activity;

    private PaymentResultListener paymentResultListener;

    private static PaymentHelper paymentHelperInstance;

    public static PaymentHelper getInstance(){
        return paymentHelperInstance;
    }

    private PaymentHelper(Context context, Activity activity){
        this.applicationContext = context;
        this.activity = activity;
        this.user = FirebaseAuth.getInstance().getCurrentUser();
        Checkout.preload(context);
    }

    public static PaymentHelper newInstance(Context context, Activity activity){
        paymentHelperInstance = new PaymentHelper(context, activity);
        return paymentHelperInstance;
    }

    public void startPayments(LessonOrderIdRp orderIdRp, Listener listener){
        this.listener = listener;
        this.orderIdRp = orderIdRp;
        this.order = orderIdRp.getOrder();
        createPaymentJSON();
    }

    public void success(String s){
        Toast.makeText(activity, "success", Toast.LENGTH_SHORT).show();
    }

    public void fail(String s){
        Toast.makeText(activity, "failed", Toast.LENGTH_SHORT).show();
    }



    private void createPaymentJSON() {
        checkout = new Checkout();
        checkout.setKeyID(orderIdRp.getApi_key());
        checkout.setImage(R.mipmap.ic_launcher);

        PayloadHelper payloadHelper = new PayloadHelper(order.getCurrency(), order.getAmount(), order.getId());

        payloadHelper.setName(applicationContext.getString(R.string.app_name));
        payloadHelper.setDescription("Receipt: "+ order.getReceipt());
        payloadHelper.setColor("#" + Integer.toHexString(applicationContext.getColor(R.color.color_bg))
                .toUpperCase());
        payloadHelper.setRetryEnabled(true);
        payloadHelper.setRetryMaxCount(3);
        payloadHelper.setSendSmsHash(true);
        payloadHelper.setPrefillName(user.getDisplayName());
        payloadHelper.setPrefillContact(user.getPhoneNumber());

        JSONObject options = payloadHelper.getJson();
        checkout.open(activity, options);
    }
}

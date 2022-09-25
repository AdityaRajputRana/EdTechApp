package com.quaser.edtechapp.Helpers;

import android.app.Activity;
import android.content.Context;
import android.util.Log;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.quaser.edtechapp.R;
import com.quaser.edtechapp.models.Order;
import com.quaser.edtechapp.rest.response.LessonOrderIdRp;
import com.quaser.edtechapp.utils.Method;
import com.razorpay.Checkout;
import com.razorpay.PayloadHelper;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.util.Locale;

public class PaymentHelper{

    FirebaseUser user;

    public interface Listener{
        void verifySuccess(String s);
        void failed(String s);
    }
    private Context applicationContext;
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

    private PaymentHelper(Context context, Activity activity, Listener listener){
        this.applicationContext = context;
        this.activity = activity;
        this.listener = listener;
        this.user = FirebaseAuth.getInstance().getCurrentUser();
        Checkout.preload(context);
    }

    public static PaymentHelper newInstance(Context context, Activity activity, Listener listener){
        paymentHelperInstance = new PaymentHelper(context, activity, listener);
        return paymentHelperInstance;
    }

    public void startPayments(LessonOrderIdRp orderIdRp){
        this.orderIdRp = orderIdRp;
        this.order = orderIdRp.getOrder();
        createPaymentJSON();
    }

    public void success(String s){
        Log.i("PHPayment", "success: "+  s);
        listener.verifySuccess(s);
    }

    public void fail(String s){
        Log.i("PHPayment", "fail: "+  s);
        String error = s;
        try{
            JSONObject object = new JSONObject(s);
            error = object.getJSONObject("error").getString("description");
        } catch (Exception e){
            e.printStackTrace();
        }
        Method.showFailedAlert(activity, error);
        listener.failed(error);
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

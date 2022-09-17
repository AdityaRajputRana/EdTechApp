package com.quaser.edtechapp.Helpers;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.Toast;

import com.quaser.edtechapp.models.ShortEvent;
import com.quaser.edtechapp.rest.api.APIMethods;
import com.quaser.edtechapp.rest.api.interfaces.APIResponseListener;
import com.quaser.edtechapp.rest.response.EventRP;
import com.quaser.edtechapp.rest.response.EventSubscriptionRP;
import com.quaser.edtechapp.rest.response.LessonOrderIdRp;
import com.quaser.edtechapp.utils.Method;

public class EventSubscriptionHelper {
    public interface EventSubscriptionListener{
        void success(EventSubscriptionRP res);
        void fail(String message, String code);
        void showProgress(String message);
        void hideProgress(String message);
    }

    private ShortEvent event;
    private Context context;
    private EventSubscriptionRP eventSubscriptionRP;
    private EventSubscriptionListener listener;
    private String unitId;
    private String lessonId;

    public EventSubscriptionHelper(ShortEvent event, Context context, EventSubscriptionListener listener,
                                   String lessonId, String unitId){
        this.context = context;
        this.event = event;
        this.listener = listener;
        this.lessonId = lessonId;
        this.unitId = unitId;
    }

    public void subscribe(){
        listener.showProgress("Initiating Payment");

        if (event.getIs_paid() != null && (event.getIs_paid()  || event.getPrice() > 0)){
            APIMethods.subscribeToPaidEvent(event.getEvent_id(), unitId, lessonId, new APIResponseListener<LessonOrderIdRp>() {
                @Override
                public void success(LessonOrderIdRp response) {
                    startPayments(response);
                }

                @Override
                public void fail(String code, String message, String redirectLink, boolean retry, boolean cancellable) {
                    listener.hideProgress(message);
                    listener.fail(message, code);
                }
            });
        } else {
            APIMethods.subscribeToFreeEvent(event.getEvent_id(), unitId, lessonId, new APIResponseListener<String>() {
                @Override
                public void success(String response) {
                    listener.hideProgress(null);
                    listener.success(null);
                }

                @Override
                public void fail(String code, String message, String redirectLink, boolean retry, boolean cancellable) {
                    listener.hideProgress(message);
                    listener.fail(message, code);
                }
            });
        }
    }

    PaymentHelper paymentHelper;
    String orderId;

    private void startPayments(LessonOrderIdRp response) {
        paymentHelper =  PaymentHelper.newInstance(context, (Activity) context, new PaymentHelper.Listener() {
            @Override
            public void verifySuccess(String s) {
                APIMethods.verifyLessonPayment(s, orderId, EventSubscriptionRP.class, new APIResponseListener<EventSubscriptionRP>() {
                    @Override
                    public void success(EventSubscriptionRP response) {
                        Toast.makeText(context, "Payment Successful!", Toast.LENGTH_SHORT).show();
                        listener.success(response);
                    }

                    @Override
                    public void fail(String code, String message, String redirectLink, boolean retry, boolean cancellable) {
                        listener.hideProgress("Failed");
                        listener.fail(message, code);
                    }
                });
            }
        });

        orderId = response.getOrder().getId();
        paymentHelper.startPayments(response);

    }
}

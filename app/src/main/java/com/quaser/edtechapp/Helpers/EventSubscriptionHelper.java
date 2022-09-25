package com.quaser.edtechapp.Helpers;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.quaser.edtechapp.models.ShortEvent;
import com.quaser.edtechapp.rest.api.APIMethods;
import com.quaser.edtechapp.rest.api.interfaces.APIResponseListener;
import com.quaser.edtechapp.rest.response.EventsListRP;
import com.quaser.edtechapp.rest.response.LessonOrderIdRp;
import com.quaser.edtechapp.rest.response.SubscribeEventRP;

public class EventSubscriptionHelper {
    public interface EventSubscriptionListener{
        void success(SubscribeEventRP res);
        void fail(String message, String code);
        void showProgress(String message);
        void hideProgress(String message);
    }

    private ShortEvent event;
    private Context context;
    private EventsListRP eventsListRP;
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
        
        if (event.getIs_subscribed()){
            Toast.makeText(context, "Already Subscribed to this event!", Toast.LENGTH_SHORT).show();
            return;
        }
        

        if (event.getIs_paid() != null && event.getIs_paid() || event.getPrice() > 0){
            listener.showProgress("Initiating Payment");

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
            listener.showProgress("Subscribing Event");
            APIMethods.subscribeToFreeEvent(event.getEvent_id(), unitId, lessonId, new APIResponseListener<SubscribeEventRP>() {
                @Override
                public void success(SubscribeEventRP response) {
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
        listener.hideProgress("Payment complete");
        listener.showProgress("Verifying payment");
        paymentHelper =  PaymentHelper.newInstance(context, (Activity) context, new PaymentHelper.Listener() {
            @Override
            public void verifySuccess(String s) {
                APIMethods.verifyLessonPayment(s, orderId, SubscribeEventRP.class, new APIResponseListener<SubscribeEventRP>() {
                    @Override
                    public void success(SubscribeEventRP response) {
                        Toast.makeText(context, "Payment Successful!", Toast.LENGTH_SHORT).show();
                        listener.hideProgress("Verified!");
                        listener.success(response);
                    }

                    @Override
                    public void fail(String code, String message, String redirectLink, boolean retry, boolean cancellable) {
                        listener.hideProgress("Failed");
                        listener.fail(message, code);
                    }
                });
            }

            @Override
            public void failed(String s){
                listener.hideProgress("s");
            }
        });

        orderId = response.getOrder().getId();
        paymentHelper.startPayments(response);

    }
}

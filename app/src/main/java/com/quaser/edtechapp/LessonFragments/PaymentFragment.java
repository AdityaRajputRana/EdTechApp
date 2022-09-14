package com.quaser.edtechapp.LessonFragments;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.quaser.edtechapp.Auth.AuthUtils;
import com.quaser.edtechapp.Helpers.PaymentHelper;
import com.quaser.edtechapp.Interface.LessonListener;
import com.quaser.edtechapp.R;
import com.quaser.edtechapp.models.ShortLesson;
import com.quaser.edtechapp.rest.api.API;
import com.quaser.edtechapp.rest.api.APIMethods;
import com.quaser.edtechapp.rest.api.interfaces.APIResponseListener;
import com.quaser.edtechapp.rest.response.LessonOrderIdRp;
import com.quaser.edtechapp.rest.response.PaymentRP;
import com.quaser.edtechapp.utils.Method;
import com.razorpay.Checkout;

public class PaymentFragment extends Fragment {

    private TextView head;
    private TextView body;
    private TextView currencySymbol;
    private TextView priceTxt;
    private TextView subPriceTxt;
    private LinearLayout paymentLayout;
    private TextView bottomTxt;
    private ProgressBar progressBar;
    private MaterialButton continueBtn;


    private String unitId;
    private ShortLesson shortLesson;
    private LessonListener listener;

    private PaymentRP paymentRP;
    private PaymentHelper paymentHelper;
    private Context context;


    public PaymentFragment() {
        // Required empty public constructor
    }

    public PaymentFragment(String unitId, ShortLesson shortLesson, LessonListener listener, Context context){
        this.unitId = unitId;
        this.shortLesson = shortLesson;
        this.listener = listener;
        this.context =context;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_payment, container, false);
        findViews(view);
        hideViews();
        setUpTitles();
        fetchPayment();
        return view;
    }

    private void fetchPayment() {
        APIMethods.getLesson(shortLesson.getId(), PaymentRP.class, new APIResponseListener<PaymentRP>() {
            @Override
            public void success(PaymentRP response) {
                progressBar.setVisibility(View.GONE);
                paymentRP = response;
                showFullLesson();
            }

            @Override
            public void fail(String code, String message, String redirectLink, boolean retry, boolean cancellable) {
                Method.showFailedAlert(getActivity(), "Failed: "
                        + code+" - " + message);
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void showFullLesson() {
        if (paymentRP.getTitle() != null
        && !paymentRP.getTitle().isEmpty()){
            head.setText(paymentRP.getTitle());
            head.setVisibility(View.VISIBLE);
        }

        if (paymentRP.getDescription() != null
                && !paymentRP.getDescription().isEmpty()){
            body.setText(paymentRP.getDescription());
            body.setVisibility(View.VISIBLE);
        }

        if (paymentRP.getPrice_description() != null
                && !paymentRP.getPrice_description().isEmpty()){
            bottomTxt.setText(paymentRP.getPrice_description());
            bottomTxt.setVisibility(View.VISIBLE);
        }

        if (paymentRP.getCurrency() != null
                && !paymentRP.getCurrency().isEmpty()){
            currencySymbol.setText(paymentRP.getCurrency());
        }

        priceTxt.setText(String.valueOf(paymentRP.getAmount()));

        if (paymentRP.getSub_amount() > 0){
            int sub = paymentRP.getSub_amount();
            if (sub < 10){
                sub *= 10;
            }
            subPriceTxt.setText(String.valueOf(sub));
        }

        paymentLayout.setVisibility(View.VISIBLE);

        continueBtn.setVisibility(View.VISIBLE);
        continueBtn.setOnClickListener(view -> {
            if (FirebaseAuth.getInstance().getCurrentUser().isAnonymous())
                askToLogin();
            else
                fetchOrderId();
        });
        paymentHelper =  PaymentHelper.newInstance(context, getActivity(), new PaymentHelper.Listener() {
            @Override
            public void verifySuccess(String s) {
                APIMethods.verifyLessonPayment(s, orderId, new APIResponseListener<String>() {
                    @Override
                    public void success(String response) {
                        listener.nextLesson();
                        Toast.makeText(getActivity(), "Payment Successful!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void fail(String code, String message, String redirectLink, boolean retry, boolean cancellable) {
                        progressBar.setVisibility(View.GONE);
                        bottomTxt.setText(message);
                        bottomTxt.setTextColor(Color.RED);
                        continueBtn.setVisibility(View.VISIBLE);
                        continueBtn.setEnabled(true);
                        Method.showFailedAlert(getActivity(), "Failed: "
                                + code+" - " + message);
                    }
                });
            }
        });
    }

    private void askToLogin() {
        //Todo: Provide option to log into same ano account
        Toast.makeText(getActivity(), "Anonymous users can make payments. Please log in.", Toast.LENGTH_SHORT).show();
    }

    private void fetchOrderId() {
        progressBar.setVisibility(View.VISIBLE);
        bottomTxt.setVisibility(View.VISIBLE);
        bottomTxt.setText("Please Wait! Don't press any button or may loose money.");
        continueBtn.setVisibility(View.GONE);

        APIMethods.getOrderId(shortLesson.getId(), unitId, new APIResponseListener<LessonOrderIdRp>() {
            @Override
            public void success(LessonOrderIdRp response) {
                startPayments(response);
            }

            @Override
            public void fail(String code, String message, String redirectLink, boolean retry, boolean cancellable) {
                Method.showFailedAlert(getActivity(), "Failed: "
                        + code+" - " + message);
                progressBar.setVisibility(View.GONE);
                bottomTxt.setText(message);
                continueBtn.setVisibility(View.VISIBLE);
            }
        });
    }
    
    String orderId;
    private void startPayments(LessonOrderIdRp response) {
        orderId = response.getOrder().getId();
        paymentHelper.startPayments(response, null);
    }

    private void setUpTitles() {
        if (shortLesson.getName() != null
        && !shortLesson.getName().isEmpty()){
            head.setText(shortLesson.getName());
            head.setVisibility(View.VISIBLE);
        }

        if (shortLesson.getShort_description() != null
                && !shortLesson.getShort_description().isEmpty()){
            body.setText(shortLesson.getShort_description());
            body.setVisibility(View.VISIBLE);
        }
        
    }

    private void hideViews() {
        body.setVisibility(View.GONE);
        paymentLayout.setVisibility(View.GONE);
        continueBtn.setVisibility(View.GONE);
    }

    private void findViews(View view) {
        head = view.findViewById(R.id.head);
        body = view.findViewById(R.id.body);
        currencySymbol = view.findViewById(R.id.rupeeSymbol);
        priceTxt = view.findViewById(R.id.priceRupeeTxt);
        subPriceTxt = view.findViewById(R.id.pricePaiseTxt);
        progressBar = view.findViewById(R.id.progressBar);
        paymentLayout = view.findViewById(R.id.paymentLayout);
        bottomTxt = view.findViewById(R.id.bottomTxt);
        continueBtn = view.findViewById(R.id.continueBtn);
    }
}
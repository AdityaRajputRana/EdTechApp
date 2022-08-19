package com.quaser.edtechapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.quaser.edtechapp.Auth.AuthUtils;
import com.quaser.edtechapp.rest.api.API;
import com.quaser.edtechapp.rest.api.APIMethods;
import com.quaser.edtechapp.rest.api.HashUtils;
import com.quaser.edtechapp.rest.api.VolleyClient;
import com.quaser.edtechapp.rest.api.interfaces.APIResponseListener;
import com.quaser.edtechapp.rest.response.AnonymousRP;
import com.quaser.edtechapp.rest.response.LoginRP;
import com.quaser.edtechapp.utils.Method;

import java.util.concurrent.TimeUnit;

import in.aabhasjindal.otptextview.OTPListener;
import in.aabhasjindal.otptextview.OtpTextView;

public class LoginActivity extends AppCompatActivity {

    private ConstraintLayout phoneLayout;
    private ConstraintLayout otpLayout;

    private EditText phoneEt;
    private TextView countryCodeTxt;
    private EditText otpEt;

    private ProgressBar progressBar;
    private TextView messageTxt;

    private TextView phoneNumTxt;
    private TextView wrongNumTxt;
    private TextView resentOtpTxt;
    private MaterialButton verifyBtn;
    private OtpTextView otpTextView;

    private int screenState = 0;
    private RequestQueue requestQueue;
    
    FirebaseAuth auth;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        initialisePhoneAuth();
        findViews();
        setVisibility();
        setUpBackBtn();
        setOnClickListener();
        initialiseVolley();
    }

    private void initialisePhoneAuth() {
        auth = FirebaseAuth.getInstance();
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                startProgress("Phone number verified! Logging you in.");
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                mVerificationId = verificationId;
                mResendToken = token;
                otpLayout.setVisibility(View.VISIBLE);
                setUpOtpLayout();
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    Method.showFailedAlert(LoginActivity.this, "Invalid Credentials");
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    Method.showFailedAlert(LoginActivity.this, "Reached max quota of SMS");
                } else {
                    Method.showFailedAlert(LoginActivity.this, e.getMessage());
                }
                e.printStackTrace();

                showPhoneLayout();


            }
        };
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            startProgress("Phone number verified! Logging you in.");
                            login(task.getResult().getAdditionalUserInfo().isNewUser());
                            // Todo: Communicate to our server and login
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                wrongCode();
                            }
                        }
                    }
                });
    }

    private boolean userCreatedOnServer = false;
    private boolean userCreatedOnFirebase = false;

    private void login(boolean newUser) {
        if (newUser){
            startProgress("Please wait while we set up your new Account!");
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName("User")
                    .build();
            FirebaseAuth
                    .getInstance()
                    .getCurrentUser()
                    .updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        userCreatedOnFirebase = true;
                        startNameActivity();
                    }else{
                        startProgress("Failed to update user name");
                    }
                }
            });

        } else
            userCreatedOnFirebase = true;
        APIMethods.login(new APIResponseListener<LoginRP>() {
            @Override
            public void success(LoginRP response) {
                Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                if (newUser || response.isIs_new_user()){
                    userCreatedOnServer = true;
                    startNameActivity();
                } else
                    startMainActivity();
            }

            @Override
            public void fail(String code, String message, String redirectLink, boolean retry, boolean cancellable) {
                Method.showFailedAlert(LoginActivity.this, code
                + "-"  + message);
                stopProgress();
                phoneLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    private void wrongCode() {
        otpLayout.setVisibility(View.VISIBLE);
        otpTextView.showError();
        Method.showFailedAlert(this, "Invalid OTP");
        stopProgress();
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() != null){
            startMainActivity();
        }
    }

    private void setUpOtpLayout() {
        stopProgress();
        otpLayout.setVisibility(View.VISIBLE);

        if (phoneNumTxt == null){
            findOTPViews();
        }

        phoneNumTxt.setText("OTP sent successfully to\n" + countryCode
         + " " + phoneEt.getText().toString());

        otpTextView.requestFocusOTP();
        InputMethodManager inputMethodManager =
                (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInputFromWindow(
                otpTextView.getApplicationWindowToken(),
                InputMethodManager.SHOW_FORCED, 0);

        otpTextView.setOtpListener(new OTPListener() {
            @Override
            public void onInteractionListener() {

            }

            @Override
            public void onOTPComplete(String otp) {
                verifyOTP(otp);
            }
        });

        verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (otpTextView.getOTP().toString().length() < 6){
                    otpTextView.showError();
                    otpTextView.requestFocusOTP();
                } else {
                    otpTextView.resetState();
                }
            }
        });

    }

    private void verifyOTP(String otp) {
        startProgress("Verifying your phone number");
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, otp);
        signInWithPhoneAuthCredential(credential);
        otpLayout.setVisibility(View.GONE);
    }

    private void findOTPViews() {
        phoneNumTxt = findViewById(R.id.otp_enter_txt);
        wrongNumTxt = findViewById(R.id.wrong_number_txt);
        resentOtpTxt = findViewById(R.id.resend_otp_txt);
        resentOtpTxt = findViewById(R.id.resend_otp_txt);
        verifyBtn = findViewById(R.id.verifyBtn);
        otpTextView = findViewById(R.id.otp_view);
    }

    private void showPhoneLayout() {
        phoneLayout.setVisibility(View.VISIBLE);
        otpLayout.setVisibility(View.GONE);
        phoneEt.setEnabled(true);
        stopProgress();
    }

    private void initialiseVolley() {
        requestQueue = VolleyClient.getRequestQueue();
    }

    private void setOnClickListener() {
        findViewById(R.id.continueBtn)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        confirmPhoneNumber();
                    }
                });

        findViewById(R.id.skipBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginAnonymously();
            }
        });
    }

    private void loginAnonymously() {
        startProgress("Signing you in anonymously");
        phoneLayout.setVisibility(View.GONE);

        startFirebaseAnonym();
    }

    private void startFirebaseAnonym() {
        auth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            startProgress("Please wait while we are setting up your account!");
                            saveAnonymUser();
                        } else {
                            stopProgress();
                            phoneLayout.setVisibility(View.VISIBLE);
                            Method.showFailedAlert(LoginActivity.this, task.getException().getMessage());
                        }
                    }
                });
    }

    private void saveAnonymUser() {
        APIMethods.signInAnonymously(new APIResponseListener<AnonymousRP>() {
            @Override
            public void success(AnonymousRP response) {
                Toast.makeText(LoginActivity.this, "Signed in anonymously", Toast.LENGTH_SHORT).show();
                startMainActivity();
            }

            @Override
            public void fail(String code, String message, String redirectLink, boolean retry, boolean cancellable) {
                Method.showFailedAlert(LoginActivity.this, code+ "-" + message);
                phoneLayout.setVisibility(View.VISIBLE);
                stopProgress();
            }
        });
    }

    private void startNameActivity() {
        if (userCreatedOnServer && userCreatedOnFirebase) {
            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                Intent intent = new Intent(this, NameActivity.class);
                startActivity(intent);
                this.finish();
            } else {
                Toast.makeText(this, "Some error occurred: User is null", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void startMainActivity() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            AuthUtils.nameAdded(this);
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            this.finish();
        } else {
            Toast.makeText(this, "Some error occurred: User is null", Toast.LENGTH_SHORT).show();
        }
    }

    private void confirmPhoneNumber() {
        String phoneNum = phoneEt.getText().toString();
        if (phoneNum.length() != 10){
            //Todo: Make it acc to int numbers
            phoneEt.setError("Phone number should be of 10 digits");
        } else {
            phoneEt.setError(null);
            sendOtp();
        }
    }

    String countryCode = "+91";
    int verificationFlag = 0;  //0-Not sent otp, 1- OTP sent, 2-OTP is verified //Todo: Make this according to docs in firebase auth for proper restarts.


    private void sendOtp() {
        startProgress("Sending otp to your phone number");
        phoneLayout.setVisibility(View.GONE);
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber(countryCode + phoneEt.getText().toString())       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
        verificationFlag = 1;
        screenState++;
    }



    private void startProgress() {
        startProgress("");
    }

    private void startProgress(String s) {
        if (!s.isEmpty()){
            messageTxt.setText(s);
        }
        messageTxt.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void stopProgress() {
        messageTxt.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
    }

    private void setUpBackBtn() {
//        findViewById(R.id.backBtn).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onBackPressed();
//            }
//        });
    }

    @Override
    public void onBackPressed() {
        switch (screenState){
            case 1:
                phoneLayout.setVisibility(View.VISIBLE);
                otpLayout.setVisibility(View.GONE);
                screenState--;
                break;
            default:
                super.onBackPressed();
        }
    }

    private void findViews() {
        phoneLayout = findViewById(R.id.phoneLayout);
        otpLayout = findViewById(R.id.otpLayout);

        countryCodeTxt = findViewById(R.id.countryCodeTxt);
        phoneEt = findViewById(R.id.phoneNumberEt);
        //Todo: find otp et

        messageTxt = findViewById(R.id.messageTxt);
        progressBar = findViewById(R.id.progressBar);
    }

    private void setVisibility() {
        phoneLayout.setVisibility(View.VISIBLE);
        otpLayout.setVisibility(View.GONE);
    }

    public void showWhyPopUp(View view) {
        //Todo: improve UI for popup

        new AlertDialog.Builder(this)
                .setTitle("Why login?")
                .setMessage("Login is required to sync your course progress across devices, payments and subscribe to events that are tagged to your name.")
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setCancelable(true)
                .show();
    }
}
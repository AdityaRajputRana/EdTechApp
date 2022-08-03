package com.quaser.edtechapp.app;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class MyApplication extends Application {
    private static RequestQueue mainRequestQueue;

    @Override
    public void onCreate() {
        super.onCreate();
        mainRequestQueue = Volley.newRequestQueue(getApplicationContext());
    }

    public static RequestQueue getMainRequestQueue() {
        return mainRequestQueue;
    }
}
package com.quaser.edtechapp.rest.api;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.quaser.edtechapp.app.MyApplication;

public class VolleyClient {

    public static String BASE_URL = "https://quasar-edtech.vercel.app/edtech/";
    public static String HOST= "quasar-edtech.herokuapp.com";

    public static String getBaseUrl() {
        return BASE_URL;
    }

    public static RequestQueue getRequestQueue() {
        return requestQueue;
    }

    private static RequestQueue requestQueue = MyApplication.getMainRequestQueue();

}

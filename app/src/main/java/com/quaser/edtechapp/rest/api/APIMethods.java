package com.quaser.edtechapp.rest.api;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.quaser.edtechapp.rest.api.interfaces.APIResponseListener;
import com.quaser.edtechapp.rest.requests.HomeReq;
import com.quaser.edtechapp.rest.response.AnonymousRP;
import com.quaser.edtechapp.rest.response.HomeRP;

public class APIMethods {
    public static void signInAnonymously(APIResponseListener<AnonymousRP> listener){
        API.postData(listener, "{}", EndPoints.anonymous, AnonymousRP.class);
    }

    public static void getHomeData(APIResponseListener<HomeRP> listener, Activity context){
        HomeReq req = new HomeReq(context);
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        API.postData(listener, req,  EndPoints.home, HomeRP.class);
    }


}

package com.quaser.edtechapp.rest.api;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.quaser.edtechapp.rest.api.interfaces.APIResponseListener;
import com.quaser.edtechapp.rest.requests.HomeReq;
import com.quaser.edtechapp.rest.requests.UnitReq;
import com.quaser.edtechapp.rest.response.AnonymousRP;
import com.quaser.edtechapp.rest.response.HomeRP;
import com.quaser.edtechapp.rest.response.UnitRP;

public class APIMethods {
    public static void signInAnonymously(APIResponseListener<AnonymousRP> listener){
        API.postData(listener, "{}", EndPoints.anonymous, AnonymousRP.class);
    }

    public static void getHomeData(APIResponseListener<HomeRP> listener, Activity context){
        HomeReq req = new HomeReq(context);
        API.postData(listener, req,  EndPoints.home, HomeRP.class);
    }

    public static void getUnit(String unitId, Activity context, APIResponseListener<UnitRP> listener){
        UnitReq req = new UnitReq(context, unitId);
        API.postData(listener, req, EndPoints.unit, UnitRP.class);
    }


}

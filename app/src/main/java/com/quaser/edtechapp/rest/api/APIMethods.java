package com.quaser.edtechapp.rest.api;

import com.quaser.edtechapp.rest.api.interfaces.APIResponseListener;
import com.quaser.edtechapp.rest.response.AnonymousRP;

public class APIMethods {
    public static void signInAnonymously(APIResponseListener<?> listener){
        API.postData(listener, "{}", EndPoints.anonymous, AnonymousRP.class);
    }


}

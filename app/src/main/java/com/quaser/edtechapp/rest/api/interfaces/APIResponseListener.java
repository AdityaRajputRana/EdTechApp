package com.quaser.edtechapp.rest.api.interfaces;

import com.google.gson.Gson;
import com.quaser.edtechapp.rest.response.AnonymousRP;

public interface APIResponseListener<K> {
    void success(K response);
    default void convertData(Object data){
        try {
            K tData = (K) data;
            success(tData);
        } catch (Exception e){
            e.printStackTrace();
            fail("-2", "Not Convertible: " + e.getMessage(), "", true, true);
        }
    }


    void fail(String code, String message, String redirectLink, boolean retry, boolean cancellable);
}

package com.quaser.edtechapp.rest.api;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.google.gson.Gson;
import com.quaser.edtechapp.app.MyApplication;
import com.quaser.edtechapp.rest.api.EndPoints;
import com.quaser.edtechapp.rest.api.VolleyClient;
import com.quaser.edtechapp.rest.api.interfaces.AnonymousResListener;
import com.quaser.edtechapp.rest.response.AnonymousRP;

import org.json.JSONException;
import org.json.JSONObject;

public class API {
    public static void signInAnonymously(AnonymousResListener listener, String data){
        try {
            JSONObject request = new JSONObject(data);
            String url = VolleyClient.getBaseUrl() + EndPoints.anonymous;
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.POST, url, request, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Boolean successful = response.getBoolean("success");
                                if (successful) {
                                    listener.success(new Gson().fromJson(response.getJSONObject("data").toString(), AnonymousRP.class));
                                } else {
                                    listener.fail("1", request.getString("message"), "", true, false);
                                }
                            } catch (Exception e) {
                                listener.fail("1", "The received response is not good", "", true, false);
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            listener.fail("000", error.getMessage(), "", true, false);
                        }
                    });

            VolleyClient.getRequestQueue().add(jsonObjectRequest);

        } catch (Exception e){
            e.printStackTrace();
        }



    }
}

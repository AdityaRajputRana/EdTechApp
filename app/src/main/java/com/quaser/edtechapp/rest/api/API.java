package com.quaser.edtechapp.rest.api;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.quaser.edtechapp.rest.api.interfaces.APIResponseListener;
import com.quaser.edtechapp.rest.response.AnonymousRP;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;

public class API {

    public HashMap<String, String> hashMap;

    public static void postData(APIResponseListener listener, String rawData, String endpoint, Class klass){
        try {
            String data = HashUtils.getHashedData(rawData);
            JSONObject request = new JSONObject(data);
            String url = VolleyClient.getBaseUrl() + endpoint;
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.POST, url, request, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.i("eta reponse", data.toString());
                            try {
                                Boolean successful = response.getBoolean("success");
                                if (successful) {
                                    String data = response.getJSONObject("data").toString();
//                                    String decodedData = HashUtils.fromBase64(data);
                                    listener.convertData(new Gson().fromJson(data, klass));
                                } else {
                                    listener.fail("2", request.getString("message"), "", true, false);
                                }
                            } catch (Exception e) {
                                listener.fail("1", "The received response is not good", "", true, false);
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (error != null) {
                                if (error.networkResponse != null) {
                                    String message = "";
                                    if (error.networkResponse.data != null) {
                                        try {
                                            JSONObject jsonObject = new JSONObject(error.networkResponse.data.toString());
                                            message = message+" " +jsonObject.getString("message");
                                        } catch (Exception e){
                                            e.printStackTrace();
                                            message = message+" Json Conversion error.";
                                        }
                                    }
                                    message = message +" " + error.getMessage();
                                    listener.fail(String.valueOf(error.networkResponse.statusCode), message, "", true, false);
                                }
                            }
                        }
                    });

            VolleyClient.getRequestQueue().add(jsonObjectRequest);

        } catch (Exception e){
            e.printStackTrace();
        }



    }
}

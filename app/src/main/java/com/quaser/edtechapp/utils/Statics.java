package com.quaser.edtechapp.utils;

import com.quaser.edtechapp.rest.api.APIMethods;
import com.quaser.edtechapp.rest.api.interfaces.APIResponseListener;
import com.quaser.edtechapp.rest.response.TagsRP;

import java.util.ArrayList;
import java.util.Collections;

public class Statics {


    public interface TagsListener{
        void onResult (boolean isSuccess, String message, ArrayList<String> tags);
    }

    private static ArrayList<String> tagsList;

    public static void getTagsList(TagsListener listener){
        if (tagsList != null)
            listener.onResult(true, "", tagsList);
        else {
            APIMethods.getTags(new APIResponseListener<TagsRP>() {
                @Override
                public void success(TagsRP response) {
                    tagsList = response.getTags();
                    Collections.reverse(tagsList);
                    tagsList.add("All");
                    Collections.reverse(tagsList);
                    listener.onResult(true, "", tagsList);
                }

                @Override
                public void fail(String code, String message, String redirectLink, boolean retry, boolean cancellable) {
                    listener.onResult(false, code + " - " + message, null);
                }
            });
        }
    }



}

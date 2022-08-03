package com.quaser.edtechapp.Auth;

import android.content.Context;
import android.content.SharedPreferences;

public class AuthUtils {
    private static AuthUtils authUtils;
    private Context context;
    private SharedPreferences mPref;
    public static AuthUtils getInstance(Context context){
        if (authUtils == null){
            authUtils = new AuthUtils(context);
        }
        return authUtils;
    }

    public AuthUtils(Context context) {
        this.context = context;
    }

    public boolean isLoggedIn(){
        return getSharedPref().getBoolean("user/isLoggedIn", false);
    }

    public String getUserName(){
        return getSharedPref().getString("user/name", "User");
    }

    public String getUserId(){
        return getSharedPref().getString("user/id", "");
    }

    public void setUser(String userId, String userType, String name){
        SharedPreferences.Editor editor = getSharedPref().edit();
        editor.putBoolean("user/isLoggedIn", true);
        editor.putString("user/id", userId);
        editor.putString("user/name", name);
        editor.putString("user/type", userType);
        editor.commit();
    }

    public SharedPreferences getSharedPref(){
        if (mPref == null){
            mPref = context.getSharedPreferences("EdTech", Context.MODE_PRIVATE);
        }
        return mPref;
    }
}

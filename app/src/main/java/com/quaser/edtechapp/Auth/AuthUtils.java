package com.quaser.edtechapp.Auth;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.auth.FirebaseAuth;

import java.io.FileFilter;

public class AuthUtils {
    private static AuthUtils authUtils;
    private static SharedPreferences mPref;
    public static AuthUtils getInstance(){
        if (authUtils == null){
            authUtils = new AuthUtils();
        }
        return authUtils;
    }


    public static boolean isLoggedIn(){
        return FirebaseAuth.getInstance().getCurrentUser() != null;
    }

    public static String getUserName(){
        if (FirebaseAuth.getInstance().getCurrentUser().isAnonymous()) {
            return "User";
        } else {
            return FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        }
    }

    public static String getUserId(){
        if(FirebaseAuth.getInstance().getCurrentUser() != null)
            return FirebaseAuth.getInstance().getCurrentUser().getUid();
        else
            return "Error: Not logged in!";
    }

    public static boolean isNameAdded(Context context){
        return getSharedPref(context).getBoolean("isNameAdded",false);
    }

    public static void nameAdded(Context context){
        getSharedPref(context).edit().putBoolean("isNameAdded", true)
                .commit();
    }

//    public void setUser(String userId, String userType, String name){
//        SharedPreferences.Editor editor = getSharedPref().edit();
//        editor.putBoolean("user/isLoggedIn", true);
//        editor.putString("user/id", userId);
//        editor.putString("user/name", name);
//        editor.putString("user/type", userType);
//        editor.commit();
//    }

    public static SharedPreferences getSharedPref(Context context){
        if (mPref == null){
            mPref = context.getSharedPreferences("EdTech", Context.MODE_PRIVATE);
        }
        return mPref;
    }
}

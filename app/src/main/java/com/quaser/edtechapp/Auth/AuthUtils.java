package com.quaser.edtechapp.Auth;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;

public class AuthUtils {
    private static AuthUtils authUtils;
    private static SharedPreferences mPref;
    public static AuthUtils getInstance(){
        if (authUtils == null){
            authUtils = new AuthUtils();
        }
        return authUtils;
    }

    public static String getFormattedPhoneNum(){
        if (phoneNum.contains("+91")){
            String p1 = phoneNum.substring(3, 8);
            String p2 = phoneNum.substring(8, 13);
            return "+91 "+ p1 + " " + p2;
        }
        return phoneNum;
    }

    public static String phoneNum = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
    private static String dp(){
        if (isAnonymousUser) {
            return "";
        }
        if (FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl()
        == null){
            return "";
        }
        String url = FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString();
        Log.i("Profile Req", url);
        return url;
    }

    public static String dp = dp();


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

    public static boolean isAnonymousUser = isAnonym();

    private static boolean isAnonym() {
        if (FirebaseAuth.getInstance().getCurrentUser() == null)
            return true;
        return FirebaseAuth.getInstance().getCurrentUser().isAnonymous();
    }

    public static boolean isNameAdded(Context context){
        if (FirebaseAuth.getInstance().getCurrentUser().isAnonymous()){
            return true;
        }
        if (FirebaseAuth.getInstance().getCurrentUser().getDisplayName() == null ||
        FirebaseAuth.getInstance().getCurrentUser().getDisplayName().isEmpty()){
            return false;
        }
        return true;
//
//        return getSharedPref(context).getBoolean("isNameAdded",false);
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

    public static void setDeviceIdVerified(Context context, boolean isVerified){
        getSharedPref(context).edit()
                .putBoolean("isDeviceIDVerified", isVerified)
                .commit();
    }

    public static boolean isDeviceIdVerified(Context context){
        return getSharedPref(context).getBoolean("isDeviceIDVerified", false);
    }
}

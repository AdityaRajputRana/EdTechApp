package com.quaser.edtechapp.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PermissionInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import androidx.core.content.ContextCompat;

import java.security.Permission;
import java.util.UUID;

public class Tools {
    public static String getUniqueDeviceId(Activity activity){
        return  Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID);
//        String id = "";
//        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE)
//        == PermissionInfo.GR) {
//            final TelephonyManager tm = (TelephonyManager) activity.getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
//
//            final String tmDevice, tmSerial, androidId;
//            id = "" + tm.getDeviceId();
//        } else if ()
//        tmSerial = "" + tm.getSimSerialNumber();
//        WifiManager m_wm = (WifiManager)getSystemService(Context.WIFI_SERVICE);
//        String m_wlanMacAdd = m_wm.getConnectionInfo().getMacAddress();
//        androidId = "" + android.provider.Settings.Secure.getString(activity.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
//
//        UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
//        String deviceId = deviceUuid.toString();
    }
}

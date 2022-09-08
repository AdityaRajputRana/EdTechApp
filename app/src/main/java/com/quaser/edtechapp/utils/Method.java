package com.quaser.edtechapp.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class Method {
    public static void showFailedAlert(Context context, String message){
        new AlertDialog.Builder(context)
                .setTitle("Failed")
                .setMessage(message)
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .show();
    }

    public static String getTime(int seconds) {
        int minutes = (int) (seconds/60);
        int hours = (int) (minutes/60);

        int days = (int) (hours/24);
        int months = (int) (days/30.5);
        int years = (int) (months/12);

        if (seconds<60){
            return String.valueOf(seconds) + " seconds";
        } else if (minutes<60){
            return String.valueOf(minutes) + " minutes";
        } else if (hours<24){
            return String.valueOf(hours) + " hours";
        } else if (days<31){
            return String.valueOf(days) + " days";
        } else if (months<12){
            return String.valueOf(months) + " months";
        } else {
            return String.valueOf(years) + " years";
        }


    }


}

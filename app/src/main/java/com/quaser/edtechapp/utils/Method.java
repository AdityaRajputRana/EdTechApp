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
}

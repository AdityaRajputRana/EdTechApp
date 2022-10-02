package com.quaser.edtechapp.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.dialog.MaterialDialogs;
import com.quaser.edtechapp.LessonActivity;
import com.quaser.edtechapp.R;

import org.w3c.dom.Text;

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

    public static void showSuccessAlert(Context context, String message){
        new AlertDialog.Builder(context)
                .setTitle("Success")
                .setMessage(message)
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .show();
    }

    public static void showDialog(Activity context,
                                  String title, String message, int lottieIcons, boolean loop){
        MaterialAlertDialogBuilder alert = new MaterialAlertDialogBuilder(context, R.style.MaterialAlertDialog_rounded);
        LayoutInflater inflater = context.getLayoutInflater();
        View view = inflater.inflate(R.layout.alert_normal, null);
        alert.setView(view);
        alert.setCancelable(false);
        TextView titleTxt = view.findViewById(R.id.title);
        titleTxt.setText(title);
        TextView messageTxt = view.findViewById(R.id.message);
        messageTxt.setText(message);
        alert.setCancelable(true);

        LottieAnimationView animationView = view.findViewById(R.id.animationView);
        animationView.setAnimation(lottieIcons);
        animationView.loop(true);
        androidx.appcompat.app.AlertDialog dialogs = alert.create();
        dialogs.show();

        view.findViewById(R.id.actionBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogs.dismiss();
            }
        });
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

    public static String getCountDownTime(long milisecs){
        int seconds = (int) milisecs/1000;
        int mins = 0;
        if (seconds>60){
            mins = seconds/60;
        }
        String time = "";
        if (mins<10){
            time = "0";
        }
        time = time+String.valueOf(mins);
        time = time + ":";
        if (seconds<10){
            time = time+"0";
        }
        seconds = seconds-mins*60;
        time = time+seconds;
        return time;
    }


}

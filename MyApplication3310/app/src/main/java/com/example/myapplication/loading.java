package com.example.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

public class loading {

    static Activity activity;
    static AlertDialog dialog;
    loading(Activity myactivity){
        activity =myactivity;
    }
    static void StartDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(activity);
        LayoutInflater inflater=activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.custom_dialog,null));
        builder.setCancelable(false);

        dialog=builder.create();
        dialog.show();

    }
    static void dismisslog(){
        dialog.dismiss();
    }
}

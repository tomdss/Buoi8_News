package com.t3h.buoi8_news.utils;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;

public class DialogUtils {
    private static Dialog progressDialog;


    public static void show(Context context){
        dissmiss();
        progressDialog = new ProgressDialog
                .Builder(context)
                .setMessage("Loading...")
                .setCancelable(false)
                .create();
        progressDialog.show();

    }

    public static void showCancel(Context context){
        dissmiss();
        progressDialog = new ProgressDialog
                .Builder(context)
                .setMessage("Loading...")
                .setCancelable(true)
                .create();
        progressDialog.show();

    }


    public static void  dissmiss(){
        if(progressDialog!=null&&progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }




}

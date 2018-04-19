package com.hfhuaizhi.sleep.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;


/**
 * Created by Administrator on 2018\4\19 0019.
 */

public class DialogUtil {
    private static AlertDialog mDialog;
    public static void showConfirmDialog(Context context, String title, String msg, final DialogResult dialogResult){

        final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(msg);

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(dialogResult!=null){
                    dialogResult.negative();
                }else{
                    if(mDialog !=null){
                        mDialog.dismiss();
                    }
                }

            }
        });

        mDialog = builder.show();

    }
    public static void showChooseDialog(Context context, String title, String msg, final DialogResult dialogResult){

        final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(msg);

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(dialogResult!=null){
                    dialogResult.negative();
                }else{
                    if(mDialog !=null){
                        mDialog.dismiss();
                    }
                }

            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialogResult.positive();
                mDialog.dismiss();
            }
        });
        mDialog = builder.show();

    }


    public interface DialogResult{
        public void positive();
        public void negative();
    }
}

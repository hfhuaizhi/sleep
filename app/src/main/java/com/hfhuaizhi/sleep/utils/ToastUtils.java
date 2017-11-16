package com.hfhuaizhi.sleep.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by fullcircle on 2016/12/31.
 */

public class ToastUtils {

    private static Toast toast;
    public static void showToast(Context context,String msg){
        if(toast==null){
            toast = Toast.makeText(context.getApplicationContext(),msg,Toast.LENGTH_SHORT);
        }else{
            toast.setText(msg);
        }
        toast.show();
    }
}

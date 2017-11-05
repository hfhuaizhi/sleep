package com.hfhuaizhi.sleep.utils;

import android.content.Context;
import android.graphics.Typeface;  
import android.widget.TextView;  
  
public final class FontUtils {  
  
    /** 
     * 设置字体 
     *  
     * @param context 
     * @param textViews 
     */  
    public static void setTypeFace(Context context, TextView... textViews) {  
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/roli.ttc");  
        for (TextView textView : textViews) {  
            textView.setTypeface(typeface);  
        }  
    }  
  
}
package com.hfhuaizhi.sleep.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.hfhuaizhi.sleep.activity.GuideActivity;
import com.hfhuaizhi.sleep.utils.ToastUtils;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        //当系统到我们设定的时间点的时候会发送广播，执行这里
        ToastUtils.showToast(context,"闹钟执行了");
        context.startActivity(new Intent(context, GuideActivity.class));
    }
}
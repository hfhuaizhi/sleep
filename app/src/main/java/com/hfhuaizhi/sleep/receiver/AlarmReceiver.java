package com.hfhuaizhi.sleep.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.hfhuaizhi.sleep.R;
import com.hfhuaizhi.sleep.activity.GuideActivity;
import com.hfhuaizhi.sleep.activity.MainActivity;
import com.hfhuaizhi.sleep.utils.ToastUtils;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        //当系统到我们设定的时间点的时候会发送广播，执行这里
        String title = intent.getStringExtra("title");
        showNotification(context,title);
    }
    private void showNotification(Context context1,String title) {

        //
        Context context = context1;
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("提醒")
                .setContentText(title);

//设置点击通知之后的响应，启动SettingActivity类
        Intent resultIntent = new Intent(context,MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,resultIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        Notification notification = builder.build();
        notification.flags = Notification.FLAG_ONGOING_EVENT;

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1,notification);


    }
}
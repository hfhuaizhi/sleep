package com.hfhuaizhi.sleep.application;

import android.app.ActivityManager;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.SoundPool;
import android.support.constraint.solver.Cache;

import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.hfhuaizhi.sleep.R;
import com.hfhuaizhi.sleep.activity.MainActivity;
import com.hfhuaizhi.sleep.db.DBUtils;
import com.hfhuaizhi.sleep.event.ContactChangeEvent;
import com.hfhuaizhi.sleep.event.ExitEvent;
import com.hfhuaizhi.sleep.utils.ThreadUtils;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMContactListener;
import com.hyphenate.EMError;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.NetUtils;


import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.OkHttpClient;


/**
 * Created by fullcircle on 2016/12/31.
 */

public class MyApplication extends Application {

    private int foregoundSound;
    private int backgoundSound;
    private SoundPool soundPool;

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化环信
        initEaseMobe();
    //初始化bmob

        try {
            final List<String> allcontacts = EMClient.getInstance().contactManager().getAllContactsFromServer();
        } catch (HyphenateException e) {
            e.printStackTrace();
        }
        //初始化数据库
        DBUtils.initDBUtils(this);

        initGetMessageListener();
        //初始化声音池
        initSoundPool();
        //注册一个监听连接状态的listener
        EMClient.getInstance().addConnectionListener(new MyConnectionListener());

    }

    private void initSoundPool() {
        //soundpool 构造 第一个参数 这个池子中管理几个音频
        //第二个参数 音频的类型 一般传入AudioManager.STREAM_MUSIC
        //第三个参数 声音的采样频率 但是 没有用默认值使用0
        soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC,0);
        foregoundSound = soundPool.load(getApplicationContext(), R.raw.duan, 1);
        backgoundSound = soundPool.load(getApplicationContext(), R.raw.yulu, 1);
    }

    private void initGetMessageListener() {
        EMClient.getInstance().chatManager().addMessageListener(new EMMessageListener() {
            @Override
            public void onMessageReceived(List<EMMessage> list) {
                //收到消息
                EventBus.getDefault().post(list);
                //获取应用处于前台还是后台的状态
                if(isInBackgoundState()){
                    soundPool.play(backgoundSound,1,1,0,0,1);
                    //发送通知
                  //  sendNotification(list.get(0));
                }else{
                    soundPool.play(foregoundSound,1,1,0,0,1);
                }
            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> list) {
                //收到透传消息
            }

            @Override
            public void onMessageRead(List<EMMessage> list) {
                    //处理消息已读回执
            }

            @Override
            public void onMessageDelivered(List<EMMessage> list) {
                    //处理消息发送回执
            }

            @Override
            public void onMessageRecalled(List<EMMessage> list) {

            }

            @Override
            public void onMessageChanged(EMMessage emMessage, Object o) {
                //消息变化

            }
        });
    }

//    private void sendNotification(EMMessage message) {
//        Notification.Builder builder = new Notification.Builder(getApplicationContext());
//        builder.setAutoCancel(true);
//        builder.setSmallIcon(R.mipmap.message);
//        builder.setContentTitle("您有一条新消息需要处理");
//       EMTextMessageBody body = (EMTextMessageBody) message.getBody();
//        builder.setContentText(body.getMessage());
//        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.avatar3));
//        builder.setContentInfo("来自"+message.getUserName());
//        Intent mainActivityIntent = new Intent(getApplicationContext(),MainActivity.class);
//        Intent chatActivityIntent = new Intent(getApplicationContext(),ChatActivity.class);
//        chatActivityIntent.putExtra("contact",message.getUserName());
//        Intent[] intents = new Intent[]{mainActivityIntent,chatActivityIntent};
//        PendingIntent pendingItent = PendingIntent.getActivities(getApplicationContext(),1,intents,PendingIntent.FLAG_UPDATE_CURRENT);
//        builder.setContentIntent(pendingItent);
//        Notification notification = builder.build();
//        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        manager.notify(1,notification);
//    }

    /**
     * 判断当前的应用是否处于后台状态
     * @param  //返回true说明应用处于后台
     *  返回false 说明应用处于前台
     */
    private boolean isInBackgoundState() {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        //通过ActivityManager 获取正在运行的 任务信息
        List<ActivityManager.RunningTaskInfo> runningTasks = manager.getRunningTasks(50);
        //获取第一个activity栈的信息
        ActivityManager.RunningTaskInfo runningTaskInfo = runningTasks.get(0);
        //获取栈中的栈顶activity  根据activity的包名判断 是否是当前应用的包名
        ComponentName componentName = runningTaskInfo.topActivity;
        if(componentName.getPackageName().equals(getPackageName())){
            //处于前台状态
            return false;
        }else{
            //处于后台状态
            return  true;
        }
    }

    private void initEaseMobe() {
        EMOptions options = new EMOptions();
// 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(false);

        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);
// 如果APP启用了远程的service，此application:onCreate会被调用2次
// 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
// 默认的APP会在以包名为默认的process name下运行，如果查到的process name不是APP的process name就立即返回

        if (processAppName == null ||!processAppName.equalsIgnoreCase(this.getPackageName())) {
            // Log.e(TAG, "enter the service process!");

            // 则此application::onCreate 是被service 调用的，直接返回
            return;
        }

//初始化
        EMClient.getInstance().init(this, options);
//在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        EMClient.getInstance().setDebugMode(false);

        //添加好友监听
        EMClient.getInstance().contactManager().setContactListener(new EMContactListener() {

            @Override
            public void onContactInvited(String username, String reason) {
                //收到好友邀请
                try {
                    //acceptInvitation 接收邀请
                    EMClient.getInstance().contactManager().acceptInvitation(username);
                    //拒绝邀请
                   // EMClient.getInstance().contactManager().declineInvitation(username);
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFriendRequestAccepted(String s) {
                //如果别人接受了邀请会走这个回调

            }

            @Override
            public void onFriendRequestDeclined(String s) {
                //如果拒绝了会走这个回调

            }

            @Override
            public void onContactDeleted(String username) {
                //被删除时回调此方法 通过evnetbus发布消息
                EventBus.getDefault().post(new ContactChangeEvent(username,false));
            }


            @Override
            public void onContactAdded(String username) {
                //增加了联系人时回调此方法
                EventBus.getDefault().post(new ContactChangeEvent(username,true));
            }
        });
    }

    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                // Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        return processName;
    }

    //实现ConnectionListener接口
    private class MyConnectionListener implements EMConnectionListener {
        @Override
        public void onConnected() {
        }
        @Override
        public void onDisconnected(final int error) {
            ThreadUtils.runOnMainThread(new Runnable() {

                @Override
                public void run() {
                    if(error == EMError.USER_REMOVED){
                        // 显示帐号已经被移除
                        EventBus.getDefault().post(new ExitEvent(EMError.USER_REMOVED));
                    }else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                        // 显示帐号在其他设备登录
                        EventBus.getDefault().post(new ExitEvent(EMError.USER_LOGIN_ANOTHER_DEVICE));
                    } else {
                        if (NetUtils.hasNetwork(getApplicationContext())){
                            //连接不到聊天服务器
                        }
                        else{
                            //当前网络不可用，请检查网络设置
                        }
                    }
                }
            });
        }
    }
}

package com.hfhuaizhi.sleep.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.hfhuaizhi.sleep.R;
import com.hfhuaizhi.sleep.db.domain.Todo;
import com.hfhuaizhi.sleep.receiver.AlarmReceiver;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TodoDetailActivity extends AppCompatActivity {

    private Context mContext;
    private LinearLayout mLl_time;
    public String starttime;
    private TextView mTv_detail_time;
    private AlarmManager mAlarmManager;
    private LinearLayout mLl_detail_state;
    private TextView mTv_detail_state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_detail);
        initView();
        initData();
    }

    private void initData() {
        mAlarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        mContext = TodoDetailActivity.this;
        Intent intent = getIntent();
        Todo todo = (Todo) intent.getSerializableExtra("tmp");
        mLl_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });
        mLl_detail_state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopUp(v);
            }
        });

    }

    private void initView() {
        mLl_time = findViewById(R.id.ll_detial_time);
        mTv_detail_time = findViewById(R.id.tv_detail_time);
        mLl_detail_state = findViewById(R.id.ll_detail_state);
        mTv_detail_state = findViewById(R.id.tv_detail_state);
    }
    private void showTimePickerDialog() {
        final Calendar mCalendar = Calendar.getInstance();
        TimePickerDialog dialog = new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                mCalendar.set(Calendar.HOUR, i);
                mCalendar.set(Calendar.MINUTE, i1);

               SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                starttime = mCalendar.getTime()+"";
                mTv_detail_time.setText("在 "+format.format(mCalendar.getTime())+" 提醒我");
                PendingIntent pi= PendingIntent.getBroadcast(mContext, 0, new Intent(mContext,AlarmReceiver.class), 0);
                mAlarmManager.set(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(), pi);
                //Toast.makeText(getApplicationContext(), "" + format.format(mCalendar.getTime()), Toast.LENGTH_SHORT).show();

            }
        }, mCalendar.get(Calendar.HOUR), mCalendar.get(Calendar.MINUTE), true);
        dialog.show();

    }
    private void showPopUp(View v) {
        View contentView = View.inflate(mContext, R.layout.dialog_popwindow, null);

        PopupWindow popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int popupWidth = contentView.getMeasuredWidth();
        int popupHeight =  contentView.getMeasuredHeight();
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, (location[0]+v.getWidth()/2)-popupWidth/2,
                location[1]-popupHeight);
    }

}

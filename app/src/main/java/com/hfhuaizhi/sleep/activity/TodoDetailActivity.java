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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.hfhuaizhi.sleep.R;
import com.hfhuaizhi.sleep.db.domain.Todo;
import com.hfhuaizhi.sleep.fragment.SleepFragment;
import com.hfhuaizhi.sleep.receiver.AlarmReceiver;
import com.hfhuaizhi.sleep.utils.ToastUtils;

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
    private Todo mTodo;
    private TextView mTv_detail_title;
    private EditText mEt_detail_msg;
    private ImageView mIv_back;
    public String mMsg;
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
        mTodo = (Todo) intent.getSerializableExtra("tmp");
        initContent();
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
        mIv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAndBack();
            }
        });
    }

    private void saveAndBack() {
        mTodo.msg = mEt_detail_msg.getText().toString().trim();
        if(mTodo.msg.isEmpty()){
            mTodo.msg = null;
        }
        Intent intent = new Intent();
        Bundle mBundle = new Bundle();
        mBundle.putSerializable("tmp", mTodo);
        intent.putExtras(mBundle);
        intent.setClass(TodoDetailActivity.this, SleepFragment.class);
        TodoDetailActivity.this.setResult(1, intent);
        finish();
    }

    private void initContent() {
        mMsg = mEt_detail_msg.getText().toString().trim();
        mTv_detail_title.setText(mTodo.getTitle());
        switch (mTodo.state){
            case 1:
                mTv_detail_state.setText("一般般");
                break;
            case 2:
                mTv_detail_state.setText("略紧急");
                break;
            case 3:
                mTv_detail_state.setText("很紧急");
                break;
            case 4:
                mTv_detail_state.setText("非常紧急");
                break;
        }
        if(mTodo.msg!=null){
            mEt_detail_msg.setText(mTodo.msg);
        }else{
            mEt_detail_msg.setText(mMsg);
        }

        if(mTodo.getStartTime()!=null){
            SimpleDateFormat format = new SimpleDateFormat("HH:mm");
            mTv_detail_time.setText("在 "+format.format(Long.parseLong(mTodo.getStartTime()))+" 提醒我");
        }

    }

    private void initView() {
        mLl_time = findViewById(R.id.ll_detial_time);
        mTv_detail_time = findViewById(R.id.tv_detail_time);
        mLl_detail_state = findViewById(R.id.ll_detail_state);
        mTv_detail_state = findViewById(R.id.tv_detail_state);
        mTv_detail_title = findViewById(R.id.tv_detil_title);
        mEt_detail_msg = findViewById(R.id.et_detail_msg);
        mIv_back = findViewById(R.id.iv_back);
    }
    private void showTimePickerDialog() {
        final Calendar mCalendar = Calendar.getInstance();
        TimePickerDialog dialog = new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                mCalendar.set(Calendar.HOUR, i);
                mCalendar.set(Calendar.MINUTE, i1);

               SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                starttime = mCalendar.getTime().getTime()+"";
                mTv_detail_time.setText("在 "+format.format(mCalendar.getTime())+" 提醒我");
                mTodo.startTime = starttime;
                PendingIntent pi= PendingIntent.getBroadcast(mContext, mTodo.id, new Intent(mContext,AlarmReceiver.class).putExtra("title",mTodo.getTitle()), 0);
                mAlarmManager.set(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(), pi);
                //Toast.makeText(getApplicationContext(), "" + format.format(mCalendar.getTime()), Toast.LENGTH_SHORT).show();

            }
        }, mCalendar.get(Calendar.HOUR), mCalendar.get(Calendar.MINUTE), true);
        dialog.show();

    }
    private void stopRemind(){

        PendingIntent pi= PendingIntent.getBroadcast(mContext, mTodo.id, new Intent(mContext,AlarmReceiver.class).putExtra("title",mTodo.getTitle()), 0);
        //取消警报
        mAlarmManager.cancel(pi);
        Toast.makeText(this, "关闭了提醒", Toast.LENGTH_SHORT).show();

    }
    private void showPopUp(View v) {
        View contentView = View.inflate(mContext, R.layout.dialog_popwindow, null);

        final PopupWindow popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int popupWidth = contentView.getMeasuredWidth();
        int popupHeight =  contentView.getMeasuredHeight();
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, (location[0]/*+v.getWidth()/2*/)-popupWidth/2,
                location[1]-popupHeight);

        LinearLayout ll_state1 = contentView.findViewById(R.id.ll_pop_1);
        LinearLayout ll_state2 = contentView.findViewById(R.id.ll_pop_2);
        LinearLayout ll_state3 = contentView.findViewById(R.id.ll_pop_3);
        LinearLayout ll_state4 = contentView.findViewById(R.id.ll_pop_4);
        ll_state1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showToast(mContext,"点第一个了");
                mTodo.state = 1;
                initContent();
                popupWindow.dismiss();
            }
        });
        ll_state2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTodo.state = 2;
                initContent();
                popupWindow.dismiss();
            }
        });
        ll_state3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTodo.state = 3;
                initContent();
                popupWindow.dismiss();

            }
        });
        ll_state4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTodo.state = 4;
                initContent();
                popupWindow.dismiss();
            }
        });

    }

    @Override
    public void onBackPressed() {
        saveAndBack();
    }
}

package com.hfhuaizhi.sleep.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hfhuaizhi.sleep.R;
import com.hfhuaizhi.sleep.db.domain.Note;
import com.hfhuaizhi.sleep.fragment.NoteFragment;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddNoteActivity extends AppCompatActivity {

    private TextView mTv_date;
    private TextView mTv_time;
    private EditText mEt_content;
    private Context mContext;
    private Note mNote;
    private ImageView mIv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        initView();
        initData();
    }

    private void initData() {
        mContext = AddNoteActivity.this;
        initContent();
        mIv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAndBack();
            }
        });
    }

    private void saveAndBack() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        mNote.date = sdf.format(new Date());
        mNote.content = mEt_content.getText().toString();
        if(mNote.content.length()>10){
            mNote.title = mNote.content.substring(0,10);
        }else{
            mNote.title = mNote.content;
        }
        Intent intent = new Intent();
        Bundle mBundle = new Bundle();
        mBundle.putSerializable("tmp", mNote);
        intent.putExtras(mBundle);
        intent.setClass(AddNoteActivity.this, NoteFragment.class);
        AddNoteActivity.this.setResult(1, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        saveAndBack();
    }

    private void initContent() {
        mNote = null;
        Intent intent = getIntent();
        mNote = (Note) intent.getSerializableExtra("tmp");
        if(mNote==null){
            mNote = new Note();
        }else {
            mEt_content.setText(mNote.content);
        }
        Date date = new Date();
        SimpleDateFormat sdf1 = new SimpleDateFormat("MM月dd日");
        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
        mTv_date.setText(sdf1.format(date));
        mTv_time.setText(sdf2.format(date));

    }

    private void initView() {
        mIv_back = findViewById(R.id.iv_back);
        mTv_date = findViewById(R.id.tv_add_date);
        mTv_time = findViewById(R.id.tv_add_time);
        mEt_content = findViewById(R.id.et_add_content);
    }
}

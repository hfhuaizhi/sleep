package com.hfhuaizhi.sleep.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.hfhuaizhi.sleep.R;
import com.hfhuaizhi.sleep.db.domain.Todo;

public class TodoDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_detail);
        initView();
        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        Todo todo = (Todo) intent.getSerializableExtra("tmp");

    }

    private void initView() {
    }
}

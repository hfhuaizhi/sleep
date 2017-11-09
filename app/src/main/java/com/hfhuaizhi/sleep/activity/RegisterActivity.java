package com.hfhuaizhi.sleep.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.hfhuaizhi.sleep.R;
import com.hfhuaizhi.sleep.utils.MyHttpUtil;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.et_reg_username)
    EditText mEtRegUsername;
    @BindView(R.id.et_reg_password)
    EditText mEtRegPassword;
    @BindView(R.id.bt_reg_register)
    Button mBtRegRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.bt_reg_register)
    public void onClick() {
        String username = mEtRegUsername.getText().toString().trim();
        String password = mEtRegPassword.getText().toString().trim();
        MyHttpUtil.doGet("http://www.baidu.com", new MyHttpUtil.MyHttpResult() {
            @Override
            public void onFailure(IOException e) {

            }

            @Override
            public void onResponse(String response) {

                    System.out.println("result"+response);
                  //  Log.i("result",response.body().string().toString());

            }
        });

    }
}

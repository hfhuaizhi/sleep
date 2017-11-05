package com.hfhuaizhi.sleep.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hfhuaizhi.sleep.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.et_login_username)
    EditText mEtLoginUsername;
    @BindView(R.id.et_login_password)
    EditText mEtLoginPassword;
    @BindView(R.id.bt_login_login)
    Button mBtLoginLogin;
    @BindView(R.id.tv_login_forget)
    TextView mTvLoginForget;
    @BindView(R.id.tv_login_register)
    TextView mTvLoginRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.bt_login_login, R.id.tv_login_forget, R.id.tv_login_register})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_login_login:
                enterHome();
                break;
            case R.id.tv_login_forget:
                break;
            case R.id.tv_login_register:
                break;
        }
    }

    private void enterHome() {
        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}

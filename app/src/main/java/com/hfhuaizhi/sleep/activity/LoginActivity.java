package com.hfhuaizhi.sleep.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hfhuaizhi.sleep.R;
import com.hfhuaizhi.sleep.callback.MyEmCalBack;
import com.hfhuaizhi.sleep.utils.ToastUtils;
import com.hyphenate.chat.EMClient;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {

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
    private String mUsername;
    private String mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
//            window.setNavigationBarColor(Color.TRANSPARENT);
        }
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
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
                break;
        }
    }

    private void enterHome() {
        showProgressDialog("正在登录...");
        mUsername = mEtLoginUsername.getText().toString().trim();
        mPassword = mEtLoginPassword.getText().toString().trim();
        if(mUsername==null||mPassword==null){
            ToastUtils.showToast(LoginActivity.this,"用户名和密码不能为空");
            return;
        }
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)== PermissionChecker.PERMISSION_GRANTED){
            //如果有权限的话就执行登录
            login();
        }else{
            //如果没有权限 就动态申请
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},0);
        }



    }
    public void login(){
        EMClient.getInstance().login(mUsername, mPassword, new MyEmCalBack() {
            @Override
            public void success() {
                cancelProgressDialog();
                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void error(int i, String s) {
                cancelProgressDialog();
                showConfirmDialog("登录结果","登录失败",null);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //requestCode 区分不同的权限申请操作 注意requestCode不能<0
        //permissions 动态申请的权限数组
        //grantResults int数组 用来封装每一个权限授权的结果  PermissionChecker.PERMISSION_GRANTED 授权了
//        PermissionChecker.PERMISSION_DENIED; 拒绝了
        if(grantResults[0]==PermissionChecker.PERMISSION_GRANTED){
            //说明给权限了
            login();
        }else{
            //说明没给权限
            cancelProgressDialog();
            showToast("没授权sd卡权限 数据库保存会失败 影响使用");
        }
    }
}

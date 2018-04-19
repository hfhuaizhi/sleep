package com.hfhuaizhi.sleep.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hfhuaizhi.sleep.R;
import com.hfhuaizhi.sleep.net.NetConfig;
import com.hfhuaizhi.sleep.net.json.ResponseResult;
import com.hfhuaizhi.sleep.utils.MyHttpUtil;
import com.hfhuaizhi.sleep.utils.ThreadUtils;
import com.hfhuaizhi.sleep.utils.ToastUtils;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Response;

public class RegisterActivity extends BaseActivity {

    @BindView(R.id.et_reg_username)
    EditText mEtRegUsername;
    @BindView(R.id.et_reg_password)
    EditText mEtRegPassword;
    @BindView(R.id.bt_reg_register)
    Button mBtRegRegister;
    private String mUsername;
    private String mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
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

    @OnClick(R.id.bt_reg_register)
    public void onClick() {
        mUsername = mEtRegUsername.getText().toString().trim();
        mPassword = mEtRegPassword.getText().toString().trim();
        showProgressDialog("正在注册...");
        MyHttpUtil.doGet(NetConfig.SERVER_REGISTER+"?username="+ mUsername +"&password="+ mPassword, new MyHttpUtil.MyHttpResult() {
            @Override
            public void onFailure(IOException e) {
                cancelProgressDialog();
                showConfirmDialog("注册结果","连接服务器失败",null);
            }

            @Override
            public void onResponse(String response) {
                Log.i("response",response+"!!!!!!!!!!!!!!!!!!!!!!!");
                //System.out.print("response is ::"+response);
                Gson gson = new Gson();
                ResponseResult rr = gson.fromJson(response, ResponseResult.class);
                Log.i("rr",rr.toString());
                cancelProgressDialog();
                if(rr!=null&&rr.state.equals(ResponseResult.SUCCESS)){
                    EMregister(mUsername,mPassword);
                }else{
                    cancelProgressDialog();
                    showConfirmDialog("注册结果",rr.msg,null);
                }

            }
        });

    }
    private void EMregister(final String username, final String password) {
        ThreadUtils.runOnNonUIThread(new Runnable() {
            @Override
            public void run() {
                try {
                    EMClient.getInstance().createAccount(username, password);
                    //说明注册成功
                    //通知界面跳转
                    ThreadUtils.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            cancelProgressDialog();
                            showConfirmDialog("注册结果", "注册成功", new DialogResult() {
                                @Override
                                public void positive() {
                                  //  startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                                    finish();
                                   // cancelProgressDialog();
                                }

                                @Override
                                public void negative() {

                                }
                            });
                        }
                    });
                } catch (HyphenateException e) {
                    e.printStackTrace();
                  //  deleteUser(username);
                    ThreadUtils.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            cancelProgressDialog();
                            showConfirmDialog("注册结果", "EM注册异常,该手机号废了", new DialogResult() {
                                @Override
                                public void positive() {

                                    cancelProgressDialog();
                                }

                                @Override
                                public void negative() {

                                }
                            });
                        }
                    });
                }
            }
        });



        
    }


}

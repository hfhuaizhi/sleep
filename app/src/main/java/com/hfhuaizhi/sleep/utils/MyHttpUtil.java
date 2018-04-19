package com.hfhuaizhi.sleep.utils;

import android.util.Log;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017\11\9 0009.
 */

public class MyHttpUtil {
    private static OkHttpClient mOkHttpClient;
    public static void doGet(String url, final MyHttpResult httpResult){
        if(mOkHttpClient==null){
            mOkHttpClient=new OkHttpClient();
        }
        Request.Builder requestBuilder = new Request.Builder().url(url);//在此处添加url
        //可以省略，默认是GET请求
        requestBuilder.method("GET",null);
        Request request = requestBuilder.build();
        Call mcall= mOkHttpClient.newCall(request);
        mcall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                ThreadUtils.runOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        if(httpResult!=null){
                            httpResult.onFailure(e);
                        }
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                final String result = response.body().string();
                ThreadUtils.runOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        if(httpResult!=null){
                                httpResult.onResponse(result);
                        }
                    }
                });
            }
        });
    }

    public static void doPost(String url,FormBody formBody, final MyHttpResult httpResult){
        if(mOkHttpClient==null){
            mOkHttpClient=new OkHttpClient();
        }


        final Request request = new Request.Builder()
                .url(url)//请求的url
                .post(formBody)
                .build();


        //创建/Call
        Call call = mOkHttpClient.newCall(request);
        //加入队列 异步操作
        call.enqueue(new Callback() {
            //请求错误回调方法
            @Override
            public void onFailure(Call call, final IOException e) {
                ThreadUtils.runOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        if(httpResult!=null){
                            httpResult.onFailure(e);
                        }
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.code()==200) {
                    final String result = response.body().string();
                    ThreadUtils.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            if(httpResult!=null){

                                httpResult.onResponse(result);
                            }
                        }
                    });
                }
            }
        });

    }
    public interface MyHttpResult{
        public void onFailure(IOException e);
        public void onResponse(String response);
    }
}

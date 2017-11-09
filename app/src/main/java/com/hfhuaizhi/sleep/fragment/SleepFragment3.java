package com.hfhuaizhi.sleep.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hfhuaizhi.sleep.R;


/**
 * Created by fullcircle on 2017/1/3.
 */

public class SleepFragment3 extends BaseFragment{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i("fragment","creat!!!!!!!!!!");
        return inflater.inflate(R.layout.fragment_sleep3,null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
    }



}

package com.hfhuaizhi.sleep.activity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.hfhuaizhi.sleep.R;
import com.hfhuaizhi.sleep.db.domain.Todo;
import com.hfhuaizhi.sleep.fragment.BaseFragment;
import com.hfhuaizhi.sleep.utils.FragmentFactory;
import com.hfhuaizhi.sleep.utils.ToastUtils;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.internal.http.HttpMethod;

public class MainActivity extends AppCompatActivity {
    public int fragmentPosition = 0;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tb_toolbar)
    Toolbar mTbToolbar;
    @BindView(R.id.fl_main)
    FrameLayout mFlMain;
    @BindView(R.id.bnb_main)
    BottomNavigationBar mBnbMain;
    private String[] titles = new String[]{"sleep","sleep2","sleep3"};
    private FloatingActionButton mFab_add;
    private Context mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initToolBar();
        initView();
        initdata();
        initBottomNavigationBar();
        initFirstFragment();
    }

    private void initdata() {
        mActivity = MainActivity.this;

    }






    private void initView() {

    }

    private void initFirstFragment() {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        FragmentTransaction tranction = getSupportFragmentManager().beginTransaction();
        if(fragments!=null&&fragments.size()>0){
            for(int i=0;i<fragments.size();i++){
                tranction.remove(fragments.get(i));
            }
            tranction.commit();
        }
        BaseFragment sleepFragment = FragmentFactory.getFragment(0);
        tranction = getSupportFragmentManager().beginTransaction();
        tranction.add(R.id.fl_main,sleepFragment,"0").commit();

    }

    private void initBottomNavigationBar() {
        BottomNavigationItem sleepItem = new BottomNavigationItem(R.mipmap.sleep_main,"sleep");
        mBnbMain.addItem(sleepItem);
        BottomNavigationItem sleepItem2 = new BottomNavigationItem(R.mipmap.sleep,"sleep");
        mBnbMain.addItem(sleepItem2);
        BottomNavigationItem sleepItem3 = new BottomNavigationItem(R.mipmap.sleep,"sleep");
        mBnbMain.addItem(sleepItem3);
        mBnbMain.setActiveColor(R.color.active);
        mBnbMain.setInActiveColor(R.color.inactive);
        mBnbMain.setBarBackgroundColor(R.color.colorPrimaryDark);
        mBnbMain.setAutoHideEnabled(true);

      //  mBnbMain.setFirstSelectedPosition(0);
        mBnbMain.initialise();//初始化



        mBnbMain.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                fragmentPosition = position;//从0开始
                Log.i("position_select",position+"");
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                BaseFragment fragment = FragmentFactory.getFragment(position);
                if(fragment.isAdded()){
                    Log.i("position_select",position+"show");
                    transaction.show(fragment).commit();
                }else{
                    Log.i("position_select",position+"add");
                    transaction.add(R.id.fl_main,fragment,position+"").commit();
                }
                mTvTitle.setText(titles[position]);
            }

            @Override
            public void onTabUnselected(int position) {
                Log.i("position_unselect",position+"");
                getSupportFragmentManager().beginTransaction().hide(FragmentFactory.getFragment(position)).commit();
            }

            @Override
            public void onTabReselected(int position) {

            }
        });

    }

    private void initToolBar() {
        mTbToolbar.setTitle("");
        setSupportActionBar(mTbToolbar);

        mTbToolbar.setNavigationIcon(R.mipmap.menu);


    }
}

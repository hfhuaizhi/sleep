package com.hfhuaizhi.sleep.activity;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
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
import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenuItem;

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
    private ResideMenu mResideMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initToolBar();
        initView();
        initdata();
        initSlider();
        initBottomNavigationBar();
        initFirstFragment();
    }

    private void initSlider() {
        mResideMenu = new ResideMenu(this);
        mResideMenu.setBackground(R.drawable.menu_background);

        mResideMenu.attachToActivity(this);
        mResideMenu.setScaleValue(0.6f);
        // create menu items;


        ResideMenuItem aboutme = new ResideMenuItem(this, R.mipmap.ico_user, "关于我");
        ResideMenuItem search = new ResideMenuItem(this, R.mipmap.ico_search, "搜索");
        ResideMenuItem history = new ResideMenuItem(this, R.mipmap.ico_history, "历史记录");
        ResideMenuItem setting = new ResideMenuItem(this, R.mipmap.ico_setting, "设置");
        ResideMenuItem about = new ResideMenuItem(this, R.mipmap.ico_app, "关于app");
        mResideMenu.addMenuItem(about, ResideMenu.DIRECTION_RIGHT);
        mResideMenu.addMenuItem(aboutme, ResideMenu.DIRECTION_LEFT);
        mResideMenu.addMenuItem(search, ResideMenu.DIRECTION_LEFT);
        mResideMenu.addMenuItem(history, ResideMenu.DIRECTION_LEFT);
        mResideMenu.addMenuItem(setting, ResideMenu.DIRECTION_LEFT);


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
        BottomNavigationItem sleepItem = new BottomNavigationItem(R.mipmap.home,"home");
        mBnbMain.addItem(sleepItem);
        BottomNavigationItem sleepItem2 = new BottomNavigationItem(R.mipmap.note,"note");
        mBnbMain.addItem(sleepItem2);
        BottomNavigationItem sleepItem3 = new BottomNavigationItem(R.mipmap.msg,"msg");
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
        mTbToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mResideMenu.isOpened()){
                    mResideMenu.closeMenu();
                }else{
                    mResideMenu.openMenu(ResideMenu.DIRECTION_LEFT);

                }
            }
        });

    }
}

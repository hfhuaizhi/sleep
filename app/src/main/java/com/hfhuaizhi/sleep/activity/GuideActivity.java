package com.hfhuaizhi.sleep.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.hfhuaizhi.sleep.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.bgabanner.BGABanner;

public class GuideActivity extends AppCompatActivity {

    @BindView(R.id.banner_guide_background)
    cn.bingoogolapple.bgabanner.BGABanner mBannerGuideBackground;
    @BindView(R.id.banner_guide_foreground)
    cn.bingoogolapple.bgabanner.BGABanner mBannerGuideForeground;
    @BindView(R.id.tv_guide_skip)
    TextView mTvGuideSkip;
    @BindView(R.id.btn_guide_enter)
    Button mBtnGuideEnter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        ButterKnife.bind(this);
        setListener();
        processLogic();
    }
    private void setListener() {
        /**
         * 设置进入按钮和跳过按钮控件资源 id 及其点击事件
         * 如果进入按钮和跳过按钮有一个不存在的话就传 0
         * 在 BGABanner 里已经帮开发者处理了防止重复点击事件
         * 在 BGABanner 里已经帮开发者处理了「跳过按钮」和「进入按钮」的显示与隐藏
         */

        mBannerGuideForeground.setEnterSkipViewIdAndDelegate(R.id.btn_guide_enter, R.id.tv_guide_skip, new BGABanner.GuideDelegate() {
            @Override
            public void onClickEnterOrSkip() {
                startActivity(new Intent(GuideActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

    private void processLogic() {
        // 设置数据源
        mBannerGuideBackground.setData(R.mipmap.guide_background_1, R.mipmap.guide_background_2, R.mipmap.guide_background_3);

        mBannerGuideForeground.setData(R.mipmap.guide_foreground_1, R.mipmap.guide_foreground_2, R.mipmap.guide_foreground_3);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // 如果开发者的引导页主题是透明的，需要在界面可见时给背景 Banner 设置一个白色背景，避免滑动过程中两个 Banner 都设置透明度后能看到 Launcher
        mBannerGuideBackground.setBackgroundResource(android.R.color.white);
    }
}

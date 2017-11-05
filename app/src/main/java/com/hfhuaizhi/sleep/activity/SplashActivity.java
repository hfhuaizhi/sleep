package com.hfhuaizhi.sleep.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hfhuaizhi.sleep.R;
import com.hfhuaizhi.sleep.utils.PrefUtils;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SplashActivity extends AppCompatActivity {


    @BindView(R.id.iv_guide_img)
    ImageView mIvGuideImg;
    @BindView(R.id.tv_guide_txt)
    TextView mTvGuideTxt;
    @BindView(R.id.ll_guide_icon)
    LinearLayout mLlGuideIcon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        initAnimtor();
    }

    private void initAnimtor() {
        Log.e("anim", "animStart");
        //ObjectAnimator big = ObjectAnimator.ofFloat(mIvGuideImg, "zhy", 0.3f, 1f);
        ObjectAnimator moveIn = ObjectAnimator.ofFloat(mIvGuideImg, "translationY", 0f, -30f);
        ObjectAnimator rotate = ObjectAnimator.ofFloat(mIvGuideImg, "rotationY", 0f, 396f);
        final ObjectAnimator rotate2 = ObjectAnimator.ofFloat(mIvGuideImg, "rotationY", 396f, 360f);
        ObjectAnimator fadeInOut = ObjectAnimator.ofFloat(mIvGuideImg, "alpha", 0f, 1f);
        ObjectAnimator fadeInOut2 = ObjectAnimator.ofFloat(mTvGuideTxt, "alpha", 0f, 1f);
        ObjectAnimator fadeInOut3 = ObjectAnimator.ofFloat(mTvGuideTxt, "alpha", 0f, 0f);

        AnimatorSet animSet = new AnimatorSet();
        animSet.play(moveIn).with(fadeInOut).with(fadeInOut3).before(rotate).before(fadeInOut2);
        animSet.setDuration(2580);
        rotate2.setDuration(500);
        animSet.start();

        animSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                rotate2.start();

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        rotate2.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if(!PrefUtils.getBoolean(getApplicationContext(),"FIRST_ENTER",false)){
                    startActivity(new Intent(getApplicationContext(),GuideActivity.class));
                    finish();
                }
            }
        });


    }

}

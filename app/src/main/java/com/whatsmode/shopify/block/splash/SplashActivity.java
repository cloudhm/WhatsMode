package com.whatsmode.shopify.block.splash;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.whatsmode.library.util.PreferencesUtil;
import com.whatsmode.shopify.AppNavigator;
import com.whatsmode.shopify.R;
import com.whatsmode.shopify.base.BaseActivity;
import com.whatsmode.shopify.block.account.data.AccountManager;
import com.whatsmode.shopify.common.KeyConstant;

/**
 * Created by tom on 17-11-23.
 */

public class SplashActivity extends AppCompatActivity {

    private ImageView mImageView;
    private LinearLayout mSplash;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        mImageView = (ImageView) findViewById(R.id.image_view);
        mSplash = (LinearLayout) findViewById(R.id.splash);
        alpha();
    }

    public void alpha(){
        Animation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation.setDuration(500);//设置动画持续时间为500毫秒
        alphaAnimation.setFillAfter(true);//设置动画结束后保持当前的位置（即不返回到动画开始前的位置）
        mSplash.startAnimation(alphaAnimation);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                getWindow().getDecorView().postDelayed(() -> splash(),1000);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void splash() {
        Animation alphaAnimation = new AlphaAnimation(1.0f, 0.05f);
        alphaAnimation.setDuration(500);//设置动画持续时间为500毫秒
        alphaAnimation.setFillAfter(true);//设置动画结束后保持当前的位置（即不返回到动画开始前的位置）
        mSplash.startAnimation(alphaAnimation);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                boolean isFirst = PreferencesUtil.getBoolean(SplashActivity.this, KeyConstant.KEY_IS_FIRST_START, true);
                if (isFirst) {
                    AppNavigator.jumpToGuide(SplashActivity.this);
                    finish();
                }else {
                    AppNavigator.jumpToMain(SplashActivity.this);
                    finish();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

}

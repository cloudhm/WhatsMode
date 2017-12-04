package com.whatsmode.shopify.block.splash;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.WindowManager;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        getWindow().getDecorView().postDelayed(() -> splash(),1000);
    }

    private void splash() {
        boolean isFirst = PreferencesUtil.getBoolean(this, KeyConstant.KEY_IS_FIRST_START, true);
        if (isFirst) {
            AppNavigator.jumpToGuide(this);
            finish();
        }else {
            AppNavigator.jumpToMain(this);
            finish();
        }
    }

}

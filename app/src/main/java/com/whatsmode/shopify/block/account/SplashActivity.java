package com.whatsmode.shopify.block.account;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.WindowManager;

import com.whatsmode.shopify.AppNavigator;
import com.whatsmode.shopify.R;
import com.whatsmode.shopify.base.BaseActivity;
import com.whatsmode.shopify.block.account.data.AccountManager;

/**
 * Created by tom on 17-11-23.
 */

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        getWindow().getDecorView().postDelayed(() -> splash(),1000);
    }

    private void splash() {
        String customerAccessToken = AccountManager.getCustomerAccessToken();
        if (TextUtils.isEmpty(customerAccessToken)) {
            AppNavigator.jumpToLogin(this);
        }else{
            AppNavigator.jumpToMain(this);
        }
        finish();
    }

}

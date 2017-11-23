package com.whatsmode.shopify.block.account;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.WindowManager;

import com.whatsmode.shopify.AppNavigator;
import com.whatsmode.shopify.R;
import com.whatsmode.shopify.block.account.data.AccountManager;

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
        String customerAccessToken = AccountManager.getCustomerAccessToken();
        if (TextUtils.isEmpty(customerAccessToken)) {
            AppNavigator.jumpToLogin(this);
        }else{
            AppNavigator.jumpToMain(this);
        }
        finish();
    }
}

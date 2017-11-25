package com.whatsmode.shopify.block.account;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.view.WindowManager;

import com.whatsmode.shopify.AppNavigator;
import com.whatsmode.shopify.R;
import com.whatsmode.shopify.block.account.data.AccountManager;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ThreadFactory;

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

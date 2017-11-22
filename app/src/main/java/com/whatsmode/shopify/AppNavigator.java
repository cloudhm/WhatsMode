package com.whatsmode.shopify;


import android.content.Context;

import com.whatsmode.shopify.block.WebActivity;
import com.whatsmode.shopify.block.account.LoginActivity;
import com.whatsmode.shopify.block.main.MainActivity;

/**
 *  页面跳转控制中心
 */
public class AppNavigator {

    public static void jumpToWebActivity(Context context, String url){
        context.startActivity(WebActivity.newIntent(context, url));
    }

    public static void jumpToMain(LoginActivity loginActivity) {
        loginActivity.startActivity(MainActivity.newIntent(loginActivity));
    }
}

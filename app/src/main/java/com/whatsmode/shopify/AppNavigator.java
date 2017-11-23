package com.whatsmode.shopify;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;

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

    public static void jumpToMain(Activity activity) {
        activity.startActivity(MainActivity.newIntent(activity));
    }

    public static void jumpToLogin(Activity activity) {
        activity.startActivity(new Intent(activity,LoginActivity.class));
    }
}

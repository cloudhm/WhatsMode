package com.whatsmode.shopify;


import android.content.Context;

import com.whatsmode.shopify.block.WebActivity;

/**
 *  页面跳转控制中心
 */
public class AppNavigator {

    public static void jumpToWebActivity(Context context, String url){
        context.startActivity(WebActivity.newIntent(context, url));
    }
}

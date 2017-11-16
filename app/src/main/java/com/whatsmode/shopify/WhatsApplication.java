package com.whatsmode.shopify;

import android.app.Application;
import android.content.Context;

import com.whatsmode.library.util.ToastUtil;


public class WhatsApplication extends Application {

    private static WhatsApplication sContext = null;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        sContext = this;
        ToastUtil.init(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static WhatsApplication getContext() {
        return sContext;
    }
}

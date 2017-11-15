package com.whatsmode.shopify;

import android.app.Application;
import android.content.Context;


public class WhatsApplication extends Application {

    private static WhatsApplication sContext = null;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        sContext = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static WhatsApplication getContext() {
        return sContext;
    }
}

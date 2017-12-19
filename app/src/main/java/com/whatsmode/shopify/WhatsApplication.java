package com.whatsmode.shopify;

import android.content.Context;
import android.support.multidex.MultiDex;

import com.mob.MobApplication;
import com.shopify.buy3.GraphClient;
import com.whatsmode.library.util.ToastUtil;

import cn.jpush.android.api.JPushInterface;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;


public class WhatsApplication extends MobApplication {

    private static WhatsApplication sContext = null;

    private static GraphClient sGraphClient;

    public static GraphClient getGraphClient(){
        return sGraphClient;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        MultiDex.install(this);
    }

    private void initJPush() {
        JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);     		// 初始化 JPush
        initGraphClient();
        Thread.setDefaultUncaughtExceptionHandler(AppException.getAppExceptionHandler());
    }


    private void initGraphClient() {
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();

        sGraphClient = GraphClient.builder(this)
                .shopDomain(BuildConfig.SHOP_DOMAIN)
                .accessToken(BuildConfig.API_KEY)
                .httpClient(httpClient)
                .build();
        ToastUtil.init(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
        initJPush();
    }

    public static WhatsApplication getContext() {
        return sContext;
    }


}

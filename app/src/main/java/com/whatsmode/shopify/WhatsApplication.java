package com.whatsmode.shopify;

import android.app.Application;
import android.content.Context;
import com.shopify.buy3.GraphClient;
import com.shopify.buy3.HttpCachePolicy;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import com.whatsmode.library.util.ToastUtil;


public class WhatsApplication extends Application {

    private static WhatsApplication sContext = null;

    private static GraphClient sGraphClient;

    public static GraphClient getGraphClient(){
        return sGraphClient;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        sContext = this;

        initGraphClient();
    }

    private void initGraphClient() {
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();

        sGraphClient = GraphClient.builder(this)
                .shopDomain(BuildConfig.SHOP_DOMAIN)
                .accessToken(BuildConfig.API_KEY)
                .httpClient(httpClient)
                .httpCache(getCacheDir(), 1024 * 1024 * 10)
                .defaultHttpCachePolicy(HttpCachePolicy.CACHE_FIRST.expireAfter(20, TimeUnit.MINUTES))
                .build();
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

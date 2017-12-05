package com.whatsmode.shopify.block.me;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.whatsmode.shopify.R;


/**
 * Created by tom on 17-12-5.
 */

public class TrackingActivity extends AppCompatActivity {

    public static final String URL = "url";

    private WebView mWebView;

    private String mUrl;
    private Toolbar mToolbar;
    private ProgressBar mIndeterminateBar;

    public static Intent newIntent(Context context,String url){
        Intent intent = new Intent(context,TrackingActivity.class);
        intent.putExtra(URL,url);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);
        StatusBarUtil.StatusBarLightMode(this);
        mWebView = (WebView) findViewById(R.id.webview);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mIndeterminateBar = (ProgressBar) findViewById(R.id.indeterminate_bar);
        mUrl = getIntent().getStringExtra(URL);
        init();
        setWebView();
    }

    private void init(){
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setWebView(){

        WebSettings webSettings = mWebView.getSettings();
        //设置WebView属性，能够执行Javascript脚本
        webSettings.setJavaScriptEnabled(true);
        //设置可以访问文件
        webSettings.setAllowFileAccess(true);
        //设置支持缩放
        webSettings.setBuiltInZoomControls(false);
        //***************************************
        //设置当前显示的网页的宽度正好是手机屏幕的宽度
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        //加载需要显示的网页
        mWebView.loadUrl(mUrl);
        //设置Web视图
        mWebView.setWebViewClient(new MyWebViewClient(){
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }
        });

    }

    //Web视图
    private class MyWebViewClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            mIndeterminateBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            mIndeterminateBar.setVisibility(View.GONE);
        }
    }
}

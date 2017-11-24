package com.whatsmode.shopify.block;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.whatsmode.shopify.R;
import com.whatsmode.shopify.base.BaseActivity;
import com.whatsmode.shopify.ui.helper.ToolbarHelper;
import com.zchu.log.Logger;


public class WebActivity extends BaseActivity {

    private static final String EXTRA_URL = "url";
    private static final String EXTRA_TITLE = "title";

    private ProgressBar mProgressBar;
    private WebView mWebView;
    private String url;

    public static final String STATE_PAY = "PAY";  // 支付頁
    public static final String STATE_PRODUCT = "PRODUCT";  // 商品詳情頁

    public static Intent newIntent(Context context,String title, String url) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra(EXTRA_URL, url);
        intent.putExtra(EXTRA_TITLE, title);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_web);
        String title = getIntent().getStringExtra(EXTRA_TITLE);
        initToolBar(title);
        ToolbarHelper.initToolbarNoFix(this, R.id.toolbar, true, title);
        mWebView = (WebView) findViewById(R.id.webview);
        mWebView.getSettings().setUserAgentString("mobile");

        mProgressBar = (ProgressBar) findViewById(R.id.indeterminateBar);
        ToolbarHelper.initToolbarNoFix(this, R.id.toolbar, true, "");
        url = getIntent().getStringExtra(EXTRA_URL);
        if (!TextUtils.isEmpty(url)){
            initWeb();
        }
    }

    private void initToolBar(String title) {
        // modify toolbar display according title
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initWeb() {
        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                mProgressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                mProgressBar.setVisibility(View.GONE);
            }
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                mWebView.loadUrl(url);
                return true;
            }

            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Logger.e("---url---" + request.getUrl());
                return super.shouldOverrideUrlLoading(view, request);
            }
        });
        mWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }
        });
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl(url);
    }
}

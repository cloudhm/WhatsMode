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
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.whatsmode.shopify.R;
import com.whatsmode.shopify.common.Constant;


/**
 * Created by tom on 17-12-5.
 */

public class TrackingActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String URL = "url";

    private WebView mWebView;

    private String mUrl;
    private Toolbar mToolbar;
    private ProgressBar mIndeterminateBar;
    private TextView mTitle;
    private FrameLayout mWebViewL;
    private Button mReload;

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
        mWebViewL = (FrameLayout) findViewById(R.id.webview_l);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mTitle = (TextView) findViewById(R.id.title);
        mIndeterminateBar = (ProgressBar) findViewById(R.id.indeterminate_bar);
        mReload = (Button) findViewById(R.id.reload);
        mReload.setOnClickListener(this);
        addWebView();
        mUrl = getIntent().getStringExtra(URL);
        init();
        setWebView();
    }

    private void addWebView() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mWebView = new WebView(getApplicationContext());
        mWebView.setLayoutParams(params);
        mWebViewL.addView(mWebView);
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
        webSettings.setUserAgentString(Constant.USER_AGENT);

        //如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        webSettings.setJavaScriptEnabled(true);

        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小

        //缩放操作
        webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件

        //其他细节操作
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //关闭webview中缓存
        webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式


        //设置Web视图
        mWebView.setWebViewClient(new MyWebViewClient(){
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }
        });
        mWebView.setWebChromeClient(new MyWebChromeClient());

        //加载需要显示的网页
        mWebView.loadUrl(mUrl);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.reload:
                mIndeterminateBar.setVisibility(View.VISIBLE);
                mWebView.loadUrl(mUrl);
                mWebView.setVisibility(View.VISIBLE);
                mReload.setVisibility(View.GONE);
                break;
        }
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

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            //reload
            mWebView.setVisibility(View.GONE);
            mReload.setVisibility(View.VISIBLE);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);
            handler.proceed();
        }
    }

    public class MyWebChromeClient extends WebChromeClient{
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            mTitle.setText(title);
        }
    }

    protected void onDestroy() {
        if (mWebView != null) {
            mWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mWebView.clearHistory();

            ((ViewGroup) mWebView.getParent()).removeView(mWebView);
            mWebView.destroy();
            mWebView = null;
        }
        super.onDestroy();
    }
}

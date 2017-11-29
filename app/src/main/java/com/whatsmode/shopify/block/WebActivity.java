package com.whatsmode.shopify.block;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;

import com.whatsmode.library.util.RegexUtils;
import com.whatsmode.shopify.AppNavigator;
import com.whatsmode.shopify.R;
import com.whatsmode.shopify.base.BaseActivity;
import com.whatsmode.shopify.block.cart.BadgeActionProvider;
import com.whatsmode.shopify.block.cart.JumpCartSelect;
import com.whatsmode.shopify.ui.helper.ToolbarHelper;

import org.greenrobot.eventbus.EventBus;


public class WebActivity extends BaseActivity implements View.OnClickListener {

    private static final String EXTRA_URL = "url";
    private static final String EXTRA_TITLE = "title";

    public static final String STATE_SEARCH = "SEARCH"; // 搜索
    public static final String STATE_PAY = "PAY";  // 支付頁
    public static final String STATE_PRODUCT = "PRODUCT";  // 商品詳情頁
    public static final String STATE_COLLECTIONS = "COLLECTIONS"; // 網紅

    private ProgressBar mProgressBar;
    private WebView mWebView;
    private Button btnAddToCart;
    private String url;
    private String title;
    private MenuItem menuItemShare;
    private MenuItem menuItemCart;


    public static Intent newIntent(Context context,String title, String url) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra(EXTRA_URL, url);
        intent.putExtra(EXTRA_TITLE, title);
        return intent;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_web, menu);
        menuItemShare = menu.findItem(R.id.action_share);
        menuItemCart = menu.findItem(R.id.action_cart);
        BadgeActionProvider mActionProvider = (BadgeActionProvider) MenuItemCompat.getActionProvider(menuItemCart);
        mActionProvider.setOnClickListener(0, what -> {
            AppNavigator.jumpToMain(this);
            EventBus.getDefault().post(new JumpCartSelect());
        });// 设置点击监听。
        initToolBar();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_web);
        title = getIntent().getStringExtra(EXTRA_TITLE);
        ToolbarHelper.initToolbarNoFix(this, R.id.toolbar, true, title);
        mWebView = (WebView) findViewById(R.id.webview);
        mWebView.getSettings().setUserAgentString("mobile-Android");

        btnAddToCart = (Button) findViewById(R.id.add_to_cart);
        btnAddToCart.setOnClickListener(this);
        mProgressBar = (ProgressBar) findViewById(R.id.indeterminateBar);
        ToolbarHelper.initToolbarNoFix(this, R.id.toolbar, true, "");
        url = getIntent().getStringExtra(EXTRA_URL);
        if (!TextUtils.isEmpty(url)){
            initWebTitle();
        }
    }

    private void initToolBar() {
        // modify toolbar display according title
        switch (title) {
            case WebActivity.STATE_COLLECTIONS:
                btnAddToCart.setVisibility(View.GONE);
                menuItemCart.setVisible(false);
                menuItemShare.setVisible(true);
                break;
            case WebActivity.STATE_PRODUCT:
                btnAddToCart.setVisibility(View.VISIBLE);
                menuItemShare.setVisible(true);
                menuItemCart.setVisible(true);
                break;
            default:
                btnAddToCart.setVisibility(View.GONE);
                menuItemShare.setVisible(false);
                menuItemCart.setVisible(false);
                break;
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initWebTitle() {
        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mProgressBar.setVisibility(View.GONE);
                initToolBar();
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
                if (RegexUtils.isProduct(url)) {
                    startActivity(WebActivity.newIntent(WebActivity.this,WebActivity.STATE_PRODUCT,url));
                } else if (RegexUtils.isCollection(url)) {
                    startActivity(WebActivity.newIntent(WebActivity.this,WebActivity.STATE_COLLECTIONS,url));
                }else{
                    view.loadUrl(url);
                }
                return true;
            }

            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return shouldOverrideUrlLoading(view, request.getUrl().toString());
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_to_cart:
                // FIXME: 2017/11/27
                break;
        }
    }
}

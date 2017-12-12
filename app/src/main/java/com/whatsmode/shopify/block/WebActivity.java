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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.whatsmode.library.util.RegexUtils;
import com.whatsmode.shopify.AppNavigator;
import com.whatsmode.shopify.R;
import com.whatsmode.shopify.actionlog.ActionLog;
import com.whatsmode.shopify.base.BaseActivity;
import com.whatsmode.shopify.block.cart.AndroidJs;
import com.whatsmode.shopify.block.cart.BadgeActionProvider;
import com.whatsmode.shopify.block.cart.CartItem;
import com.whatsmode.shopify.block.cart.JumpMainTab;
import com.whatsmode.shopify.block.me.ShareUtil;
import com.whatsmode.shopify.common.Constant;
import com.whatsmode.shopify.ui.helper.ToolbarHelper;
import com.zchu.log.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.net.URL;


public class WebActivity extends BaseActivity implements View.OnClickListener {

    private static final String EXTRA_URL = "mUrl";
    private static final String EXTRA_TITLE = "title";

    public static final String STATE_SEARCH = "SEARCH"; // 搜索
    public static final String STATE_PAY = "PAY";  // 支付頁
    public static final String STATE_PRODUCT = "PRODUCT";  // 商品詳情頁
    public static final String STATE_COLLECTIONS = "COLLECTIONS"; // 網紅
    public static final String STATE_ABOUT_US = "ABOUT_US"; // 關於我們

    private ProgressBar mProgressBar;
    private WebView mWebView;
    private String mUrl;
    private String title;
    private MenuItem menuItemShare;
    private MenuItem menuItemCart;
    private MenuItem menuItemSearch;
    private BadgeActionProvider mActionProvider;
    private RelativeLayout errorView;
    private ImageView ivLogo;
    private ToolbarHelper.ToolbarHolder toolbarHolder;


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
        menuItemSearch = menu.findItem(R.id.action_search);
        menuItemCart = menu.findItem(R.id.action_cart);
        mActionProvider = (BadgeActionProvider) MenuItemCompat.getActionProvider(menuItemCart);
        mActionProvider.setOnClickListener(0, what -> {
            AppNavigator.jumpToMain(this);
            EventBus.getDefault().post(new JumpMainTab(2));
            ActionLog.onEvent(Constant.Event.MY_CART);
        });// 设置点击监听。
        initToolBar();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                ShareUtil.showShare(this,EXTRA_TITLE,"",EXTRA_URL,EXTRA_URL);
                shareActionLog();
                break;
            case R.id.action_search:
                AppNavigator.jumpToWebActivity(this,STATE_SEARCH,Constant.URL_SEARCH);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void shareActionLog() {
        if (STATE_PRODUCT.equals(title)) {
            ActionLog.onEvent(Constant.Event.PRODUCT_SHARE);
        } else if (STATE_COLLECTIONS.equals(title)) {
            ActionLog.onEvent(Constant.Event.SHOP_SHARE);
        }
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
        errorView = (RelativeLayout) findViewById(R.id.net_error_view);
        Button btnRefresh = (Button)findViewById(R.id.refresh);
        btnRefresh.setOnClickListener(this);

        toolbarHolder = ToolbarHelper.initToolbar(this, R.id.toolbar, true, title);
        toolbarHolder.titleView.setVisibility(View.VISIBLE);
        ivLogo = (ImageView) findViewById(R.id.logo);
        mWebView = (WebView) findViewById(R.id.webview);
        mWebView.getSettings().setUserAgentString(Constant.USER_AGENT);
        mProgressBar = (ProgressBar) findViewById(R.id.indeterminateBar);
        mUrl = getIntent().getStringExtra(EXTRA_URL);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.addJavascriptInterface(new AndroidJs(), "android");//AndroidtoJS类对象映射到js的test对象
        if (!TextUtils.isEmpty(mUrl)){
            initWebTitle();
        }
        if (STATE_PRODUCT.equals(title)) {
            if(!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
        }
    }

    @Subscribe
    public void receive(CartItem list) {
        if (mActionProvider != null) {runOnUiThread(() -> mActionProvider.initIcon());}
    }

    private void initToolBar() {
        // modify toolbar display according title
        switch (title) {
            case WebActivity.STATE_COLLECTIONS:
                menuItemCart.setVisible(false);
                menuItemShare.setVisible(true);
                menuItemSearch.setVisible(true);
                ivLogo.setVisibility(View.GONE);
                toolbarHolder.titleView.setVisibility(View.VISIBLE);
                break;
            case WebActivity.STATE_PRODUCT:
                menuItemShare.setVisible(true);
                menuItemSearch.setVisible(false);
                menuItemCart.setVisible(true);
                ivLogo.setVisibility(View.GONE);
                toolbarHolder.titleView.setVisibility(View.VISIBLE);
                break;
            case WebActivity.STATE_ABOUT_US:
                menuItemCart.setVisible(false);
                menuItemShare.setVisible(false);
                menuItemSearch.setVisible(false);
                ivLogo.setVisibility(View.VISIBLE);
                toolbarHolder.titleView.setVisibility(View.GONE);
                break;
            default:
                menuItemShare.setVisible(false);
                menuItemCart.setVisible(false);
                ivLogo.setVisibility(View.GONE);
                menuItemSearch.setVisible(false);
                toolbarHolder.titleView.setVisibility(View.VISIBLE);
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
                if(!RegexUtils.isBlock(url))
                mProgressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                mProgressBar.setVisibility(View.GONE);
                if (mUrl.equals(failingUrl)) {
                    mWebView.setVisibility(View.GONE);
                    view.loadUrl("about:blank");
                    errorView.setVisibility(View.VISIBLE);
                }
            }
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                onReceivedError(view,error.getErrorCode(),error.getDescription().toString(),request.getUrl().toString());
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (RegexUtils.isProduct(url)) {
                    startActivity(WebActivity.newIntent(WebActivity.this,WebActivity.STATE_PRODUCT,url));
                } else if (RegexUtils.isCollection(url)) {
                    startActivity(WebActivity.newIntent(WebActivity.this,WebActivity.STATE_COLLECTIONS,url));
                } else if (RegexUtils.isPages(url)){
                    startActivity(WebActivity.newIntent(WebActivity.this,WebActivity.STATE_ABOUT_US,url));
                    aboutUsAnalytics(url);
                }else if (RegexUtils.isBlock(url)) {
                } else {
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
        mWebView.loadUrl(mUrl);
    }

    private void aboutUsAnalytics(String url){
        if (RegexUtils.isJoinUs(url)) {
            ActionLog.onEvent(Constant.Event.JOIN_US);
        } else if (RegexUtils.isPriacyPolicy(url)) {
            ActionLog.onEvent(Constant.Event.PRIVACY_POLICY);
        } else if (RegexUtils.isContactUs(url)) {
            ActionLog.onEvent(Constant.Event.CONTACT_US);
        } else if (RegexUtils.isPromoteUs(url)) {
            ActionLog.onEvent(Constant.Event.PROMOTE_WITH_US);
        } else if (RegexUtils.isShippingDelivery(url)) {
            ActionLog.onEvent(Constant.Event.SHIPPING_DELIVERY);
        } else if (RegexUtils.isTermsConditions(url)) {
            ActionLog.onEvent(Constant.Event.TERMS_CONDITIONS);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View v) {
        if (!TextUtils.isEmpty(mUrl)) {
            errorView.setVisibility(View.GONE);
            mWebView.loadUrl(mUrl);
            mWebView.postDelayed(() -> mWebView.setVisibility(View.VISIBLE), 2000);
            mProgressBar.setVisibility(View.VISIBLE);
        }
    }
}

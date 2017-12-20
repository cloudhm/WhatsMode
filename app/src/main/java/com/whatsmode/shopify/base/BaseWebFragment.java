package com.whatsmode.shopify.base;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.whatsmode.library.util.RegexUtils;
import com.whatsmode.shopify.AppNavigator;
import com.whatsmode.shopify.R;
import com.whatsmode.shopify.actionlog.ActionLog;
import com.whatsmode.shopify.block.WebActivity;
import com.whatsmode.shopify.common.Constant;

public class BaseWebFragment extends BaseFragment implements View.OnClickListener {

    private ProgressBar mProgressBar;
    private static final String KEY_URL = "key_url";
    private String mUrl;
    private RelativeLayout net_error_view;
    private WebView mWebView;

    public static BaseWebFragment newInstance(String url) {
        Bundle args = new Bundle();
        args.putString(KEY_URL,url);
        BaseWebFragment fragment = new BaseWebFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = getActivity().getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
        }

        return false;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mWebView = (WebView) view.findViewById(R.id.webView);
        net_error_view = (RelativeLayout) view.findViewById(R.id.net_error_view);
        Button btnRefresh = (Button) view.findViewById(R.id.refresh);
        btnRefresh.setOnClickListener(this);

        WebSettings settings = mWebView.getSettings();
        //settings.setUserAgentString(Constant.USER_AGENT);
        mProgressBar = (ProgressBar) view.findViewById(R.id.indeterminateBar);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mProgressBar.setVisibility(View.GONE);
                if(TextUtils.equals(mUrl,Constant.URL_TAB_MODE)){
                    ActionLog.onEvent(Constant.Event.PRODUCT_LIST);
                } else if (TextUtils.equals(mUrl,Constant.URL_TAB_INFLUENCE)) {
                    ActionLog.onEvent(Constant.Event.SHOP_LIST);
                }
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (RegexUtils.isCollection(url) || RegexUtils.isProduct(url)) {
                    mProgressBar.setVisibility(View.GONE);
                }else{
                    mProgressBar.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                //super.onReceivedError(view, errorCode, description, failingUrl);
                mProgressBar.setVisibility(View.GONE);
                if (mUrl.equals(failingUrl)) {
                    mWebView.setVisibility(View.GONE);
                    view.loadUrl("about:blank");
                    net_error_view.setVisibility(View.VISIBLE);
                }
            }
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                //super.onReceivedError(view, request, error);
                onReceivedError(view,error.getErrorCode(),error.getDescription().toString(),request.getUrl().toString());
            }

            public boolean shouldOverrideUrlLoading(WebView view, String url){
                //  重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
                prefixNotHttp(url);
                if (RegexUtils.isProduct(url)) {
                    AppNavigator.jumpToWebActivity(getActivity(), WebActivity.STATE_PRODUCT,url);
                    ActionLog.onEvent(Constant.Event.PRODUCT_DETAIL);
                } else if (RegexUtils.isCollection(url)) {
                    AppNavigator.jumpToWebActivity(getContext(),WebActivity.STATE_COLLECTIONS,url);
                }else if((RegexUtils.isPages(url))){
                    AppNavigator.jumpToWebActivity(getContext(),WebActivity.STATE_COLLECTIONS,url);
                } else if (RegexUtils.contactUsUrl(url)) {
                    AppNavigator.jumpToWebActivity(getContext(),"",url);
                    return true;
                } else {
                    view.loadUrl(url);
                }
                return true;
            }
        });
        mWebView.getSettings().setJavaScriptEnabled(true);
        if (getArguments() != null) {
            if (!TextUtils.isEmpty(getArguments().getString(KEY_URL))) {
                String url = getArguments().getString(KEY_URL);
                mUrl = url;
                mWebView.loadUrl(url);
            }
        }
    }

    private void prefixNotHttp(String url) {
        if (!url.startsWith("http")) {
            try {
                // 以下固定写法
                final Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(url));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                getActivity().startActivity(intent);
            } catch (Exception e) {
                // 防止没有安装的情况
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.web_fragment_layout;
    }

    @Override
    public void onClick(View v) {
        if (!TextUtils.isEmpty(mUrl)) {
            net_error_view.setVisibility(View.GONE);
            mWebView.loadUrl(mUrl);
            mWebView.postDelayed(() -> mWebView.setVisibility(View.VISIBLE), 2000);
            mProgressBar.setVisibility(View.VISIBLE);
        }
    }
}

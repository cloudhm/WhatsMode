package com.whatsmode.shopify.base;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.whatsmode.library.rx.Util;
import com.whatsmode.library.util.RegexUtils;
import com.whatsmode.shopify.AppNavigator;
import com.whatsmode.shopify.R;
import com.whatsmode.shopify.block.WebActivity;
import com.zchu.log.Logger;

public class BaseWebFragment extends BaseFragment {

    private ProgressBar mProgressBar;
    private static final String KEY_URL = "key_url";
    public static BaseWebFragment newInstance(String url) {
        Bundle args = new Bundle();
        args.putString(KEY_URL,url);
        BaseWebFragment fragment = new BaseWebFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        WebView mWebView = (WebView) view.findViewById(R.id.webView);
        WebSettings settings = mWebView.getSettings();
        settings.setUserAgentString("mobile");
        mProgressBar = (ProgressBar) view.findViewById(R.id.indeterminateBar);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Logger.e("---finish---" + url);
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
                Logger.e("--receive--" + failingUrl);
                mProgressBar.setVisibility(View.GONE);
            }
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                mProgressBar.setVisibility(View.GONE);
                Logger.e("---error---" + request.getUrl());
            }

            public boolean shouldOverrideUrlLoading(WebView view, String url){
                //  重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
                if (RegexUtils.isProduct(url)) {
                    AppNavigator.jumpToWebActivity(getActivity(), WebActivity.STATE_PRODUCT,url);
                } else if (RegexUtils.isCollection(url)) {
                    AppNavigator.jumpToWebActivity(getContext(),WebActivity.STATE_COLLECTIONS,url);
                }else{
                    view.loadUrl(url);
                }
                Logger.e("---override---" + url);
                return true;
            }
        });
        mWebView.getSettings().setJavaScriptEnabled(true);
        if (getArguments() != null) {
            if (!TextUtils.isEmpty(getArguments().getString(KEY_URL))) {
                String url = getArguments().getString(KEY_URL);
                mWebView.loadUrl(url);
            }
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.web_fragment_layout;
    }
}

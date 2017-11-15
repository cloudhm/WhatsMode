package com.whatsmode.shopify.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.whatsmode.shopify.R;

public class BaseWebFragment extends BaseFragment {

    private WebView mWebView;
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
        mWebView = (WebView) view.findViewById(R.id.webView);
        mWebView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            { //  重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
                view.loadUrl(url);
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

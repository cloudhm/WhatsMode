package com.whatsmode.shopify.base;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.whatsmode.library.util.DisplayUtil;
import com.whatsmode.shopify.ui.helper.LoadingDialog;

public class BaseActivity extends BaseSuperActivity{
    /**
     * 窗体第一次获取焦点
     */
    private boolean isFirstFocus = true;
    private LoadingDialog mLoadingDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        ViewGroup decorViewGroup = (ViewGroup) window.getDecorView();
        View statusBarView = new View(window.getContext());
        int statusBarHeight = DisplayUtil.getStateBarHeight(this);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, statusBarHeight);
        params.gravity = Gravity.TOP;
        statusBarView.setLayoutParams(params);
        statusBarView.setBackgroundColor(Color.WHITE);
        decorViewGroup.addView(statusBarView);
    }

    protected void showLoading(){
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(this);
        }
        if(!mLoadingDialog.isShowing())
        mLoadingDialog.show();
    }

    protected void hideLoading(){
        if(mLoadingDialog != null && mLoadingDialog.isShowing())
        mLoadingDialog.dismiss();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && isFirstFocus) {
            onWindowFocusFirstObtain();
        }
    }

    /**
     * 当窗体第一次获取到焦点会回调该方法
     */
    protected void onWindowFocusFirstObtain() {}


    @Override
    protected void onStop() {
        super.onStop();
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLoadingDialog = null;
    }
}

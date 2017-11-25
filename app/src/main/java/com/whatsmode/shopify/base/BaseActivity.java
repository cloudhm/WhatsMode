package com.whatsmode.shopify.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

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

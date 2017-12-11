package com.whatsmode.shopify.block.me;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.whatsmode.library.exception.APIException;
import com.whatsmode.library.util.SnackUtil;
import com.whatsmode.shopify.AppNavigator;
import com.whatsmode.shopify.R;
import com.whatsmode.shopify.mvp.MvpActivity;

/**
 * Created by tom on 17-11-30.
 */

public class ChangePasswordActivity extends MvpActivity<ChangePasswordContract.Presenter> implements ChangePasswordContract.View, View.OnClickListener {

    private Toolbar mToolbar;
    private EditText mCurrentPassword;
    private EditText mNewPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        StatusBarUtil.StatusBarLightMode(this);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView save = (TextView) findViewById(R.id.save);
        save.setOnClickListener(this);
        mCurrentPassword = (EditText) findViewById(R.id.current_password);
        mNewPassword = (EditText) findViewById(R.id.new_password);
        init();
    }

    private void init(){
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @NonNull
    @Override
    public ChangePasswordContract.Presenter createPresenter() {
        return new ChangePasswordPresenter();
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

    @Override
    public void onClick(View view) {
        String currentPwd = mCurrentPassword.getText().toString();
        String newPwd = mNewPassword.getText().toString();
        if (TextUtils.isEmpty(currentPwd)) {
            SnackUtil.toastShow(this,R.string.please_enter_the_current_password);
            return;
        }
        if (TextUtils.isEmpty(newPwd)) {
            SnackUtil.toastShow(this,R.string.please_enter_a_new_password);
            return;
        }
        mPresenter.setPassword(currentPwd,newPwd);
        showLoading();
    }

    @Override
    public void updateSuccess() {
        hideLoading();
        SnackUtil.toastShow(this,"change success");
        finish();
    }

    @Override
    public void onError(int code, String msg) {
        hideLoading();
        SnackUtil.toastShow(this,msg);
        if (code == APIException.CODE_SESSION_EXPIRE) {
            AppNavigator.jumpToLogin(this);
        }
    }
}

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
import com.whatsmode.shopify.common.KeyConstant;
import com.whatsmode.shopify.mvp.MvpActivity;

/**
 * Created by tom on 17-11-30.
 */

public class ChangePasswordActivity extends MvpActivity<ChangePasswordContract.Presenter> implements ChangePasswordContract.View, View.OnClickListener {
    public static final int TYPE_CHANGE_PASSWORD = 0;
    public static final int TYPE_FIRST_NAME = 1;
    public static final int TYPE_LAST_NAME = 2;

    private Toolbar mToolbar;
    private EditText mCurrentPassword;
    private EditText mNewPassword;

    private int mType = TYPE_CHANGE_PASSWORD;
    private TextView mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        StatusBarUtil.StatusBarLightMode(this);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView save = (TextView) findViewById(R.id.save);
        save.setOnClickListener(this);
        mTitle = (TextView) findViewById(R.id.title);
        mCurrentPassword = (EditText) findViewById(R.id.current_password);
        mNewPassword = (EditText) findViewById(R.id.new_password);
        mType = getIntent().getIntExtra(KeyConstant.KEY_MY_TYPE,TYPE_CHANGE_PASSWORD);
        init();
    }

    private void init(){
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (mType == TYPE_CHANGE_PASSWORD) {
            mTitle.setText(R.string.change_password);
            mNewPassword.setVisibility(View.VISIBLE);
        } else if (mType == TYPE_FIRST_NAME) {
            mTitle.setText(R.string.first_name);
            mNewPassword.setVisibility(View.GONE);
            mCurrentPassword.setHint(R.string.first_name);
            String value = getIntent().getStringExtra(KeyConstant.KEY_MY_VALUE);
            if (!TextUtils.isEmpty(value)) {
                mCurrentPassword.setText(value);
            }
        } else if (mType == TYPE_LAST_NAME) {
            mTitle.setText(R.string.last_name);
            mNewPassword.setVisibility(View.GONE);
            mCurrentPassword.setHint(R.string.last_name);
            String value = getIntent().getStringExtra(KeyConstant.KEY_MY_VALUE);
            if (!TextUtils.isEmpty(value)) {
                mCurrentPassword.setText(value);
            }
        }
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
        if (mType == TYPE_CHANGE_PASSWORD) {
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
        } else if (mType == TYPE_FIRST_NAME) {
            String firstName = mCurrentPassword.getText().toString();
            if (TextUtils.isEmpty(firstName)) {
                SnackUtil.toastShow(this,R.string.first_name_empty_prompt);
                return;
            }
            mPresenter.setName(firstName,null);
            showLoading();
        } else if (mType == TYPE_LAST_NAME) {
            String lastName = mCurrentPassword.getText().toString();
            if (TextUtils.isEmpty(lastName)) {
                SnackUtil.toastShow(this,R.string.last_name_empty_prompt);
                return;
            }
            mPresenter.setName(null,lastName);
            showLoading();
        }

    }

    @Override
    public void updateSuccess() {
        hideLoading();
        //SnackUtil.toastShow(this,"change success");
        if (mType == TYPE_FIRST_NAME || mType == TYPE_LAST_NAME) {
            setResult(RESULT_OK);
        }
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

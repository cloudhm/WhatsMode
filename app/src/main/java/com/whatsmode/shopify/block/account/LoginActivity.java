package com.whatsmode.shopify.block.account;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.whatsmode.library.util.SnackUtil;
import com.whatsmode.library.util.Util;
import com.whatsmode.shopify.BuildConfig;
import com.whatsmode.shopify.R;
import com.whatsmode.shopify.block.me.MyFragment;
import com.whatsmode.shopify.block.me.StatusBarUtil;
import com.whatsmode.shopify.mvp.MvpActivity;

/**
 * Created by tom on 17-11-16.
 */

public class LoginActivity extends MvpActivity<LoginPresenter> implements LoginContract.View, View.OnClickListener, View.OnFocusChangeListener {


    private TextInputEditText mEmail;
    private TextInputEditText mPwd;
    private TextInputLayout mEmailL;
    private TextInputLayout mPwdL;
    private TextView mForgotPwd;
    private TextView mCreateAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        StatusBarUtil.StatusBarLightMode(this);
        mEmail = (TextInputEditText) findViewById(R.id.email);
        mEmailL = (TextInputLayout)findViewById(R.id.email_l);
        mPwdL = (TextInputLayout)findViewById(R.id.pwd_l);
        mPwd = (TextInputEditText) findViewById(R.id.pwd);
        findViewById(R.id.forgot_pwd).setOnClickListener(this);
        mForgotPwd = (TextView) findViewById(R.id.forgot_pwd);
        mForgotPwd.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG );
        mCreateAccount = (TextView) findViewById(R.id.create_account);
        mCreateAccount.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG );
        mCreateAccount.setOnClickListener(this);
        mEmail.setOnFocusChangeListener(this);

    }


    public void login(View view) {
        String email = mEmail.getText().toString();
        String pwd = mPwd.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailL.setError(getString(R.string.please_enter_the_email));
            return;
        }else{
            if(!Util.isEmail(email)){
                mEmailL.setError(getString(R.string.please_enter_a_valid_email));
                return;
            }else{
                mEmailL.setError(null);
            }
        }
        if (TextUtils.isEmpty(pwd)) {
            mPwdL.setError(getString(R.string.please_input_a_password));
            return;
        }else{
            mPwdL.setError(null);
        }
        mPresenter.login(email,pwd);
    }

    @NonNull
    @Override
    public LoginPresenter createPresenter() {
        return new LoginPresenter();
    }

    @Override
    public void loginSuccess() {
        SnackUtil.toastShow(this,"登录成功");
    }

    @Override
    public void loginFail(String msg) {
        /*if ("Unidentified customer".equals(msg)) {
            SnackUtil.toastShow(this, msg);
            startActivity(new Intent(this,RegisterActivity.class));
        }*/
        SnackUtil.toastShow(this, msg);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.forgot_pwd:
                startActivity(new Intent(this,ForgotPwdActivity.class));
                break;
            case R.id.create_account:
                startActivity(new Intent(this,RegisterActivity.class));
                break;
        }
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        if (view.getId() == R.id.email) {
            if (!b) {
                String email = mEmail.getText().toString();
                if(!Util.isEmail(email)){
                    mEmailL.setError(getString(R.string.please_enter_a_valid_email));
                }else{
                    mEmailL.setError(null);
                }
            }
        }
    }
}

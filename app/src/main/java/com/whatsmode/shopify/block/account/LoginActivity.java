package com.whatsmode.shopify.block.account;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.whatsmode.library.util.SnackUtil;
import com.whatsmode.shopify.R;
import com.whatsmode.shopify.mvp.MvpActivity;

/**
 * Created by tom on 17-11-16.
 */

public class LoginActivity extends MvpActivity<LoginPresenter> implements LoginContract.View, View.OnClickListener {


    private EditText mEmail;
    private EditText mPwd;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mEmail = (EditText) findViewById(R.id.email);
        mPwd = (EditText) findViewById(R.id.pwd);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        findViewById(R.id.forgot_pwd).setOnClickListener(this);
        init();
    }

    private void init(){
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("登录");
    }

    public void login(View view) {
        String email = mEmail.getText().toString();
        String pwd = mPwd.getText().toString();
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this,"请输入邮箱",Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(pwd)) {
            Toast.makeText(this,"请输入密码",Toast.LENGTH_SHORT).show();
            return;
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
        if ("Unidentified customer".equals(msg)) {
            startActivity(new Intent(this,RegisterActivity.class));
        }else {
            SnackUtil.toastShow(this, msg);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.forgot_pwd:
                startActivity(new Intent(this,ForgotPwdActivity.class));
                break;
        }
    }
}

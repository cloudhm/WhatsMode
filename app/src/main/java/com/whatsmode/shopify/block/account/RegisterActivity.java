package com.whatsmode.shopify.block.account;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.whatsmode.library.util.SnackUtil;
import com.whatsmode.shopify.R;
import com.whatsmode.shopify.mvp.MvpActivity;

/**
 * Created by tom on 17-11-16.
 */

public class RegisterActivity extends MvpActivity<RegisterPresenter> implements RegisterContract.View {


    private Toolbar mToolbar;
    private EditText mEmail;
    private EditText mPwd;
    private EditText mFirstName;
    private EditText mLastName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mEmail = (EditText) findViewById(R.id.email);
        mPwd = (EditText) findViewById(R.id.pwd);
        mFirstName = (EditText) findViewById(R.id.first_name);
        mLastName = (EditText) findViewById(R.id.last_name);
        init();
    }

    private void init(){
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("注册");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void register(View view) {
        String email = mEmail.getText().toString();
        String pwd = mPwd.getText().toString();
        String firstName = mFirstName.getText().toString();
        String lastName = mLastName.getText().toString();
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this,"请输入邮箱",Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(pwd)) {
            Toast.makeText(this,"请输入密码",Toast.LENGTH_SHORT).show();
            return;
        }
        mPresenter.register(email,pwd,firstName,lastName);
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

    @NonNull
    @Override
    public RegisterPresenter createPresenter() {
        return new RegisterPresenter();
    }

    @Override
    public void registerSuccess() {
        finish();
    }

    @Override
    public void registerFail(String msg) {
        SnackUtil.toastShow(this,msg);
    }
}

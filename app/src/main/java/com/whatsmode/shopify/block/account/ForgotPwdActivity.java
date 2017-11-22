package com.whatsmode.shopify.block.account;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.whatsmode.library.util.SnackUtil;
import com.whatsmode.library.util.Util;
import com.whatsmode.shopify.R;
import com.whatsmode.shopify.block.me.StatusBarUtil;
import com.whatsmode.shopify.mvp.MvpActivity;

/**
 * Created by tom on 17-11-20.
 */

public class ForgotPwdActivity extends MvpActivity<ForgotPwdPresenter> implements ForgotPwdContract.View, View.OnClickListener {

    private static final int REQUEST_CODE = 10;

    private TextInputEditText mEmail;
    private Button mContinueSure;
    private Button mBack;
    private TextInputLayout mEmailL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pwd);
        StatusBarUtil.StatusBarLightMode(this);
        mEmail = (TextInputEditText) findViewById(R.id.email);
        mEmailL = (TextInputLayout) findViewById(R.id.email_l);
        mContinueSure = (Button) findViewById(R.id.continue_sure);
        mBack = (Button) findViewById(R.id.back);
        mContinueSure.setOnClickListener(this);
        mBack.setOnClickListener(this);
    }

    @NonNull
    @Override
    public ForgotPwdPresenter createPresenter() {
        return new ForgotPwdPresenter();
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
        switch (view.getId()) {
            case R.id.continue_sure:
                String email = mEmail.getText().toString();
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
                //Intent intent = new Intent(this,CheckEmailActivity.class);
                //startActivityForResult(intent,REQUEST_CODE);
                mPresenter.recover(email);
                break;
            case R.id.back:
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            finish();
        }
    }

    @Override
    public void success() {
        finish();
    }

    @Override
    public void onError(int code, String msg) {
        SnackUtil.toastShow(this,msg);
    }
}

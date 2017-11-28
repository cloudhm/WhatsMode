package com.whatsmode.shopify.block.account;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.whatsmode.library.util.SnackUtil;
import com.whatsmode.library.util.Util;
import com.whatsmode.shopify.R;
import com.whatsmode.shopify.block.me.StatusBarUtil;
import com.whatsmode.shopify.common.Constant;
import com.whatsmode.shopify.common.KeyConstant;
import com.whatsmode.shopify.mvp.MvpActivity;

/**
 * Created by tom on 17-11-20.
 */

public class ForgotPwdActivity extends MvpActivity<ForgotPwdPresenter> implements ForgotPwdContract.View, View.OnClickListener, TextWatcher {

    private TextInputEditText mEmail;
    private Button mContinueSure;
    private Button mBack;
    private TextInputLayout mEmailL;
    private Toolbar mToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pwd);
        StatusBarUtil.StatusBarLightMode(this);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mEmail = (TextInputEditText) findViewById(R.id.email);
        mEmail.addTextChangedListener(this);
        mEmailL = (TextInputLayout) findViewById(R.id.email_l);
        mContinueSure = (Button) findViewById(R.id.continue_sure);
        mBack = (Button) findViewById(R.id.back);
        mContinueSure.setOnClickListener(this);
        mBack.setOnClickListener(this);
        String email = getIntent().getStringExtra(KeyConstant.KEY_EMAIL);
        mEmail.setText(email);
        mEmail.performClick();
        init();
    }

    private void init(){
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
                setResult(RESULT_OK);
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
                showLoading();
                break;
            case R.id.back:
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.KEY_ACCOUNT_DISMISS && resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        }
    }

    @Override
    public void success() {
        hideLoading();
        showToast();
        finish();
    }

    @Override
    public void onError(int code, String msg) {
        hideLoading();
        SnackUtil.toastShow(this,msg);
    }


    void changeSubmitStatus() {
        if (!TextUtils.isEmpty(mEmail.getText())) {
            mContinueSure.setEnabled(true);
        } else {
            mContinueSure.setEnabled(false);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        changeSubmitStatus();
    }

    private void showToast(){
        Toast toast = new Toast(this);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.setView(View.inflate(this,R.layout.toast_sent_email,null));
        toast.show();
    }
}

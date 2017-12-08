package com.whatsmode.shopify.block.account;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.whatsmode.library.util.SnackUtil;
import com.whatsmode.library.util.Util;
import com.whatsmode.shopify.AppNavigator;
import com.whatsmode.shopify.R;
import com.whatsmode.shopify.actionlog.ActionLog;
import com.whatsmode.shopify.block.me.StatusBarUtil;
import com.whatsmode.shopify.common.Constant;
import com.whatsmode.shopify.common.KeyConstant;
import com.whatsmode.shopify.mvp.MvpActivity;

/**
 * Created by tom on 17-11-16.
 */

public class RegisterActivity extends MvpActivity<RegisterPresenter> implements RegisterContract.View, View.OnFocusChangeListener, View.OnClickListener, TextWatcher {


    private Toolbar mToolbar;
    private TextInputEditText mEmail;
    private TextInputEditText mPwd;
    private TextInputEditText mFirstName;
    private TextInputEditText mLastName;
    private TextInputLayout mEmailL;
    private TextInputLayout mPwdL;
    private TextInputLayout mFirstNameL;
    private TextInputLayout mLastNameL;
    private Button mRegister;

    private boolean mInLoginJump;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        StatusBarUtil.StatusBarLightMode(this);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mEmail = (TextInputEditText) findViewById(R.id.email);
        mEmail.addTextChangedListener(this);
        mPwd = (TextInputEditText) findViewById(R.id.pwd);
        mPwd.addTextChangedListener(this);
        mFirstName = (TextInputEditText) findViewById(R.id.first_name);
        mLastName = (TextInputEditText) findViewById(R.id.last_name);
        mEmailL = (TextInputLayout) findViewById(R.id.email_l);
        mPwdL = (TextInputLayout) findViewById(R.id.pwd_l);
        mFirstNameL = (TextInputLayout) findViewById(R.id.first_name_l);
        mLastNameL = (TextInputLayout) findViewById(R.id.last_name_l);
        mRegister = (Button) findViewById(R.id.register);
        TextView signIn = (TextView) findViewById(R.id.sign_in);
        signIn.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG );
        signIn.setOnClickListener(this);
        mEmail.setOnFocusChangeListener(this);
        mInLoginJump = getIntent().getBooleanExtra(KeyConstant.KEY_IN_LOGIN_JUMP,false);
        init();
    }

    private void init(){
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void register(View view) {
        String email = mEmail.getText().toString();
        String pwd = mPwd.getText().toString();
        String firstName = mFirstName.getText().toString();
        String lastName = mLastName.getText().toString();
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
        if (TextUtils.isEmpty(mEmailL.getError())) {

            mPresenter.register(email,pwd,firstName,lastName);
            showLoading();
        }
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

    @NonNull
    @Override
    public RegisterPresenter createPresenter() {
        return new RegisterPresenter();
    }

    @Override
    public void registerSuccess() {
        ActionLog.onEvent(Constant.Event.CREATE_ACCOUNT);
        finish();
    }

    @Override
    public void registerFail(String msg) {
        hideLoading();
        if (Constant.EMAIL_HAS_ALREADY_BEEN_TAKEN.equals(msg)) {
            finish();
        }
        SnackUtil.toastShow(this,msg);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sign_in:
                if (mInLoginJump) {
                    finish();
                }else{
                    AppNavigator.jumpToLogin(this);
                    finish();
                }
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

    void changeSubmitStatus() {
        if (!TextUtils.isEmpty(mEmail.getText()) && !TextUtils.isEmpty(mPwd.getText())) {
            mRegister.setEnabled(true);
        } else {
            mRegister.setEnabled(false);
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
}

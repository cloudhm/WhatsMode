package com.whatsmode.shopify.block.account;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.whatsmode.library.rx.RxBus;
import com.whatsmode.library.util.SnackUtil;
import com.whatsmode.library.util.Util;
import com.whatsmode.shopify.AppNavigator;
import com.whatsmode.shopify.BuildConfig;
import com.whatsmode.shopify.R;
import com.whatsmode.shopify.actionlog.ActionLog;
import com.whatsmode.shopify.block.account.data.AccountManager;
import com.whatsmode.shopify.block.cart.JumpMainTab;
import com.whatsmode.shopify.block.me.MyFragment;
import com.whatsmode.shopify.block.me.StatusBarUtil;
import com.whatsmode.shopify.block.me.event.LoginEvent;
import com.whatsmode.shopify.common.Constant;
import com.whatsmode.shopify.common.KeyConstant;
import com.whatsmode.shopify.mvp.MvpActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * Created by tom on 17-11-16.
 */

public class LoginActivity extends MvpActivity<LoginPresenter> implements LoginContract.View, View.OnClickListener, View.OnFocusChangeListener, TextWatcher {


    private TextInputEditText mEmail;
    private TextInputEditText mPwd;
    private TextInputLayout mEmailL;
    private TextInputLayout mPwdL;
    private TextView mForgotPwd;
    private TextView mCreateAccount;
    private Button mLogin;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        StatusBarUtil.StatusBarLightMode(this);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mEmail = (TextInputEditText) findViewById(R.id.email);
        mEmailL = (TextInputLayout)findViewById(R.id.email_l);
        mPwdL = (TextInputLayout)findViewById(R.id.pwd_l);
        mPwd = (TextInputEditText) findViewById(R.id.pwd);
        mLogin = (Button) findViewById(R.id.login);
        findViewById(R.id.forgot_pwd).setOnClickListener(this);
        mForgotPwd = (TextView) findViewById(R.id.forgot_pwd);
        mForgotPwd.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG );
        mCreateAccount = (TextView) findViewById(R.id.create_account);
        mCreateAccount.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG );
        mCreateAccount.setOnClickListener(this);
        mEmail.setOnFocusChangeListener(this);
        mEmail.addTextChangedListener(this);
        mPwd.addTextChangedListener(this);
        init();
    }

    private void init(){
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
        showLoading();
    }

    @NonNull
    @Override
    public LoginPresenter createPresenter() {
        return new LoginPresenter();
    }

    @Override
    public void loginSuccess() {
        ActionLog.onEvent(Constant.Event.SIGN_IN);
        setJPushAlias(AccountManager.getUsername());
        //EventBus.getDefault().post(new JumpMainTab(JumpMainTab.RefreshMainPage));
        RxBus.getInstance().post(new LoginEvent(true));
        hideLoading();
        //AppNavigator.jumpToMain(this);
        finish();
    }

    @Override
    public void loginFail(String msg) {
        setJPushAlias(null);
        /*if ("Unidentified customer".equals(msg)) {
            SnackUtil.toastShow(this, msg);
            startActivity(new Intent(this,RegisterActivity.class));
        }*/
        hideLoading();
        SnackUtil.toastShow(this, msg);
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
            case R.id.forgot_pwd:
                Intent intent = new Intent(this, ForgotPwdActivity.class);
                intent.putExtra(KeyConstant.KEY_EMAIL,mEmail.getText().toString());
                startActivityForResult(intent, Constant.KEY_ACCOUNT_DISMISS);
                break;
            case R.id.create_account:
                Intent intentRegister = new Intent(this, RegisterActivity.class);
                intentRegister.putExtra(KeyConstant.KEY_IN_LOGIN_JUMP,true);
                startActivityForResult(intentRegister,Constant.KEY_ACCOUNT_DISMISS);
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

    void changeSubmitStatus() {
        if (!TextUtils.isEmpty(mEmail.getText()) && !TextUtils.isEmpty(mPwd.getText())) {
            mLogin.setEnabled(true);
        } else {
            mLogin.setEnabled(false);
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


    /**
     * JPush set alias
     * @param alias
     */
    private void setJPushAlias(String alias){
        if (mHandler != null) {
            if (alias == null) {
                // 调用 Handler 来异步设置别名
                mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, ""));
            } else {
                // 调用 Handler 来异步设置别名
                mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, alias));
                Log.i(TAG, "alias:"+alias);
            }

        }
    }

    private static final String TAG = "JPush";

    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs ;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    Log.i(TAG, logs);
                    // 建议这里往 SharePreference 里写一个成功设置的状态。成功设置一次后，以后不必再次设置了。
                    break;
                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    Log.i(TAG, logs);
                    // 延迟 60 秒来调用 Handler 设置别名
                    mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                    break;
                default:
                    logs = "Failed with errorCode = " + code;
                    Log.e(TAG, logs);
            }

        }
    };
    private static final int MSG_SET_ALIAS = 1001;
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    Log.d(TAG, "Set alias in handler.");
                    // 调用 JPush 接口来设置别名。
                    JPushInterface.setAliasAndTags(LoginActivity.this,
                            (String) msg.obj,
                            null,
                            mAliasCallback);
                    break;
                default:
                    Log.i(TAG, "Unhandled msg - " + msg.what);
            }
        }
    };
}

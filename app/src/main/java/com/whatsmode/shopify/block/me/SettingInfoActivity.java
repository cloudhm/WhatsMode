package com.whatsmode.shopify.block.me;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.whatsmode.library.exception.APIException;
import com.whatsmode.library.util.SnackUtil;
import com.whatsmode.shopify.AppNavigator;
import com.whatsmode.shopify.R;
import com.whatsmode.shopify.block.account.LoginActivity;
import com.whatsmode.shopify.mvp.MvpActivity;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * Created by tom on 17-11-25.
 */

public class SettingInfoActivity extends MvpActivity<SettingInfoContract.Presenter> implements SettingInfoContract.View, View.OnClickListener {

    private Toolbar mToolbar;
    private EditText mFirstName;
    private EditText mLastName;
    private EditText mEmail;
    private TextView mChangePassword;
    private TextView mClearCache;
    private TextView mSignOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_info);
        StatusBarUtil.StatusBarLightMode(this);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        mFirstName = (EditText) findViewById(R.id.first_name);
        mLastName = (EditText) findViewById(R.id.last_name);
        mEmail = (EditText) findViewById(R.id.email);
        mChangePassword = (TextView) findViewById(R.id.change_password);
        mClearCache = (TextView) findViewById(R.id.clear_cache);
        mSignOut = (TextView) findViewById(R.id.sign_out);
        mChangePassword.setOnClickListener(this);
        mSignOut.setOnClickListener(this);
        init();
        mPresenter.getCustomer();
    }

    private void init(){
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @NonNull
    @Override
    public SettingInfoContract.Presenter createPresenter() {
        return new SettingInfoPresenter();
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
    public void showCustomer(Customer customer) {
        mFirstName.setText(customer.getFirstName());
        mLastName.setText(customer.getLastName());
        mEmail.setText(customer.getEmail());
    }

    @Override
    public void onError(int code, String msg) {
        if (code == APIException.CODE_SESSION_EXPIRE) {
            AppNavigator.jumpToLogin(this);
        }
        SnackUtil.toastShow(this,msg);
    }

    @Override
    public void updateSuccess() {
        SnackUtil.toastShow(this,"update success");
    }

    @Override
    public void signoutSuccess() {
        setJPushAlias(null);
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.change_password:
                //changePassword();
                startActivity(new Intent(this,ChangePasswordActivity.class));
                break;
            case R.id.sign_out:
                mPresenter.signout();
                break;
        }
    }


    private void changePassword() {
        Dialog dialog = new Dialog(this);
        dialog.show();
        View inflate = View.inflate(this, R.layout.dialog_update_passwod, null);
        Window window = dialog.getWindow();
        window.setContentView(inflate);
        EditText newPassword = (EditText) inflate.findViewById(R.id.new_password);
        Button complete = (Button) inflate.findViewById(R.id.complete);
        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pwd = newPassword.getText().toString();
                mPresenter.setCustomer(null,null,pwd);
                dialog.dismiss();
            }
        });
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
                    JPushInterface.setAliasAndTags(SettingInfoActivity.this,
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

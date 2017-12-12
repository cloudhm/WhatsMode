package com.whatsmode.shopify.block.me;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.whatsmode.library.exception.APIException;
import com.whatsmode.library.rx.RxBus;
import com.whatsmode.library.util.GlideCacheUtil;
import com.whatsmode.library.util.PreferencesUtil;
import com.whatsmode.library.util.SnackUtil;
import com.whatsmode.shopify.AppNavigator;
import com.whatsmode.shopify.R;
import com.whatsmode.shopify.actionlog.ActionLog;
import com.whatsmode.shopify.block.account.data.AccountManager;
import com.whatsmode.shopify.block.cart.JumpMainTab;
import com.whatsmode.shopify.block.me.event.LoginEvent;
import com.whatsmode.shopify.common.Constant;
import com.whatsmode.shopify.common.KeyConstant;
import com.whatsmode.shopify.mvp.MvpActivity;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * Created by tom on 17-11-25.
 */

public class SettingInfoActivity extends MvpActivity<SettingInfoContract.Presenter> implements SettingInfoContract.View, View.OnClickListener, View.OnFocusChangeListener, CompoundButton.OnCheckedChangeListener {

    private Toolbar mToolbar;
    private EditText mFirstName;
    private EditText mLastName;
    private EditText mEmail;
    private TextView mChangePassword;
    private TextView mClearCache;
    private TextView mSignOut;

    private String mFirstNameValue = "";
    private String mLastNameValue = "";
    private TextView mCacheSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_info);
        StatusBarUtil.StatusBarLightMode(this);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        mFirstName = (EditText) findViewById(R.id.first_name);
        mLastName = (EditText) findViewById(R.id.last_name);
        mFirstName.setOnFocusChangeListener(this);
        mLastName.setOnFocusChangeListener(this);
        mEmail = (EditText) findViewById(R.id.email);
        mChangePassword = (TextView) findViewById(R.id.change_password);
        mClearCache = (TextView) findViewById(R.id.clear_cache);
        mCacheSize = (TextView) findViewById(R.id.cache_size);
        mSignOut = (TextView) findViewById(R.id.sign_out);
        Switch pushSwitch = (Switch) findViewById(R.id.push_switch);
        pushSwitch.setOnCheckedChangeListener(this);
        findViewById(R.id.clear_cache_l).setOnClickListener(this);
        mChangePassword.setOnClickListener(this);
        mSignOut.setOnClickListener(this);
        init();
        mPresenter.getCustomer();
        setCacheSize();
        boolean isOpen = PreferencesUtil.getBoolean(this, KeyConstant.KEY_IS_OPEN_JPUSH, true);
        pushSwitch.setChecked(isOpen);
        ActionLog.onEvent(Constant.Event.SETTING);
    }

    private void init(){
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setCacheSize(){
        File cacheDir = getCacheDir();
        try {
            long folderSize = GlideCacheUtil.getInstance().getFolderSize(cacheDir);
            String formatSize = GlideCacheUtil.getInstance().getFormatSize(folderSize);
            mCacheSize.setText(formatSize);
        } catch (Exception e) {

        }
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
        mFirstNameValue = customer.getFirstName() == null ? "" : customer.getFirstName();
        mLastNameValue = customer.getLastName() == null ? "" : customer.getLastName();
    }

    @Override
    public void onError(int code, String msg) {
        hideLoading();
        if (code == APIException.CODE_SESSION_EXPIRE) {
            AppNavigator.jumpToLogin(this);
        }
        SnackUtil.toastShow(this,msg);
    }

    @Override
    public void updateSuccess() {
        //SnackUtil.toastShow(this,"update success");
        hideLoading();
    }

    @Override
    public void signoutSuccess() {
        hideLoading();
        setJPushAlias(null);
        AccountManager.getInstance().writeCustomerDefaultAddress(null);
        RxBus.getInstance().post(new LoginEvent(false));
        //EventBus.getDefault().post(new JumpMainTab(JumpMainTab.RefreshMainPage));
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.change_password:
                //changePassword();
                performFocusChange();
                startActivity(new Intent(this,ChangePasswordActivity.class));
                break;
            case R.id.sign_out:
                signOut();
                break;
            case R.id.clear_cache_l:
                clearCache();
                break;
        }
    }

    private void clearCache() {
        new AlertDialog.Builder(this,R.style.DialogTheme)
                .setTitle(R.string.prompt)
                .setMessage(R.string.confirm_the_scavenging_cache)
                .setNegativeButton(R.string.no_, (dialogInterface, i)
                        -> dialogInterface.dismiss())
                .setPositiveButton(R.string.yes_, (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    GlideCacheUtil.getInstance().deleteFolderFile(getCacheDir().getAbsolutePath(),false);
                    mCacheSize.setText("0.0B");
                }).create().show();
    }

    private void signOut() {
        new AlertDialog.Builder(this,R.style.DialogTheme)
                .setTitle(R.string.prompt)
                .setMessage(R.string.confirm_exit)
                .setNegativeButton(R.string.no_, (dialogInterface, i)
                        -> dialogInterface.dismiss())
                .setPositiveButton(R.string.yes_, (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    performFocusChange();
                    mPresenter.signout();
                    ActionLog.onEvent(Constant.Event.SIGN_OUT);
                    showLoading();
                }).create().show();
    }

    //Ａ版　废弃
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

    @Override
    public void onFocusChange(View view, boolean b) {
        if(b) return;
        switch (view.getId()) {
            case R.id.first_name:
                String firstName = mFirstName.getText().toString();
                if (!TextUtils.equals(firstName, mFirstNameValue)) {
                    mFirstNameValue = firstName;
                    mPresenter.setCustomer(firstName,null,null);
                    showLoading();
                }
                break;
            case R.id.last_name:
                String lastName = mLastName.getText().toString();
                if (!TextUtils.equals(lastName, mLastNameValue)) {
                    mLastNameValue = lastName;
                    mPresenter.setCustomer(null,lastName,null);
                    showLoading();
                }
                break;
        }
    }

    //模拟触发焦点改变
    private void performFocusChange(){
        String firstName = mFirstName.getText().toString();
        if (!TextUtils.equals(firstName, mFirstNameValue)) {
            onFocusChange(mFirstName,false);
        }
        String lastName = mLastName.getText().toString();
        if (!TextUtils.equals(lastName, mLastNameValue)) {
            onFocusChange(mLastName,false);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (!b) {
            //jpush close
            JPushInterface.setPushTime(this,new HashSet<>(),0,0);
        }else{
            //jpush open
            JPushInterface.setPushTime(this,null,0,0);
        }
        //save status
        PreferencesUtil.putBoolean(this,KeyConstant.KEY_IS_OPEN_JPUSH,b);
        ActionLog.onEvent(Constant.Event.PUSH_NOTIFICATION);
    }
}

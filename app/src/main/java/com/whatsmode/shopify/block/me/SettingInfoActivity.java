package com.whatsmode.shopify.block.me;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.api.Api;
import com.whatsmode.library.exception.APIException;
import com.whatsmode.library.util.SnackUtil;
import com.whatsmode.shopify.AppNavigator;
import com.whatsmode.shopify.R;
import com.whatsmode.shopify.mvp.MvpActivity;

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
        getSupportActionBar().setTitle("Setting");
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
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.change_password:
                changePassword();
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
}

package com.whatsmode.shopify.block.account;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.whatsmode.shopify.R;
import com.whatsmode.shopify.mvp.MvpActivity;

/**
 * Created by tom on 17-11-20.
 */

public class ForgotPwdActivity extends MvpActivity<ForgotPwdPresenter> implements ForgotPwdContract.View, View.OnClickListener {

    private static final int REQUEST_CODE = 10;

    private EditText mEmail;
    private Button mContinueSure;
    private TextView mBack;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pwd);
        mEmail = (EditText) findViewById(R.id.email);
        mContinueSure = (Button) findViewById(R.id.continue_sure);
        mBack = (TextView) findViewById(R.id.back);
        mContinueSure.setOnClickListener(this);
        mBack.setOnClickListener(this);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        init();
    }

    private void init(){
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("忘记密码");
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
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.continue_sure:
                Intent intent = new Intent(this,CheckEmailActivity.class);
                startActivityForResult(intent,REQUEST_CODE);
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
}

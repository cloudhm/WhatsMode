package com.whatsmode.shopify.block.account;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.whatsmode.shopify.R;
import com.whatsmode.shopify.base.BaseActivity;

/**
 * Created by tom on 17-11-20.
 */

public class CheckEmailActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_email);
        findViewById(R.id.login).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        setResult(RESULT_OK);
        finish();
    }
}

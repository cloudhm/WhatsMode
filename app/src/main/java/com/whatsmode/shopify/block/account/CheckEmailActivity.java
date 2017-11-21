package com.whatsmode.shopify.block.account;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.whatsmode.shopify.R;

/**
 * Created by tom on 17-11-20.
 */

public class CheckEmailActivity extends AppCompatActivity implements View.OnClickListener {

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

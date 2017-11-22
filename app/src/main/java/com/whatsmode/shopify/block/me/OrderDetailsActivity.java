package com.whatsmode.shopify.block.me;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.whatsmode.shopify.R;
import com.whatsmode.shopify.mvp.MvpActivity;

/**
 * Created by tom on 17-11-22.
 */

public class OrderDetailsActivity extends MvpActivity<OrderDetailsContract.Presenter> implements OrderDetailsContract.View {

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        init();
    }

    private void init(){
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Order Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

    @NonNull
    @Override
    public OrderDetailsContract.Presenter createPresenter() {
        return new OrderDetailsPresenter();
    }
}

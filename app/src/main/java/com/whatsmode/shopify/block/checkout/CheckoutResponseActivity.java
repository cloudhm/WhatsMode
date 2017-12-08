package com.whatsmode.shopify.block.checkout;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.whatsmode.shopify.AppNavigator;
import com.whatsmode.shopify.R;
import com.whatsmode.shopify.base.BaseActivity;
import com.whatsmode.shopify.block.cart.JumpMainTab;
import com.whatsmode.shopify.block.me.Order;
import com.whatsmode.shopify.block.me.OrderDetailsActivity;
import com.whatsmode.shopify.common.KeyConstant;
import com.whatsmode.shopify.ui.helper.ToolbarHelper;

import org.greenrobot.eventbus.EventBus;

public class CheckoutResponseActivity extends BaseActivity implements View.OnClickListener {

    public static final String EXTRA_STATUS = "EXTRA_STATUS";
    public static final String EXTRA_ORDER = "EXTRA_ORDER";

    private boolean status;
    private Order order;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_response);
        ToolbarHelper.initToolbar(this, R.id.toolbar, true, "");
        getParameter();
        checkView();
    }

    private void checkView() {
        RelativeLayout relativeLayoutPayed = (RelativeLayout) findViewById(R.id.checkout_payed);
        RelativeLayout relativeLayoutNotPayed = (RelativeLayout) findViewById(R.id.checkout_not_payed);
        if (status) {
            relativeLayoutNotPayed.setVisibility(View.GONE);
            relativeLayoutPayed.setVisibility(View.VISIBLE);
        }else{
            relativeLayoutPayed.setVisibility(View.GONE);
            relativeLayoutNotPayed.setVisibility(View.VISIBLE);
        }
        findViewById(R.id.complete).setOnClickListener(this);
        findViewById(R.id.repay).setOnClickListener(this);
        findViewById(R.id.view_order).setOnClickListener(this);
        findViewById(R.id.home).setOnClickListener(this);
        TextView tvPrice = (TextView) findViewById(R.id.order_price);
        if (order != null) {
            tvPrice.setText(new StringBuilder("$").append(order.getTotalPrice()));
        }
    }

    private void getParameter() {
        if (getIntent().hasExtra(EXTRA_STATUS)) {
            status = getIntent().getBooleanExtra(EXTRA_STATUS, false);
        }
        if (getIntent().hasExtra(EXTRA_ORDER)) {
            order = (Order) getIntent().getSerializableExtra(EXTRA_ORDER);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.complete:
                AppNavigator.jumpToMain(this);
                break;
            case R.id.repay:
                finish();
                break;
            case R.id.view_order:
                if (order == null) {
                    return;
                }
                Intent intent = new Intent(this, OrderDetailsActivity.class);
                intent.putExtra(KeyConstant.KEY_ORDER, order);
                startActivity(intent);
                finish();
                break;
            case R.id.home:
                EventBus.getDefault().post(new JumpMainTab(0));
                finish();
                break;
        }
    }
}

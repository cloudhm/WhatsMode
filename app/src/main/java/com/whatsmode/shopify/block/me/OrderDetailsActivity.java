package com.whatsmode.shopify.block.me;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.whatsmode.library.util.Util;
import com.whatsmode.shopify.R;
import com.whatsmode.shopify.block.WebActivity;
import com.whatsmode.shopify.block.address.Address;
import com.whatsmode.shopify.common.KeyConstant;
import com.whatsmode.shopify.mvp.MvpActivity;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.List;

/**
 * Created by tom on 17-11-22.
 */

public class OrderDetailsActivity extends MvpActivity<OrderDetailsContract.Presenter> implements OrderDetailsContract.View, View.OnClickListener {

    private Toolbar mToolbar;

    private Order mOrder;
    private RecyclerView mRecyclerView;
    private TextView mOrderNum;
    private TextView mPlacedOn;
    private TextView mShippingName;
    private TextView mShippingAddress;
    private TextView mProduct;
    private TextView mSubtotal;
    private TextView mShipping;
    private TextView mTaxes;
    private TextView mCoupon;
    private TextView mTotal;
    private TextView mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        StatusBarUtil.StatusBarLightMode(this);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mTitle = (TextView) findViewById(R.id.title);

        mOrderNum = (TextView) findViewById(R.id.order_num);
        mPlacedOn = (TextView) findViewById(R.id.placed_on);
        mShippingName = (TextView) findViewById(R.id.shipping_name);
        mShippingAddress = (TextView) findViewById(R.id.shipping_address);
        mProduct = (TextView) findViewById(R.id.product);
        mSubtotal = (TextView) findViewById(R.id.subtotal);
        mShipping = (TextView) findViewById(R.id.shipping);
        mTaxes = (TextView) findViewById(R.id.taxes);
        mCoupon = (TextView) findViewById(R.id.coupon);
        mTotal = (TextView) findViewById(R.id.total);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mOrder = (Order) getIntent().getSerializableExtra(KeyConstant.KEY_ORDER);
        findViewById(R.id.tracking_jump).setOnClickListener(this);
        init();
        initDetail();
    }

    private void initDetail() {
        if (mOrder == null) {
            return;
        }
        Integer orderNumber = mOrder.getOrderNumber();
        mOrderNum.setText(getString(R.string.order_) + String.valueOf(orderNumber) );
        DateTime processedAt = mOrder.getProcessedAt();
        mPlacedOn.setText(Util.dateTimeToString(processedAt));
        Address shippingAddress = mOrder.getShippingAddress();
        if (shippingAddress != null) {
            mShippingName.setText(shippingAddress.getLastName() + " " + shippingAddress.getFirstName());
            StringBuilder builder = new StringBuilder();
            if (!TextUtils.isEmpty(shippingAddress.getAddress1())) {
                builder.append(shippingAddress.getAddress1()).append(",");
            }
            if (!TextUtils.isEmpty(shippingAddress.getAddress2())) {
                builder.append(shippingAddress.getAddress2()).append(",");
            }
            if (!TextUtils.isEmpty(shippingAddress.getCity())) {
                builder.append(shippingAddress.getCity()).append(",");
            }
            if (!TextUtils.isEmpty(shippingAddress.getProvince())) {
                builder.append(shippingAddress.getProvince()).append(",");
            }
            if (!TextUtils.isEmpty(shippingAddress.getCountry())) {
                builder.append(shippingAddress.getCountry()).append(",");
            }
            if (!TextUtils.isEmpty(shippingAddress.getZip())) {
                builder.append(shippingAddress.getZip());
            }
            mShippingAddress.setText(builder.toString());
        }

        mSubtotal.setText(getString(R.string.dollar_sign)+String.valueOf(mOrder.getSubtotalPrice() != null ? mOrder.getSubtotalPrice().doubleValue() : 0.00));
        double shippingPrice = mOrder.getTotalShippingPrice() != null ? mOrder.getTotalShippingPrice().doubleValue() : 0.00;
        mShipping.setText((shippingPrice >= 0 ? "+" : "" )+ getString(R.string.dollar_sign)+String.valueOf(shippingPrice));
        double taxPrice = mOrder.getTotalTax() != null ? mOrder.getTotalTax().doubleValue() : 0.00;
        mTaxes.setText((taxPrice >= 0 ? "+" : "" )+ getString(R.string.dollar_sign)+String.valueOf(taxPrice));

        double totalPrice = mOrder.getTotalPrice() != null ? mOrder.getTotalPrice().doubleValue() : 0;
        mTotal.setText((totalPrice >= 0 ? "+" : "") + getString(R.string.dollar_sign)+String.valueOf(totalPrice));

        mProduct.setText(getString(R.string.product) + "(" + (mOrder.getLineItems() == null ? "0" : mOrder.getLineItems().size()) + ")");
        initRecycler(mOrder.getLineItems());
    }

    private void initRecycler(List<LineItem> lineItems) {
        if(lineItems == null || lineItems.isEmpty()) return;
        LineItemAdapter adapter = new LineItemAdapter(R.layout.item_line_item,lineItems);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        mRecyclerView.setAdapter(adapter);
    }

    private void init(){
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tracking_jump:
                Intent intent = WebActivity.newIntent(this,"",mOrder.getCustomerUrl());
                startActivity(intent);
                break;
        }
    }
}

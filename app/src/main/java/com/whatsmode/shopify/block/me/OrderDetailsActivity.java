package com.whatsmode.shopify.block.me;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.whatsmode.shopify.R;
import com.whatsmode.shopify.block.address.Address;
import com.whatsmode.shopify.common.KeyConstant;
import com.whatsmode.shopify.mvp.MvpActivity;

import java.util.List;

/**
 * Created by tom on 17-11-22.
 */

public class OrderDetailsActivity extends MvpActivity<OrderDetailsContract.Presenter> implements OrderDetailsContract.View {

    private Toolbar mToolbar;
    private TextView mAddress;
    private TextView mSubtotal;
    private TextView mShipping;
    private TextView mTotal;
    private Order mOrder;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mAddress = (TextView) findViewById(R.id.address);
        mSubtotal = (TextView) findViewById(R.id.subtotal);
        mShipping = (TextView) findViewById(R.id.shipping);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mTotal = (TextView) findViewById(R.id.total);
        mOrder = (Order) getIntent().getSerializableExtra(KeyConstant.KEY_ORDER);
        init();
        initDetail();
    }

    private void initDetail() {
        if (mOrder == null) {
            return;
        }
        Address shippingAddress = mOrder.getShippingAddress();
        if (shippingAddress != null) {
            mAddress.setText(shippingAddress.toString());
        }
        mSubtotal.setText(String.valueOf(mOrder.getSubtotalPrice() != null ? mOrder.getSubtotalPrice().doubleValue() : 0));
        mShipping.setText(String.valueOf(mOrder.getTotalShippingPrice() != null ? mOrder.getTotalShippingPrice().doubleValue():0));
        mTotal.setText(String.valueOf(mOrder.getTotalPrice() != null ? mOrder.getTotalPrice().doubleValue() : 0));

        initRecycler(mOrder.getLineItems());
    }

    private void initRecycler(List<LineItem> lineItems) {
        if(lineItems == null || lineItems.isEmpty()) return;
        LineItemAdapter adapter = new LineItemAdapter(R.layout.item_line_item,lineItems);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(adapter);
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

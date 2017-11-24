package com.whatsmode.shopify.block.checkout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shopify.graphql.support.ID;
import com.whatsmode.library.util.ListUtils;
import com.whatsmode.shopify.R;
import com.whatsmode.shopify.block.cart.CartItem;
import com.whatsmode.shopify.block.cart.CartItemLists;
import com.whatsmode.shopify.mvp.MvpActivity;
import com.whatsmode.shopify.ui.helper.ToolbarHelper;
import com.zchu.log.Logger;

/**
 * Created by Administrator on 2017/11/23.
 */

public class CheckoutUpdateActivity extends MvpActivity<CheckoutUpdateContact.Presenter> implements CheckoutUpdateContact.View, View.OnClickListener {

    private static final String EXTRA_ID = "id";
    private static final String EXTRA_ITEMS = "items";
    private static final String EXTRA_BUNDLE = "bundle";

    private TextView mTvSelectAddress,mTvSelectMethod,mTvPay,mTvTotal;
    public ID id;
    private CartItemLists dataSource;
    private LinearLayout layoutContainer;

    @NonNull
    @Override
    public CheckoutUpdateContact.Presenter createPresenter() {
        return new CheckoutUpdatePresenter();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_update);
        if (getIntent().hasExtra(EXTRA_ID)) {
            id = (ID) getIntent().getSerializableExtra(EXTRA_ID);
            Logger.e(id + "====id");
        }
        if (getIntent().hasExtra(EXTRA_BUNDLE)) {
            CartItemLists cartItemLists = (CartItemLists) getIntent().getBundleExtra(EXTRA_BUNDLE).getSerializable(EXTRA_ITEMS);
            dataSource = cartItemLists;
        }
        ToolbarHelper.initToolbarNoFix(this, R.id.toolbar, true, "CHECK OUT");
        mTvSelectAddress = (TextView) findViewById(R.id.select_address);
        mTvSelectMethod = (TextView) findViewById(R.id.select_method);
        layoutContainer = (LinearLayout) findViewById(R.id.container);
        mTvPay = (TextView) findViewById(R.id.pay);
        mTvTotal = (TextView) findViewById(R.id.total_count);
        mTvSelectAddress.setOnClickListener(this);
        mTvSelectMethod.setOnClickListener(this);
        mTvPay.setOnClickListener(this);
        addItemToLayout();
    }

    private void addItemToLayout() {
        if(dataSource == null || ListUtils.isEmpty(dataSource.cartItems))
            return;
        for (CartItem i : dataSource.cartItems) {
            View view = LayoutInflater.from(this).inflate(R.layout.item_cart, null);
            TextView tvName = (TextView) view.findViewById(R.id.name);
            tvName.setText(i.getName());
            TextView tvPrice = (TextView) view.findViewById(R.id.price);
            tvPrice.setVisibility(View.VISIBLE);
            tvPrice.setText(String.valueOf(i.getPrice()));
            TextView tvQuality = (TextView) view.findViewById(R.id.tvQuality);
            tvQuality.setVisibility(View.VISIBLE);
            tvQuality.setText(String.valueOf(i.getQuality()));
            view.findViewById(R.id.operation_layout).setVisibility(View.GONE);
            layoutContainer.addView(view);
        }
        for (CartItem i : dataSource.cartItems) {
            View view = LayoutInflater.from(this).inflate(R.layout.item_cart, null);
            TextView tvName = (TextView) view.findViewById(R.id.name);
            tvName.setText(i.getName());
            TextView tvPrice = (TextView) view.findViewById(R.id.price);
            tvPrice.setVisibility(View.VISIBLE);
            tvPrice.setText(String.valueOf(i.getPrice()));
            TextView tvQuality = (TextView) view.findViewById(R.id.tvQuality);
            tvQuality.setVisibility(View.VISIBLE);
            tvQuality.setText(String.valueOf(i.getQuality()));
            view.findViewById(R.id.operation_layout).setVisibility(View.GONE);
            layoutContainer.addView(view);
        }
        for (CartItem i : dataSource.cartItems) {
            View view = LayoutInflater.from(this).inflate(R.layout.item_cart, null);
            TextView tvName = (TextView) view.findViewById(R.id.name);
            tvName.setText(i.getName());
            TextView tvPrice = (TextView) view.findViewById(R.id.price);
            tvPrice.setVisibility(View.VISIBLE);
            tvPrice.setText(String.valueOf(i.getPrice()));
            TextView tvQuality = (TextView) view.findViewById(R.id.tvQuality);
            tvQuality.setVisibility(View.VISIBLE);
            tvQuality.setText(String.valueOf(i.getQuality()));
            view.findViewById(R.id.operation_layout).setVisibility(View.GONE);
            layoutContainer.addView(view);
        }
    }

    public static Intent newIntent(Context context, ID id, CartItemLists cartItemList) {
        Intent intent = new Intent(context, CheckoutUpdateActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_ITEMS,cartItemList);
        intent.putExtra(EXTRA_BUNDLE, bundle);
        intent.putExtra(EXTRA_ID, id);
        return intent;
    }

    @Override
    public void onClick(View v) {
        mPresenter.onClickView(v);
    }

    @Override
    public ID getCheckoutId() {
        return id;
    }
}

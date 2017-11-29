package com.whatsmode.shopify.block.checkout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.shopify.graphql.support.ID;
import com.whatsmode.library.util.ListUtils;
import com.whatsmode.library.util.PreferencesHelper;
import com.whatsmode.library.util.PreferencesUtil;
import com.whatsmode.library.util.ScreenUtils;
import com.whatsmode.library.util.ToastUtil;
import com.whatsmode.shopify.AppNavigator;
import com.whatsmode.shopify.R;
import com.whatsmode.shopify.block.WebActivity;
import com.whatsmode.shopify.block.account.data.AccountManager;
import com.whatsmode.shopify.block.address.Address;
import com.whatsmode.shopify.block.cart.CartItem;
import com.whatsmode.shopify.block.cart.CartItemLists;
import com.whatsmode.shopify.common.Constant;
import com.whatsmode.shopify.mvp.MvpActivity;
import com.whatsmode.shopify.ui.helper.ToolbarHelper;

import java.io.IOException;
import java.util.List;

public class CheckoutUpdateActivity extends MvpActivity<CheckoutUpdateContact.Presenter> implements CheckoutUpdateContact.View, View.OnClickListener {

    private static final String EXTRA_ID = "id";
    private static final String EXTRA_ITEMS = "items";
    private static final String EXTRA_BUNDLE = "bundle";

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
        }
        if (getIntent().hasExtra(EXTRA_BUNDLE)) {
            dataSource = (CartItemLists) getIntent().getBundleExtra(EXTRA_BUNDLE).getSerializable(EXTRA_ITEMS);
        }
        ToolbarHelper.ToolbarHolder toolbarHolder = ToolbarHelper.initToolbarNoFix(this, R.id.toolbar, true, "CHECK OUT");
        toolbarHolder.titleView.setVisibility(View.VISIBLE);
        layoutContainer = (LinearLayout) findViewById(R.id.container);
        addItemToLayout();
    }

    private void addItemToLayout() {
        if(dataSource == null || ListUtils.isEmpty(dataSource.cartItems))
            return;
        TextView tvTitle = new TextView(this);
        try {
            List<CartItem> cartItems = (List<CartItem>) PreferencesUtil.getObject(this, Constant.CART_LOCAL);
            tvTitle.setText("Product(" + (ListUtils.isEmpty(cartItems) ? 0 : cartItems.size()) + ")");
            tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
            tvTitle.setPadding(ScreenUtils.dip2px(this,15),ScreenUtils.dip2px(this,15),ScreenUtils.dip2px(this,15),0);
            tvTitle.setTextColor(Color.BLACK);
            layoutContainer.addView(tvTitle);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        for (CartItem i : dataSource.cartItems) {
            View view = LayoutInflater.from(this).inflate(R.layout.checkout_item_cart, null);
            TextView tvName = (TextView) view.findViewById(R.id.description);
            tvName.setText(i.getName());
            ImageView icon = (ImageView) view.findViewById(R.id.icon);
            Glide.with(this).load(i.getIcon())
                    .asBitmap()
                    .centerCrop()
                    .into(icon);
            TextView tvPrice = (TextView) view.findViewById(R.id.price);
            TextView tvFontSize = (TextView) view.findViewById(R.id.sizeAndColor);
            tvFontSize.setText(i.getSize());
            tvPrice.setText(new StringBuilder(String.valueOf(i.getPrice())).append("USD"));
            TextView tvCount = (TextView) view.findViewById(R.id.checkout_count);
            tvCount.setText(new StringBuilder("x").append(String.valueOf(i.getQuality())));
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
    public ID getCheckoutId() {return id;}

    @Override
    public void showSuccess(String url) {runOnUiThread(() -> {hideLoading();AppNavigator.jumpToWebActivity(CheckoutUpdateActivity.this, WebActivity.STATE_PAY, url);});}

    @Override
    public void showError(String message) {runOnUiThread(() -> {hideLoading();ToastUtil.showToast(message);});}

    @Override
    public void showLoading() {
        runOnUiThread(() -> super.showLoading());
    }

    @Override
    public void hideLoading() {
        runOnUiThread(() -> super.hideLoading());
    }

    @Override
    public Address getAddress() {
        return new Address("", "guiping road", "minhangqu", "Alabama", "Alabama", "", "United States",
                "", "", "z", "ym", "zym", "13333333333", "35201", "");
    }

    @Override
    public void jumpToGiftSelect() {
        AppNavigator.jumpToGiftActivity(this, getCheckoutId());
    }
}

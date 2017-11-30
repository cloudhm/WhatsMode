package com.whatsmode.shopify.block.checkout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.shopify.buy3.Storefront;
import com.shopify.graphql.support.ID;
import com.whatsmode.library.util.ListUtils;
import com.whatsmode.library.util.PreferencesUtil;
import com.whatsmode.library.util.ScreenUtils;
import com.whatsmode.library.util.ToastUtil;
import com.whatsmode.shopify.AppNavigator;
import com.whatsmode.shopify.R;
import com.whatsmode.shopify.block.WebActivity;
import com.whatsmode.shopify.block.account.data.AccountManager;
import com.whatsmode.shopify.block.address.Address;
import com.whatsmode.shopify.block.address.AddressListActivity;
import com.whatsmode.shopify.block.cart.CartItem;
import com.whatsmode.shopify.block.cart.CartItemLists;
import com.whatsmode.shopify.common.Constant;
import com.whatsmode.shopify.common.KeyConstant;
import com.whatsmode.shopify.mvp.MvpActivity;
import com.whatsmode.shopify.ui.helper.ToolbarHelper;
import com.zchu.log.Logger;

import java.io.IOException;
import java.util.List;

public class CheckoutUpdateActivity extends MvpActivity<CheckoutUpdateContact.Presenter> implements CheckoutUpdateContact.View, View.OnClickListener {

    private static final String EXTRA_ID = "id";
    private static final String EXTRA_ITEMS = "items";
    private static final String EXTRA_BUNDLE = "bundle";

    public ID id;
    private CartItemLists dataSource;
    private LinearLayout layoutContainer;
    private RelativeLayout signInLayout;
    private LinearLayout addAddressLayout;
    private RelativeLayout addressDetailLayout;
    private Address mCurrentAddr;
    private boolean mCreateState;
    public static final int REQUEST_CODE_ADDRESS = 1001;
    private EditText etGiftCard;
    private TextView mTvGiftAmount;
    private TextView mTvGiftUnit;
    private Double mBalance;
    private TextView mTvName;
    private TextView mTvPhone;
    private TextView mTvAddressDetail;
    private TextView mTvShippingMethod;
    private Double shippingCost;
    private TextView mTvShippingCost;
    public String webUrl;

    @NonNull
    @Override
    public CheckoutUpdateContact.Presenter createPresenter() {
        return new CheckoutUpdatePresenter();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_update);

        signInLayout = (RelativeLayout) findViewById(R.id.sign_in);
        findViewById(R.id.sign_in_icon).setOnClickListener(this);

        addAddressLayout = (LinearLayout) findViewById(R.id.add_address);
        findViewById(R.id.iv_add).setOnClickListener(this);

        //findViewById(R.id.shipping_method_layout).setOnClickListener(this);

        mTvName = (TextView) findViewById(R.id.name);
        mTvPhone = (TextView) findViewById(R.id.phone);
        mTvAddressDetail = (TextView) findViewById(R.id.detail_address);
        mTvShippingCost = (TextView) findViewById(R.id.shipping_cost);

        mTvShippingMethod = (TextView) findViewById(R.id.shiping_method);
        findViewById(R.id.check_card).setOnClickListener(this);
        findViewById(R.id.pay).setOnClickListener(this);
        addressDetailLayout = (RelativeLayout) findViewById(R.id.address_detail);
        getParseData();
        ToolbarHelper.ToolbarHolder toolbarHolder = ToolbarHelper.initToolbar(this, R.id.toolbar, true, "CHECK OUT");
        toolbarHolder.titleView.setVisibility(View.VISIBLE);
        layoutContainer = (LinearLayout) findViewById(R.id.container);
        addItemToLayout();

        mTvGiftAmount = (TextView) findViewById(R.id.gift_amount);
        mTvGiftUnit = (TextView) findViewById(R.id.gift_unit);

        etGiftCard = (EditText) findViewById(R.id.gift_card_edit);
        checkSignState();
        mCreateState = true;
    }

    private void getParseData() {
        if (getIntent().hasExtra(EXTRA_ID)) {
            id = (ID) getIntent().getSerializableExtra(EXTRA_ID);
        }
        if (getIntent().hasExtra(EXTRA_BUNDLE)) {
            dataSource = (CartItemLists) getIntent().getBundleExtra(EXTRA_BUNDLE).getSerializable(EXTRA_ITEMS);
        }
    }

    private void checkSignState() {
        if (!mCreateState) {
            return;
        }
        if (AccountManager.isLoginStatus()) {
            signInLayout.setVisibility(View.GONE);
            addAddressLayout.setVisibility(View.VISIBLE);
        } else {
            signInLayout.setVisibility(View.VISIBLE);
            addAddressLayout.setVisibility(View.GONE);
        }
        if (mCurrentAddr == null) {
            addAddressLayout.setVisibility(View.VISIBLE);
            addressDetailLayout.setVisibility(View.GONE);
        } else {
            addAddressLayout.setVisibility(View.GONE);
            addressDetailLayout.setVisibility(View.VISIBLE);
            fillAddress();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_ADDRESS:
                    mCurrentAddr = (Address) data.getSerializableExtra(KeyConstant.KEY_EXTRA_SELECT_ADDRESS);
                    fillAddress();
                    showLoading();
                    mPresenter.bindAddress(id, mCurrentAddr);
                    break;
            }
        }
    }

    private void fillAddress() {
        if (mCurrentAddr == null) {
            return;
        }
        mTvName.setText(mCurrentAddr.getName());
        mTvPhone.setText(mCurrentAddr.getPhone());
        mTvAddressDetail.setText(new StringBuilder()
                .append(mCurrentAddr.getAddress1())
                .append(mCurrentAddr.getAddress2())
                .append(mCurrentAddr.getCity())
                .append(mCurrentAddr.getCountry()));
    }

    private void addItemToLayout() {
        if (dataSource == null || ListUtils.isEmpty(dataSource.cartItems))
            return;
        TextView tvTitle = new TextView(this);
        try {
            List<CartItem> cartItems = (List<CartItem>) PreferencesUtil.getObject(this, Constant.CART_LOCAL);
            tvTitle.setText("Product(" + (ListUtils.isEmpty(cartItems) ? 0 : cartItems.size()) + ")");
            tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            tvTitle.setPadding(ScreenUtils.dip2px(this, 15), ScreenUtils.dip2px(this, 15), ScreenUtils.dip2px(this, 15), 0);
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
            tvFontSize.setText(i.getColorAndSize());
            tvPrice.setText(new StringBuilder("$").append(i.getPrice()));
            TextView tvCount = (TextView) view.findViewById(R.id.checkout_count);
            tvCount.setText(new StringBuilder("x").append(String.valueOf(i.getQuality())));
            layoutContainer.addView(view);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkSignState();
    }

    public static Intent newIntent(Context context, ID id, CartItemLists cartItemList) {
        Intent intent = new Intent(context, CheckoutUpdateActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_ITEMS, cartItemList);
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

    @Override
    public void jumpToPay() {
        if (mCurrentAddr == null) {
            ToastUtil.showToast(getString(R.string.plz_select_address));
        } else if (!TextUtils.isEmpty(webUrl)) {
            AppNavigator.jumpToWebActivity(CheckoutUpdateActivity.this, WebActivity.STATE_PAY, webUrl);
            finish();
        }
    }

    @Override
    public void showError(String message) {
        runOnUiThread(() -> {
            hideLoading();
            ToastUtil.showToast(message);
        });
    }

    @Override
    public void showLoading() {
        runOnUiThread(() -> super.showLoading());
    }

    @Override
    public void hideLoading() {
        runOnUiThread(() -> super.hideLoading());
    }


    @Override
    public void checkGiftCard() {
        String cardNum = etGiftCard.getText().toString();
        if (!TextUtils.isEmpty(cardNum)) {
            showLoading();
            mPresenter.checkGiftCard(cardNum, id);
        }else{
            ToastUtil.showToast(getString(R.string.plz_fill_card_num));
        }
    }

    @Override
    public void jumpToLogin() {
        AppNavigator.jumpToLogin(this);
    }

    @Override
    public void jumpToSelectAddress() {
        Intent intent = new Intent(this, AddressListActivity.class);
        startActivityForResult(intent, REQUEST_CODE_ADDRESS);
    }

    @Override
    public void showGiftCardLegal(String balance) {
        runOnUiThread(() -> {
            hideLoading();
            if (!TextUtils.isEmpty(balance)) {
                mTvGiftAmount.setText(new StringBuilder("-$").append(balance));
                this.mBalance = Double.parseDouble(balance);
                mTvGiftUnit.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void showGiftIllegal(String message) {
        runOnUiThread(() -> {
            hideLoading();
            ToastUtil.showToast(message);
        });
    }

    @Override
    public void onShippingResponse(List<Storefront.ShippingRate> shippingRates, String url) {
        runOnUiThread(() -> {
            hideLoading();
            if (ListUtils.isEmpty(shippingRates)) {
                return;
            }
            webUrl = url;
            mTvShippingMethod.setText(shippingRates.get(0).getTitle());
            shippingCost = shippingRates.get(0).getPrice().doubleValue();
            mTvShippingCost.setText(new StringBuilder("+$").append(String.valueOf(shippingCost)));
        });
    }
}

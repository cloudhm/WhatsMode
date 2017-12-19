package com.whatsmode.shopify.block.checkout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.shopify.buy3.Storefront;
import com.shopify.graphql.support.ID;
import com.whatsmode.library.rx.Util;
import com.whatsmode.library.util.ListUtils;
import com.whatsmode.library.util.PreferencesUtil;
import com.whatsmode.library.util.ScreenUtils;
import com.whatsmode.library.util.ToastUtil;
import com.whatsmode.shopify.AppNavigator;
import com.whatsmode.shopify.R;
import com.whatsmode.shopify.actionlog.ActionLog;
import com.whatsmode.shopify.block.WebActivity;
import com.whatsmode.shopify.block.account.data.AccountManager;
import com.whatsmode.shopify.block.address.AddEditAddressActivity;
import com.whatsmode.shopify.block.address.Address;
import com.whatsmode.shopify.block.address.AddressDefaultSuccess;
import com.whatsmode.shopify.block.address.AddressListActivity;
import com.whatsmode.shopify.block.cart.CartItem;
import com.whatsmode.shopify.block.cart.CartItemLists;
import com.whatsmode.shopify.block.me.Order;
import com.whatsmode.shopify.common.Constant;
import com.whatsmode.shopify.common.KeyConstant;
import com.whatsmode.shopify.mvp.MvpActivity;
import com.whatsmode.shopify.ui.helper.ToolbarHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;


public class CheckoutUpdateActivity extends MvpActivity<CheckoutUpdateContact.Presenter> implements CheckoutUpdateContact.View, View.OnClickListener {

    private static final String EXTRA_ID = "id";
    private static final String EXTRA_ITEMS = "items";
    private static final String EXTRA_BUNDLE = "bundle";
    private static final String EXTRA_PRICE = "price";

    public ID id;
    private CartItemLists dataSource;
    private LinearLayout layoutContainer;
    private RelativeLayout signInLayout;
    private LinearLayout addAddressLayout;
    private RelativeLayout addressDetailLayout;
    private Address currentAddress;
    private boolean mCreateState;
    public static final int REQUEST_CODE_ADDRESS = 1001;
    private EditText etGiftCard;
    private TextView mTvGiftAmount;
    private TextView mTvGiftUnit;
    private TextView mTvName;
    private TextView mTvPhone;
    private TextView mTvAddressDetail;
    private TextView mTvShippingMethod;
    private Double shippingCost;
    private TextView mTvShippingCost;
    public String webUrl;
    private TextView mTvTax;
    private Double totalPrice;
    private TextView mTvTotal;
    private Double taxCost;
    private LinearLayout mLayoutCard;
    private TextView mTvSubTotal;
    private boolean waitingReply;
    private TextView btnPay;
    private boolean addressValid;

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

        findViewById(R.id.shipping_method_layout).setOnClickListener(this);

        mTvName = (TextView) findViewById(R.id.name);
        mTvPhone = (TextView) findViewById(R.id.phone);
        mTvAddressDetail = (TextView) findViewById(R.id.detail_address);
        mTvTotal = (TextView) findViewById(R.id.total_count);
        mTvShippingCost = (TextView) findViewById(R.id.shipping_cost);
        mTvTax = (TextView) findViewById(R.id.taxes);
        mTvSubTotal = (TextView) findViewById(R.id.subtotal);

        mTvShippingMethod = (TextView) findViewById(R.id.shipping_method);
        findViewById(R.id.check_card).setOnClickListener(this);
        btnPay = (TextView) findViewById(R.id.pay);
        btnPay.setOnClickListener(this);
        addressDetailLayout = (RelativeLayout) findViewById(R.id.address_detail);
        mLayoutCard = (LinearLayout) findViewById(R.id.cardLayout);
        addressDetailLayout.setOnClickListener(this);
        findViewById(R.id.edit).setOnClickListener(this);
        getParseData();
        ToolbarHelper.ToolbarHolder toolbarHolder = ToolbarHelper.initToolbar(this, R.id.toolbar, true, "CHECK OUT");
        toolbarHolder.titleView.setVisibility(View.VISIBLE);
        layoutContainer = (LinearLayout) findViewById(R.id.container);
        addItemToLayout();

        mTvGiftAmount = (TextView) findViewById(R.id.gift_amount);
        mTvGiftUnit = (TextView) findViewById(R.id.gift_unit);

        etGiftCard = (EditText) findViewById(R.id.gift_card_edit);
        mCreateState = true;
        if (AccountManager.getCustomerDefaultAddress() != null) {
            currentAddress = AccountManager.getCustomerDefaultAddress();
            //fillAddress();
            showLoading();
            mPresenter.bindAddress(id, currentAddress,true);
        }
        checkSignState();
        ActionLog.onEvent(Constant.Event.CHECK_OUT);
        EventBus.getDefault().register(this);
    }

    private void getParseData() {
        if (getIntent().hasExtra(EXTRA_ID)) {
            id = (ID) getIntent().getSerializableExtra(EXTRA_ID);
        }
        if (getIntent().hasExtra(EXTRA_BUNDLE)) {
            dataSource = (CartItemLists) getIntent().getBundleExtra(EXTRA_BUNDLE).getSerializable(EXTRA_ITEMS);
        }
        if (getIntent().hasExtra(EXTRA_PRICE)) {
            totalPrice = getIntent().getDoubleExtra(EXTRA_PRICE, 0.0);
            mTvTotal.setText(new StringBuilder("$").append(totalPrice));
            mTvSubTotal.setText(new StringBuilder("$").append(totalPrice));
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
        if (!addressValid && currentAddress == null) {
            addAddressLayout.setVisibility(View.VISIBLE);
            addressDetailLayout.setVisibility(View.GONE);
        } else {
            addAddressLayout.setVisibility(View.GONE);
            addressDetailLayout.setVisibility(View.VISIBLE);
            //fillAddress();
        }
    }

    @Subscribe
    public void receive(AddressDefaultSuccess addressDefaultSuccess) {
        currentAddress = AccountManager.getCustomerDefaultAddress();
        showLoading();
        mPresenter.bindAddress(id,currentAddress,true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_ADDRESS:
                    if (data != null) {
                        if (data.hasExtra(KeyConstant.KEY_EXTRA_SELECT_ADDRESS)) {
                            currentAddress = (Address) data.getSerializableExtra(KeyConstant.KEY_EXTRA_SELECT_ADDRESS);
                        } else if (data.hasExtra(KeyConstant.KEY_EXTRA_ADDRESS)) {
                            currentAddress = (Address) data.getSerializableExtra(KeyConstant.KEY_EXTRA_ADDRESS);
                        }
                    }
                    showLoading();
                    mPresenter.bindAddress(id, currentAddress,false);
                    break;
            }
        } else if (resultCode == RESULT_CANCELED) {
            if (AccountManager.getCustomerDefaultAddress() != null) {
                currentAddress = AccountManager.getCustomerDefaultAddress();
                showLoading();
                mPresenter.bindAddress(id, currentAddress,true);
            }
        }
    }

    public void fillAddress() {
        runOnUiThread(() -> {
            if (currentAddress == null) {
                return;
            }
            mTvName.setText(currentAddress.getName());
            mTvPhone.setText(currentAddress.getPhone());
            mTvAddressDetail.setText(new StringBuilder()
                    .append(currentAddress.getAddress1())
                    .append(currentAddress.getAddress2())
                    .append(currentAddress.getCity())
                    .append("  ")
                    .append(currentAddress.getCountry()));
            btnPay.setEnabled(true);
        });
    }

    @Override
    public void jumpToAddressList() {
        Intent intent = new Intent(this, AddressListActivity.class);
        startActivityForResult(intent, REQUEST_CODE_ADDRESS);
    }

    private void addItemToLayout() {
        if (dataSource == null || ListUtils.isEmpty(dataSource.cartItems))
            return;
        TextView tvTitle = new TextView(this);
            int badge = 0;
            if (!ListUtils.isEmpty(dataSource.cartItems)) {
                for (CartItem cartItem : dataSource.cartItems) {
                    badge += cartItem.getQuality();
                }
            }
            tvTitle.setText("Product(" + badge + ")");
            tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            tvTitle.setPadding(ScreenUtils.dip2px(this, 15), ScreenUtils.dip2px(this, 15), ScreenUtils.dip2px(this, 15), 0);
            tvTitle.setTextColor(Color.BLACK);
            layoutContainer.addView(tvTitle);
        for (CartItem i : dataSource.cartItems) {
            View view = LayoutInflater.from(this).inflate(R.layout.checkout_item_cart, null);
            TextView tvName = (TextView) view.findViewById(R.id.description);
            tvName.setText(i.getName());
            ImageView icon = (ImageView) view.findViewById(R.id.icon);
            Glide.with(this).load(i.getIcon())
                    .asBitmap()
                    .centerCrop()
                    .placeholder(R.drawable.defaut_product)
                    .error(R.drawable.defaut_product)
                    .into(icon);
            TextView tvPrice = (TextView) view.findViewById(R.id.price);
            TextView tvFontSize = (TextView) view.findViewById(R.id.sizeAndColor);
            TextView comparePrice = (TextView) view.findViewById(R.id.comparePrice);
            comparePrice.setText(new StringBuilder("$").append(Util.getFormatDouble(i.getComparePrice())).append("USD"));
            comparePrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            tvFontSize.setText(i.getColorAndSize());
            if (Double.doubleToLongBits(i.getComparePrice()) == Double.doubleToLongBits(i.getPrice())) {
                comparePrice.setVisibility(View.GONE);
            }
            tvPrice.setText(new StringBuilder("$").append(i.getPrice()));
            TextView tvCount = (TextView) view.findViewById(R.id.checkout_count);
            tvCount.setText(new StringBuilder("x").append(String.valueOf(i.getQuality())));
            layoutContainer.addView(view);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (waitingReply) {
            showLoading();
            mPresenter.checkOrderExist(getCheckoutId());
            mPresenter.getAddress(id);
        }
        checkSignState();
    }

    public static Intent newIntent(Context context, Double price,ID id, CartItemLists cartItemList) {
        Intent intent = new Intent(context, CheckoutUpdateActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_ITEMS, cartItemList);
        intent.putExtra(EXTRA_BUNDLE, bundle);
        intent.putExtra(EXTRA_PRICE, price);
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
        if (currentAddress == null) {
            ToastUtil.showToast(getString(R.string.plz_select_address));
        } else if (!TextUtils.isEmpty(webUrl)) {
            waitingReply = true;
            AppNavigator.jumpToWebActivity(CheckoutUpdateActivity.this, WebActivity.STATE_PAY, webUrl);
        }
    }

    private void removeOrderContainerFromCart() {
        try {
            List<CartItem> cartList  = (List<CartItem>) PreferencesUtil.getObject(this, Constant.CART_LOCAL);
            if (!ListUtils.isEmpty(cartList)) {
                //dataSource.cartItems.stream().filter(cartList::contains).forEach(cartList::remove);
                for (CartItem cartItem : dataSource.cartItems) {
                    if (cartList.contains(cartItem)) {
                        cartList.remove(cartItem);
                    }
                }
                PreferencesUtil.putObject(this,Constant.CART_LOCAL,cartList);
                EventBus.getDefault().post(new CartItem());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void jumpToModifyAddress() {
        Intent intent = new Intent(this, AddEditAddressActivity.class);
        intent.putExtra(KeyConstant.KEY_ADD_EDIT_ADDRESS,AddEditAddressActivity.TYPE_EDIT_ADDRESS);
        intent.putExtra(KeyConstant.KEY_ADDRESS, currentAddress);
        startActivityForResult(intent,REQUEST_CODE_ADDRESS);
    }

    @Override
    public void ViewResponseFailed() {
        runOnUiThread(() -> {
            waitingReply = false;
            startActivity(new Intent(CheckoutUpdateActivity.this,CheckoutResponseActivity.class));
        });
    }

    @Override
    public void ViewResponseSuccess(Order order) {
        waitingReply = false;
        removeOrderContainerFromCart();
        Intent intent = new Intent(this, CheckoutResponseActivity.class);
        intent.putExtra(CheckoutResponseActivity.EXTRA_STATUS, true);
        intent.putExtra(CheckoutResponseActivity.EXTRA_ORDER, order);
        startActivity(intent);
        finish();
    }

    @Override
    public void updateAddress(Storefront.MailingAddress address, Storefront.ShippingRate shippingRate) {
        runOnUiThread(() -> {
            if (currentAddress != null && address != null) {
                currentAddress.setName(address.getName());
                currentAddress.setPhone(address.getPhone());
                currentAddress.setCity(address.getCity());
                currentAddress.setCountry(address.getCountry());
                currentAddress.setAddress1(address.getAddress1());
                currentAddress.setAddress2(address.getAddress2());
                fillAddress();
            }
            if (shippingRate != null) {
                mTvShippingMethod.setText(shippingRate.getTitle());
            }
        });
    }

    @Override
    public void showError(String message) {
        runOnUiThread(() -> {
            addressValid = false;
            checkSignState();
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
        Intent intent = new Intent(this, AddEditAddressActivity.class);
        startActivityForResult(intent, REQUEST_CODE_ADDRESS);
    }
    private Double discount = 0.0;
    @Override
    public void showGiftCardLegal(String balance) {
        runOnUiThread(() -> {
            if (!TextUtils.isEmpty(balance)) {
                this.discount = totalPrice - Double.parseDouble(balance);
                mTvGiftAmount.setText(new StringBuilder("-$").append(Util.getFormatDouble(discount)));
                mTvGiftUnit.setVisibility(View.VISIBLE);
                mPresenter.checkShippingMethods(getCheckoutId(),false);
            }else{
                hideLoading();
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
    public void onShippingResponse(Double tax,List<Storefront.ShippingRate> shippingRates, String url) {
        runOnUiThread(() -> {
            hideLoading();
            addressValid = true;
            if (ListUtils.isEmpty(shippingRates)) {
                return;
            }
            webUrl = url;
            this.taxCost = tax;
            mTvShippingMethod.setText(shippingRates.get(0).getTitle());
            shippingCost = shippingRates.get(0).getPrice().doubleValue();
            mTvTax.setText(new StringBuilder("+$").append(tax));
            mTvShippingCost.setText(new StringBuilder("+$").append(String.valueOf(shippingCost)));
            mTvTotal.setText(new StringBuilder("$").append(Util.getFormatDouble(totalPrice + shippingCost + tax)));
            mLayoutCard.setVisibility(View.VISIBLE);
            mTvTotal.setText(new StringBuilder("$").append(Util.getFormatDouble(totalPrice + shippingCost + taxCost - discount)));
            checkSignState();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}

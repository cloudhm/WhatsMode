package com.whatsmode.shopify.block.checkout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.shopify.graphql.support.ID;
import com.whatsmode.library.util.ToastUtil;
import com.whatsmode.shopify.R;
import com.whatsmode.shopify.base.BaseActivity;
import com.whatsmode.shopify.block.cart.CartRepository;
import com.whatsmode.shopify.ui.helper.ToolbarHelper;

/**
 * Created by Administrator on 2017/11/25.
 */

public class CheckGiftCardActivity extends BaseActivity implements View.OnClickListener {

    private EditText mEtGiftNumber;
    private ID checkoutId;
    private static final String EXTRA_CHECKOUT = "checkoutId";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_gift_card);
        if (getIntent().hasExtra(EXTRA_CHECKOUT)) {
            checkoutId = (ID) getIntent().getSerializableExtra(EXTRA_CHECKOUT);
        }
        ToolbarHelper.initToolbarNoFix(this, R.id.toolbar, true, getString(R.string.gift_card));
        Button mBtnApply = (Button) findViewById(R.id.check);
        mEtGiftNumber = (EditText) findViewById(R.id.cardEdit);
        mBtnApply.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (TextUtils.isEmpty(mEtGiftNumber.getText().toString()) || TextUtils.isEmpty(checkoutId.toString())) {
            ToastUtil.showToast(R.string.plz_fill_card_num);
        }else{
            CartRepository.create().checkout(checkoutId.toString(), mEtGiftNumber.getText().toString(), new CartRepository.GiftCheckListener() {
                @Override
                public void exist(String balance) {
                    runOnUiThread(() -> ToastUtil.showToast(balance));
                }

                @Override
                public void illegal(String message) {
                    runOnUiThread(() -> ToastUtil.showToast(message));
                }
            });
        }
    }

    public static Intent newIntent(Activity activity, ID checkoutId) {
        Intent intent = new Intent(activity, CheckGiftCardActivity.class);
        intent.putExtra(EXTRA_CHECKOUT, checkoutId);
        return intent;
    }
}

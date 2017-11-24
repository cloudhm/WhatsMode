package com.whatsmode.shopify.block.checkout;

import android.view.View;

import com.shopify.graphql.support.ID;
import com.whatsmode.shopify.R;
import com.whatsmode.shopify.base.BaseRxPresenter;
import com.whatsmode.shopify.block.cart.CartRepository;

/**
 * Created by Administrator on 2017/11/23.
 */

public class CheckoutUpdatePresenter extends BaseRxPresenter<CheckoutUpdateContact.View> implements CheckoutUpdateContact.Presenter{


    @Override
    public void onClickView(View v) {
        switch (v.getId()) {
            case R.id.pay:
                if (isViewAttached()) {
                    ID checkoutId = getView().getCheckoutId();
                    CartRepository.create().bindUser(checkoutId)
                            .bindAddress();
                }

                break;
        }
    }
}

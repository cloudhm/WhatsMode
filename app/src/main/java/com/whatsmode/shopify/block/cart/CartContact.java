package com.whatsmode.shopify.block.cart;

import com.whatsmode.shopify.base.BaseListContract;

import java.util.List;


public class CartContact{
    interface View extends BaseListContract.View {

        void checkOut();
    }

    interface Presenter extends BaseListContract.Presenter<View> {
        void saveCart(List<CartItem> data);

        void onClickView(android.view.View v);
    }
}

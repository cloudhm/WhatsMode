package com.whatsmode.shopify.block.cart;

import com.shopify.graphql.support.ID;
import com.whatsmode.shopify.base.BaseListContract;

import java.util.List;


public class CartContact{
    interface View extends BaseListContract.View {

        void onCheckSelect(boolean selected, CartItem cartItems);

        void showSuccess(Double price,ID webUrl);

        void showError(String message);

        List<CartItem> getCheckedCartItem();

        void checkTotal();

        void selectAll(boolean selectAll);

        void jumpToDetail(CartItem item);

        void showDeleteDialog();

        void deleteItem(CartItem item);

        void checkSpanner();
    }

    interface Presenter extends BaseListContract.Presenter<View> {
        void saveCart(List<CartItem> data);

        void onClickView(android.view.View v);

        boolean isSelectAll();
    }
}

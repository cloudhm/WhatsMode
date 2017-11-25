package com.whatsmode.shopify.block.checkout;


import com.shopify.graphql.support.ID;
import com.whatsmode.shopify.block.address.Address;
import com.whatsmode.shopify.mvp.MvpPresenter;
import com.whatsmode.shopify.mvp.MvpView;


public class CheckoutUpdateContact {
    interface View extends MvpView{

        ID getCheckoutId();

        void showSuccess(String url);

        void showError(String message);

        void showLoading();

        void hideLoading();

        Address getAddress();
    }

    interface Presenter extends MvpPresenter<View> {

        void onClickView(android.view.View v);
    }
}

package com.whatsmode.shopify.block.checkout;


import com.shopify.graphql.support.ID;
import com.whatsmode.shopify.mvp.MvpPresenter;
import com.whatsmode.shopify.mvp.MvpView;

/**
 * Created by Administrator on 2017/11/23.
 */

public class CheckoutUpdateContact {
    interface View extends MvpView{

        ID getCheckoutId();
    }

    interface Presenter extends MvpPresenter<View> {

        void onClickView(android.view.View v);
    }
}

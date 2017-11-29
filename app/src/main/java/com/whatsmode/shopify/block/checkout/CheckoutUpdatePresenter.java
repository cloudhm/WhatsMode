package com.whatsmode.shopify.block.checkout;

import android.text.TextUtils;
import android.view.View;

import com.shopify.graphql.support.ID;
import com.whatsmode.library.util.ToastUtil;
import com.whatsmode.shopify.R;
import com.whatsmode.shopify.base.BaseRxPresenter;
import com.whatsmode.shopify.block.cart.CartRepository;

public class CheckoutUpdatePresenter extends BaseRxPresenter<CheckoutUpdateContact.View> implements CheckoutUpdateContact.Presenter{

    @Override
    public void onClickView(View v) {
        switch (v.getId()) {
            case R.id.pay:
                if (isViewAttached()) {
                    getView().showLoading();
                    ID checkoutId = getView().getCheckoutId();
                    CartRepository.create().bindUser(checkoutId)
                            .updateCheckoutListener(new CartRepository.UpdateCheckoutListener() {
                                @Override
                                public void onSuccess(String url) {
                                    if (isViewAttached())
                                    getView().showSuccess(url);
                                }

                                @Override
                                public void onError(String message) {
                                    if(isViewAttached())
                                    getView().showError(message);
                                }
                            })
                            .bindAddress(getView().getAddress());
                }

                break;
            case R.id.sign_in_icon:
                if (isViewAttached()) {
                    getView().jumpToLogin();
                }
                break;
            case R.id.iv_add:
                if (isViewAttached()) {
                    getView().jumpToSelectAddress();
                }
                break;
            case R.id.check_card:
                if (isViewAttached()) {
                    getView().checkGiftCard();
                }
                break;
//            case R.id.select_gift:
//                if (isViewAttached()) {
//                    getView().checkGiftCard();
//                }
//                break;
        }
    }

    @Override
    public void checkGiftCard(String cardNum, ID id) {
        if (TextUtils.isEmpty(cardNum) || TextUtils.isEmpty(id.toString())) {
            ToastUtil.showToast(R.string.plz_fill_card_num);
        }else{
            CartRepository.create().checkout(id.toString(),cardNum, new CartRepository.GiftCheckListener() {
                @Override
                public void exist(String balance) {
                    if (isViewAttached()) {
                        getView().showGiftCardLegal(balance);
                    }
                }

                @Override
                public void illegal(String message) {
                    if (isViewAttached()) {
                        getView().showGiftIllegal(message);
                    }
                }
            });
        }
    }

    @Override
    public void checkShippingMethods(ID id) {
        // TODO: 2017/11/29  
    }
}

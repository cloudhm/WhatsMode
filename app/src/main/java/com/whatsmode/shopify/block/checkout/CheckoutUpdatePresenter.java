package com.whatsmode.shopify.block.checkout;

import android.view.View;

import com.shopify.buy3.Storefront;
import com.shopify.graphql.support.ID;
import com.whatsmode.shopify.R;
import com.whatsmode.shopify.base.BaseRxPresenter;
import com.whatsmode.shopify.block.address.Address;
import com.whatsmode.shopify.block.cart.CartRepository;
import com.whatsmode.shopify.block.me.Order;
import com.zchu.log.Logger;

import java.util.List;

public class CheckoutUpdatePresenter extends BaseRxPresenter<CheckoutUpdateContact.View> implements CheckoutUpdateContact.Presenter{

    @Override
    public void onClickView(View v) {
        switch (v.getId()) {
            case R.id.pay:
                if (isViewAttached()) {
                    getView().jumpToPay();
                }
                break;
            case R.id.sign_in_icon:
                if (isViewAttached()) {
                    getView().jumpToLogin();
                }
                break;
            case R.id.iv_add:
            case R.id.address_detail:
                if (isViewAttached()) {
                    getView().jumpToSelectAddress();
                }
                break;
            case R.id.check_card:
                if (isViewAttached()) {
                    getView().checkGiftCard();
                }
                break;
            case R.id.shipping_method_layout:
                break;
            case R.id.edit:
                if (isViewAttached()) {
                    getView().jumpToModifyAddress();
                }
                break;
        }
    }

    @Override
    public void checkGiftCard(String cardNum, ID id) {
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

    @Override
    public void checkShippingMethods(ID id,boolean isDefault) {
        CartRepository.create().bindUser(id)
                .shippingListener(new CartRepository.ShippingListener() {
                    @Override
                    public void onSuccess(Double tax,List<Storefront.ShippingRate> shippingRates,String url) {
                        if (isViewAttached()) {
                            getView().fillAddress();
                            getView().onShippingResponse(tax,shippingRates,url);
                        }
                    }

                    @Override
                    public void onError(String message) {
                        if (isViewAttached()) {
                            if (isDefault) {
                                getView().showError(new StringBuilder("Default Address's").append(message).toString());
                            }else{
                                getView().showError(message);
                            }
                        }
                    }
                })
                .shippingMethods();
    }

    @Override
    public void bindAddress(ID id, Address a,boolean isDefault) {
        CartRepository.create().bindUser(id)
                .updateCheckoutListener(new CartRepository.UpdateCheckoutListener() {
                    @Override
                    public void onSuccess(String url) {
                        if (isViewAttached()) {
                            checkShippingMethods(id,isDefault);
                        }
                    }

                    @Override
                    public void onError(String message) {
                        if (isViewAttached()) {
                            if (isDefault) {
                                getView().showError(new StringBuilder("Default Address's").append(message).toString());
                            }else{
                                getView().showError(message);
                            }
                        }
                    }
                })
                .bindAddress(a);
    }

    @Override
    public void checkOrderExist(ID checkoutId) {
        CartRepository.create().orderListener(new CartRepository.OrderDetailListener() {
            @Override
            public void onSuccess(Order order) {
                if (isViewAttached()) {
                    getView().ViewResponseSuccess(order);
                }
            }

            @Override
            public void onFailure() {
                if (isViewAttached()) {
                    getView().ViewResponseFailed();
                }
            }
        }).checkOrderExist(checkoutId);
    }

    @Override
    public void getAddress(ID id) {
        CartRepository.create().addressUpdateListener((address, shippingRate) -> {
            if (isViewAttached()) {
                getView().updateAddress(address,shippingRate);
            }
        }).checkAddress(id);
    }
}

package com.whatsmode.shopify.block.cart;

import com.whatsmode.shopify.R;
import com.whatsmode.shopify.base.BaseRxPresenter;
import com.whatsmode.shopify.ui.helper.CommonAdapter;
import com.whatsmode.shopify.ui.helper.CommonViewHolder;
import com.zchu.log.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/11/20.
 */

public class CartPresenter extends BaseRxPresenter<CartContact.View> implements CartContact.Presenter{

    @Override
    public void doLoadData(boolean isRefresh) {
        io.reactivex.Observable.create((ObservableOnSubscribe<List<CartItem>>) e -> e.onNext(CartItem.mockItem()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(cartItems1 -> {
                    if (isViewAttached()) {
                        getView().showContent(true);
                        getView().setAdapter(new CommonAdapter<CartItem>(R.layout.item_cart,CartItem.mockItem()) {
                            @Override
                            protected void convert(CommonViewHolder helper, CartItem item) {
                                helper.setText(R.id.name, item.name)
                                        .setText(R.id.quality, String.valueOf(item.quality));
                            }
                        });
                    }
                });
    }

    @Override
    public void doLoadMoreData() {

    }
}

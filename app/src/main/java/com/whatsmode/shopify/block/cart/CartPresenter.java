package com.whatsmode.shopify.block.cart;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.whatsmode.shopify.R;
import com.whatsmode.shopify.base.BaseRxPresenter;
import com.whatsmode.shopify.ui.helper.CommonAdapter;
import com.whatsmode.shopify.ui.helper.CommonViewHolder;

import java.util.List;

import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class CartPresenter extends BaseRxPresenter<CartContact.View> implements CartContact.Presenter{

    @Override
    public void doLoadData(boolean isRefresh) {
        io.reactivex.Observable.create((ObservableOnSubscribe<List<CartItem>>) e -> e.onNext(CartItem.mockItem()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(cartItems1 -> {
                    if (isViewAttached()) {
                        getView().showContent(true);
                        getView().setAdapter(createAdapter(CartItem.mockItem()));
                    }
                });
    }

    private CommonAdapter<CartItem> mAdapter;
    private BaseQuickAdapter createAdapter(List<CartItem> cartItems) {
        mAdapter = new CommonAdapter<CartItem>(R.layout.item_cart,cartItems) {
            @Override
            protected void convert(CommonViewHolder helper, CartItem item) {
                helper.setText(R.id.name, item.name)
                        .setText(R.id.quality, String.valueOf(item.quality));
                TextView tvQuality = helper.getView(R.id.quality);
                helper.getView(R.id.tv_descrease).setOnClickListener(v ->
                        tvQuality.setText(String.valueOf((Integer.parseInt(tvQuality.getText().toString())-1)<0?0
                                :Integer.parseInt(tvQuality.getText().toString())-1)));
                helper.getView(R.id.tv_increase).setOnClickListener(v->
                        tvQuality.setText(String.valueOf(Integer.parseInt(tvQuality.getText().toString())+1)));
            }
        };
        return mAdapter;
    }

    @Override
    public void doLoadMoreData() {
        io.reactivex.Observable.create((ObservableOnSubscribe<List<CartItem>>) e -> e.onNext(CartItem.mockItem()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(cartItems1 -> {
                        if (mAdapter != null && isViewAttached()) {
                            if (mAdapter.getData().size() > 20) {
                                getView().showTheEnd();
                            } else {
                                getView().showContent(false);
                                mAdapter.addData(CartItem.mockItem());
                            }
                        }
                });
    }
}

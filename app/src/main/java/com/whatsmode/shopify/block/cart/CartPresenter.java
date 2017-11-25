package com.whatsmode.shopify.block.cart;

import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.shopify.graphql.support.ID;
import com.whatsmode.library.util.ListUtils;
import com.whatsmode.library.util.PreferencesUtil;
import com.whatsmode.shopify.R;
import com.whatsmode.shopify.WhatsApplication;
import com.whatsmode.shopify.base.BaseRxPresenter;
import com.whatsmode.shopify.block.account.data.AccountManager;
import com.whatsmode.shopify.ui.helper.CommonAdapter;
import com.whatsmode.shopify.ui.helper.CommonViewHolder;
import com.zchu.log.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

class CartPresenter extends BaseRxPresenter<CartContact.View> implements CartContact.Presenter {

    @Override
    public void doLoadData(boolean isRefresh) {
        Observable.create((ObservableOnSubscribe<List<CartItem>>) e -> {
            try {
                List<CartItem> cartItemList = (List<CartItem>) PreferencesUtil.getObject(WhatsApplication.getContext(), AccountManager.getUsername());
                if (ListUtils.isEmpty(cartItemList)) {
                    cartItemList = new ArrayList<>();
                }
                e.onNext(cartItemList);
            } catch (Exception ioe) {
                ioe.printStackTrace();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(cartItems -> {
                    if (isViewAttached()) {
                        getView().setAdapter(createAdapter(cartItems));
                        if (!ListUtils.isEmpty(cartItems)) {
                            getView().showContent(true);
                        } else {
                            getView().showTheEnd();
                        }
                    }
                });
    }

    private CommonAdapter<CartItem> mAdapter;

    private BaseQuickAdapter createAdapter(List<CartItem> cartItems) {
        mAdapter = new CommonAdapter<CartItem>(R.layout.item_cart, cartItems) {
            @Override
            protected void convert(CommonViewHolder helper, CartItem item) {
                helper.setText(R.id.name, item.name)
                        .setText(R.id.quality, String.valueOf(item.quality));
                TextView tvQuality = helper.getView(R.id.quality);
                helper.getView(R.id.tv_descrease).setOnClickListener(v -> {
                    int quality = Math.max(0, Integer.parseInt(tvQuality.getText().toString())- 1);
                    item.quality = quality;
                    tvQuality.setText(String.valueOf(quality));
                    if (isViewAttached()) {
                        getView().checkTotal();
                    }
                });
                View ivCheck = helper.getView(R.id.iv_radio);
                ivCheck.setSelected(selectAll);
                ivCheck.setOnClickListener(v -> {
                    if (isViewAttached()) {
                        ivCheck.setSelected(!ivCheck.isSelected());
                        getView().onCheckSelect(ivCheck.isSelected(), item);
                    }
                });

                helper.getView(R.id.tv_increase).setOnClickListener(v -> {
                    int quality = Integer.parseInt(tvQuality.getText().toString()) + 1;
                    item.quality = quality;
                    tvQuality.setText(String.valueOf(quality));
                    if (isViewAttached()) {
                        getView().checkTotal();
                    }
                });
                helper.itemView.setOnClickListener(v -> {
                    // TODO: 2017/11/21
                });
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
//                        if (mAdapter.getData().size() > 20) {
//                            getView().showTheEnd();
//                        } else {
//                            getView().showContent(false);
//                            mAdapter.addData(CartItem.mockItem());
//                        }
                        getView().showTheEnd();
                    }
                });
    }

    @Override
    public void saveCart(List<CartItem> data) {
        try {
            PreferencesUtil.putObject(WhatsApplication.getContext(), AccountManager.getUsername(), data);
        } catch (IOException e) {
            Logger.e(e);
            e.printStackTrace();
        }
    }

    private boolean selectAll;

    @Override
    public void onClickView(View v) {
        switch (v.getId()) {
            case R.id.checkOut:
                if (isViewAttached()) {
                    checkOut(getView().getCheckedCartItem());
                }
                break;
            case R.id.all_text:
                if (isViewAttached()) {
                    if (mAdapter != null) {
                        selectAll = !selectAll;
                        getView().selectAll(selectAll);
                        mAdapter.notifyDataSetChanged();
                    }
                }
                break;
        }
    }

    private void checkOut(List<CartItem> data) {
        if (!ListUtils.isEmpty(data)) {
            if (isViewAttached()) {
                getView().showLoading(false);
            }
            CartRepository.create().parameter(data).checkoutListener(new CartRepository.QueryListener() {
                @Override
                public void onSuccess(ID id) {
                    // TODO: 2017/11/23  save  checkoutID
                    if (isViewAttached()) {
                        getView().hideLoading();
                        getView().showSuccess(id);
                    }
                }

                @Override
                public void onError(String message) {
                    if (isViewAttached()) {
                        getView().hideLoading();
                        getView().showError(message);
                    }
                }
            }).execute();
        } else {
            getView().showError(WhatsApplication.getContext().getString(R.string.plz_select_products));
        }
    }
}

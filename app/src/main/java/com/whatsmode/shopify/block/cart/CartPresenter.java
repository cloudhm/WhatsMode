package com.whatsmode.shopify.block.cart;

import android.graphics.Paint;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.shopify.graphql.support.ID;
import com.whatsmode.library.util.ListUtils;
import com.whatsmode.library.util.PreferencesUtil;
import com.whatsmode.shopify.R;
import com.whatsmode.shopify.WhatsApplication;
import com.whatsmode.shopify.base.BaseRxPresenter;
import com.whatsmode.shopify.common.Constant;
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
                List<CartItem> cartItemList = (List<CartItem>) PreferencesUtil.getObject(WhatsApplication.getContext(), Constant.CART_LOCAL);
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
                        getView().checkSpanner();
                        getView().clearCheckItems(selectAll);
                    }
                });
    }

    private CommonAdapter<CartItem> mAdapter;

    private BaseQuickAdapter createAdapter(List<CartItem> cartItems) {
        mAdapter = new CommonAdapter<CartItem>(R.layout.item_cart, cartItems) {
            @Override
            protected void convert(CommonViewHolder helper, CartItem item) {
                ImageView ivIcon  = helper.getView(R.id.icon);
                View line = helper.getView(R.id.separator);
                line.setVisibility(helper.getAdapterPosition() == cartItems.size() -1 ? View.GONE:View.VISIBLE);
                Glide.with(WhatsApplication.getContext())
                        .load(item.getIcon())
                        .asBitmap()
                        .centerCrop()
                        .placeholder(R.drawable.defaut_product)
                        .error(R.drawable.defaut_product)
                        .into(ivIcon);
                helper.setText(R.id.description, item.name)
                        .setText(R.id.sizeAndColor,item.getColorAndSize())
                        .setText(R.id.price, new StringBuilder("$").append(String.valueOf(item.getPrice())))
                        .setText(R.id.quality, String.valueOf(item.quality));
                TextView comparePrice = helper.getView(R.id.comparePrice);
                if (Double.doubleToLongBits(item.getPrice()) == Double.doubleToLongBits(item.getComparePrice())) {
                    comparePrice.setVisibility(View.GONE);
                } else {
                    comparePrice.setVisibility(View.VISIBLE);
                    comparePrice.setText(new StringBuilder("$").append(String.valueOf(item.getComparePrice())).append("USD"));
                    comparePrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                }
                TextView tvQuality = helper.getView(R.id.quality);
                helper.getView(R.id.reduce).setOnClickListener(v -> {
                    item.quality = Math.max(1, Integer.parseInt(tvQuality.getText().toString())- 1);
                    tvQuality.setText(String.valueOf(item.quality));
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

                helper.getView(R.id.add).setOnClickListener(v -> {
                    int quality = Integer.parseInt(tvQuality.getText().toString()) + 1;
                    item.quality = quality;
                    tvQuality.setText(String.valueOf(quality));
                    if (isViewAttached()) {
                        getView().checkTotal();
                    }
                });
                helper.itemView.setOnClickListener(v -> {
                    getView().jumpToDetail(item);
                });
                helper.itemView.setOnLongClickListener(v -> {
                    if (isViewAttached()) {
                        getView().deleteItem(item);
                    }
                    return true;
                });
            }
        };
        return mAdapter;
    }

    @Override
    public void doLoadMoreData() {
        io.reactivex.Observable.create((ObservableOnSubscribe<List<CartItem>>)
                e -> e.onNext(CartItem.mockItem()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(cartItems1 -> {
                    if (mAdapter != null && isViewAttached()) {
                        getView().showTheEnd();
                    }
                });
    }

    @Override
    public void saveCart(List<CartItem> data) {
        try {
            PreferencesUtil.putObject(WhatsApplication.getContext(), Constant.CART_LOCAL, data);
        } catch (IOException e) {
            Logger.e(e);
            e.printStackTrace();
        }
    }

    public boolean selectAll;

    @Override
    public void onClickView(View v) {
        switch (v.getId()) {
            case R.id.checkOut:
                if (isViewAttached()) {
                    checkOut(getView().getCheckedCartItem());
                }
                break;
            case R.id.checkOut_select:
                if (isViewAttached()) {
                    if (mAdapter != null) {
                        selectAll = !selectAll;
                        getView().selectAll(selectAll);
                        mAdapter.notifyDataSetChanged();
                    }
                    v.setSelected(!v.isSelected());
                }
                break;
            case R.id.delete:
                getView().showDeleteDialog();
                break;
        }
    }

    @Override
    public boolean isSelectAll() {
        return selectAll;
    }

    @Override
    public void setSelectAll(boolean isSelectAll,boolean isNotify) {
        selectAll = isSelectAll;
        if(isNotify)
        mAdapter.notifyDataSetChanged();
    }

    private void checkOut(List<CartItem> data) {
        if (!ListUtils.isEmpty(data)) {
            if (isViewAttached()) {
                getView().showLoading(false);
            }
            CartRepository.create().parameter(data).checkoutListener(new CartRepository.QueryListener() {
                @Override
                public void onSuccess(Double price,ID id,List<CartItem> response) {
                    if (isViewAttached()) {
                        getView().hideLoading();
                        getView().showSuccess(price,id,response);
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
            if(isViewAttached())
            getView().showError(WhatsApplication.getContext().getString(R.string.plz_select_products));
        }
    }
}

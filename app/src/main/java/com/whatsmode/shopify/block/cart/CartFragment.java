package com.whatsmode.shopify.block.cart;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shopify.graphql.support.ID;
import com.whatsmode.library.rx.RxBus;
import com.whatsmode.library.rx.Util;
import com.whatsmode.library.util.ListUtils;
import com.whatsmode.library.util.ToastUtil;
import com.whatsmode.shopify.AppNavigator;
import com.whatsmode.shopify.R;
import com.whatsmode.shopify.base.BaseListFragment;
import com.whatsmode.shopify.block.WebActivity;

import java.util.ArrayList;
import java.util.List;


public class CartFragment extends BaseListFragment<CartContact.Presenter> implements CartContact.View, View.OnClickListener {

    private TextView tvTotal;
    private AlertDialog alertDialog;
    private Button btnDelete;
    private Button btnCheckout;
    private TextView totalTv;

    public static CartFragment newInstance() {
        return new CartFragment();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RelativeLayout rlSpannerCheck = (RelativeLayout) view.findViewById(R.id.spanner_layout);
        btnCheckout = (Button) view.findViewById(R.id.checkOut);
        ImageView ivCheckAll = (ImageView) view.findViewById(R.id.checkOut_select);
        ivCheckAll.setOnClickListener(this);
        btnCheckout.setOnClickListener(this);
        tvTotal = (TextView) view.findViewById(R.id.total_count);
        totalTv = (TextView) view.findViewById(R.id.total);

        btnDelete = (Button) view.findViewById(R.id.delete);
        btnDelete.setOnClickListener(this);

        rlSpannerCheck.setVisibility(View.VISIBLE);

        btnCheckout.setOnClickListener(this);
        RxBus.getInstance().register(RxRefreshCartList.class);
    }

    @Override
    public void setRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setClipToPadding(false);
        recyclerView.setClipChildren(false);
        recyclerView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
    }

    @NonNull
    @Override
    public CartContact.Presenter createPresenter() {
        return new CartPresenter();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser) {
            if (mPresenter == null) {
                return;
            }
            if (mAdapter != null && !ListUtils.isEmpty(mAdapter.getData())) {
                mPresenter.saveCart(mAdapter.getData());
            }else{
                mPresenter.saveCart(CartItem.mockItem());
            }
        }
    }

    @Override
    public void onClick(View v) {
        mPresenter.onClickView(v);
    }


    private List<CartItem> checkItem = new ArrayList<>();

    @Override
    public void onCheckSelect(boolean selected, CartItem cartItems) {
        Double currentTotal;
        if (tvTotal.getText().toString().contains(getString(R.string.unit))) {
            currentTotal = Double.parseDouble(tvTotal.getText().toString().substring(3));
        }else{
            currentTotal = Double.parseDouble(tvTotal.getText().toString());
        }
        if (selected) {
            checkItem.add(cartItems);
            currentTotal += cartItems.price * cartItems.quality;
        }else{
            checkItem.remove(cartItems);
            currentTotal -= cartItems.price * cartItems.quality;
        }
        tvTotal.setText(new StringBuilder(getString(R.string.unit)).append(Util.getFormatDouble(Math.max(0.0, currentTotal))));
    }

    @Override
    public void showSuccess(ID id) {
        //AppNavigator.jumpToWebActivity(getActivity(), WebActivity.STATE_CHECKOUT,webUrl);
        AppNavigator.jumpToCheckoutUpdateActivity(getActivity(),id,new CartItemLists(checkItem));
    }

    @Override
    public void showError(String message) {
        getActivity().runOnUiThread(() -> ToastUtil.showToast(message));

    }

    @Override
    public List<CartItem> getCheckedCartItem() {
        return checkItem;
    }

    @Override
    public void checkTotal() {
        double total = 0.0;
        for (CartItem cartItem : checkItem) {
            total += cartItem.getPrice() * cartItem.quality;
        }
        tvTotal.setText(new StringBuilder(getString(R.string.unit)).append(Util.getFormatDouble(Math.max(total, 0.0))));
    }

    @Override
    public void selectAll(boolean selectAll) {
        Double total = 0.0;
        if (!ListUtils.isEmpty(mAdapter.getData()) && selectAll) {
            checkItem.clear();
            checkItem.addAll(mAdapter.getData());
            for (CartItem cartItem : checkItem) {
                total += cartItem.getPrice() * cartItem.quality;
            }
        }else{
            checkItem.clear();
        }
        tvTotal.setText(new StringBuilder(getString(R.string.unit)).append(Util.getFormatDouble(Math.max(0.0, total))));
    }

    @Override
    public void jumpToDetail(CartItem item) {
        AppNavigator.jumpToWebActivity(getActivity(),WebActivity.STATE_PRODUCT,item.getUrl());
    }

    @Override
    public void showDeleteDialog() {
        if (!ListUtils.isEmpty(checkItem) && mAdapter != null) {
            if (alertDialog == null)
                alertDialog = new AlertDialog.Builder(getActivity())
                        .setNegativeButton(R.string.cancel, null)
                        .setPositiveButton(R.string.confirm, (dialog, which) -> {
                            mAdapter.getData().removeAll(checkItem);
                            mAdapter.notifyDataSetChanged();
                            mPresenter.saveCart(mAdapter.getData());
                            tvTotal.setText("0.0");
                            checkItem.clear();
                        }).setMessage(R.string.confirm_delete)
                        .setTitle(R.string.delete)
                        .create();
            alertDialog.show();
        }
    }

    @Override
    public void deleteItem(CartItem item) {
        alertDialog = new AlertDialog.Builder(getActivity())
                .setMessage(R.string.confirm_delete)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.confirm, (dialog, which) -> {
                    mAdapter.getData().remove(item);
                    mAdapter.notifyDataSetChanged();
                    mPresenter.saveCart(mAdapter.getData());
                    checkItem.remove(item);
                    checkTotal();
                })
                .setTitle(R.string.delete)
                .create();
        alertDialog.show();
    }


    public void deleteCartItems(String title) {
        if (TextUtils.isEmpty(title)) {
            return;
        }
        if (title.equalsIgnoreCase(getString(R.string.edit))) {
            btnCheckout.setVisibility(View.VISIBLE);
            btnDelete.setVisibility(View.GONE);
            tvTotal.setVisibility(View.VISIBLE);
            totalTv.setVisibility(View.VISIBLE);
        }else{
            btnDelete.setVisibility(View.VISIBLE);
            tvTotal.setVisibility(View.GONE);
            totalTv.setVisibility(View.GONE);
            btnCheckout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RxBus.getInstance().unregisterAll();
    }
}

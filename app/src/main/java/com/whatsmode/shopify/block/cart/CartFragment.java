package com.whatsmode.shopify.block.cart;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.whatsmode.library.util.ListUtils;
import com.whatsmode.shopify.R;
import com.whatsmode.shopify.base.BaseListFragment;
import com.whatsmode.shopify.block.account.data.AccountManager;
import com.zchu.log.Logger;


public class CartFragment extends BaseListFragment<CartContact.Presenter> implements CartContact.View, View.OnClickListener {

    private TextView tvTotal;

    public static CartFragment newInstance() {
        return new CartFragment();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RelativeLayout rlSpanner = (RelativeLayout) view.findViewById(R.id.spanner_layout);
        Button btnCheckout = (Button) view.findViewById(R.id.checkOut);
        tvTotal = (TextView) view.findViewById(R.id.total);
        rlSpanner.setVisibility(View.VISIBLE);
        btnCheckout.setOnClickListener(this);
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
            if (mAdapter != null && !ListUtils.isEmpty(mAdapter.getData()) && !AccountManager.getUsername().isEmpty()) {
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

    @Override
    public void checkOut() {
        Logger.e("====checkOut===");
    }
}

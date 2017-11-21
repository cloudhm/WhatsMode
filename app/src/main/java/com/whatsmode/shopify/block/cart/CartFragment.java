package com.whatsmode.shopify.block.cart;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.whatsmode.library.util.ListUtils;
import com.whatsmode.shopify.base.BaseListFragment;
import com.whatsmode.shopify.block.account.data.AccountManager;


public class CartFragment extends BaseListFragment<CartContact.Presenter> implements CartContact.View {

    public static CartFragment newInstance() {
        return new CartFragment();
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
    public void onPause() {
        super.onPause();
    }
}

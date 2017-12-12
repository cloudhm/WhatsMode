package com.whatsmode.shopify.block.cart;

import android.app.Activity;
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
import com.whatsmode.shopify.block.main.MainActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;


public class CartFragment extends BaseListFragment<CartContact.Presenter> implements CartContact.View, View.OnClickListener {

    private TextView tvTotal,subIndicator;
    private AlertDialog alertDialog;
    private Button btnDelete;
    private Button btnCheckout;
    private TextView totalTv;
    private RelativeLayout rlSpannerCheck;
    private ImageView ivCheckAll;

    public static CartFragment newInstance() {
        return new CartFragment();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rlSpannerCheck = (RelativeLayout) view.findViewById(R.id.spanner_layout);
        btnCheckout = (Button) view.findViewById(R.id.checkOut);
        ivCheckAll = (ImageView) view.findViewById(R.id.checkOut_select);
        ivCheckAll.setOnClickListener(this);
        btnCheckout.setOnClickListener(this);
        tvTotal = (TextView) view.findViewById(R.id.total_count);
        totalTv = (TextView) view.findViewById(R.id.total);
        subIndicator = (TextView) view.findViewById(R.id.subIndicator);
        btnDelete = (Button) view.findViewById(R.id.delete);
        btnDelete.setOnClickListener(this);

        btnCheckout.setOnClickListener(this);
        if(!EventBus.getDefault().isRegistered(this))
        EventBus.getDefault().register(this);
    }

    @Subscribe
    public void receive(CartItem list) {
        if (getActivity() == null) {
            return;
        }
        getActivity().runOnUiThread(() -> {
            mPresenter.doLoadData(true);
            checkSpanner();
            if (mPresenter.isSelectAll()) {
                if (!TextUtils.isEmpty(list.getId())) {
                    onCheckSelect(true,list);
                }
                checkTotal();
            }else{
                showOrHideEdit();
            }
        });
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
            }
        }else{
            showOrHideEdit();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mPresenter != null && mAdapter != null && !ListUtils.isEmpty(mAdapter.getData())) {
            mPresenter.saveCart(mAdapter.getData());
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
            currentTotal = Double.parseDouble(tvTotal.getText().toString().substring(1));
        }else{
            currentTotal = Double.parseDouble(tvTotal.getText().toString());
        }
        if (selected) {
            checkItem.add(cartItems);
            if (checkItem.size() == mAdapter.getData().size()) {
                mPresenter.setSelectAll(true,true);
                ivCheckAll.setSelected(true);
            }
            currentTotal += cartItems.price * cartItems.quality;
        }else{
            if (checkItem.size() == 1) {
                mPresenter.setSelectAll(false,true);
            }else{
                mPresenter.setSelectAll(false,false);
            }
            checkItem.remove(cartItems);
            ivCheckAll.setSelected(false);
            currentTotal -= cartItems.price * cartItems.quality;
        }
        tvTotal.setText(new StringBuilder(getString(R.string.unit)).append(Util.getFormatDouble(Math.max(0.0, currentTotal))));
    }

    @Override
    public void showSuccess(Double price,ID id,List<CartItem> response) {
        AppNavigator.jumpToCheckoutUpdateActivity(getActivity(),price,id,new CartItemLists(response));
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
        int badge = 0;
        int totalQuality = 0;
        for (CartItem cartItem : checkItem) {
            total += cartItem.getPrice() * cartItem.quality;
            totalQuality += cartItem.quality;
        }
        List<CartItem> totalData = mAdapter.getData();
        for (CartItem cartItem : totalData) {
            badge += cartItem.getQuality();
        }
        tvTotal.setText(new StringBuilder(getString(R.string.unit)).append(Util.getFormatDouble(Math.max(total, 0.0))));
        if (totalQuality == 0) {
            btnCheckout.setText(R.string.checkout);
        }else{
            btnCheckout.setText(new StringBuilder(getString(R.string.checkout)).append("(").append(totalQuality).append(")"));
        }
        Activity activity = getActivity();
        if (activity instanceof MainActivity) {
            ((MainActivity)activity).refreshBottomBar(badge);
        }
        showOrHideEdit();
    }

    private void showOrHideEdit() {
        Activity activity = getActivity();
        if (activity instanceof MainActivity) {
            if (mAdapter == null || ListUtils.isEmpty(mAdapter.getData())) {
                ((MainActivity)activity).hideEdit(false);
            }else{
                ((MainActivity)activity).hideEdit(true);
            }
        } else if (activity instanceof CartFromProductActivity) {
            if (mAdapter == null || ListUtils.isEmpty(mAdapter.getData())) {
                ((CartFromProductActivity)activity).hideEdit(false);
            }else{
                ((CartFromProductActivity)activity).hideEdit(true);
            }
        }
    }

    @Override
    public void selectAll(boolean selectAll) {
        Double total = 0.0;
        int mQuality = 0;
        if (!ListUtils.isEmpty(mAdapter.getData()) && selectAll) {
            checkItem.clear();
            checkItem.addAll(mAdapter.getData());
            for (CartItem cartItem : checkItem) {
                total += cartItem.getPrice() * cartItem.quality;
                mQuality += cartItem.quality;
            }
        }else{
            checkItem.clear();
        }
        if (mQuality == 0) {
            btnCheckout.setText(R.string.checkout);
        }else{
            btnCheckout.setText(new StringBuilder(getString(R.string.checkout)).append("(").append(mQuality).append(")"));
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
            mAdapter.getData().removeAll(checkItem);
            mAdapter.notifyDataSetChanged();
            mPresenter.saveCart(mAdapter.getData());
            tvTotal.setText("0.0");
            checkItem.clear();
            checkSpanner();
            EventBus.getDefault().post(new CartItem());
        }else{
            ToastUtil.showToast(getString(R.string.plz_select_products));
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
                    CartItem temp = null;
                    for (CartItem cartItem : checkItem) {
                        if (cartItem.getId().equals(item.getId())) {
                            temp =  cartItem;
                        }
                    }
                    checkItem.remove(temp);
                    checkTotal();
                    checkSpanner();
                    EventBus.getDefault().post(new CartItem());
                })
                .setTitle(R.string.delete)
                .create();
        alertDialog.show();
    }

    @Override
    public void checkSpanner() {
        if (getActivity() == null) {
            return;
        }
        getActivity().runOnUiThread(() -> {
            if (mAdapter != null && !ListUtils.isEmpty(mAdapter.getData())) {
                rlSpannerCheck.setVisibility(View.VISIBLE);
            }else{
                rlSpannerCheck.setVisibility(View.GONE);
            }
            Activity activity = getActivity();
            if (activity instanceof MainActivity) {
                ((MainActivity)activity).defineCartTitle();
            }else if(activity instanceof CartFromProductActivity){
                ((CartFromProductActivity) activity).defineCartTitle();
            }
            showOrHideEdit();
        });
    }

    @Override
    public void clearCheckItems(boolean selectAll) {
        if (checkItem != null) {
            checkItem.clear();
            if (selectAll) {
                checkItem.addAll(mAdapter.getData());
            }
            checkTotal();
        }
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
            subIndicator.setVisibility(View.VISIBLE);
        }else{
            btnDelete.setVisibility(View.VISIBLE);
            tvTotal.setVisibility(View.GONE);
            totalTv.setVisibility(View.GONE);
            btnCheckout.setVisibility(View.GONE);
            subIndicator.setVisibility(View.GONE);
        }
        showOrHideEdit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RxBus.getInstance().unregisterAll();
    }
}

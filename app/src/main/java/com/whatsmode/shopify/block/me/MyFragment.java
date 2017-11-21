package com.whatsmode.shopify.block.me;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.whatsmode.library.util.SnackUtil;
import com.whatsmode.shopify.R;
import com.whatsmode.shopify.block.account.LoginActivity;
import com.whatsmode.shopify.block.address.AddressListActivity;
import com.whatsmode.shopify.block.address.LoadType;
import com.whatsmode.shopify.mvp.MvpFragment;

import java.util.List;

/**
 * Created by tom on 17-11-20.
 */

public class MyFragment extends MvpFragment<MyContract.Presenter> implements MyContract.View, View.OnClickListener {

    public static MyFragment newInstance(){
        MyFragment fragment = new MyFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.avatar).setOnClickListener(this);
        view.findViewById(R.id.view_address).setOnClickListener(this);
        view.findViewById(R.id.order_history).setOnClickListener(this);
    }

    @NonNull
    @Override
    public MyContract.Presenter createPresenter() {
        return new MyPresenter();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_my;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.avatar:
                startActivity(new Intent(getActivity(), LoginActivity.class));
                break;
            case R.id.view_address:
                startActivity(new Intent(getActivity(), AddressListActivity.class));
                break;
            case R.id.order_history:
                mPresenter.refreshOrderList();
                break;
        }
    }

    @Override
    public void showContent(@LoadType.checker int type, @NonNull List<Order> orders) {
        System.out.println("---------------------orders : "+orders);
        SnackUtil.toastShow(getContext(),orders.toString());
    }

    @Override
    public void onError(int code, String msg) {
        SnackUtil.toastShow(getContext(),msg);
    }
}

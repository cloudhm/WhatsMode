package com.whatsmode.shopify.block.me;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.whatsmode.shopify.R;
import com.whatsmode.shopify.block.address.LoadType;
import com.whatsmode.shopify.mvp.MvpFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tom on 17-11-21.
 */

public class OrderListFragment extends MvpFragment<OrderListPresenter> implements OrderListContract.View {

    private OrderListAdapter mOrderListAdapter;
    private List<Order> mList;
    private RecyclerView mRecyclerView;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        initRecycler();
    }

    private void initRecycler() {
        mList = new ArrayList<>();
        mList.add(null);
        mList.add(null);
        mList.add(null);
        mOrderListAdapter = new OrderListAdapter(
                R.layout.item_order_item, mList
        );
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mOrderListAdapter);
    }

    @NonNull
    @Override
    public OrderListPresenter createPresenter() {
        return new OrderListPresenter();
    }

    @Override
    public void showContent(@LoadType.checker int type, @NonNull List<Order> orders) {

    }

    @Override
    public void onError(int code, String msg) {

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_order_list;
    }
}

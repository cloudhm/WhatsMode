package com.whatsmode.shopify.block.me;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.whatsmode.library.util.SnackUtil;
import com.whatsmode.shopify.R;
import com.whatsmode.shopify.block.address.Address;
import com.whatsmode.shopify.block.address.LoadType;
import com.whatsmode.shopify.mvp.MvpFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tom on 17-11-21.
 */

public class OrderListFragment extends MvpFragment<OrderListPresenter> implements OrderListContract.View, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemClickListener {

    private OrderListAdapter mOrderListAdapter;
    private List<Order> mList;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipe;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mSwipe = (SwipeRefreshLayout) view.findViewById(R.id.swipe);
        initRecycler();
        mPresenter.refreshOrderList();
    }

    private void initRecycler() {
        mList = new ArrayList<>();
        mOrderListAdapter = new OrderListAdapter(
                R.layout.item_order_item, mList
        );
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mOrderListAdapter);
        mOrderListAdapter.setOnLoadMoreListener(this);
        mOrderListAdapter.setOnItemClickListener(this);
        mSwipe.setOnRefreshListener(this);
    }

    @NonNull
    @Override
    public OrderListPresenter createPresenter() {
        return new OrderListPresenter();
    }

    public void completeRefresh(){
        if (mSwipe != null && mSwipe.isRefreshing()) {
            mSwipe.setRefreshing(false);
        }
    }

    @Override
    public void showContent(@LoadType.checker int type, @NonNull List<Order> orders) {
        if(mOrderListAdapter == null) return;
        completeRefresh();
        switch (type) {
            case LoadType.TYPE_REFRESH_SUCCESS:
                if (orders.isEmpty()) {
                    SnackUtil.toastShow(getActivity(),"order list is empty");
                    mList.clear();
                    mOrderListAdapter.notifyDataSetChanged();
                    return;
                }
                mOrderListAdapter.refresh(orders);
                break;
            case LoadType.TYPE_LOAD_MORE_SUCCESS:
                mOrderListAdapter.addData(orders);
                break;
        }
        if (Order.sHasNextPage) {
            mOrderListAdapter.loadMoreComplete();
        }else {
            mOrderListAdapter.loadMoreEnd();
        }
    }

    @Override
    public void onError(int code, String msg) {
        completeRefresh();
        SnackUtil.toastShow(getContext(),msg);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_order_list;
    }

    @Override
    public void onRefresh() {
        mPresenter.refreshOrderList();
    }

    @Override
    public void onLoadMoreRequested() {
        if (Order.sHasNextPage) {
            mPresenter.loadMoreOrderList();
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Order order = mList.get(position);
        Intent intent = new Intent(getActivity(),OrderDetailsActivity.class);

        startActivity(intent);
    }
}

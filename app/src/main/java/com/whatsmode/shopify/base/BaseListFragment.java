package com.whatsmode.shopify.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.whatsmode.shopify.R;
import com.whatsmode.shopify.mvp.MvpFragment;
import com.whatsmode.shopify.ui.helper.OnLoadMoreListener;
import com.whatsmode.shopify.ui.helper.OnRefreshListener;
import com.whatsmode.shopify.ui.helper.RecycleViewDivider;
import com.whatsmode.shopify.ui.widget.SwipeToLoadLayout;
import com.zchu.log.Logger;


public abstract  class BaseListFragment<P extends BaseListContract.Presenter> extends MvpFragment<P> implements OnRefreshListener,OnLoadMoreListener, BaseListContract.View {

    private SwipeToLoadLayout mSwipeToLoadLayout;
    private RecyclerView recyclerView;

    @Override
    public int getLayoutId() {
        return R.layout.layout_list;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSwipeToLoadLayout = (SwipeToLoadLayout) view.findViewById(R.id.swipeToLoadLayout);
        mSwipeToLoadLayout.setOnRefreshListener(this);
        mSwipeToLoadLayout.setOnLoadMoreListener(this);
        recyclerView = (RecyclerView) view.findViewById(R.id.swipe_target);
        setRecyclerView(recyclerView);
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new RecycleViewDivider(getActivity(), LinearLayoutManager.VERTICAL));
    }
    @Override
    public void onShow(boolean isFirstShow) {
        super.onShow(isFirstShow);
        if (isFirstShow) {
            mSwipeToLoadLayout.post(() -> mSwipeToLoadLayout.setRefreshing(true));
        }
    }

    @Override
    public void onRefresh() {
        loadData(true);
    }

    @Override
    public void showLoading(boolean isRefresh) {
        mSwipeToLoadLayout.post(() -> {
            mSwipeToLoadLayout.setLoadingMore(false);
            mSwipeToLoadLayout.setRefreshing(isRefresh);
        });
    }

    @Override
    public void showError(String errorMsg, boolean isRefresh) {
//        if (isRefresh) {
//            ToastUtil.showToast(errorMsg);
//        } else {
//            contentView.post(new Runnable() {
//                @Override
//                public void run() {
//                    contentView.setRefreshing(false);
//                }
//            });
//            loadErrorView.makeError();
//        }
    }

    @Override
    public void setAdapter(BaseQuickAdapter adapter) {
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void showContent(boolean isRefresh) {
        mSwipeToLoadLayout.setRefreshing(false);
    }

    private void loadData(boolean isRefresh) {
        getPresenter().doLoadData(isRefresh);
    }

    private void loadMoreData() {
        getPresenter().doLoadMoreData();
    }

    private void setRefreshOrLoadingMore(final boolean isLoadMore, final boolean flag){
        mSwipeToLoadLayout.post(() -> {
            if(isLoadMore) {
                mSwipeToLoadLayout.setLoadingMore(flag);
            }
            else {
                mSwipeToLoadLayout.setRefreshing(flag);
            }
        });
    }

    @Override
    public void showMoreLoading() {

    }

    @Override
    public void showMoreError() {

    }

    @Override
    public void showTheEnd() {

    }

    @Override
    public void showMoreFrom() {

    }

    private boolean hasNext;
    @Override
    public void onLoadMore() {
        if(hasNext){
            loadMoreData();
        }else {
            setRefreshOrLoadingMore(true,false);
        }
    }
}

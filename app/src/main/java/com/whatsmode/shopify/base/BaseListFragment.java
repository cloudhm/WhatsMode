package com.whatsmode.shopify.base;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.whatsmode.library.util.ScreenUtils;
import com.whatsmode.shopify.R;
import com.whatsmode.shopify.WhatsApplication;
import com.whatsmode.shopify.mvp.MvpFragment;
import com.whatsmode.shopify.ui.helper.OnLoadMoreListener;
import com.whatsmode.shopify.ui.helper.OnRefreshListener;
import com.whatsmode.shopify.ui.helper.RecycleViewDivider;
import com.whatsmode.shopify.ui.widget.SwipeToLoadLayout;


public abstract  class BaseListFragment<P extends BaseListContract.Presenter> extends MvpFragment<P> implements OnRefreshListener,OnLoadMoreListener, BaseListContract.View {

    private SwipeToLoadLayout mSwipeToLoadLayout;
    protected RecyclerView recyclerView;
    protected  int pageNum = 0;

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

    protected BaseQuickAdapter mAdapter;
    @Override
    public void setAdapter(BaseQuickAdapter adapter) {
        this.mAdapter = adapter;
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void showContent(boolean isRefresh) {
        hasNext = true;
        mSwipeToLoadLayout.setLoadMoreEnabled(true);
        mSwipeToLoadLayout.setRefreshing(false);
        mSwipeToLoadLayout.setLoadingMore(false);
    }

    private void loadData(boolean isRefresh) {
        if(isRefresh)
        pageNum = 0;
        getPresenter().doLoadData(isRefresh);
    }

    private void loadMoreData() {
        pageNum++;
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
        hasNext = false;
        if (mAdapter != null) {
            TextView textView = new TextView(WhatsApplication.getContext());
            textView.setText(WhatsApplication.getContext().getString(R.string.no_more));
            textView.setGravity(Gravity.CENTER);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
            textView.setPadding(ScreenUtils.dip2px(getActivity(),5)
                                ,ScreenUtils.dip2px(getActivity(),5)
                                ,ScreenUtils.dip2px(getActivity(),5)
                                ,ScreenUtils.dip2px(getActivity(),5));
            textView.setTextColor(Color.BLUE);
            mAdapter.addFooterView(textView);
        }
        mSwipeToLoadLayout.postDelayed(() -> mSwipeToLoadLayout.setLoadMoreEnabled(false),100);
        setRefreshOrLoadingMore(true,false);
    }

    @Override
    public void showMoreFrom() {

    }

    protected boolean hasNext = true;
    @Override
    public void onLoadMore() {
        if(hasNext){
            loadMoreData();
        }
    }
}

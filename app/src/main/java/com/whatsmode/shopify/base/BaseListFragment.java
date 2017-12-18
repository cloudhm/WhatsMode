package com.whatsmode.shopify.base;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.whatsmode.library.util.ScreenUtils;
import com.whatsmode.shopify.AppNavigator;
import com.whatsmode.shopify.R;
import com.whatsmode.shopify.WhatsApplication;
import com.whatsmode.shopify.block.cart.CartFromProductActivity;
import com.whatsmode.shopify.block.cart.JumpMainTab;
import com.whatsmode.shopify.common.Constant;
import com.whatsmode.shopify.mvp.MvpFragment;
import com.whatsmode.shopify.ui.helper.LoadingDialog;
import com.whatsmode.shopify.ui.helper.OnLoadMoreListener;
import com.whatsmode.shopify.ui.helper.OnRefreshListener;
import com.whatsmode.shopify.ui.helper.RecycleViewDivider;
import com.whatsmode.shopify.ui.widget.SwipeToLoadLayout;

import org.greenrobot.eventbus.EventBus;


public abstract class BaseListFragment<P extends BaseListContract.Presenter> extends MvpFragment<P> implements OnRefreshListener, OnLoadMoreListener, BaseListContract.View {

    private SwipeToLoadLayout mSwipeToLoadLayout;
    protected RecyclerView recyclerView;
    protected int pageNum = 0;
    private LoadingDialog mLoadingDialog;

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

    public void setRefreshEnable(boolean enable) {
        mSwipeToLoadLayout.post(() -> {
            mSwipeToLoadLayout.setRefreshing(false);
            mSwipeToLoadLayout.setRefreshEnabled(enable);
        });
    }

    @Override
    public void onRefresh() {
        loadData(true);
    }

    @Override
    public void showLoading(boolean isRefresh) {
        if (isRefresh) {
            mSwipeToLoadLayout.post(() -> {
                mSwipeToLoadLayout.setLoadingMore(false);
                mSwipeToLoadLayout.setRefreshing(isRefresh);
            });
        }else{
            if (mLoadingDialog == null) {
                mLoadingDialog = new LoadingDialog(getActivity());
            }
            mLoadingDialog.show();
        }
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
        if (Constant.CART_FRAGMENT_NAME.equals(this.getClass().getName()) && mAdapter.getEmptyView() == null) {
            View empty = LayoutInflater.from(getActivity()).inflate(R.layout.empty_cart, null);
            mAdapter.setEmptyView(empty);
            empty.findViewById(R.id.shopping).setOnClickListener(v -> {
                if (getActivity() instanceof CartFromProductActivity) {
                    AppNavigator.jumpToMain(getActivity());
                } else {
                    EventBus.getDefault().post(new JumpMainTab(0));
                }
            });
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void hideLoading() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }

    @Override
    public void showContent(boolean isRefresh) {
        hasNext = true;
        mSwipeToLoadLayout.setLoadMoreEnabled(true);
        mSwipeToLoadLayout.setRefreshing(false);
        mSwipeToLoadLayout.setLoadingMore(false);
    }

    private void loadData(boolean isRefresh) {
        if (isRefresh) {
            pageNum = 0;
            if (mAdapter != null) {
                mAdapter.removeAllFooterView();
            }
        }
        getPresenter().doLoadData(isRefresh);
    }

    private void loadMoreData() {
        pageNum++;
        getPresenter().doLoadMoreData();
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
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            textView.setPadding(ScreenUtils.dip2px(getActivity(), 5)
                    , ScreenUtils.dip2px(getActivity(), 5)
                    , ScreenUtils.dip2px(getActivity(), 5)
                    , ScreenUtils.dip2px(getActivity(), 5));
            textView.setTextColor(Color.BLACK);
            mAdapter.addFooterView(textView);
        }
        mSwipeToLoadLayout.postDelayed(() -> mSwipeToLoadLayout.setLoadMoreEnabled(false), 100);
        mSwipeToLoadLayout.setRefreshing(false);
        mSwipeToLoadLayout.setLoadingMore(false);
    }

    @Override
    public void showMoreFrom() {

    }

    protected boolean hasNext = true;

    @Override
    public void onLoadMore() {
        if (hasNext) {
            loadMoreData();
        }
    }
}

//package com.example.administrator.whatshots.base;
//
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v4.content.ContextCompat;
//import android.support.v4.widget.SwipeRefreshLayout;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.example.administrator.whatshots.mvp.MvpFragment;
//
//
//public abstract  class BaseListFragment<P extends BaseListContract.Presenter> extends MvpFragment<P> implements SwipeRefreshLayout.OnRefreshListener, LoadErrorView.OnRetryListener,BaseListContract.View {
//
//    private SwipeRefreshLayout contentView;
//    private RecyclerView recyclerView;
//    private LoadErrorView loadErrorView;
//    private LoadMoreView loadMoreView;
//
//    @Override
//    public int getLayoutId() {
//        return R.layout._layout_list;
//    }
//
//    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        contentView = (SwipeRefreshLayout) view.findViewById(R.id.content_view);
//        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
//        loadErrorView = (LoadErrorView) view.findViewById(R.id.load_error_view);
//        contentView.setColorSchemeColors(ContextCompat.getColor(getActivity(), R.color.red));
//        loadErrorView.setOnRetryListener(this);
//        contentView.setOnRefreshListener(this);
//        recyclerView.addOnScrollListener(new OnLoadMoreScrollListener() {
//            @Override
//            public void onLoadMore(RecyclerView var1) {
//                loadMoreData();
//            }
//        });
//        setRecyclerView(recyclerView);
//    }
//
//    public void setRecyclerView(RecyclerView recyclerView) {
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        recyclerView.addItemDecoration(new RecycleViewDivider(getActivity(), LinearLayoutManager.VERTICAL));
//    }
//    @Override
//    public void onShow(boolean isFirstShow) {
//        super.onShow(isFirstShow);
//        if (isFirstShow) {
//            loadData(false);
//        }
//    }
//
//    @Override
//    public void onRefresh() {
//        loadData(true);
//    }
//
//    @Override
//    public void onRetry(View view) {
//        loadData(false);
//    }
//
//    @Override
//    public void showLoading(boolean isRefresh) {
//        if (isRefresh) {
//            contentView.post(new Runnable() {
//                @Override
//                public void run() {
//                    contentView.setRefreshing(true);
//                }
//            });
//        } else {
//            contentView.setVisibility(View.GONE);
//            loadErrorView.makeLoading();
//        }
//
//    }
//
//    @Override
//    public void showError(String errorMsg, boolean isRefresh) {
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
//
//    }
//
//    @Override
//    public void setAdapter(BaseQuickAdapter adapter) {
//        loadMoreView = new LoadMoreView(getActivity());
//        loadMoreView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtil.dp2px(getContext(), 64)));
//        adapter.addFooterView(loadMoreView);
//        recyclerView.setAdapter(adapter);
//    }
//
//    @Override
//    public void showContent(boolean isRefresh) {
//        if(!isRefresh){
//            InContentAnim inContentAnim = new InContentAnim(contentView, loadErrorView);
//            inContentAnim.start();
//        }
//        contentView.post(new Runnable() {
//            @Override
//            public void run() {
//                contentView.setRefreshing(false);
//            }
//        });
//    }
//
//    @Override
//    public void showMoreLoading() {
//        if (loadMoreView != null)
//            loadMoreView.makeMoreLoading();
//    }
//
//    @Override
//    public void showMoreError() {
//        if (loadMoreView != null)
//            loadMoreView.makeMoreError();
//    }
//
//    @Override
//    public void showTheEnd() {
//        if (loadMoreView != null)
//            loadMoreView.makeTheEnd();
//    }
//
//    @Override
//    public void showMoreFrom() {
//        if (loadMoreView != null)
//            loadMoreView.makeMoreLoading();
//    }
//
//    private void loadData(boolean isRefresh) {
//        getPresenter().doLoadData(isRefresh);
//
//    }
//
//    private void loadMoreData() {
//        getPresenter().doLoadMoreData();
//    }
//
//
//}

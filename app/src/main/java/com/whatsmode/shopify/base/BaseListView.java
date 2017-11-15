package com.whatsmode.shopify.base;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.whatsmode.shopify.mvp.MvpView;

public interface BaseListView<E> extends MvpView {
    /**
     * 显示加载中
     * @param isRefresh 是否为刷新
     */
    void showLoading(boolean isRefresh);

    /**
     * 显示加载出错
     * @param errorMsg 异常信息
     * @param isRefresh 是否为刷新
     */
    void showError(String errorMsg, boolean isRefresh);

    /**
     * 设置adapter
     */
    void setAdapter(BaseQuickAdapter adapter);

    /**
     * 显示内容
     */
    void showContent();


    /**
     * 显示加载更多
     */
    void showMoreLoading();

    /**
     * 显示加载更多出错
     */
    void showMoreError();

    /**
     * 显示没有更多内容
     */
    void showTheEnd();


    /**
     * 设置回最初状态，显示加载的数据
     */
    void showMoreFrom();

}

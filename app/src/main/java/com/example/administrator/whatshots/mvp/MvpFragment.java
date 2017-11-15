package com.example.administrator.whatshots.mvp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.example.administrator.whatshots.base.BaseFragment;


public abstract class MvpFragment<P extends MvpPresenter> extends BaseFragment
        implements MvpView {
    protected P mPresenter;


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter = createPresenter();
        mPresenter.attachView(this);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }

    @NonNull
    public abstract P createPresenter();

    @NonNull
    public P getPresenter() {
        return mPresenter;
    }
}

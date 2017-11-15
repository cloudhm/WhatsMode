package com.whatsmode.shopify.base;


import com.whatsmode.shopify.mvp.MvpBasePresenter;
import com.whatsmode.shopify.mvp.MvpView;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class BaseRxPresenter<V extends MvpView> extends MvpBasePresenter<V> {

    protected CompositeDisposable mCompositeDisposable;
    @Override
    public void attachView(V view) {
        super.attachView(view);
    }

    @Override
    public void detachView() {
        super.detachView();
        //与View断开联系时 ,取消注册RxJava 防止内存溢出
        unSubscribe();
        mCompositeDisposable = null;
    }


    protected void unSubscribe(){
        if (mCompositeDisposable != null) {
            if (!mCompositeDisposable.isDisposed()) {
                mCompositeDisposable.dispose();
            }
            mCompositeDisposable.clear();
        }
    }

    public boolean addSubscribe(Disposable disposable) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }

        return mCompositeDisposable.add(disposable);
    }

    /*protected <U> void addRxBusSubscribe(Class<U> eventType, Consumer<U> act) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(RxBus.getInstance().toDefalutFlowable(eventType, act));
    }*/
}

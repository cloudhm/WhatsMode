package com.example.administrator.whatshots.mvp.loader;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.example.administrator.whatshots.mvp.MvpPresenter;
import com.example.administrator.whatshots.mvp.MvpView;

public abstract class MvpLoaderActivity<P extends MvpPresenter> extends BaseActivity implements MvpView, LoaderManager.LoaderCallbacks<P> {
    protected P mPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //传入一个id，需要保证在一个Activity/Fragment内单一即可，不需要全局单一。这个id就是用来识别Loader的
        getSupportLoaderManager().initLoader(1, null, this);
    }

    public void onAttachPresenter() {}

    @Override
    protected void onDestroy() {
        super.onDestroy();
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


    @Override
    public Loader<P> onCreateLoader(int i, Bundle bundle) {
        return new PresenterLoader<P>(this) {
            @Override
            P create() {
                return createPresenter();
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<P> loader, P p) {
        if (mPresenter == null) {
            this.mPresenter = p;
            mPresenter.attachView(this);
            onAttachPresenter();
        }
    }

    @Override
    public void onLoaderReset(Loader<P> loader) {
        mPresenter.detachView();
    }
}

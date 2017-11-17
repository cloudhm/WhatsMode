package com.whatsmode.shopify.block.account;

import com.shopify.buy3.Storefront;
import com.whatsmode.shopify.WhatsApplication;
import com.whatsmode.shopify.base.BaseRxPresenter;
import com.whatsmode.shopify.block.account.data.AccountManager;
import com.whatsmode.shopify.mvp.MvpBasePresenter;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Created by tom on 17-11-16.
 */

public class LoginPresenter extends BaseRxPresenter<LoginContract.View> implements LoginContract.Presenter {

    @Override
    public void login(String email, String pwd) {
        Storefront.CustomerAccessTokenCreateInput input = new Storefront.CustomerAccessTokenCreateInput(email,pwd);

        SingleObserver<Storefront.CustomerAccessToken> singleObserver = AccountRepository.create().login(input, _queryBuilder ->
                _queryBuilder.customerAccessToken(
                        _queryBuilder1 -> _queryBuilder1.accessToken().expiresAt())
                        .userErrors(_queryBuilder2 -> _queryBuilder2.message().field()))
                .map(t -> {
                    AccountManager.getInstance().writeCustomerAccessToken(t != null ? t.getAccessToken() : null);
                    return t;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new SingleObserver<Storefront.CustomerAccessToken>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        addSubscribe(d);
                    }

                    @Override
                    public void onSuccess(@NonNull Storefront.CustomerAccessToken customerAccessToken) {
                        String accessToken = null;
                        if (customerAccessToken != null) {
                            accessToken = customerAccessToken.getAccessToken();
                        }
                        if (isViewAttached()) {
                            getView().loginSuccess();
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                        if (isViewAttached()) {
                            getView().loginFail(e.getMessage());
                        }
                    }
                });

    }
}

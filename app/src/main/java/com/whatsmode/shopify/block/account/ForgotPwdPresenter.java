package com.whatsmode.shopify.block.account;

import com.shopify.buy3.Storefront;
import com.whatsmode.library.exception.APIException;
import com.whatsmode.shopify.base.BaseRxPresenter;
import com.whatsmode.shopify.block.account.data.AccountManager;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Created by tom on 17-11-20.
 */

public class ForgotPwdPresenter extends BaseRxPresenter<ForgotPwdContract.View> implements ForgotPwdContract.Presenter {
    @Override
    public void recover(String email) {
        AccountRepository.create().recover(email,new RecoverFragment())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new SingleObserver<Storefront.CustomerRecoverPayload>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        addSubscribe(d);
                    }

                    @Override
                    public void onSuccess(@NonNull Storefront.CustomerRecoverPayload customerRecoverPayload) {
                        if (isViewAttached()) {
                            getView().success();
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        if (isViewAttached()) {
                            if (e instanceof APIException) {
                                APIException t = (APIException) e;
                                getView().onError(t.getCode(),t.getMessage());
                            }else{
                                getView().onError(APIException.CODE_COMMON_EXCEPTION,e.getMessage());
                            }
                        }
                    }
                });
    }


    public class RecoverFragment implements Storefront.CustomerRecoverPayloadQueryDefinition {

        @Override
        public void define(Storefront.CustomerRecoverPayloadQuery _queryBuilder) {
            _queryBuilder.userErrors(_queryBuilder1 -> _queryBuilder1.field().message());
        }
    }
}

package com.whatsmode.shopify.block.me;

import android.text.TextUtils;

import com.shopify.buy3.Storefront;
import com.whatsmode.library.exception.APIException;
import com.whatsmode.shopify.R;
import com.whatsmode.shopify.WhatsApplication;
import com.whatsmode.shopify.base.BaseRxPresenter;
import com.whatsmode.shopify.block.account.AccountRepository;
import com.whatsmode.shopify.block.account.data.AccountManager;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Created by tom on 17-11-30.
 */

public class ChangePasswordPresenter extends BaseRxPresenter<ChangePasswordContract.View> implements ChangePasswordContract.Presenter {

    @Override
    public void setPassword(String currentPwd, String newPwd) {
        String username = AccountManager.getUsername();
        if (TextUtils.isEmpty(username)) {
            return;
        }
        Storefront.CustomerAccessTokenCreateInput input = new Storefront.CustomerAccessTokenCreateInput(username,currentPwd);

        AccountRepository.create().login(input, _queryBuilder ->
                _queryBuilder.customerAccessToken(
                        _queryBuilder1 -> _queryBuilder1.accessToken().expiresAt())
                        .userErrors(_queryBuilder2 -> _queryBuilder2.message().field()))
                .flatMap(f -> {
                    Storefront.CustomerUpdateInput inputT = new Storefront.CustomerUpdateInput();
                    if (!TextUtils.isEmpty(newPwd)) inputT.setPassword(newPwd);
                    return MyRepository.create().updateCustomer(AccountManager.getCustomerAccessToken(),inputT,new UpdateCustomerFragment());
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new SingleObserver<Storefront.Customer>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        addSubscribe(d);
                    }

                    @Override
                    public void onSuccess(@NonNull Storefront.Customer customer) {
                        if (isViewAttached()) {
                            getView().updateSuccess();
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        if (isViewAttached()) {
                            String message = e.getMessage();
                            if ("Unidentified customer".equals(message)) {
                                message = WhatsApplication.getContext().getString(R.string.current_password_input_is_mistaken);
                            }
                            if (e instanceof APIException) {
                                APIException t = (APIException) e;
                                getView().onError(t.getCode(),message);
                            }else{
                                getView().onError(APIException.CODE_COMMON_EXCEPTION,message);
                            }
                        }
                    }
                });

    }

    public class UpdateCustomerFragment implements Storefront.CustomerUpdatePayloadQueryDefinition{

        @Override
        public void define(Storefront.CustomerUpdatePayloadQuery _queryBuilder) {
            _queryBuilder.userErrors(e -> e.message().field()).customer(c -> c.id().email().firstName().lastName());
        }
    }
}

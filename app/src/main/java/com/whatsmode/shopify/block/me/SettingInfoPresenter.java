package com.whatsmode.shopify.block.me;

import android.text.TextUtils;

import com.shopify.buy3.Storefront;
import com.whatsmode.library.exception.APIException;
import com.whatsmode.shopify.base.BaseRxPresenter;
import com.whatsmode.shopify.block.account.data.AccountManager;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Created by tom on 17-11-25.
 */

public class SettingInfoPresenter extends BaseRxPresenter<SettingInfoContract.View> implements SettingInfoContract.Presenter {
    @Override
    public void getCustomer() {
        MyRepository.create().getCustomer(AccountManager.getCustomerAccessToken(),new CustomerFragment())
                .map(m -> {
                    Customer customer = new Customer(m.getEmail(),m.getFirstName(),m.getId().toString(),m.getLastName());
                    return customer;
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new SingleObserver<Customer>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        addSubscribe(d);
                    }

                    @Override
                    public void onSuccess(@NonNull Customer customer) {
                        if (isViewAttached()) {
                            getView().showCustomer(customer);
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

    @Override
    public void setCustomer(String firsrName, String lastName, String password) {
        Storefront.CustomerUpdateInput input = new Storefront.CustomerUpdateInput();
        if (!TextUtils.isEmpty(firsrName)) input.setFirstName(firsrName);
        if (!TextUtils.isEmpty(lastName)) input.setLastName(lastName);
        if (!TextUtils.isEmpty(password)) input.setPassword(password);
        MyRepository.create().updateCustomer(AccountManager.getCustomerAccessToken(),input,new UpdateCustomerFragment())
                .observeOn(AndroidSchedulers.mainThread())
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

    @Override
    public void signout() {
        MyRepository.create().signout(AccountManager.getCustomerAccessToken(),new SignoutFragment())
                .map(t -> {
                    AccountManager.getInstance().writeCustomerAccessToken(null);
                    return t;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new SingleObserver<String>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        addSubscribe(d);
                    }

                    @Override
                    public void onSuccess(@NonNull String s) {
                        if (isViewAttached()) {
                            getView().signoutSuccess();
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

    public class CustomerFragment implements Storefront.CustomerQueryDefinition{

        @Override
        public void define(Storefront.CustomerQuery _queryBuilder) {
            _queryBuilder.lastName().firstName().email().id();
        }
    }

    public class UpdateCustomerFragment implements Storefront.CustomerUpdatePayloadQueryDefinition{

        @Override
        public void define(Storefront.CustomerUpdatePayloadQuery _queryBuilder) {
            _queryBuilder.userErrors(e -> e.message().field()).customer(c -> c.id().email().firstName().lastName());
        }
    }

    public class SignoutFragment implements Storefront.CustomerAccessTokenDeletePayloadQueryDefinition{

        @Override
        public void define(Storefront.CustomerAccessTokenDeletePayloadQuery _queryBuilder) {
            _queryBuilder.userErrors(e -> e.field().message())
                    .deletedAccessToken().deletedCustomerAccessTokenId();
        }
    }
}

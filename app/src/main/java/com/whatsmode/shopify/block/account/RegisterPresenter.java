package com.whatsmode.shopify.block.account;

import com.shopify.buy3.Storefront;
import com.shopify.graphql.support.ID;
import com.whatsmode.shopify.base.BaseRxPresenter;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Created by tom on 17-11-16.
 */

public class RegisterPresenter extends BaseRxPresenter<RegisterContract.View> implements RegisterContract.Presenter {

    @Override
    public void register(String email, String pwd, String firstName, String lastName) {
        Storefront.CustomerCreateInput input = new Storefront.CustomerCreateInput(email, pwd)
                .setFirstName(firstName)
                .setLastName(lastName);
        AccountRepository.create().register(input,q ->
                q.customer(new CustomerCreateFragment())
                        .userErrors(new UserErrorFragment()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new SingleObserver<Storefront.Customer>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        addSubscribe(d);
                    }

                    @Override
                    public void onSuccess(@NonNull Storefront.Customer customer) {
                        ID id = customer.getId();
                        if (isViewAttached()) {
                            getView().registerSuccess();
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        if (isViewAttached()) {
                            getView().registerFail(e.getMessage());
                        }
                    }
                });
    }


    public class CustomerCreateFragment implements Storefront.CustomerQueryDefinition{

        @Override
        public void define(Storefront.CustomerQuery _queryBuilder) {
            _queryBuilder.id().firstName().lastName().displayName();
        }
    }
}

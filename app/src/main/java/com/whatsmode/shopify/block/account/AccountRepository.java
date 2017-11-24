package com.whatsmode.shopify.block.account;

import android.support.annotation.NonNull;

import com.shopify.buy3.GraphClient;
import com.shopify.buy3.MutationGraphCall;
import com.shopify.buy3.Storefront;
import com.whatsmode.library.exception.APIException;
import com.whatsmode.library.rx.RxUtil;
import com.whatsmode.library.rx.Util;
import com.whatsmode.shopify.WhatsApplication;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by tom on 17-11-16.
 */

public class AccountRepository {
    private final GraphClient mGraphClient;

    public AccountRepository() {
        mGraphClient = WhatsApplication.getGraphClient();
    }

    public static AccountRepository create(){
        return new AccountRepository();
    }

    public Single<Storefront.CustomerAccessToken> login(Storefront.CustomerAccessTokenCreateInput input,
                                             Storefront.CustomerAccessTokenCreatePayloadQueryDefinition queryDef){
        MutationGraphCall call = mGraphClient.mutateGraph(Storefront.mutation(
                root -> root.customerAccessTokenCreate(input, queryDef)
        ));

        return RxUtil.rxGraphMutationCall(call)
                .map(Storefront.Mutation::getCustomerAccessTokenCreate)
                .flatMap(payload -> {
                    if (payload.getUserErrors().isEmpty()) {
                        return Single.just(payload);
                    } else {
                        return Single.error(new APIException(0,Util.mapItems(payload.getUserErrors(),Storefront.UserError::getMessage)));
                    }
                })
                .map(Storefront.CustomerAccessTokenCreatePayload::getCustomerAccessToken)
                .subscribeOn(Schedulers.io())
                .onErrorResumeNext(e -> Single.error(e));
    }

    public Single<Storefront.Customer> register(Storefront.CustomerCreateInput input,
                                                             Storefront.CustomerCreatePayloadQueryDefinition definition){
        MutationGraphCall call = mGraphClient.mutateGraph(Storefront.mutation(root -> root.customerCreate(input, definition)));

        return RxUtil.rxGraphMutationCall(call)
                .map(Storefront.Mutation::getCustomerCreate)
                .flatMap(it -> {
                    if (it.getUserErrors().isEmpty()) {
                        return Single.just(it);
                    }else{
                        return Single.error(new APIException(0,Util.mapItems(it.getUserErrors(),Storefront.UserError::getMessage)));
                    }
                }).map(Storefront.CustomerCreatePayload::getCustomer)
                .subscribeOn(Schedulers.io());
    }

    public Single<Storefront.CustomerRecoverPayload> recover(String email, Storefront.CustomerRecoverPayloadQueryDefinition queryDef){
        MutationGraphCall call = mGraphClient.mutateGraph(Storefront.mutation(root -> root.customerRecover(email, queryDef)));

        return RxUtil.rxGraphMutationCall(call)
                .map(Storefront.Mutation::getCustomerRecover)
                .flatMap(it -> {
                    if (it.getUserErrors().isEmpty()) {
                        return Single.just(it);
                    }else{
                        return Single.error(new APIException(0,Util.mapItems(it.getUserErrors(),Storefront.UserError::getMessage)));
                    }
                }).subscribeOn(Schedulers.io());
    }

}

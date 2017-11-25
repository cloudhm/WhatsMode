package com.whatsmode.shopify.block.me;

import com.shopify.buy3.GraphClient;
import com.shopify.buy3.MutationGraphCall;
import com.shopify.buy3.QueryGraphCall;
import com.shopify.buy3.Storefront;
import com.whatsmode.library.exception.APIException;
import com.whatsmode.library.rx.RxUtil;
import com.whatsmode.library.rx.Util;
import com.whatsmode.shopify.WhatsApplication;
import com.whatsmode.shopify.block.account.AccountRepository;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by tom on 17-11-21.
 */

public class MyRepository {
    private final GraphClient mGraphClient;

    public MyRepository() {
        mGraphClient = WhatsApplication.getGraphClient();
    }

    public static MyRepository create(){
        return new MyRepository();
    }

    public Single<Storefront.OrderConnection> getOrders(String customerAccessToken, Storefront.CustomerQueryDefinition queryDef){
        QueryGraphCall call = mGraphClient.queryGraph(Storefront.query(q -> q.customer(customerAccessToken, queryDef)));

        return RxUtil.rxGraphQueryCall(call)
                .flatMap(m -> {
                    if(m.getCustomer() == null){
                        return Single.error(new APIException(APIException.CODE_SESSION_EXPIRE,"token expire"));
                    }else{
                        return Single.just(m);
                    }
                }).map(Storefront.QueryRoot::getCustomer)
                .map(Storefront.Customer::getOrders)
                .subscribeOn(Schedulers.io());

    }

    public Single<Storefront.Customer> updateCustomer(String customerAccessToken, Storefront.CustomerUpdateInput customer, Storefront.CustomerUpdatePayloadQueryDefinition queryDef){
        MutationGraphCall call = mGraphClient.mutateGraph(Storefront.mutation(root -> root.customerUpdate(customerAccessToken, customer, queryDef)));

        return RxUtil.rxGraphMutationCall(call)
                .map(Storefront.Mutation::getCustomerUpdate)
                .flatMap(m -> {
                    if (m.getUserErrors().isEmpty()) {
                        return Single.just(m);
                    }else{
                        return Single.error(new APIException(APIException.CODE_COMMON_EXCEPTION, Util.mapItems(m.getUserErrors(),Storefront.UserError::getMessage)));
                    }
                }).map(Storefront.CustomerUpdatePayload::getCustomer)
                .subscribeOn(Schedulers.io());
    }

    public Single<Storefront.Customer> getCustomer(String customerAccessToken, Storefront.CustomerQueryDefinition queryDef){
        QueryGraphCall call = mGraphClient.queryGraph(Storefront.query(q -> q.customer(customerAccessToken, queryDef)));

        return RxUtil.rxGraphQueryCall(call)
                .flatMap(m -> {
                    if(m.getCustomer() == null){
                        return Single.error(new APIException(APIException.CODE_SESSION_EXPIRE,"token expire"));
                    }else{
                        return Single.just(m);
                    }
                }).map(Storefront.QueryRoot::getCustomer)
                .subscribeOn(Schedulers.io());
    }

    public Single<String> signout(String customerAccessToken, Storefront.CustomerAccessTokenDeletePayloadQueryDefinition queryDef){
        MutationGraphCall call = mGraphClient.mutateGraph(Storefront.mutation(root -> root.customerAccessTokenDelete(customerAccessToken, queryDef)));

        return RxUtil.rxGraphMutationCall(call)
                .map(Storefront.Mutation::getCustomerAccessTokenDelete)
                .flatMap(m -> {
                    if (m.getUserErrors().isEmpty()) {
                        return Single.just(m);
                    }else{
                        return Single.error(new APIException(APIException.CODE_COMMON_EXCEPTION, Util.mapItems(m.getUserErrors(),Storefront.UserError::getMessage)));
                    }
                }).map(Storefront.CustomerAccessTokenDeletePayload::getDeletedAccessToken)
                .subscribeOn(Schedulers.io());
    }
}

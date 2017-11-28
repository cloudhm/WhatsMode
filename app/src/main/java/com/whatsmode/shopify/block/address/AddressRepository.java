package com.whatsmode.shopify.block.address;

import com.shopify.buy3.GraphClient;
import com.shopify.buy3.HttpCachePolicy;
import com.shopify.buy3.MutationGraphCall;
import com.shopify.buy3.QueryGraphCall;
import com.shopify.buy3.Storefront;
import com.shopify.graphql.support.ID;
import com.whatsmode.library.exception.APIException;
import com.whatsmode.library.rx.RxUtil;
import com.whatsmode.library.rx.Util;
import com.whatsmode.shopify.WhatsApplication;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by tom on 17-11-16.
 */

public class AddressRepository {
    private final GraphClient mGraphClient;

    public AddressRepository() {
        mGraphClient = WhatsApplication.getGraphClient();
    }

    public static AddressRepository create(){
        return new AddressRepository();
    }

    public Single<Storefront.MailingAddressConnection> getAddressList(String customerAccessToken, Storefront.CustomerQueryDefinition queryDef){
        QueryGraphCall call = mGraphClient.queryGraph(Storefront.query(q ->  q.customer(customerAccessToken, queryDef)))
                .cachePolicy(HttpCachePolicy.NETWORK_FIRST.expireAfter(20, TimeUnit.MINUTES));

        return RxUtil.rxGraphQueryCall(call)
                .flatMap(m -> {
                    if(m.getCustomer() == null){
                        return Single.error(new APIException(APIException.CODE_SESSION_EXPIRE,"token expire"));
                    }else{
                        return Single.just(m);
                    }
                })
                .map(Storefront.QueryRoot::getCustomer)
                .map(Storefront.Customer::getAddresses)
                .subscribeOn(Schedulers.io());
    }

    public Observable<Storefront.Customer> getAddressListObservable(String customerAccessToken, Storefront.CustomerQueryDefinition queryDef){
        QueryGraphCall call = mGraphClient.queryGraph(Storefront.query(q ->  q.customer(customerAccessToken, queryDef)))
                .cachePolicy(HttpCachePolicy.NETWORK_FIRST.expireAfter(20, TimeUnit.MINUTES));

        return RxUtil.rxGraphQueryCallObservable(call)
                .flatMap(m -> {
                    if(m.getCustomer() == null){
                        return Observable.error(new APIException(APIException.CODE_SESSION_EXPIRE,"token expire"));
                    }else{
                        return Observable.just(m);
                    }
                })
                .map(Storefront.QueryRoot::getCustomer)
                //.map(Storefront.Customer::getAddresses)
                .subscribeOn(Schedulers.io());
    }

    public Single<Storefront.MailingAddress> createAddress(Storefront.MutationQueryDefinition definition) {
        MutationGraphCall call = mGraphClient.mutateGraph(Storefront.mutation(definition));

        return RxUtil.rxGraphMutationCall(call)
                .map(Storefront.Mutation::getCustomerAddressCreate)
                .flatMap(t -> {
                    if (t.getUserErrors().isEmpty()) {
                        return Single.just(t);
                    }else{
                        return Single.error(new APIException(APIException.CODE_COMMON_EXCEPTION, Util.mapItems(t.getUserErrors(),Storefront.UserError::getMessage)));
                    }
                }).map(Storefront.CustomerAddressCreatePayload::getCustomerAddress)
                .subscribeOn(Schedulers.io());
    }

    public Single<Storefront.MailingAddress> updateAddress(String customerAccessToken, String id, Storefront.MailingAddressInput address,Storefront.CustomerAddressUpdatePayloadQueryDefinition queryDef){
        MutationGraphCall call = mGraphClient.mutateGraph(Storefront.mutation(root -> root.customerAddressUpdate(customerAccessToken, new ID(id), address, queryDef)));

        return RxUtil.rxGraphMutationCall(call)
                .map(Storefront.Mutation::getCustomerAddressUpdate)
                .flatMap(t -> {
                    if (t.getUserErrors().isEmpty()) {
                        return Single.just(t);
                    }else {
                        return Single.error(new APIException(APIException.CODE_COMMON_EXCEPTION, Util.mapItems(t.getUserErrors(),Storefront.UserError::getMessage)));
                    }
                }).map(Storefront.CustomerAddressUpdatePayload::getCustomerAddress)
                .subscribeOn(Schedulers.io());
    }

    public Single<String> deleteAddress(String id, String customerAccessToken, Storefront.CustomerAddressDeletePayloadQueryDefinition queryDef){
        MutationGraphCall call = mGraphClient.mutateGraph(Storefront.mutation(root -> root.customerAddressDelete(new ID(id), customerAccessToken, queryDef)));

        return RxUtil.rxGraphMutationCall(call)
                .map(Storefront.Mutation::getCustomerAddressDelete)
                .flatMap(t -> {
                    if (t.getUserErrors().isEmpty()) {
                        return Single.just(t);
                    }else{
                        return Single.error(new APIException(APIException.CODE_COMMON_EXCEPTION, Util.mapItems(t.getUserErrors(),Storefront.UserError::getMessage)));
                    }
                }).map(Storefront.CustomerAddressDeletePayload::getDeletedCustomerAddressId)
                .subscribeOn(Schedulers.io());
    }

    public Single<Storefront.Customer> updateDefaultAddress(String customerAccessToken, String addressId, Storefront.CustomerDefaultAddressUpdatePayloadQueryDefinition queryDef){
        MutationGraphCall call = mGraphClient.mutateGraph(Storefront.mutation(root -> root.customerDefaultAddressUpdate(customerAccessToken, new ID(addressId), queryDef)));

        return RxUtil.rxGraphMutationCall(call)
                .map(Storefront.Mutation::getCustomerDefaultAddressUpdate)
                .flatMap(t -> {
                    if (t.getUserErrors().isEmpty()) {
                        return Single.just(t);
                    }else{
                        return Single.error(new APIException(APIException.CODE_COMMON_EXCEPTION, Util.mapItems(t.getUserErrors(),Storefront.UserError::getMessage)));
                    }
                }).map(Storefront.CustomerDefaultAddressUpdatePayload::getCustomer)
                .subscribeOn(Schedulers.io());
    }
}

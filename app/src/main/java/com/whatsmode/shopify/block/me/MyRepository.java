package com.whatsmode.shopify.block.me;

import com.shopify.buy3.GraphClient;
import com.shopify.buy3.QueryGraphCall;
import com.shopify.buy3.Storefront;
import com.whatsmode.library.exception.APIException;
import com.whatsmode.library.rx.RxUtil;
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
}

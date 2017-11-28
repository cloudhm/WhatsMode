package com.whatsmode.shopify.block.me;

import com.shopify.buy3.Storefront;
import com.whatsmode.library.exception.APIException;
import com.whatsmode.shopify.base.BaseRxPresenter;
import com.whatsmode.shopify.block.account.data.AccountManager;
import com.whatsmode.shopify.block.address.Address;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;


/**
 * Created by tom on 17-11-20.
 */

public class MyPresenter extends BaseRxPresenter<MyContract.View> implements MyContract.Presenter {
    @Override
    public void getCustomer() {
        MyRepository.create().getCustomer(AccountManager.getCustomerAccessToken(),new CustomerFragment())
                .map(m -> {
                    Customer customer = new Customer(m.getEmail(),m.getFirstName(),m.getId().toString(),m.getLastName());
                    Storefront.MailingAddress defaultAddress = m.getDefaultAddress();
                    if (defaultAddress != null) {
                        Storefront.MailingAddress node = defaultAddress;
                        Address address = new Address(node.getId().toString(),node.getAddress1(),node.getAddress2(),
                                node.getCity(),node.getProvince(),node.getProvinceCode(),node.getCountry(),node.getCountryCode(),
                                node.getCompany(),node.getFirstName(),node.getLastName(), node.getName(),
                                node.getPhone(),node.getZip(),null);
                        customer.setDefaultAddress(address);
                    }
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

    public class CustomerFragment implements Storefront.CustomerQueryDefinition{

        @Override
        public void define(Storefront.CustomerQuery _queryBuilder) {
            _queryBuilder.lastName().firstName().email().id().defaultAddress(q -> q.address1().address2().city().province().provinceCode().country().countryCode().company().
                    firstName().lastName().name().phone().zip());
        }
    }

}

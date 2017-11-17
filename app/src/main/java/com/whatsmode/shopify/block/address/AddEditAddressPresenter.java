package com.whatsmode.shopify.block.address;

import com.shopify.buy3.Storefront;
import com.whatsmode.library.exception.APIException;
import com.whatsmode.shopify.base.BaseRxPresenter;
import com.whatsmode.shopify.block.account.data.AccountManager;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Created by tom on 17-11-17.
 */

public class AddEditAddressPresenter extends BaseRxPresenter<AddEditAddressContract.View> implements AddEditAddressContract.Presenter {
    @Override
    public void createAddress(Address address) {
        AddressRepository.create().createAddress(d -> d.customerAddressCreate(AccountManager.getCustomerAccessToken(),
                getAddressInput(address),
                c -> c.customerAddress(new AddressQuery()).userErrors(e -> e.message().field())))
                .map(node -> {
                    Address addr = new Address(node.getId().toString(),node.getAddress1(),node.getAddress2(),
                            node.getCity(),node.getProvince(),node.getProvinceCode(),node.getCountry(),node.getCountryCode(),
                            node.getCompany(),node.getFirstName(),node.getLastName(), node.getName(),
                            node.getPhone(),node.getZip(),null);
                    return addr;
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new SingleObserver<Address>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        addSubscribe(d);
                    }

                    @Override
                    public void onSuccess(@NonNull Address address) {
                        if (isViewAttached()) {
                            getView().addAddressSuccess(address);
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
    public void updateAddress(Address address) {
        AddressRepository.create().updateAddress(AccountManager.getCustomerAccessToken(),address.getId(),
                getAddressInput(address), c -> c.customerAddress(new AddressQuery()).userErrors(e -> e.field().message()))
                .map(node -> {
                    Address addr = new Address(node.getId().toString(),node.getAddress1(),node.getAddress2(),
                            node.getCity(),node.getProvince(),node.getProvinceCode(),node.getCountry(),node.getCountryCode(),
                            node.getCompany(),node.getFirstName(),node.getLastName(), node.getName(),
                            node.getPhone(),node.getZip(),null);
                    return addr;
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new SingleObserver<Address>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        addSubscribe(d);
                    }

                    @Override
                    public void onSuccess(@NonNull Address address) {
                        if (isViewAttached()) {
                            getView().updateAddressSuccess(address);
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

    Storefront.MailingAddressInput getAddressInput(Address address){
        return new Storefront.MailingAddressInput()
                .setFirstName(address.getFirstName())
                .setLastName(address.getLastName())
                .setAddress1(address.getAddress1())
                .setAddress2(address.getAddress2())
                .setCity(address.getCity())
                .setProvince(address.getProvince())
                .setCountry(address.getCountry())
                .setZip(address.getZip())
                .setPhone(address.getPhone());

    }

    public class AddressQuery implements Storefront.MailingAddressQueryDefinition{

        @Override
        public void define(Storefront.MailingAddressQuery _queryBuilder) {
            _queryBuilder.address1().address2().city().province().country().firstName().lastName().name().phone().zip();
        }
    }
}

package com.whatsmode.shopify.block.address;

import android.text.TextUtils;

import com.shopify.buy3.Storefront;
import com.whatsmode.library.exception.APIException;
import com.whatsmode.shopify.base.BaseRxPresenter;
import com.whatsmode.shopify.block.account.data.AccountManager;
import com.whatsmode.shopify.common.Constant;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

/**
 * Created by tom on 17-11-16.
 */

public class AddressListPresenter extends BaseRxPresenter<AddressListContract.View> implements AddressListContract.Presenter {

    int pageSize = Constant.PAGE_SIZE_LOAD_DATA;
    String cursor;//last cursor

    @Override
    public void refreshAddressList() {
        AddressQuery query = new AddressQuery(true, pageSize,null);
        addSubscribe(AddressRepository.create().getAddressListObservable(AccountManager.getCustomerAccessToken(),query)
                .map(m -> {
                    Storefront.MailingAddress defaultAddress = m.getDefaultAddress();
                    String defaultId = defaultAddress.getId().toString();
                    Storefront.MailingAddressConnection addresses = m.getAddresses();
                    Address.sHasNextPage = addresses.getPageInfo().getHasNextPage();
                    List<Storefront.MailingAddressEdge> edges = addresses.getEdges();
                    List<Address> addressList = new ArrayList<Address>();
                    if (edges != null && !edges.isEmpty()) {
                        for (Storefront.MailingAddressEdge edge : edges) {
                            Storefront.MailingAddress node = edge.getNode();
                            Address address = new Address(node.getId().toString(),node.getAddress1(),node.getAddress2(),
                                    node.getCity(),node.getProvince(),node.getProvinceCode(),node.getCountry(),node.getCountryCode(),
                                    node.getCompany(),node.getFirstName(),node.getLastName(), node.getName(),
                                    node.getPhone(),node.getZip(),edge.getCursor());
                            if (TextUtils.equals(node.getId().toString(), defaultId)) {
                                address.setDefault(true);
                            }else{
                                address.setDefault(false);
                            }
                            addressList.add(address);
                        }
                    }
                    return addressList;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<Address>>() {
                    @Override
                    public void onNext(List<Address> addresses) {
                        if (isViewAttached()) {
                            if (!addresses.isEmpty()) {
                                cursor = addresses.get(addresses.size() - 1).getCursor();
                            }
                            getView().showContent(LoadType.TYPE_REFRESH_SUCCESS,addresses);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (isViewAttached()) {
                            if (e instanceof APIException) {
                                APIException t = (APIException) e;
                                getView().onError(t.getCode(),t.getMessage());
                            }else{
                                getView().onError(APIException.CODE_COMMON_EXCEPTION,e.getMessage());
                            }
                        }

                    }

                    @Override
                    public void onComplete() {

                    }
                }));

    }

    @Override
    public void loadModeAddressList() {
        if(cursor == null) return;
        AddressQuery query = new AddressQuery(false, pageSize,cursor);
        AddressRepository.create().getAddressList(AccountManager.getCustomerAccessToken(),query)
                .map(m -> {
                    Address.sHasNextPage = m.getPageInfo().getHasNextPage();
                    List<Storefront.MailingAddressEdge> edges = m.getEdges();
                    List<Address> addressList = new ArrayList<Address>();
                    if (edges != null && !edges.isEmpty()) {
                        for (Storefront.MailingAddressEdge edge : edges) {
                            Storefront.MailingAddress node = edge.getNode();
                            Address address = new Address(node.getId().toString(),node.getAddress1(),node.getAddress2(),
                                    node.getCity(),node.getProvince(),node.getProvinceCode(),node.getCountry(),node.getCountryCode(),
                                    node.getCompany(),node.getFirstName(),node.getLastName(), node.getName(),
                                    node.getPhone(),node.getZip(),edge.getCursor());
                            addressList.add(address);
                        }
                    }
                    return addressList;
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new SingleObserver<List<Address>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        addSubscribe(d);
                    }

                    @Override
                    public void onSuccess(@NonNull List<Address> addresses) {
                        if (isViewAttached()) {
                            if (!addresses.isEmpty()) {
                                cursor = addresses.get(addresses.size() - 1).getCursor();
                            }
                            getView().showContent(LoadType.TYPE_LOAD_MORE_SUCCESS,addresses);
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
    public void deleteAddress(String id) {
        if(TextUtils.isEmpty(id)) return;
        AddressRepository.create().deleteAddress(id,AccountManager.getCustomerAccessToken(),c ->
                c.userErrors(e -> e.message().field()).deletedCustomerAddressId())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new SingleObserver<String>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        addSubscribe(d);
                    }

                    @Override
                    public void onSuccess(@NonNull String s) {
                        if (isViewAttached()) {
                            getView().deleteSuccess();
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
    public void updateDefaultAddress(String addressId) {
        AddressRepository.create().updateDefaultAddress(AccountManager.getCustomerAccessToken(),addressId,new DefaultAddressFragment())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new SingleObserver<Storefront.Customer>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        addSubscribe(d);
                    }

                    @Override
                    public void onSuccess(@NonNull Storefront.Customer customer) {
                        if (isViewAttached()) {
                            getView().updateDefaultAddressSuccess();
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

    public class AddressQuery implements Storefront.CustomerQueryDefinition{

        boolean isRefresh;
        int first;
        String cursor;

        public AddressQuery(boolean isRefresh, int first, String cursor) {
            this.isRefresh = isRefresh;
            this.first = first;
            this.cursor = cursor;
        }

        @Override
        public void define(Storefront.CustomerQuery _queryBuilder) {
            _queryBuilder.defaultAddress(d -> d.address1().address2().city().province().provinceCode().country().countryCode().company().
                    firstName().lastName().name().phone().zip())
                    .addresses(a -> {a.first(first); if(!isRefresh) a.after(cursor); },
                    q -> q.edges(qds -> qds.node(qd ->
                            qd.address1().address2().city().province().provinceCode().country().countryCode().company().
                                    firstName().lastName().name().phone().zip()).cursor())
                            .pageInfo(pi -> pi.hasPreviousPage().hasNextPage()));
        }
    }

    public class DefaultAddressFragment implements Storefront.CustomerDefaultAddressUpdatePayloadQueryDefinition{

        @Override
        public void define(Storefront.CustomerDefaultAddressUpdatePayloadQuery _queryBuilder) {
            _queryBuilder.userErrors(e -> e.message().field()).customer(c -> c.id().defaultAddress(q -> q.country()));
        }
    }
}

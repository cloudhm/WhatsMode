package com.whatsmode.shopify.block.me;

import android.text.TextUtils;

import com.shopify.buy3.Storefront;
import com.whatsmode.library.exception.APIException;
import com.whatsmode.shopify.base.BaseRxPresenter;
import com.whatsmode.shopify.block.account.data.AccountManager;
import com.whatsmode.shopify.block.address.Address;
import com.whatsmode.shopify.block.address.LoadType;
import com.whatsmode.shopify.common.Constant;

import java.util.ArrayList;
import java.util.List;

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
                    String defaultId = getDefaultId(defaultAddress);
                    if (defaultAddress != null) {
                        Storefront.MailingAddress node = defaultAddress;
                        Address address = new Address(node.getId().toString(),node.getAddress1(),node.getAddress2(),
                                node.getCity(),node.getProvince(),node.getProvinceCode(),node.getCountry(),node.getCountryCode(),
                                node.getCompany(),node.getFirstName(),node.getLastName(), node.getName(),
                                node.getPhone(),node.getZip(),null);
                        if (TextUtils.equals(node.getId().toString(), defaultId) && !TextUtils.isEmpty(defaultId)) {
                            address.setDefault(true);
                        }else{
                            address.setDefault(false);
                        }
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


    private String getDefaultId(Storefront.MailingAddress defaultAddress){
        if (defaultAddress == null || defaultAddress.getId() == null) {
            return "";
        }
        return defaultAddress.getId().toString();
    }


    //*******************************************

    int pageSize = Constant.PAGE_SIZE_LOAD_DATA;
    String cursor;//last cursor


    @Override
    public void refreshOrderList() {
        MyRepository.create().getOrders(AccountManager.getCustomerAccessToken(),new OrderFragment(true,pageSize,null))
                .map(m -> {
                    Order.sHasNextPage = m.getPageInfo().getHasNextPage();
                    List<Storefront.OrderEdge> edges = m.getEdges();
                    List<Order> orders = new ArrayList<Order>();
                    if (edges != null && !edges.isEmpty()) {
                        for (Storefront.OrderEdge edge : edges) {
                            Storefront.Order node = edge.getNode();
                            List<LineItem> lineItem = getLineItem(node.getLineItems());
                            Address orderAddress = getOrderAddress(node.getShippingAddress());
                            Order order = new Order(node.getCustomerUrl(),node.getEmail(),node.getId().toString(),
                                    node.getOrderNumber(),node.getPhone(),node.getProcessedAt(),orderAddress,node.getSubtotalPrice(),
                                    node.getTotalPrice(),node.getTotalRefunded(),node.getTotalShippingPrice(),
                                    node.getTotalTax(),edge.getCursor(),lineItem);
                            orders.add(order);
                        }
                    }
                    return orders;
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new SingleObserver<List<Order>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        addSubscribe(d);
                    }

                    @Override
                    public void onSuccess(@NonNull List<Order> orders) {
                        if (isViewAttached()) {
                            if (!orders.isEmpty()) {
                                cursor = orders.get(orders.size() - 1).getCursor();
                            }
                            getView().showContent(LoadType.TYPE_REFRESH_SUCCESS,orders);
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
    public void loadMoreOrderList() {
        if(cursor == null) return;
        MyRepository.create().getOrders(AccountManager.getCustomerAccessToken(),new OrderFragment(false,pageSize,cursor))
                .map(m -> {
                    Order.sHasNextPage = m.getPageInfo().getHasNextPage();
                    List<Storefront.OrderEdge> edges = m.getEdges();
                    List<Order> orders = new ArrayList<Order>();
                    if (edges != null && !edges.isEmpty()) {
                        for (Storefront.OrderEdge edge : edges) {
                            Storefront.Order node = edge.getNode();
                            List<LineItem> lineItem = getLineItem(node.getLineItems());
                            Address orderAddress = getOrderAddress(node.getShippingAddress());
                            Order order = new Order(node.getCustomerUrl(),node.getEmail(),node.getId().toString(),
                                    node.getOrderNumber(),node.getPhone(),node.getProcessedAt(),orderAddress,node.getSubtotalPrice(),
                                    node.getTotalPrice(),node.getTotalRefunded(),node.getTotalShippingPrice(),
                                    node.getTotalTax(),edge.getCursor(),lineItem);
                            orders.add(order);
                        }
                    }
                    return orders;
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new SingleObserver<List<Order>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        addSubscribe(d);
                    }

                    @Override
                    public void onSuccess(@NonNull List<Order> orders) {
                        if (isViewAttached()) {
                            if (!orders.isEmpty()) {
                                cursor = orders.get(orders.size() - 1).getCursor();
                            }
                            getView().showContent(LoadType.TYPE_LOAD_MORE_SUCCESS,orders);
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

    private List<LineItem> getLineItem(Storefront.OrderLineItemConnection orderLineItem) {
        List<Storefront.OrderLineItemEdge> edges = orderLineItem.getEdges();
        List<LineItem> items = new ArrayList<>();
        if (edges != null && !edges.isEmpty()) {
            for (Storefront.OrderLineItemEdge edge : edges) {
                LineItem lineItem = new LineItem();
                Storefront.OrderLineItem node = edge.getNode();
                Storefront.ProductVariant variant = node.getVariant();
                if (variant != null) {
                    List<Storefront.SelectedOption> selectedOptions = variant.getSelectedOptions();
                    lineItem.setVariant(new LineItem.Variant(variant.getAvailableForSale(),variant.getTitle(),
                            variant.getSku(),variant.getPrice(),variant.getCompareAtPrice()
                            ,new LineItem.Variant.Image(variant.getImage() == null ? null : variant.getImage().getSrc()),getSelectedOptions(selectedOptions)));
                }
                lineItem.setQuantity(node.getQuantity());
                lineItem.setTitle(node.getTitle());
                LineItem.CustomAttributes customAttributes = lineItem.getCustomAttributes();
                if (customAttributes != null) {
                    lineItem.setCustomAttributes(new LineItem.CustomAttributes(customAttributes.getKey(),customAttributes.getValue()));
                }
                items.add(lineItem);
            }
        }
        return items;
    }

    private List<LineItem.Variant.SelectedOptions> getSelectedOptions(List<Storefront.SelectedOption> selectedOptions){
        List<LineItem.Variant.SelectedOptions> optionses = new ArrayList<>();
        if (selectedOptions != null && !selectedOptions.isEmpty()) {
            for (Storefront.SelectedOption selectedOption : selectedOptions) {
                LineItem.Variant.SelectedOptions options = new LineItem.Variant.SelectedOptions(selectedOption.getName(),selectedOption.getValue());
                optionses.add(options);
            }
        }
        return optionses;
    }

    private Address getOrderAddress(Storefront.MailingAddress node){
        Address address = new Address(node.getId().toString(),node.getAddress1(),node.getAddress2(),
                node.getCity(),node.getProvince(),node.getProvinceCode(),node.getCountry(),node.getCountryCode(),
                node.getCompany(),node.getFirstName(),node.getLastName(), node.getName(),
                node.getPhone(),node.getZip(),null);
        return address;
    }


    public class OrderFragment implements Storefront.CustomerQueryDefinition{

        boolean isRefresh;
        int first;
        String cursor;

        public OrderFragment(boolean isRefresh, int first, String cursor) {
            this.isRefresh = isRefresh;
            this.first = first;
            this.cursor = cursor;
        }

        @Override
        public void define(Storefront.CustomerQuery _queryBuilder) {
            _queryBuilder.orders(args -> {args.first(first);if(!isRefresh) args.after(cursor);},
                    o -> {o.pageInfo(e -> e.hasNextPage().hasPreviousPage())
                            .edges(q -> q.cursor().node(qd -> qd.orderNumber()
                                    .currencyCode().customerLocale().email()
                                    .customerUrl().phone().processedAt()
                                    .subtotalPrice().totalPrice().totalRefunded()
                                    .totalShippingPrice().totalTax()
                                    .lineItems(   //order goods list
                                            args -> args.first(100) ,new OrderLineItemFragment()
                                    ).shippingAddress(new OrderAddressFragment())  //order address
                            ));
                    });
        }
    }

    //order goods list
    class OrderLineItemFragment implements Storefront.OrderLineItemConnectionQueryDefinition{

        @Override
        public void define(Storefront.OrderLineItemConnectionQuery query) {
            query.pageInfo(info -> info.hasPreviousPage().hasNextPage())
                    .edges(queyDef ->  queyDef.cursor()
                            .node(q -> q.variant(qd -> qd.availableForSale().compareAtPrice().title().sku().price().selectedOptions(s -> s.name().value())
                                    .image(args -> args.maxHeight(150).maxWidth(100).crop(Storefront.CropRegion.CENTER), quey -> quey.src()))
                                    .quantity().title().customAttributes(a -> a.value().key())));
        }
    }

    //order address
    class OrderAddressFragment implements Storefront.MailingAddressQueryDefinition{

        @Override
        public void define(Storefront.MailingAddressQuery _queryBuilder) {
            _queryBuilder.address1().address2().city().province().provinceCode().country().countryCode().company().
                    firstName().lastName().name().phone().zip();
        }
    }

}

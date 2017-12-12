package com.whatsmode.shopify.block.cart;

import com.shopify.buy3.GraphCall;
import com.shopify.buy3.GraphClient;
import com.shopify.buy3.GraphError;
import com.shopify.buy3.GraphResponse;
import com.shopify.buy3.Storefront;
import com.shopify.graphql.support.ID;
import com.shopify.graphql.support.Input;
import com.whatsmode.library.util.ListUtils;
import com.whatsmode.library.util.PreferencesUtil;
import com.whatsmode.shopify.R;
import com.whatsmode.shopify.WhatsApplication;
import com.whatsmode.shopify.block.account.data.AccountManager;
import com.whatsmode.shopify.block.address.Address;
import com.whatsmode.shopify.block.me.Order;
import com.whatsmode.shopify.common.Constant;
import com.zchu.log.Logger;

import org.greenrobot.eventbus.EventBus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;


public class CartRepository {

    private GraphClient client;
    private static CartRepository cartRepository;
    private List<CartItem> dataSource;
    private QueryListener mListener;
    private UpdateCheckoutListener updateCheckoutListener;
    private ID checkoutId;
    private ShippingListener shippingListener;
    private OrderDetailListener orderListener;
    private UpdateAddressListener updateAddressListener;
    private CheckExistListener checkExistListener;

    private CartRepository() {
        client = WhatsApplication.getGraphClient();
    }

    public static CartRepository create() {
        if (cartRepository == null) {
            cartRepository = new CartRepository();
        }
        return cartRepository;
    }

    CartRepository parameter(List<CartItem> dataSource) {
        this.dataSource = dataSource;
        return cartRepository;
    }

    CartRepository checkoutListener(QueryListener listener) {
        this.mListener = listener;
        return cartRepository;
    }

    public CartRepository checkExistListener(CheckExistListener listener) {
        this.checkExistListener = listener;
        return cartRepository;
    }

    public CartRepository updateCheckoutListener(UpdateCheckoutListener listener) {
        updateCheckoutListener = listener;
        return cartRepository;
    }

    public CartRepository shippingListener(ShippingListener listener) {
        this.shippingListener = listener;
        return cartRepository;
    }

    public CartRepository bindUser(ID checkoutId) {
        this.checkoutId = checkoutId;
        return cartRepository;
    }

    public CartRepository orderListener(OrderDetailListener listener) {
        this.orderListener = listener;
        return cartRepository;
    }

    public CartRepository addressUpdateListener(UpdateAddressListener listener) {
        this.updateAddressListener = listener;
        return cartRepository;
    }

    public void bindAddress(Address address) {
        Storefront.MailingAddressInput input = new Storefront.MailingAddressInput()
                .setAddress1(address.getAddress1())
                .setAddress2(address.getAddress2())
                .setCity(address.getCity())
                .setCountry(address.getCountry())
                .setFirstName(address.getFirstName())
                .setLastName(address.getLastName())
                .setPhone(address.getPhone())
                .setProvince(address.getProvince())
                .setZip(address.getZip());
        Storefront.MutationQuery query = Storefront.mutation(mutationQuery
                -> mutationQuery.checkoutCustomerAssociate(checkoutId, AccountManager.getCustomerAccessToken(), emailUpdatePayloadQuery
                -> emailUpdatePayloadQuery.checkout(checkoutQuery
                -> checkoutQuery.webUrl().email()).userErrors(userErrorQuery
                -> userErrorQuery.field().message())).checkoutShippingAddressUpdate(input, checkoutId, shippingAddressUpdatePayloadQuery
                -> shippingAddressUpdatePayloadQuery.checkout(checkoutQuery
                -> checkoutQuery.webUrl().shippingAddress(new Storefront.MailingAddressQueryDefinition() {
            @Override
            public void define(Storefront.MailingAddressQuery _queryBuilder) {
                _queryBuilder.firstName().address1();
            }
        })).userErrors(userErrorQuery
                -> userErrorQuery.field().message())));

        client.mutateGraph(query)
                .enqueue(new GraphCall.Callback<Storefront.Mutation>() {
                    @Override
                    public void onResponse(@android.support.annotation.NonNull GraphResponse<Storefront.Mutation> response) {
                        if (!ListUtils.isEmpty(response.data().getCheckoutShippingAddressUpdate().getUserErrors())) {
                            if (updateCheckoutListener != null) {
                                updateCheckoutListener.onError(response.data().getCheckoutShippingAddressUpdate().getUserErrors().get(0).getMessage());
                            }
                        } else if (updateCheckoutListener != null) {
                            updateCheckoutListener.onSuccess(response.data().getCheckoutShippingAddressUpdate().getCheckout().getWebUrl());
                        }
                    }

                    @Override
                    public void onFailure(@android.support.annotation.NonNull GraphError error) {
                        if (updateCheckoutListener != null) {
                            updateCheckoutListener.onError(error.getMessage());
                        }
                    }
                });
    }

    // get checkout ID
    void execute() {

        Storefront.MutationQuery query = Storefront.mutation(mutationQuery
                        -> mutationQuery.checkoutCreate(generateInput(dataSource), createPayloadQuery
                        -> createPayloadQuery.checkout(checkoutQuery
                        -> checkoutQuery.webUrl().totalPrice().lineItems(new Storefront.CheckoutQuery.LineItemsArgumentsDefinition() {
                    @Override
                    public void define(Storefront.CheckoutQuery.LineItemsArguments args) {
                        args.first(250);
                    }
                }, new Storefront.CheckoutLineItemConnectionQueryDefinition() {
                    @Override
                    public void define(Storefront.CheckoutLineItemConnectionQuery _queryBuilder) {
                        _queryBuilder.edges(_queryBuilder1
                                -> _queryBuilder1.node(new Storefront.CheckoutLineItemQueryDefinition() {
                            @Override
                            public void define(Storefront.CheckoutLineItemQuery _queryBuilder1) {
                                _queryBuilder1.quantity().title().variant(_queryBuilder11
                                        -> _queryBuilder11.price().compareAtPrice().title().product(new Storefront.ProductQueryDefinition() {
                                    @Override
                                    public void define(Storefront.ProductQuery _queryBuilder) {
                                        _queryBuilder.title().images(args
                                                -> args.first(1), _queryBuilder22
                                                -> _queryBuilder22.edges(_queryBuilder2
                                                -> _queryBuilder2.node(new Storefront.ImageQueryDefinition() {
                                            @Override
                                            public void define(Storefront.ImageQuery _queryBuilder2) {
                                                _queryBuilder2.src();
                                            }
                                        })));
                                    }
                                }));
                            }
                        }));
                    }
                })
                ).userErrors(userErrorQuery -> userErrorQuery
                        .field()
                        .message())));
        client.mutateGraph(query).enqueue(new GraphCall.Callback<Storefront.Mutation>() {
            @Override
            public void onResponse(@NonNull GraphResponse<Storefront.Mutation> response) {
                if (response.data() == null || response.data().getCheckoutCreate() == null) {
                    if (mListener != null) {
                        mListener.onError(WhatsApplication.getContext().getString(R.string.no_clear_error));
                    }
                    return;
                }
                if (!ListUtils.isEmpty(response.data().getCheckoutCreate().getUserErrors())) {
                    if (mListener != null) {
                        List<Storefront.UserError> userErrors = response.data().getCheckoutCreate().getUserErrors();
                        List<CartItem> temlateCartLists = new ArrayList<>();
                        for (Storefront.UserError userError : userErrors) {
                            List<String> field = userError.getField();
                            if (!ListUtils.isEmpty(field) && field.size() > 3) {
                                String index = field.get(2);
                                temlateCartLists.add(dataSource.get(Integer.parseInt(index)));
                            }
                        }
                        CartRepository.modifyCartItemStates(temlateCartLists);
                        mListener.onError(response.data().getCheckoutCreate().getUserErrors().get(0).getMessage());
                    }
                } else {
                    if (response.data().getCheckoutCreate().getCheckout() != null) {
                        if (mListener != null) {
                            ID checkoutID = response.data().getCheckoutCreate().getCheckout().getId();
                            List<Storefront.CheckoutLineItemEdge> edges = response.data().getCheckoutCreate().getCheckout().getLineItems().getEdges();
                            List<CartItem> responseData = new ArrayList<>();
                            for (Storefront.CheckoutLineItemEdge edge : edges) {
                                CartItem cartItem = new CartItem();
                                cartItem.setId(edge.getNode().getVariant().getId().toString());
                                cartItem.setColorAndSize(edge.getNode().getVariant().getTitle());
                                BigDecimal compareAtPrice = edge.getNode().getVariant().getCompareAtPrice();
                                cartItem.setComparePrice(compareAtPrice == null ? 0.0 : compareAtPrice.doubleValue());
                                cartItem.setName(edge.getNode().getTitle());
                                cartItem.setPrice(edge.getNode().getVariant().getPrice().doubleValue());
                                cartItem.setQuality(edge.getNode().getQuantity());
                                String src = edge.getNode().getVariant().getProduct().getImages().getEdges().get(0).getNode().getSrc();
                                cartItem.setIcon(src);
                                responseData.add(cartItem);
                            }
                            mListener.onSuccess(response.data().getCheckoutCreate().getCheckout().getTotalPrice().doubleValue(), checkoutID, responseData);
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull GraphError error) {
                // handle errors
                if (mListener != null) {
                    mListener.onError(error.getMessage());
                }
            }
        });
    }

    private Storefront.CheckoutCreateInput generateInput(List<CartItem> data) {

//        Storefront.CheckoutCreateInput input = new Storefront.CheckoutCreateInput()
//                .setLineItemsInput(Input.value(Arrays.asList(
//                        new Storefront.CheckoutLineItemInput(new ID("mFyaWFu"), 5),
//                        new Storefront.CheckoutLineItemInput(new ID("8vc2hGl"), 3)
//                )));

        ArrayList<Storefront.CheckoutLineItemInput> arrayList = new ArrayList<>();
        for (CartItem cartItem : data) {
            Storefront.CheckoutLineItemInput itemInput = new Storefront.
                    CheckoutLineItemInput(cartItem.getQuality(), new ID(cartItem.getId().replace("\n", "")));
            arrayList.add(itemInput);
        }
//        ArrayList<Storefront.CheckoutLineItemInput> arrayList =
//                data.stream().map(cartItem ->
//                        new Storefront.CheckoutLineItemInput(cartItem.quality, new ID(cartItem.getId().replace("\n",""))))
//                        .collect(Collectors.toCollection(ArrayList::new));
        return new Storefront.CheckoutCreateInput()
                .setLineItemsInput(Input.value(arrayList));
    }

    public void checkout(String checkoutId, String s, GiftCheckListener giftCheckListener) {
        Storefront.MutationQuery mutation = Storefront.mutation(mutationQuery -> mutationQuery.checkoutGiftCardApply(s,
                new ID(checkoutId), _queryBuilder -> _queryBuilder.checkout(_queryBuilder13
                        -> _queryBuilder13.appliedGiftCards
                        (Storefront.AppliedGiftCardQuery::balance))
                        .userErrors(Storefront.UserErrorQuery::message)).checkoutDiscountCodeApply(s, new ID(checkoutId), new Storefront.CheckoutDiscountCodeApplyPayloadQueryDefinition() {
                    @Override
                    public void define(Storefront.CheckoutDiscountCodeApplyPayloadQuery _queryBuilder) {
                        _queryBuilder.checkout(_queryBuilder1
                                -> _queryBuilder1.paymentDue()
                                .totalPrice().subtotalPrice()
                        )
                                .userErrors(_queryBuilder12 -> _queryBuilder12.message());
                    }
                })
        );
        client.mutateGraph(mutation)
                .enqueue(new GraphCall.Callback<Storefront.Mutation>() {
                    @Override
                    public void onResponse(@android.support.annotation.NonNull GraphResponse<Storefront.Mutation> response) {
                        if (giftCheckListener == null) {
                            return;
                        }
                        if (ListUtils.isEmpty(response.data().getCheckoutGiftCardApply().getUserErrors())) {
                            giftCheckListener.exist(String.valueOf(response.data().getCheckoutGiftCardApply()
                                    .getCheckout().getAppliedGiftCards().get(0).getBalance()));
                        } else if (ListUtils.isEmpty(response.data().getCheckoutDiscountCodeApply().getUserErrors())) {
                            //Storefront.Checkout checkout = response.data().getCheckoutDiscountCodeApply().getCheckout();
                            giftCheckListener.exist(String.valueOf(response.data().getCheckoutDiscountCodeApply().getCheckout()
                                    .getPaymentDue().doubleValue()));
                        } else {
                            giftCheckListener.illegal(WhatsApplication.getContext().getString(R.string.card_num_not_exist));
                        }
                    }

                    @Override
                    public void onFailure(@android.support.annotation.NonNull GraphError error) {
                        if (giftCheckListener != null) {
                            giftCheckListener.illegal(error.getMessage());
                        }
                    }
                });
    }

    public void shippingMethods() {
        Storefront.QueryRootQuery query = Storefront.query(rootQuery -> rootQuery
                .node(checkoutId, nodeQuery -> nodeQuery
                        .onCheckout(checkoutQuery -> checkoutQuery
                                .shippingLine(_queryBuilder -> _queryBuilder.title().price().handle())
                                .availableShippingRates(availableShippingRatesQuery -> availableShippingRatesQuery
                                        .ready()
                                        .shippingRates(shippingRateQuery -> shippingRateQuery
                                                .handle()
                                                .price()
                                                .title()
                                        )
                                ).webUrl().totalTax()
                        )
                )
        );
        client.queryGraph(query)
                .enqueue(new GraphCall.Callback<Storefront.QueryRoot>() {
                    @Override
                    public void onResponse(@android.support.annotation.NonNull GraphResponse<Storefront.QueryRoot> response) {
                        if (shippingListener == null) {
                            return;
                        }
                        Storefront.Checkout check = (Storefront.Checkout) response.data().getNode();
                        if (check != null) {
                            List<Storefront.ShippingRate> shippingRates = check.getAvailableShippingRates().getShippingRates();
                            shippingListener.onSuccess(check.getTotalTax().doubleValue(), shippingRates, check.getWebUrl());
                        } else {
                            shippingListener.onError(WhatsApplication.getContext().getString(R.string.no_shipping_currently));
                        }
                    }

                    @Override
                    public void onFailure(@android.support.annotation.NonNull GraphError error) {
                        if (shippingListener == null) {
                            return;
                        }
                        shippingListener.onError(error.getMessage());
                    }
                });
    }

    public void checkVariantExist(List<ID> ids) {
        Storefront.QueryRootQuery query = Storefront.query(_queryBuilder
                -> _queryBuilder.nodes(ids, new Storefront.NodeQueryDefinition() {
            @Override
            public void define(Storefront.NodeQuery _queryBuilder) {
                _queryBuilder.onProductVariant(_queryBuilder1
                        -> _queryBuilder1.product(new Storefront.ProductQueryDefinition() {
                    @Override
                    public void define(Storefront.ProductQuery _queryBuilder1) {
                        _queryBuilder1.title();
                    }
                }));
            }
        }));
        client.queryGraph(query)
                .enqueue(new GraphCall.Callback<Storefront.QueryRoot>() {
                    @Override
                    public void onResponse(@android.support.annotation.NonNull GraphResponse<Storefront.QueryRoot> response) {
                        List<Storefront.Node> nodes = response.data().getNodes();
                        List<ID> responseIds = new ArrayList<>();
                        for (Storefront.Node node : nodes) {
                            Storefront.ProductVariant checkout = (Storefront.ProductVariant) node;
                            Storefront.Product product = checkout.getProduct();
                            if (product != null) {
                                responseIds.add(checkout.getId());
                            }
                        }
                        if (checkExistListener != null) {
                            checkExistListener.onSuccess(responseIds);
                        }
                    }

                    @Override
                    public void onFailure(@android.support.annotation.NonNull GraphError error) {
                        if (checkExistListener != null) {
                            checkExistListener.onFailed(error.getMessage());
                        }
                    }
                });
    }


    public void checkOrderExist(ID checkoutId) {
        Storefront.QueryRootQuery query = Storefront.query(_queryBuilder
                -> _queryBuilder.node(checkoutId, _queryBuilder12
                -> _queryBuilder12.onCheckout(new Storefront.CheckoutQueryDefinition() {
            @Override
            public void define(Storefront.CheckoutQuery _queryBuilder) {
                _queryBuilder.order(_queryBuilder14
                        -> _queryBuilder14.orderNumber().lineItems(args
                        -> args.first(250), _queryBuilder1
                        -> _queryBuilder1.edges(new Storefront.OrderLineItemEdgeQueryDefinition() {
                    @Override
                    public void define(Storefront.OrderLineItemEdgeQuery _queryBuilder1) {
                        _queryBuilder1.node(_queryBuilder11
                                -> _queryBuilder11.variant(qd1
                                -> qd1.availableForSale().compareAtPrice().title().sku().price().selectedOptions(s
                                -> s.name().value()).image(args
                                -> args.maxHeight(150).maxWidth(100).crop(Storefront.CropRegion.CENTER), Storefront.ImageQuery::src)).quantity().title().customAttributes(a
                                -> a.value().key()));
                    }
                }))
                        .currencyCode().customerLocale().email()
                        .shippingAddress(_queryBuilder13
                                -> _queryBuilder13.address1().address2().city().province().provinceCode().country().countryCode().company().
                                firstName().lastName().name().phone().zip()).customerUrl().phone().processedAt().subtotalPrice().totalPrice().totalRefunded()
                        .totalShippingPrice().totalTax());
            }
        })));


        client.queryGraph(query)
                .enqueue(new GraphCall.Callback<Storefront.QueryRoot>() {
                    @Override
                    public void onResponse(@android.support.annotation.NonNull GraphResponse<Storefront.QueryRoot> response) {
                        if (orderListener == null) {
                            return;
                        }
                        if (response.data() == null) {
                            orderListener.onFailure();
                        }
                        Storefront.Checkout checkout = (Storefront.Checkout) response.data().getNode();
                        Storefront.Order node = checkout.getOrder();
                        if (node == null) {
                            orderListener.onFailure();
                            return;
                        }
                        Order order = Order.parseOrder(node);
                        Logger.e(order);
                        if (order != null) {
                            orderListener.onSuccess(order);
                        }
                    }

                    @Override
                    public void onFailure(@android.support.annotation.NonNull GraphError error) {
                        if (orderListener == null) {
                            return;
                        }
                        orderListener.onFailure();
                    }
                });
    }

    public void checkAddress(ID id) {
        Storefront.QueryRootQuery query = Storefront.query(_queryBuilder
                -> _queryBuilder.node(id, new Storefront.NodeQueryDefinition() {
            @Override
            public void define(Storefront.NodeQuery _queryBuilder) {
                _queryBuilder.onCheckout(_queryBuilder1
                        -> _queryBuilder1
                        .shippingLine(_queryBuilder2 -> _queryBuilder2.title().price())
                        .shippingAddress(new Storefront.MailingAddressQueryDefinition() {
                            @Override
                            public void define(Storefront.MailingAddressQuery _queryBuilder1) {
                                _queryBuilder1.address1().address2().city().name().phone().country();
                            }
                        }));
            }
        }));
        client.queryGraph(query)
                .enqueue(new GraphCall.Callback<Storefront.QueryRoot>() {
                    @Override
                    public void onResponse(@android.support.annotation.NonNull GraphResponse<Storefront.QueryRoot> response) {
                        if (updateCheckoutListener == null) {
                            return;
                        }
                        Storefront.Checkout checkout = (Storefront.Checkout) response.data().getNode();
                        if (checkout == null) {
                            return;
                        }
                        Storefront.MailingAddress shippingAddress = checkout.getShippingAddress();
                        Storefront.ShippingRate shippingLine = checkout.getShippingLine();
                        if (shippingAddress != null) {
                            updateAddressListener.onSuccess(shippingAddress, shippingLine);
                        }
                    }

                    @Override
                    public void onFailure(@android.support.annotation.NonNull GraphError error) {

                    }
                });
    }

    public static void modifyCartItemStates(List<CartItem> ids) {
        try {
            List<CartItem> cartItemList = (List<CartItem>) PreferencesUtil.getObject(WhatsApplication.getContext(), Constant.CART_LOCAL);
            if (!ListUtils.isEmpty(cartItemList) && !ListUtils.isEmpty(ids)) {
                for (CartItem cartItem : cartItemList) {
                    if (ids.contains(cartItem)) {
                        cartItem.isSoldOut = true;
                    }
                }
                PreferencesUtil.putObject(WhatsApplication.getContext(), Constant.CART_LOCAL, cartItemList);
                EventBus.getDefault().post(new CartItem());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public interface ShippingListener {
        void onSuccess(Double tax, List<Storefront.ShippingRate> shippingRates, String url);

        void onError(String message);
    }

    public interface QueryListener {
        void onSuccess(Double totalPrice, ID id, List<CartItem> responseData);

        void onError(String message);
    }

    public interface GiftCheckListener {
        void exist(String balance);

        void illegal(String message);
    }

    public interface UpdateCheckoutListener {
        void onSuccess(String url);

        void onError(String message);
    }

    public interface UpdateAddressListener {
        void onSuccess(Storefront.MailingAddress address, Storefront.ShippingRate shippingRate);
    }

    public interface OrderDetailListener {
        void onSuccess(Order order);

        void onFailure();
    }

    public interface CheckExistListener {
        void onSuccess(List<ID> ids);

        void onFailed(String message);
    }
}

package com.whatsmode.shopify.block.cart;

import android.annotation.TargetApi;
import android.os.Build;

import com.shopify.buy3.GraphCall;
import com.shopify.buy3.GraphClient;
import com.shopify.buy3.GraphError;
import com.shopify.buy3.GraphResponse;
import com.shopify.buy3.Storefront;
import com.shopify.graphql.support.ID;
import com.shopify.graphql.support.Input;
import com.whatsmode.library.util.ListUtils;
import com.whatsmode.shopify.R;
import com.whatsmode.shopify.WhatsApplication;
import com.whatsmode.shopify.block.account.data.AccountManager;
import com.whatsmode.shopify.block.address.Address;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.annotations.NonNull;


public class CartRepository {

    private GraphClient client;
    private static CartRepository cartRepository;
    private List<CartItem> dataSource;
    private QueryListener mListener;
    private UpdateCheckoutListener updateCheckoutListener;
    private ID checkoutId;

    private CartRepository(){
        client = WhatsApplication.getGraphClient();
    }

    public static CartRepository create() {
        if (cartRepository == null) {
            cartRepository = new CartRepository();
        }
        return cartRepository;
    }

    CartRepository parameter(List<CartItem> dataSource){
        this.dataSource = dataSource;
        return cartRepository;
    }

    CartRepository checkoutListener(QueryListener listener){
        this.mListener = listener;
        return cartRepository;
    }

    public CartRepository updateCheckoutListener(UpdateCheckoutListener listener) {
        updateCheckoutListener = listener;
        return cartRepository;
    }

    public CartRepository bindUser(ID checkoutId) {
        this.checkoutId = checkoutId;
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
                        -> mutationQuery.checkoutEmailUpdate(checkoutId, AccountManager.getUsername(), emailUpdatePayloadQuery
                        -> emailUpdatePayloadQuery.checkout(checkoutQuery
                        -> checkoutQuery.webUrl()).userErrors(userErrorQuery
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
                        }else if (updateCheckoutListener != null) {
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

    void execute(){

        Storefront.MutationQuery query = Storefront.mutation(mutationQuery -> mutationQuery
                .checkoutCreate(generateInput(dataSource), createPayloadQuery -> createPayloadQuery
                        .checkout(checkoutQuery -> checkoutQuery
                                .webUrl()
                        )
                        .userErrors(userErrorQuery -> userErrorQuery
                                .field()
                                .message()
                        )
                )
        );
        client.mutateGraph(query).enqueue(new GraphCall.Callback<Storefront.Mutation>() {
            @Override public void onResponse(@NonNull GraphResponse<Storefront.Mutation> response) {
                if (response.data() == null || response.data().getCheckoutCreate() == null) {
                    if (mListener != null) {
                        mListener.onError(WhatsApplication.getContext().getString(R.string.no_clear_error));
                    }
                    return;
                }
                if (!ListUtils.isEmpty(response.data().getCheckoutCreate().getUserErrors())) {
                    if (mListener != null) {
                        mListener.onError(response.data().getCheckoutCreate().getUserErrors().get(0).getMessage());
                    }
                } else {
                    if (response.data().getCheckoutCreate().getCheckout() != null) {
                        if (mListener != null) {
                            ID checkoutID = response.data().getCheckoutCreate().getCheckout().getId();
                            mListener.onSuccess(checkoutID);
                        }
                    }
                }
            }

            @Override public void onFailure(@NonNull GraphError error) {
                // handle errors
                if (mListener != null) {
                    mListener.onError(error.getMessage());
                }
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.N)
    private Storefront.CheckoutCreateInput generateInput(List<CartItem> data) {
            ArrayList<Storefront.CheckoutLineItemInput> arrayList =
                    data.stream().map(cartItem ->
                            new Storefront.CheckoutLineItemInput(cartItem.quality, new ID(cartItem.id)))
                            .collect(Collectors.toCollection(ArrayList::new));
            return new Storefront.CheckoutCreateInput()
                    .setLineItemsInput(Input.value(arrayList));
    }

    public void checkout(String checkoutId, String s, GiftCheckListener giftCheckListener) {
        Storefront.MutationQuery mutation = Storefront.mutation(mutationQuery -> mutationQuery.checkoutGiftCardApply(s, new ID(checkoutId), new Storefront.CheckoutGiftCardApplyPayloadQueryDefinition() {
            @Override
            public void define(Storefront.CheckoutGiftCardApplyPayloadQuery _queryBuilder) {
                _queryBuilder.checkout(_queryBuilder13
                        -> _queryBuilder13.appliedGiftCards
                        (Storefront.AppliedGiftCardQuery::balance))
                        .userErrors(Storefront.UserErrorQuery::message);
            }
        }));
        client.mutateGraph(mutation)
                .enqueue(new GraphCall.Callback<Storefront.Mutation>() {
                    @Override
                    public void onResponse(@android.support.annotation.NonNull GraphResponse<Storefront.Mutation> response) {
                        if (giftCheckListener != null) {
                            if (ListUtils.isEmpty(response.data().getCheckoutGiftCardApply().getUserErrors())) {
                                giftCheckListener.exist(String.valueOf(response.data().getCheckoutGiftCardApply()
                                        .getCheckout().getAppliedGiftCards()
                                        .get(0).getBalance()));
                            }else{
                                giftCheckListener.illegal(response.data().getCheckoutGiftCardApply().getUserErrors().get(0).getMessage());
                            }
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

    public interface QueryListener {
        void onSuccess(ID id);
        void onError(String message);
    }

    public interface GiftCheckListener{
        void exist(String balance);
        void illegal(String message);
    }

    public interface UpdateCheckoutListener {
        void onSuccess(String url);
        void onError(String message);
    }
}

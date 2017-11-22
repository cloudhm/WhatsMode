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
import com.whatsmode.shopify.WhatsApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.annotations.NonNull;


class CartRepository {

    private GraphClient client;
    private static CartRepository cartRepository;
    private List<CartItem> dataSource;
    private QueryListener mListener;

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

    CartRepository listener(QueryListener listener){
        this.mListener = listener;
        return cartRepository;
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
                        mListener.onError("服務器忙,請稍後再試");
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
                            mListener.onSuccess(response.data().getCheckoutCreate().getCheckout().getWebUrl());
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

    public interface QueryListener {
        void onSuccess(String webUrl);
        void onError(String message);
    }
}

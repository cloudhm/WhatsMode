package com.whatsmode.shopify.block.cart;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/11/23.
 */

public class CartItemLists implements Serializable {

    private static final long serialVersionUID = 1L;
    public List<CartItem> cartItems;

    public CartItemLists(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }
}

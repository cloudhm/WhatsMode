package com.whatsmode.shopify.block.cart;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/20.
 */

public class CartItem {
    String name;
    int quality;
    String url;
    String id;

    public CartItem(String name, int quality, String url, String id) {
        this.name = name;
        this.quality = quality;
        this.url = url;
        this.id = id;
    }

    public static List<CartItem> mockItem(){
        List<CartItem> cartItems = new ArrayList<>();
        cartItems.add(new CartItem("a",11,"aaa","s"));
        cartItems.add(new CartItem("b",12,"bbb","s"));
        cartItems.add(new CartItem("c",13,"ccc","s"));
        cartItems.add(new CartItem("d",14,"ddd","s"));
        return cartItems;
    }
 }

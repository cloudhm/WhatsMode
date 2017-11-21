package com.whatsmode.shopify.block.cart;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

class CartItem implements Serializable{

    private static final long serialVersionUID = 1L;
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

    static List<CartItem> mockItem(){
        List<CartItem> cartItems = new ArrayList<>();
        cartItems.add(new CartItem("a",11,"aaa","s"));
        cartItems.add(new CartItem("b",12,"bbb","s"));
        cartItems.add(new CartItem("c",13,"ccc","s"));
        cartItems.add(new CartItem("d",14,"ddd","s"));
        return cartItems;
    }


 }

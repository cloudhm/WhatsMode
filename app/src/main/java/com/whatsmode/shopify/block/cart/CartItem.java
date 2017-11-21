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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuality() {
        return quality;
    }

    public void setQuality(int quality) {
        this.quality = quality;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public CartItem(String name, int quality, String url, String id) {
        this.name = name;
        this.quality = quality;
        this.url = url;
        this.id = id;
    }

    static List<CartItem> mockItem(){
        List<CartItem> cartItems = new ArrayList<>();
        cartItems.add(new CartItem("a",11,"aaa","s"));
        cartItems.add(new CartItem("b",12,"bbb","sd"));
        cartItems.add(new CartItem("c",13,"ccc","saa"));
        cartItems.add(new CartItem("d",14,"ddd","sdd"));
        return cartItems;
    }


 }

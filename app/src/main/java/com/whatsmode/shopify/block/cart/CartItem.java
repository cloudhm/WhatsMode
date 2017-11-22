package com.whatsmode.shopify.block.cart;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CartItem implements Serializable{

    private static final long serialVersionUID = 1L;
    String name;
    int quality;
    String url;
    String id;
    Double price;

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

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

    public CartItem(String name, int quality, String url, String id,Double price) {
        this.name = name;
        this.quality = quality;
        this.price = price;
        this.url = url;
        this.id = id;
    }

    static List<CartItem> mockItem(){
        /**
         * {
         "ids": ["Z2lkOi8vc2hvcGlmeS9Qcm9kdWN0VmFyaWFudC8xMjE3MTc3NzE0NzE1",
         "Z2lkOi8vc2hvcGlmeS9Qcm9kdWN0VmFyaWFudC8xMjE3MTc3NzQ3NDgz"]
         }
         */
        List<CartItem> cartItems = new ArrayList<>();
        cartItems.add(new CartItem("a",11,"aaa","Z2lkOi8vc2hvcGlmeS9Qcm9kdWN0VmFyaWFudC8xMjE3MTc3NzE0NzE1",10.0));
        cartItems.add(new CartItem("b",12,"bbb","Z2lkOi8vc2hvcGlmeS9Qcm9kdWN0VmFyaWFudC8xMjE3MTc3NzQ3NDgz",23.3));
        return cartItems;
    }


 }

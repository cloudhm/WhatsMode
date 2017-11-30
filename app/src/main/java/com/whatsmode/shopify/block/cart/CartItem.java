package com.whatsmode.shopify.block.cart;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CartItem implements Serializable{

    private static final long serialVersionUID = 1L;
    String name;
    int quality;
    String url;
    String icon = "https://img14.360buyimg.com/da/jfs/t4024/266/968344120/18745/d610233c/5863a879Nb8f02aeb.jpg";
    String id;
    Double price;
    Double comparePrice;
    String colorAndSize;

    public String getColorAndSize() {
        return colorAndSize;
    }

    public void setColorAndSize(String colorAndSize) {
        this.colorAndSize = colorAndSize;
    }

    public Double getComparePrice() {
        return comparePrice;
    }

    public void setComparePrice(Double comparePrice) {
        this.comparePrice = comparePrice;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

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

    public CartItem() {
    }

    static List<CartItem> mockItem(){
        /**
         * {
         "ids": ["Z2lkOi8vc2hvcGlmeS9Qcm9kdWN0VmFyaWFudC8xMjE3MTc3NzE0NzE1",
         "Z2lkOi8vc2hvcGlmeS9Qcm9kdWN0VmFyaWFudC8xMjE3MTc3NzQ3NDgz"]
         }
         */
        List<CartItem> cartItems = new ArrayList<>();
        cartItems.add(new CartItem("a",11,"https://whatsmode.com/collections/coats-jackets/products/sweet-like-strawberry-ice-cream-embroidered-zipper-pu-leather-jacket","Z2lkOi8vc2hvcGlmeS9Qcm9kdWN0VmFyaWFudC8xMjE3MTc3NzE0NzE1",10.0));
        cartItems.add(new CartItem("b",12,"https://whatsmode.com/collections/coats-jackets/products/camouflage-green-two-pockets-contracted-jacket","Z2lkOi8vc2hvcGlmeS9Qcm9kdWN0VmFyaWFudC8xMjE3MTc3NzQ3NDgz",23.3));
        return cartItems;
    }


 }

package com.whatsmode.shopify.block.me;

import com.whatsmode.shopify.block.address.Address;

import java.math.BigDecimal;

/**
 * Created by tom on 17-11-21.
 */

public class Order {
    public static boolean sHasNextPage;
    //currencyCode
    //customerLocale
    private String customerUrl;
    private String email;
    private String id;
    //lineItems
    private Integer orderNumber;
    private String phone;
    //processedAt
    private Address shippingAddress;
    private BigDecimal subtotalPrice;
    private BigDecimal totalPrice;
    private BigDecimal totalRefunded;
    private BigDecimal totalShippingPrice;
    private BigDecimal totalTax;
    private String cursor;

    public Order(String customerUrl, String email, String id, Integer orderNumber, String phone,
                 Address shippingAddress, BigDecimal subtotalPrice, BigDecimal totalPrice,
                 BigDecimal totalRefunded, BigDecimal totalShippingPrice, BigDecimal totalTax,
                 String cursor) {
        this.customerUrl = customerUrl;
        this.email = email;
        this.id = id;
        this.orderNumber = orderNumber;
        this.phone = phone;
        this.shippingAddress = shippingAddress;
        this.subtotalPrice = subtotalPrice;
        this.totalPrice = totalPrice;
        this.totalRefunded = totalRefunded;
        this.totalShippingPrice = totalShippingPrice;
        this.totalTax = totalTax;
        this.cursor = cursor;
    }

    public static boolean isHasNextPage() {
        return sHasNextPage;
    }

    public static void setHasNextPage(boolean hasNextPage) {
        sHasNextPage = hasNextPage;
    }

    public String getCustomerUrl() {
        return customerUrl;
    }

    public void setCustomerUrl(String customerUrl) {
        this.customerUrl = customerUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Address getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(Address shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public BigDecimal getSubtotalPrice() {
        return subtotalPrice;
    }

    public void setSubtotalPrice(BigDecimal subtotalPrice) {
        this.subtotalPrice = subtotalPrice;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public BigDecimal getTotalRefunded() {
        return totalRefunded;
    }

    public void setTotalRefunded(BigDecimal totalRefunded) {
        this.totalRefunded = totalRefunded;
    }

    public BigDecimal getTotalShippingPrice() {
        return totalShippingPrice;
    }

    public void setTotalShippingPrice(BigDecimal totalShippingPrice) {
        this.totalShippingPrice = totalShippingPrice;
    }

    public BigDecimal getTotalTax() {
        return totalTax;
    }

    public void setTotalTax(BigDecimal totalTax) {
        this.totalTax = totalTax;
    }


    public String getCursor() {
        return cursor;
    }

    public void setCursor(String cursor) {
        this.cursor = cursor;
    }

    @Override
    public String toString() {
        return "Order{" +
                "customerUrl='" + customerUrl + '\'' +
                ", email='" + email + '\'' +
                ", id='" + id + '\'' +
                ", orderNumber=" + orderNumber +
                ", phone='" + phone + '\'' +
                ", shippingAddress=" + shippingAddress +
                ", subtotalPrice=" + subtotalPrice +
                ", totalPrice=" + totalPrice +
                ", totalRefunded=" + totalRefunded +
                ", totalShippingPrice=" + totalShippingPrice +
                ", totalTax=" + totalTax +
                ", cursor='" + cursor + '\'' +
                '}';
    }
}

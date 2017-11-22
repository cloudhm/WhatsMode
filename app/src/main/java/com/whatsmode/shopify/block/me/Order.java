package com.whatsmode.shopify.block.me;

import com.whatsmode.shopify.block.address.Address;

import java.math.BigDecimal;
import java.util.List;

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
    private List<LineItem> lineItems;

    public Order(String customerUrl, String email, String id, Integer orderNumber, String phone,
                 Address shippingAddress, BigDecimal subtotalPrice, BigDecimal totalPrice,
                 BigDecimal totalRefunded, BigDecimal totalShippingPrice, BigDecimal totalTax,
                 String cursor,List<LineItem> lineItems) {
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
        this.lineItems = lineItems;
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

    public List<LineItem> getLineItems() {
        return lineItems;
    }

    public void setLineItems(List<LineItem> lineItems) {
        this.lineItems = lineItems;
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
                ", lineItems=" + lineItems +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Order order = (Order) o;

        if (customerUrl != null ? !customerUrl.equals(order.customerUrl) : order.customerUrl != null)
            return false;
        if (email != null ? !email.equals(order.email) : order.email != null) return false;
        if (id != null ? !id.equals(order.id) : order.id != null) return false;
        if (orderNumber != null ? !orderNumber.equals(order.orderNumber) : order.orderNumber != null)
            return false;
        if (phone != null ? !phone.equals(order.phone) : order.phone != null) return false;
        if (shippingAddress != null ? !shippingAddress.equals(order.shippingAddress) : order.shippingAddress != null)
            return false;
        if (subtotalPrice != null ? !subtotalPrice.equals(order.subtotalPrice) : order.subtotalPrice != null)
            return false;
        if (totalPrice != null ? !totalPrice.equals(order.totalPrice) : order.totalPrice != null)
            return false;
        if (totalRefunded != null ? !totalRefunded.equals(order.totalRefunded) : order.totalRefunded != null)
            return false;
        if (totalShippingPrice != null ? !totalShippingPrice.equals(order.totalShippingPrice) : order.totalShippingPrice != null)
            return false;
        if (totalTax != null ? !totalTax.equals(order.totalTax) : order.totalTax != null)
            return false;
        if (cursor != null ? !cursor.equals(order.cursor) : order.cursor != null) return false;
        return lineItems != null ? lineItems.equals(order.lineItems) : order.lineItems == null;

    }

    @Override
    public int hashCode() {
        int result = customerUrl != null ? customerUrl.hashCode() : 0;
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (orderNumber != null ? orderNumber.hashCode() : 0);
        result = 31 * result + (phone != null ? phone.hashCode() : 0);
        result = 31 * result + (shippingAddress != null ? shippingAddress.hashCode() : 0);
        result = 31 * result + (subtotalPrice != null ? subtotalPrice.hashCode() : 0);
        result = 31 * result + (totalPrice != null ? totalPrice.hashCode() : 0);
        result = 31 * result + (totalRefunded != null ? totalRefunded.hashCode() : 0);
        result = 31 * result + (totalShippingPrice != null ? totalShippingPrice.hashCode() : 0);
        result = 31 * result + (totalTax != null ? totalTax.hashCode() : 0);
        result = 31 * result + (cursor != null ? cursor.hashCode() : 0);
        result = 31 * result + (lineItems != null ? lineItems.hashCode() : 0);
        return result;
    }
}

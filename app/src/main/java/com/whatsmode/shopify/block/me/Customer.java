package com.whatsmode.shopify.block.me;

import android.text.TextUtils;

import com.shopify.buy3.Storefront;
import com.whatsmode.shopify.block.address.Address;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by tom on 17-11-25.
 */

public class Customer implements Serializable{
    private List<Address> addresses;
    private List<Order> orders;
    private boolean acceptsMarketing;
    private Date createdAt;
    private Address defaultAddress;
    private String displayName;
    private String email;
    private String firstName;
    private String id;
    private String lastName;
    private String phone;
    private Date updatedAt;

    public Customer(List<Address> addresses, List<Order> orders, boolean acceptsMarketing, Date createdAt, Address defaultAddress, String displayName, String email, String firstName, String id, String lastName, String phone, Date updatedAt) {
        this.addresses = addresses;
        this.orders = orders;
        this.acceptsMarketing = acceptsMarketing;
        this.createdAt = createdAt;
        this.defaultAddress = defaultAddress;
        this.displayName = displayName;
        this.email = email;
        this.firstName = firstName;
        this.id = id;
        this.lastName = lastName;
        this.phone = phone;
        this.updatedAt = updatedAt;
    }

    public Customer() {
    }

    public Customer(String email, String firstName, String id, String lastName) {
        this.email = email;
        this.firstName = firstName;
        this.id = id;
        this.lastName = lastName;
    }

    public Customer(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public boolean isAcceptsMarketing() {
        return acceptsMarketing;
    }

    public void setAcceptsMarketing(boolean acceptsMarketing) {
        this.acceptsMarketing = acceptsMarketing;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Address getDefaultAddress() {
        return defaultAddress;
    }

    public void setDefaultAddress(Address defaultAddress) {
        this.defaultAddress = defaultAddress;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Customer customer = (Customer) o;

        if (acceptsMarketing != customer.acceptsMarketing) return false;
        if (addresses != null ? !addresses.equals(customer.addresses) : customer.addresses != null)
            return false;
        if (orders != null ? !orders.equals(customer.orders) : customer.orders != null)
            return false;
        if (createdAt != null ? !createdAt.equals(customer.createdAt) : customer.createdAt != null)
            return false;
        if (defaultAddress != null ? !defaultAddress.equals(customer.defaultAddress) : customer.defaultAddress != null)
            return false;
        if (displayName != null ? !displayName.equals(customer.displayName) : customer.displayName != null)
            return false;
        if (email != null ? !email.equals(customer.email) : customer.email != null) return false;
        if (firstName != null ? !firstName.equals(customer.firstName) : customer.firstName != null)
            return false;
        if (id != null ? !id.equals(customer.id) : customer.id != null) return false;
        if (lastName != null ? !lastName.equals(customer.lastName) : customer.lastName != null)
            return false;
        if (phone != null ? !phone.equals(customer.phone) : customer.phone != null) return false;
        return updatedAt != null ? updatedAt.equals(customer.updatedAt) : customer.updatedAt == null;

    }

    @Override
    public int hashCode() {
        int result = addresses != null ? addresses.hashCode() : 0;
        result = 31 * result + (orders != null ? orders.hashCode() : 0);
        result = 31 * result + (acceptsMarketing ? 1 : 0);
        result = 31 * result + (createdAt != null ? createdAt.hashCode() : 0);
        result = 31 * result + (defaultAddress != null ? defaultAddress.hashCode() : 0);
        result = 31 * result + (displayName != null ? displayName.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (phone != null ? phone.hashCode() : 0);
        result = 31 * result + (updatedAt != null ? updatedAt.hashCode() : 0);
        return result;
    }

    public static Customer parseCustomer(Storefront.Customer m){
        Customer customer = new Customer(m.getEmail(),m.getFirstName(),m.getId().toString(),m.getLastName());
        Storefront.MailingAddress defaultAddress = m.getDefaultAddress();
        String defaultId = getDefaultId(defaultAddress);
        if (defaultAddress != null) {
            Storefront.MailingAddress node = defaultAddress;
            Address address = Address.parseOrderAddress(node);
            if (TextUtils.equals(node.getId().toString(), defaultId) && !TextUtils.isEmpty(defaultId)) {
                address.setDefault(true);
            }else{
                address.setDefault(false);
            }
            customer.setDefaultAddress(address);
        }
        return customer;
    }

    private static String getDefaultId(Storefront.MailingAddress defaultAddress){
        if (defaultAddress == null || defaultAddress.getId() == null) {
            return "";
        }
        return defaultAddress.getId().toString();
    }
}

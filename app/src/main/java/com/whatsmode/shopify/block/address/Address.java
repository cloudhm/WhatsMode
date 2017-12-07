package com.whatsmode.shopify.block.address;

import android.os.Parcel;
import android.os.Parcelable;

import com.shopify.buy3.Storefront;

import java.io.Serializable;

/**
 * Created by tom on 17-11-17.
 */

public class Address implements Serializable{
    public static boolean sHasNextPage;
    private String id;
    private String address1;
    private String address2;
    private String city;
    private String province;
    private String provinceCode;
    private String country;
    private String countryCode;
    private String company;
    private String firstName;
    private String lastName;
    private String name;
    private String phone;
    private String zip;
    private String cursor;
    private boolean isDefault;

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public Address(String id, String address1, String address2, String city, String province,
                   String provinceCode, String country, String countryCode, String company,
                   String firstName, String lastName, String name, String phone, String zip, String cursor) {
        this.id = id;
        this.address1 = address1;
        this.address2 = address2;
        this.city = city;
        this.province = province;
        this.provinceCode = provinceCode;
        this.country = country;
        this.countryCode = countryCode;
        this.company = company;
        this.firstName = firstName;
        this.lastName = lastName;
        this.name = name;
        this.phone = phone;
        this.zip = zip;
        this.cursor = cursor;
    }

    public void setAddress(String id, String address1, String address2, String city, String province,
                           String provinceCode, String country, String countryCode, String company,
                           String firstName, String lastName, String name, String phone, String zip, String cursor){
        this.id = id;
        this.address1 = address1;
        this.address2 = address2;
        this.city = city;
        this.province = province;
        this.provinceCode = provinceCode;
        this.country = country;
        this.countryCode = countryCode;
        this.company = company;
        this.firstName = firstName;
        this.lastName = lastName;
        this.name = name;
        this.phone = phone;
        this.zip = zip;
        this.cursor = cursor;
    }

    protected Address(Parcel in) {
        id = in.readString();
        address1 = in.readString();
        address2 = in.readString();
        city = in.readString();
        province = in.readString();
        provinceCode = in.readString();
        country = in.readString();
        countryCode = in.readString();
        company = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        name = in.readString();
        phone = in.readString();
        zip = in.readString();
        cursor = in.readString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCursor() {
        return cursor;
    }

    public void setCursor(String cursor) {
        this.cursor = cursor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Address address = (Address) o;

        if (id != null ? !id.equals(address.id) : address.id != null) return false;
        if (address1 != null ? !address1.equals(address.address1) : address.address1 != null)
            return false;
        if (address2 != null ? !address2.equals(address.address2) : address.address2 != null)
            return false;
        if (city != null ? !city.equals(address.city) : address.city != null) return false;
        if (province != null ? !province.equals(address.province) : address.province != null)
            return false;
        if (provinceCode != null ? !provinceCode.equals(address.provinceCode) : address.provinceCode != null)
            return false;
        if (country != null ? !country.equals(address.country) : address.country != null)
            return false;
        if (countryCode != null ? !countryCode.equals(address.countryCode) : address.countryCode != null)
            return false;
        if (company != null ? !company.equals(address.company) : address.company != null)
            return false;
        if (firstName != null ? !firstName.equals(address.firstName) : address.firstName != null)
            return false;
        if (lastName != null ? !lastName.equals(address.lastName) : address.lastName != null)
            return false;
        if (name != null ? !name.equals(address.name) : address.name != null) return false;
        if (phone != null ? !phone.equals(address.phone) : address.phone != null) return false;
        if (zip != null ? !zip.equals(address.zip) : address.zip != null) return false;
        return cursor != null ? cursor.equals(address.cursor) : address.cursor == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (address1 != null ? address1.hashCode() : 0);
        result = 31 * result + (address2 != null ? address2.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (province != null ? province.hashCode() : 0);
        result = 31 * result + (provinceCode != null ? provinceCode.hashCode() : 0);
        result = 31 * result + (country != null ? country.hashCode() : 0);
        result = 31 * result + (countryCode != null ? countryCode.hashCode() : 0);
        result = 31 * result + (company != null ? company.hashCode() : 0);
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (phone != null ? phone.hashCode() : 0);
        result = 31 * result + (zip != null ? zip.hashCode() : 0);
        result = 31 * result + (cursor != null ? cursor.hashCode() : 0);
        return result;
    }
    @Override
    public String toString() {
        return "Address{" +
                "id='" + id + '\'' +
                ", address1='" + address1 + '\'' +
                ", address2='" + address2 + '\'' +
                ", city='" + city + '\'' +
                ", province='" + province + '\'' +
                ", provinceCode='" + provinceCode + '\'' +
                ", country='" + country + '\'' +
                ", countryCode='" + countryCode + '\'' +
                ", company='" + company + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", zip='" + zip + '\'' +
                ", cursor='" + cursor + '\'' +
                '}';
    }


    public static Address parseOrderAddress(Storefront.MailingAddress node){
        Address address = new Address(node.getId().toString(),node.getAddress1(),node.getAddress2(),
                node.getCity(),node.getProvince(),node.getProvinceCode(),node.getCountry(),node.getCountryCode(),
                node.getCompany(),node.getFirstName(),node.getLastName(), node.getName(),
                node.getPhone(),node.getZip(),null);
        return address;
    }
}

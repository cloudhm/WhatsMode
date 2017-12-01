package com.whatsmode.shopify.block.address;

import android.text.TextUtils;

/**
 * Created by tom on 17-11-30.
 */

public class AddressUtil {
    public static String getJoinAddress(Address address){
        if (address != null) {
            StringBuilder builder = new StringBuilder();
            if (!TextUtils.isEmpty(address.getAddress1())) {
                builder.append(address.getAddress1()).append(",");
            }
            if (!TextUtils.isEmpty(address.getAddress2())) {
                builder.append(address.getAddress2()).append(",");
            }
            if (!TextUtils.isEmpty(address.getCity())) {
                builder.append(address.getCity()).append(",");
            }
            if (!TextUtils.isEmpty(address.getProvince())) {
                builder.append(address.getProvince()).append(",");
            }
            if (!TextUtils.isEmpty(address.getCountry())) {
                builder.append(address.getCountry()).append(",");
            }
            if (!TextUtils.isEmpty(address.getZip())) {
                builder.append(address.getZip());
            }
            return builder.toString();
        }
        return "";
    }
}

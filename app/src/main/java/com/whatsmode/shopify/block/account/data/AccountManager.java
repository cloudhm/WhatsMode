package com.whatsmode.shopify.block.account.data;

import android.text.TextUtils;

import com.whatsmode.library.util.PreferencesUtil;
import com.whatsmode.library.util.SerializableUtil;
import com.whatsmode.library.util.SnackUtil;
import com.whatsmode.shopify.WhatsApplication;
import com.whatsmode.shopify.block.address.Address;
import com.whatsmode.shopify.common.Constant;
import com.whatsmode.shopify.common.KeyConstant;

import java.io.File;
import java.io.Serializable;

/**
 * Created by tom on 17-11-16.
 */

public class AccountManager {

    private String mCustomerAccessToken;
    private UserInfo mUserInfo;
    private Address mDefaultAddress;

    private AccountManager(){
        readCustomerAccessToken();
    }

    private void readCustomerAccessToken() {
        mCustomerAccessToken = PreferencesUtil.getString(WhatsApplication.getContext(), KeyConstant.TOKEN_CUSTOMER_ACCESS);
        mUserInfo = SerializableUtil.readObject(new File(getSerializableUserInfoFile()));
        mDefaultAddress = SerializableUtil.readObject(new File(getSerializableDefaultAddressFile()));
    }

    public void writeCustomerAccessToken(String token){
        mCustomerAccessToken = token;
        PreferencesUtil.putString(WhatsApplication.getContext(),KeyConstant.TOKEN_CUSTOMER_ACCESS,token);
    }

    public void writeCustomerUserInfo(UserInfo userInfo) {
        mUserInfo = userInfo;
        SerializableUtil.writeObject(userInfo,getSerializableUserInfoFile());
    }

    public void writeCustomerDefaultAddress(Address address) {
        mDefaultAddress = address;
        SerializableUtil.writeObject(address,getSerializableDefaultAddressFile());
    }

    public String getSerializableUserInfoFile(){
        return SerializableUtil.getSerializableFile(Constant.ROOTPATH_CUSTOMER_USERINFO, Constant.USERINFO).getAbsolutePath();
    }

    public String getSerializableDefaultAddressFile(){
        return SerializableUtil.getSerializableFile(Constant.ROOTPATH_CUSTOMER_USERINFO, Constant.DEFAULT_ADDRESS).getAbsolutePath();
    }

    public static AccountManager getInstance(){
        return AccountManagerHolder.instance;
    }
    private static class AccountManagerHolder{
        private static AccountManager instance = new AccountManager();
    }

    public String getCustomerAccessTokenInner(){
        return mCustomerAccessToken == null ? "" : mCustomerAccessToken;
    }

    public static String getCustomerAccessToken(){
        return getInstance().getCustomerAccessTokenInner();
    }

    public UserInfo getUserInfoInner(){
        return mUserInfo;
    }

    public static UserInfo getUserInfo(){
        return getInstance().getUserInfoInner();
    }

    public static String getUsername(){
        return getUserInfo() == null ? null : getUserInfo().getUsername();
    }

    public static boolean isLoginStatus(){
        return !TextUtils.isEmpty(getCustomerAccessToken());
    }

    public Address getDefaultAddress(){
        return mDefaultAddress;
    }

    public static Address getCustomerDefaultAddress(){
        return getInstance().getDefaultAddress();
    }
}

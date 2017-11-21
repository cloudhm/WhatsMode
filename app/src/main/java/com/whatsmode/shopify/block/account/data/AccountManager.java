package com.whatsmode.shopify.block.account.data;

import com.whatsmode.library.util.PreferencesUtil;
import com.whatsmode.library.util.SerializableUtil;
import com.whatsmode.library.util.SnackUtil;
import com.whatsmode.shopify.WhatsApplication;
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

    private AccountManager(){
        readCustomerAccessToken();
    }

    private void readCustomerAccessToken() {
        mCustomerAccessToken = PreferencesUtil.getString(WhatsApplication.getContext(), KeyConstant.TOKEN_CUSTOMER_ACCESS);
        mUserInfo = SerializableUtil.readObject(new File(getSerializableUserInfoFile()));
    }

    public void writeCustomerAccessToken(String token){
        mCustomerAccessToken = token;
        PreferencesUtil.putString(WhatsApplication.getContext(),KeyConstant.TOKEN_CUSTOMER_ACCESS,token);
    }

    public void writeCustomerUserInfo(UserInfo userInfo) {
        mUserInfo = userInfo;
        SerializableUtil.writeObject(userInfo,getSerializableUserInfoFile());
    }

    public String getSerializableUserInfoFile(){
        return SerializableUtil.getSerializableFile(Constant.ROOTPATH_CUSTOMER_USERINFO, Constant.USERINFO).getAbsolutePath();
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
        return getUserInfo().getUsername();
    }
}

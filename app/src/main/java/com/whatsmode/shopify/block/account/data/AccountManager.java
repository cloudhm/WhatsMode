package com.whatsmode.shopify.block.account.data;

import com.whatsmode.library.util.PreferencesUtil;
import com.whatsmode.shopify.WhatsApplication;
import com.whatsmode.shopify.common.KeyConstant;

/**
 * Created by tom on 17-11-16.
 */

public class AccountManager {

    private String mCustomerAccessToken;

    private AccountManager(){
        readCustomerAccessToken();
    }

    private void readCustomerAccessToken() {
        mCustomerAccessToken = PreferencesUtil.getString(WhatsApplication.getContext(), KeyConstant.TOKEN_CUSTOMER_ACCESS);
    }

    public void writeCustomerAccessToken(String token){
        mCustomerAccessToken = token;
        PreferencesUtil.putString(WhatsApplication.getContext(),KeyConstant.TOKEN_CUSTOMER_ACCESS,token);
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
}

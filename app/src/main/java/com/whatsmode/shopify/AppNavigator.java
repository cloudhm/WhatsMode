package com.whatsmode.shopify;


import android.content.Context;

import com.shopify.graphql.support.ID;
import com.whatsmode.shopify.block.WebActivity;
import com.whatsmode.shopify.block.account.LoginActivity;
import com.whatsmode.shopify.block.cart.CartItem;
import com.whatsmode.shopify.block.cart.CartItemLists;
import com.whatsmode.shopify.block.checkout.CheckoutUpdateActivity;
import com.whatsmode.shopify.block.main.MainActivity;

import java.util.List;

/**
 *  页面跳转控制中心
 */
public class AppNavigator {

    public static void jumpToWebActivity(Context context,String title, String url){
        context.startActivity(WebActivity.newIntent(context, title,url));
    }


    public static void jumpToCheckoutUpdateActivity(Context context, ID id, CartItemLists cartItemList){
        context.startActivity(CheckoutUpdateActivity.newIntent(context,id,cartItemList));
    }


    public static void jumpToMain(LoginActivity loginActivity) {
        loginActivity.startActivity(MainActivity.newIntent(loginActivity));
    }
}

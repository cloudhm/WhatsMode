package com.whatsmode.shopify.block.cart;

import android.text.TextUtils;
import android.util.Base64;
import android.webkit.JavascriptInterface;

import com.whatsmode.library.util.ListUtils;
import com.whatsmode.library.util.PreferencesUtil;
import com.whatsmode.shopify.WhatsApplication;
import com.whatsmode.shopify.common.Constant;
import com.zchu.log.Logger;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class AndroidJs extends Object {
    // 定义JS需要调用的方法
    // 被JS调用的方法必须加入@JavascriptInterface注解
    @JavascriptInterface
    public void addToCart(String json) {
       Logger.e("JS调用了Android的hello方法----" + json);
        if (TextUtils.isEmpty(json)) {
            return;
        }
        // FIXME: 2017/11/30
        try {
            JSONObject jsonObject = new JSONObject(json);
            String title = jsonObject.getString("productTitle");
            String productVariantTitle = jsonObject.getString("productVariantTitle");
            String productVariantID = jsonObject.getString("productVariantID");
            String productVariantImage = jsonObject.getString("productVariantImage");
            String productVariantCompareAtPrice = jsonObject.getString("productVariantCompareAtPrice");
            String productVariantPrice = jsonObject.getString("productVariantPrice");
            String link = jsonObject.getString("link");
            int  quantity = 1;
            if (jsonObject.has("quantity")) {
                quantity = Integer.parseInt(jsonObject.getString("quantity"));
            }
            CartItem cartItem = new CartItem();
            cartItem.quality += quantity;
            cartItem.price = Double.parseDouble(productVariantPrice);
            cartItem.comparePrice = Double.parseDouble(productVariantCompareAtPrice);
            cartItem.colorAndSize = productVariantTitle;
            cartItem.url = link;
            cartItem.icon = productVariantImage;
            cartItem.name = title;
            //gid://shopify/ProductVariant/{productVariant_id}
            String encrpId = "gid://shopify/ProductVariant/" + productVariantID;
            cartItem.id  = new String(Base64.encode(encrpId.getBytes(), Base64.DEFAULT));
            List<CartItem> cartItemList = (List<CartItem>) PreferencesUtil.getObject
                    (WhatsApplication.getContext(), Constant.CART_LOCAL);
            if (ListUtils.isEmpty(cartItemList)) {
                cartItemList = new ArrayList<>();
                cartItemList.add(cartItem);
            }else {
                CartItem modiyItem = null;
                for (CartItem item : cartItemList) {
                    if (item.id.equals(cartItem.id)) {
                        modiyItem = item;
                        break;
                    }
                }
                if (modiyItem == null) {
                    cartItemList.add(cartItem);
                }else{
                    modiyItem.quality += cartItem.quality;
                }
            }
            PreferencesUtil.putObject(WhatsApplication.getContext(), Constant.CART_LOCAL, cartItemList);
            EventBus.getDefault().post(cartItem); // 發送rx消息通知購物車更新
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package com.whatsmode.shopify.common;

import com.whatsmode.shopify.WhatsApplication;

import java.io.File;

/**
 * Created by tom on 17-11-17.
 */

public class Constant {
    public static final int PAGE_SIZE_LOAD_DATA = 10;
    public static final String WEB_PREFIX = "https://whatsmode.com";
    public static final String EMAIL_HAS_ALREADY_BEEN_TAKEN = "Email has already been taken";
    public static final String CREATING_CUSTOMER_LIMIT_EXCEEDED_PLEASE_TRY_AGAIN_LATER = "Creating Customer Limit exceeded. Please try again later.";

    public static final String ROOTPATH_CUSTOMER_USERINFO;
    public static final String USERINFO = "userinfo";
    public static final String URL_TAB_INFLUENCE = "https://whatsmode.com/pages/influencer-brands-1";
    public static final String URL_TAB_MODE = "https://whatsmode.com/";
    public static final String URL_TAB_MODE_TYPE = "https://whatsmode.com";
    public static final int KEY_ACCOUNT_DISMISS = 0x4444;
    public static final String URL_SEARCH = "https://whatsmode.com/search";
    public static final String CART_LOCAL = "cart_local";
    public static final String CART_FRAGMENT_NAME = "com.whatsmode.shopify.block.cart.CartFragment";
    public static final String EXTERNAL = "external";
    public static final String USER_AGENT = "mobile-Android";


    static {
        ROOTPATH_CUSTOMER_USERINFO = WhatsApplication.getContext().getFilesDir().getAbsolutePath()
                + File.separator + "cache";
    }


    public static class Param {
        public static final String EMAIL = "email";

        protected Param(){}
    }

    public static class Event{
        public static final String CREATE_ACCOUNT = "create_account";
        public static final String SIGN_IN = "sign_in";
        public static final String MODE = "mode";  //导航_主页
        public static final String INFLUENCER = "influencer"; ///导航_网红店铺
        public static final String MY_CART = "my_cart";//导航_购物车
        public static final String ACCOUNT = "account";//导航_我的
        public static final String CATEGORY = "category";//分类
        public static final String SREARCH = "srearch";//搜索
        public static final String PRODUCT_LIST = "product_list";//     商品列表
        public static final String PRODUCT_DETAIL = "product_detail";//     商品详情
        public static final String PRODUCT_SHARE = "product_share";//     商品分享
        public static final String SHOP_LIST = "shop_list";//   店铺列表
        public static final String SHOP_SHARE = "shop_share";//  店铺分享
        public static final String NEW_ARRIVALS = "new_arrivals";//新品
        public static final String DISCOVER = "discover";//打折
        public static final String SALE = "sale";//特卖
        public static final String APPAREL = "apparel";//衣服
        public static final String ACCESSORIES = "accessories";//配饰
        public static final String BAGS = "bags";//包
        public static final String SHOES = "shoes";//鞋
        public static final String ABOUT_US = "about_us";//关于我们
        public static final String DELETE = "delete";//product 删除
        public static final String CHECK_OUT = "check_out";//建立订单
        public static final String ORDER_DETAIL = "order_detail";//订单详情
        public static final String SETTING = "setting";//设置
        public static final String PUSH_NOTIFICATION = "push_notification";//通知
        public static final String SIGN_OUT = "sign_out";//登出
        public static final String ADD_TO_CART = "add_to_cart";//加入购物车
        public static final String PROUDCT_DETAIL = "proudct_detail";//商品分享
        public static final String PAY = "pay";//支付
        public static final String MORE = "more";//更多
        public static final String JOIN_US = "join_us";//加入我们
        public static final String CONTACT_US = "contact_us";//联系我们
        public static final String PROMOTE_WITH_US = "promote_with_us";//与我们合作

        protected Event(){}
    }

}

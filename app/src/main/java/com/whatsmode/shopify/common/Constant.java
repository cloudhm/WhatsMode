package com.whatsmode.shopify.common;

import android.os.Environment;

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
    public static final String ROOTPATH_IMAGE_CACHE;
    public static final String USERINFO = "userinfo";
    public static final String DEFAULT_ADDRESS = "default_address";
    public static final String URL_TAB_INFLUENCE = "https://whatsmode.com/pages/influencer-brands-1";
    public static final String URL_TAB_MODE = "https://whatsmode.com/";
    public static final String URL_TAB_MODE_TYPE = "https://whatsmode.com";
    public static final int KEY_ACCOUNT_DISMISS = 0x4444;
    public static final String URL_SEARCH = "https://whatsmode.com/search";
    public static final String CART_LOCAL = "cart_local";
    public static final String CART_FRAGMENT_NAME = "com.whatsmode.shopify.block.cart.CartFragment";
    public static final String EXTERNAL = "external";
    public static final String USER_AGENT = "mobile-Android";
    public static final String ABOUT_US = "https://whatsmode.com/pages/introduce-us";
    public static final String DOWNLOAD_MESSENGER = "https://m.me/283544598790548?ref=messenger_commerce_1163199097047119_https://whatsmode.com/";
    public static final String CHECKOUT_URL = "https://whatsmode.com/23507085/checkouts/";

    public static final String DEFAULT_CONTACT_US = "intent://user/283544598790548/?intent_trigger=mme&ref=messenger_commerce_1163199097047119_https%3A%2F%2Fwhatsmode.com%2F&nav=discover#Intent;scheme=fb-messenger;package=com.facebook.orca;end";
    public static final String DEFAULT_CONTACT_US_REDICT = "https://m.facebook.com/msg/283544598790548/?ref=messenger_commerce_1163199097047119_https%3A%2F%2Fwhatsmode.com%2Fpages%2Fholiday&show_interstitial=0&mdotme_uri=https%3A%2F%2Fm.me%2F283" +
            "544598790548%3Fref%3Dmessenger_commerce_1163199097047119_https%253A%252F%252Fwhatsmode.com%252Fpages%252Fholiday&handler=m.me&referer&refsrc=http%3A%2F%2Fm.me%2F283544598790548%3Fref%3Dmessenger_commerce_1163199097047119_https%253A%252F%252Fwhatsm&_rdr";
    public static final  String influence_arrays[] = {"Adelaine Morin", "Aleksandra Wojtysiak", "Alessandra Cara", "Alexandra Kusio", "Alinagvozdeva", "Amanda Wizping",
            "Anna Albus", "Annabellagutman", "Anotherarabgurl", "Beautyfineprint", "Claudia Dibenedetto", "CloeCouture",
            "Demi Flydi", "Donabogut", "Diandra Maria Picone", "Donabogut", "Donatella Gazze", "Elsalucia", "Emmyinstyle",
            "Essy Park", "Fabiana Russo", "Fabiola Picone", "Federica La Spina", "Fg Nar", "Flavia Berruti", "Gaia Schiavetti",
            "Giovanna Caruso", "Giulia Magno", "Hangngyn", "Hodan.ysf", "Ida Di Filippo", "Jass Dz", "Jennifer Quattrucci", "Jesenia Sosa",
            "Jessica Du", "Jmayo", "Karolayne Alexandre Darosa", "Karolinasworld", "KERSTYN INOUYE", "La Demi", "Liliana Loreggia",
            "Loveyoli", "Majestic At", "Maria Yuzhakova", "Marilyn Hucek", "Marina Del Regno", "Martiiitram", "Minilucie",
            "Missperfectfashion and Beauty", "Mordilyn Chioma Amadi", "Moscolovita", "Mundoflavs", "Natalia Morgillo",
            "Nely Major", "Nicole", "Ossspva", "Pamela Jean Noble", "Pia Adelina", "Radmila", "Rosalba Carnevale", "Sarah Rocksdale",
            "Shanique Wallace", "Simona Musci", "Simplygailg", "Snoopy Kirsten", "Sosarmenia", "Stelaplaka", "stephanie petrey",
            "Stylevinaigrette", "Susanna Giovanardi", "Tar Mar", "The Girl From Mars", "Thecaramelcurly", "The Sassy Jess",
            "Trish Lindo", "Valentina Fantini", "Veronica D'onofrio", "Yasmeena Rasheed", "Zaira D'urso"
    };

    static {
        ROOTPATH_CUSTOMER_USERINFO = WhatsApplication.getContext().getFilesDir().getAbsolutePath()
                + File.separator + "cache";
        //ROOTPATH_IMAGE_CACHE = Environment.getExternalStorageDirectory().getAbsolutePath()
        ROOTPATH_IMAGE_CACHE = WhatsApplication.getContext().getExternalCacheDir().getAbsolutePath()
                + File.separator + "image";
    }

    public static final String SHARE_DEFAULT_IMAGE_PATH = ROOTPATH_IMAGE_CACHE + File.separator + "share";
    public static final String SHARE_DEFAULT_IMAGE_NAME = "default_share.png";

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
        public static final String PRIVACY_POLICY = "privacy_policy";//
        public static final String SHIPPING_DELIVERY = "shipping_delivery";//
        public static final String TERMS_CONDITIONS = "Terms_conditions";//

        protected Event(){}
    }

}

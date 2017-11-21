package com.whatsmode.shopify.common;

import com.whatsmode.shopify.WhatsApplication;

import java.io.File;

/**
 * Created by tom on 17-11-17.
 */

public class Constant {
    public static final int PAGE_SIZE_LOAD_DATA = 10;
    public static final String WEB_PREFIX = "https://whatsmode.com";

    public static final String ROOTPATH_CUSTOMER_USERINFO;
    public static final String USERINFO = "userinfo";

    static {
        ROOTPATH_CUSTOMER_USERINFO = WhatsApplication.getContext().getFilesDir().getAbsolutePath()
                + File.separator + "cache";
    }
}

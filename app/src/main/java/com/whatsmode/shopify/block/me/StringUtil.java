package com.whatsmode.shopify.block.me;

import com.whatsmode.shopify.R;
import com.whatsmode.shopify.WhatsApplication;

/**
 * Created by tom on 17-12-18.
 */

public class StringUtil {
    public static String replaceString(String str){
        switch (str) {
            case "Unidentified customer":
                return WhatsApplication.getContext().getString(R.string.replace_unidentified_customer);
        }
        return str;
    }
}

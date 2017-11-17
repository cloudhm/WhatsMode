package com.whatsmode.shopify.block.address;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by tom on 17-11-17.
 */

public class LoadType {
    public static final int TYPE_REFRESH_SUCCESS = 1;
    public static final int TYPE_LOAD_MORE_SUCCESS = 2;
    public static final int TYPE_LOAD_DATA_END = 3;

    @IntDef({TYPE_REFRESH_SUCCESS,TYPE_LOAD_MORE_SUCCESS,TYPE_LOAD_DATA_END})
    @Retention(RetentionPolicy.SOURCE)
    public @interface checker{
    }
}

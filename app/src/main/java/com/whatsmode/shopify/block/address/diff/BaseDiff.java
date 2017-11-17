package com.whatsmode.shopify.block.address.diff;

import android.support.v7.util.DiffUtil;

import java.util.List;

/**
 * Created by tom on 17-10-30.
 */

public abstract class BaseDiff<T> extends DiffUtil.Callback {

    protected List<T> mOldData;
    protected List<T> mNewData;

    public BaseDiff(List<T> oldData, List<T> newData) {
        mOldData = oldData;
        mNewData = newData;
    }

    @Override
    public int getOldListSize() {
        return mOldData == null ? 0 : mOldData.size();
    }

    @Override
    public int getNewListSize() {
        return mNewData == null ? 0 : mNewData.size();
    }
}

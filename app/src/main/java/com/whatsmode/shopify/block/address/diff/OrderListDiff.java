package com.whatsmode.shopify.block.address.diff;

import com.whatsmode.shopify.block.address.Address;
import com.whatsmode.shopify.block.me.Order;

import java.util.List;

/**
 * Created by tom on 17-10-30.
 */

public class OrderListDiff extends BaseDiff<Order> {
    public OrderListDiff(List<Order> oldData, List<Order> newData) {
        super(oldData, newData);
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        if (getHashCode(mOldData.get(oldItemPosition).getId()) == getHashCode(mNewData.get(newItemPosition).getId())
                ) {
            return true;
        }
        return false;
    }

    private int getHashCode(Object o) {
        if (o != null) {
            return o.hashCode();
        }
        return 0;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return mNewData.get(newItemPosition).equals(mOldData.get(oldItemPosition));
    }
}

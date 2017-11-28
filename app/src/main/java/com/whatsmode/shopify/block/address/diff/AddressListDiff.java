package com.whatsmode.shopify.block.address.diff;

import com.whatsmode.shopify.block.address.Address;

import java.util.List;

/**
 * Created by tom on 17-10-30.
 */

public class AddressListDiff extends BaseDiff<Address> {
    public AddressListDiff(List<Address> oldData, List<Address> newData) {
        super(oldData, newData);
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        if (getHashCode(mOldData.get(oldItemPosition).getId()) == getHashCode(mNewData.get(newItemPosition).getId()) &&
                getHashCode(mOldData.get(oldItemPosition).getAddress1()) == getHashCode(mNewData.get(newItemPosition).getAddress1())&&
                        getHashCode(mOldData.get(oldItemPosition).getAddress2()) == getHashCode(mNewData.get(newItemPosition).getAddress2())&&
                                getHashCode(mOldData.get(oldItemPosition).getCity()) == getHashCode(mNewData.get(newItemPosition).getCity())&&
                                        getHashCode(mOldData.get(oldItemPosition).getProvince()) == getHashCode(mNewData.get(newItemPosition).getProvince())&&
                                                getHashCode(mOldData.get(oldItemPosition).getCountry()) == getHashCode(mNewData.get(newItemPosition).getCountry())&&
                mOldData.get(oldItemPosition).isDefault() == mNewData.get(newItemPosition).isDefault()
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

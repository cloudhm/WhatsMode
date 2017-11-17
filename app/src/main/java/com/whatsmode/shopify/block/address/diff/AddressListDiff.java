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
        if (mOldData.get(oldItemPosition).getId().hashCode() == mNewData.get(newItemPosition).getId().hashCode() &&
        mOldData.get(oldItemPosition).getAddress1().hashCode() == mNewData.get(newItemPosition).getAddress1().hashCode()&&
                mOldData.get(oldItemPosition).getAddress2().hashCode() == mNewData.get(newItemPosition).getAddress2().hashCode()&&
                mOldData.get(oldItemPosition).getCity().hashCode() == mNewData.get(newItemPosition).getCity().hashCode()&&
                mOldData.get(oldItemPosition).getProvince().hashCode() == mNewData.get(newItemPosition).getProvince().hashCode()&&
                mOldData.get(oldItemPosition).getCountry().hashCode() == mNewData.get(newItemPosition).getCountry().hashCode()
                ) {
            return true;
        }
        return false;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return mNewData.get(newItemPosition).equals(mOldData.get(oldItemPosition));
    }
}

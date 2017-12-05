package com.whatsmode.shopify.block.address;

import android.support.v7.util.DiffUtil;

import com.whatsmode.shopify.R;
import com.whatsmode.shopify.block.address.diff.AddressListDiff;
import com.whatsmode.shopify.ui.helper.CommonAdapter;
import com.whatsmode.shopify.ui.helper.CommonViewHolder;

import java.util.List;

/**
 * Created by tom on 17-11-17.
 */

public class AddressListAdapter extends CommonAdapter<Address> {
    public AddressListAdapter(int layoutResId, List<Address> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(CommonViewHolder helper, Address item) {
        String joinAddress = AddressUtil.getJoinAddress(item);
        helper.setText(R.id.name,item.getName())
                .setText(R.id.phone,item.getPhone())
                .setText(R.id.address,joinAddress)
                .addOnClickListener(R.id.delete)
                .addOnClickListener(R.id.update)
                .addOnClickListener(R.id.is_default)
                .setChecked(R.id.is_default,item.isDefault());
        if (item.isDefault()) {
            helper.setText(R.id.is_default_desc,R.string.default_address);
        }else{
            helper.setText(R.id.is_default_desc,R.string.set_as_default);
        }
    }

    public void refresh(List<Address> list) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new AddressListDiff(getData(), list), true);
        getData().clear();
        getData().addAll(list);
        diffResult.dispatchUpdatesTo(this);
    }
}

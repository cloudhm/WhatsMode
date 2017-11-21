package com.whatsmode.shopify.block.me;

import com.whatsmode.shopify.R;
import com.whatsmode.shopify.ui.helper.CommonAdapter;
import com.whatsmode.shopify.ui.helper.CommonViewHolder;

import java.util.List;

/**
 * Created by tom on 17-11-21.
 */

public class OrderListAdapter extends CommonAdapter<Order> {
    public OrderListAdapter(int layoutResId, List<Order> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(CommonViewHolder helper, Order item) {
        helper.setText(R.id.name, "test");
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }
}

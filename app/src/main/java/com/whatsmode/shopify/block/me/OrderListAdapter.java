package com.whatsmode.shopify.block.me;

import android.support.v7.util.DiffUtil;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.whatsmode.shopify.R;
import com.whatsmode.shopify.block.address.Address;
import com.whatsmode.shopify.block.address.diff.AddressListDiff;
import com.whatsmode.shopify.block.address.diff.OrderListDiff;
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
        helper.setText(R.id.order_num, "OrderNumber: "+String.valueOf(item.getOrderNumber()))
            .setText(R.id.price,"TotalPrice: "+String.valueOf(item.getTotalPrice().intValue()));
        List<LineItem> lineItems = item.getLineItems();
        String src = null;
        if ((src = getSrc(lineItems)) != null) {
            Glide.with(mContext).load(src).placeholder(R.mipmap.ic_launcher).into((ImageView) helper.getView(R.id.image_view));
        }else{
            Glide.with(mContext).load(R.mipmap.ic_launcher).placeholder(R.mipmap.ic_launcher).into((ImageView) helper.getView(R.id.image_view));
        }

    }

    private String getSrc(List<LineItem> lineItems){
        if(lineItems.isEmpty()) return null;
        for (LineItem lineItem : lineItems) {
            LineItem.Variant variant = lineItem.getVariant();
            if (variant != null) {
                if (variant.getImage() != null) {
                    return variant.getImage().getSrc();
                }
            }
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    public void refresh(List<Order> orders) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new OrderListDiff(getData(), orders), true);
        getData().clear();
        getData().addAll(orders);
        diffResult.dispatchUpdatesTo(this);
    }
}

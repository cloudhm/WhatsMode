package com.whatsmode.shopify.block.me;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.whatsmode.shopify.R;
import com.whatsmode.shopify.ui.helper.CommonAdapter;
import com.whatsmode.shopify.ui.helper.CommonViewHolder;

import java.util.List;

/**
 * Created by tom on 17-11-23.
 */

public class LineItemAdapter extends CommonAdapter<LineItem> {
    public LineItemAdapter(int layoutResId, List<LineItem> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(CommonViewHolder helper, LineItem item) {
        LineItem.Variant variant = item.getVariant();
        String price = String.valueOf(variant == null ? 0 : variant.getPrice());
        helper.setText(R.id.title,item.getTitle())
                .setText(R.id.price,price)
                .setText(R.id.quantity,String.valueOf(item.getQuantity()));
        String src = null;
        if ((src = getSrc(variant)) != null) {
            Glide.with(mContext).load(src).placeholder(R.mipmap.ic_launcher).into((ImageView) helper.getView(R.id.image_view));
        }else{
            Glide.with(mContext).load(R.mipmap.ic_launcher).placeholder(R.mipmap.ic_launcher).into((ImageView) helper.getView(R.id.image_view));
        }
    }

    private String getSrc(LineItem.Variant variant){
        if (variant != null) {
            if (variant.getImage() != null) {
                return variant.getImage().getSrc();
            }
        }
        return null;
    }
}

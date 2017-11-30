package com.whatsmode.shopify.block.me;

import android.graphics.Paint;
import android.widget.ImageView;
import android.widget.TextView;

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
        String compareAtPrice = String.valueOf(variant == null ? 0 : variant.getCompareAtPrice());
        helper.setText(R.id.title,item.getTitle())
                .setText(R.id.price,mContext.getString(R.string.dollar_sign) + price)
                .setText(R.id.quantity,"x"+String.valueOf(item.getQuantity()))
                .setText(R.id.sku,mContext.getString(R.string.sku_) + (variant == null ? "" : variant.getSku()))
                .setText(R.id.model,getSelectOptions(variant != null ? variant.getSelectedOptions() : null))
                .setText(R.id.original_price,mContext.getString(R.string.dollar_sign) + compareAtPrice + mContext.getString(R.string.usd));
        ((TextView)helper.getView(R.id.original_price)).getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        String src = null;
        if ((src = getSrc(variant)) != null) {
            Glide.with(mContext).load(src).placeholder(R.mipmap.ic_launcher).into((ImageView) helper.getView(R.id.image_view));
        }else{
            Glide.with(mContext).load(R.mipmap.ic_launcher).placeholder(R.mipmap.ic_launcher).into((ImageView) helper.getView(R.id.image_view));
        }
    }

    private String getSelectOptions(List<LineItem.Variant.SelectedOptions> selectedOptions){
        if (selectedOptions == null) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (LineItem.Variant.SelectedOptions selectedOption : selectedOptions) {
            builder.append(selectedOption.getValue()).append("/");
        }
        if (builder.length() > 1) {
            builder.replace(builder.length() - 1 ,builder.length() - 0,"");
        }
        return builder.toString();
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

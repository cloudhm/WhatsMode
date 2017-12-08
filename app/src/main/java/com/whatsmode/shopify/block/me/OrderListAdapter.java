package com.whatsmode.shopify.block.me;

import android.support.v7.util.DiffUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HeaderViewListAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.whatsmode.library.util.ScreenUtils;
import com.whatsmode.library.util.Util;
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
    public CommonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    protected void convert(CommonViewHolder helper, Order item) {
        List<LineItem> lineItems = item.getLineItems();
        helper.setText(R.id.order_num, "Order#"+String.valueOf(item.getOrderNumber()))
            .setText(R.id.price,"Total:$"+String.valueOf(item.getTotalPrice().doubleValue()))
            .setText(R.id.items,String.valueOf(lineItems == null ? 0 : lineItems.size()) + " items");

        ClickRecyclerView recyclerView = helper.getView(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.HORIZONTAL,false));
        recyclerView.setAdapter(new ImageAdapter(R.layout.item_image_view,lineItems));
        recyclerView.setOnRecyclerClickLinstener(new ClickRecyclerView.OnRecyclerClickLinstener() {
            @Override
            public void onRecyclerClick() {
                int position = helper.getLayoutPosition() - getHeaderLayoutCount();
                mMyFragment.onItemClick(OrderListAdapter.this,helper.itemView,position);
            }
        });

    }

    MyFragment mMyFragment;

    public void setFragment(MyFragment myFragment) {
        mMyFragment = myFragment;
    }

    class ImageAdapter extends CommonAdapter<LineItem>{

        public ImageAdapter(int layoutResId, List<LineItem> data) {
            super(layoutResId, data);
        }

        @Override
        public CommonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if(viewType == BaseQuickAdapter.HEADER_VIEW) super.onCreateViewHolder(parent,viewType);
            CommonViewHolder commonViewHolder = super.onCreateViewHolder(parent, viewType);
            ImageView imageView = commonViewHolder.getView(R.id.image_view);
            ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
            if (layoutParams == null) {
                layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }
            int screenWidth = ScreenUtils.getScreenWidth(mContext);
            int mar = ScreenUtils.dip2px(mContext, 16 + 16 + 8 * 4);
            int width = (screenWidth - mar) / 4;
            layoutParams.width = width;
            layoutParams.height = (int)(width * 1.5);
            imageView.setLayoutParams(layoutParams);
            return commonViewHolder;
        }

        @Override
        protected void convert(CommonViewHolder helper, LineItem item) {
            LineItem.Variant variant = item.getVariant();
            if (variant != null && variant.getImage() != null) {
                String url = variant.getImage().getSrc();
                Glide.with(mContext).load(url).placeholder(R.drawable.default_mode).into((ImageView) helper.getView(R.id.image_view));
            }else{
                Glide.with(mContext).load(R.drawable.default_mode).placeholder(R.drawable.default_mode).into((ImageView) helper.getView(R.id.image_view));
            }
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

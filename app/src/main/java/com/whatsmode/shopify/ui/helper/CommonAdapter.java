package com.whatsmode.shopify.ui.helper;

import android.content.Context;
import android.support.annotation.IdRes;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

public abstract class CommonAdapter<T> extends BaseQuickAdapter<T, CommonViewHolder> {

    public ItemClickInterface itemClickInterface;
    protected CommonViewHolder holder;

    public CommonAdapter(int layoutResId, List<T> data) {
        super(layoutResId, data);
    }

    public CommonAdapter(List<T> data) {
        super(data);
    }

    protected CommonViewHolder createBaseViewHolder(ViewGroup parent, int layoutResId) {
        holder = this.createBaseViewHolder(this.getItemView(layoutResId, parent));
        return holder;
    }

    protected CommonViewHolder createBaseViewHolder(View view) {
        holder = new CommonViewHolder(view);
        return holder;
    }

    public Context getContext() {
        return mContext;

    }

    public interface ItemClickInterface{
        void onItemClick(@IdRes int viewId, Object data);
    }

    public void setItemClickInterface(ItemClickInterface itemClickInterface){
        this.itemClickInterface = itemClickInterface;
    }

    /*@Override
    protected void convert(CommonViewHolder holder, T t) {
        this.holder = holder;
        if(itemClickInterface != null){
            holder.setChildClickListener(itemClickInterface);
        }
    }*/
}

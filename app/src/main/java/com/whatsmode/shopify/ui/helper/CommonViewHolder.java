package com.whatsmode.shopify.ui.helper;

import android.net.Uri;
import android.support.annotation.IdRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseViewHolder;
import com.whatsmode.shopify.R;

import java.io.File;

public class CommonViewHolder extends BaseViewHolder {
    public CommonAdapter.ItemClickInterface itemClickInterface;
    public CommonViewHolder(View view) {
        super(view);
    }

    public CommonViewHolder loadImage(@IdRes int viewId, String string){
        if (TextUtils.isEmpty(string)) {
            Glide.with(convertView.getContext())
                    .load(R.mipmap.ic_launcher)
                    .into((ImageView) getView(viewId));
        }else {
            Glide
                    .with(convertView.getContext())
                    .load(string)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存全尺寸
                    .into((ImageView) getView(viewId));
        }
        return  this;
    }

    public CommonViewHolder loadImage(@IdRes int viewId, Uri uri){
        Glide
                .with(convertView.getContext())
                .load(uri)
                .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存全尺寸
                .into((ImageView) getView(viewId));
        return  this;
    }

    public CommonViewHolder loadImage(@IdRes int viewId, File file){
        Glide
                .with(convertView.getContext())
                .load(file)
                .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存全尺寸
                .into((ImageView) getView(viewId));
        return  this;
    }

    public CommonViewHolder loadImage(@IdRes int viewId, byte[] model){
        Glide
                .with(convertView.getContext())
                .load(model)
                .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存全尺寸
                .into((ImageView) getView(viewId));
        return  this;
    }

    public CommonViewHolder setVisibility(@IdRes int viewId, int visibility){
        getView(viewId).setVisibility(visibility);
        return this;
    }


    public void addChildClickListener(final @IdRes int viewId, final Object data) {
        getView(viewId).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(itemClickInterface != null){
                    itemClickInterface.onItemClick(viewId, data);
                }
            }
        });

    }

    public void setChildClickListener(CommonAdapter.ItemClickInterface itemClickInterface){
        this.itemClickInterface = itemClickInterface;
    }
}

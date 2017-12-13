package com.whatsmode.shopify.ui.helper;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.whatsmode.shopify.R;
import com.whatsmode.shopify.WhatsApplication;

public class LoadingDialog extends Dialog {

    private Context mContext;
    private ImageView ivLoading;
    private AnimationDrawable frameAnim;

    public LoadingDialog(Context context) {
        super(context, R.style.LoadingDialog);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        return false;
    }

    public void init() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.loading_dialog, null);
        ivLoading = (ImageView) view.findViewById(R.id.loading);
        // 把AnimationDrawable设置为ImageView的背景
        AnimationDrawable drawable = (AnimationDrawable) ivLoading.getDrawable();
        drawable.start();
        setContentView(view);
        setCanceledOnTouchOutside(true);
        setCancelable(true);
    }

    public void show() {
        super.show();


    }

    public void dismiss() {
        super.dismiss();
    }

}

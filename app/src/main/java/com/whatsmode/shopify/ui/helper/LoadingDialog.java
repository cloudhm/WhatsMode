package com.whatsmode.shopify.ui.helper;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;

import com.whatsmode.shopify.R;


/**
 * Created by tom on 17-8-10
 */

public class LoadingDialog extends Dialog {

    private Context mContext;

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

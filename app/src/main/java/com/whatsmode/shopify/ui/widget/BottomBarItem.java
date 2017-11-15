package com.whatsmode.shopify.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.whatsmode.shopify.R;

public class BottomBarItem extends RelativeLayout {


    private ImageView iconView;
    private TextView textView;

    private Drawable mIconNormal;
    private Drawable mIconSelected;
    private String mText;
    private int mTextSize = 12;//sp
    private int mTextColorNormal = 0xFF909090;
    private int mTextColorSelected = 0xff1976D2;

    public BottomBarItem(Context context) {
        this(context, null);
    }

    public BottomBarItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomBarItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //获取所有的自定义属性
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BottomBarItem);
        TypedValue typedValue = new TypedValue();
        mIconNormal = a.getDrawable(R.styleable.BottomBarItem_bbi_iconNormal);
        if (mIconNormal instanceof VectorDrawableCompat) {
            a.getValue(R.styleable.BottomBarItem_bbi_iconNormal, typedValue);
            mIconNormal = VectorDrawableCompat.create(a.getResources(), typedValue.resourceId, null);
        }
        mIconSelected = a.getDrawable(R.styleable.BottomBarItem_bbi_iconSelected);
        if (mIconSelected instanceof VectorDrawableCompat) {
            a.getValue(R.styleable.BottomBarItem_bbi_iconSelected, typedValue);
            mIconSelected = VectorDrawableCompat.create(a.getResources(), typedValue.resourceId, null);
        }
        mText = a.getString(R.styleable.BottomBarItem_bbi_text);
        mTextSize = a.getDimensionPixelSize(R.styleable.BottomBarItem_bbi_textSize, mTextSize);
        mTextColorNormal = a.getColor(R.styleable.BottomBarItem_bbi_textColorNormal, mTextColorNormal);
        mTextColorSelected = a.getColor(R.styleable.BottomBarItem_bbi_textColorSelected, mTextColorSelected);
        a.recycle();

        initView();

    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_bottombar_item,
                this, true);
        iconView = (ImageView) findViewById(R.id.bottomBar_item_iconView);
        textView = (TextView) findViewById(R.id.bottomBar_item_textView);
        textView.setText(mText);
        textView.setTextSize(mTextSize);
        setSelectedState(isSelected());
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        setSelectedState(selected);

    }

    private void setSelectedState(boolean selected) {
        if (selected) {
            iconView.setImageDrawable(mIconSelected);
            textView.setTextColor(mTextColorSelected);
        } else {
            iconView.setImageDrawable(mIconNormal);
            textView.setTextColor(mTextColorNormal);

        }
    }
}

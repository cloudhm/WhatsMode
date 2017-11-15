package com.whatsmode.shopify.ui.widget;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;


public class BottomBar extends LinearLayout {

    private ViewPager viewPager;

    /**
     * 上一次选中的条目索引
     */
    private int mLastSelectedIndex = -1;

    public BottomBar(Context context) {
        super(context);
    }

    public BottomBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BottomBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void attachView(ViewPager viewPager) {
        this.viewPager = viewPager;
        initView();
    }

    private void initView() {
        if (viewPager == null) {
            throw new IllegalArgumentException("参数不能为空");
        }
       int childCount = getChildCount();
        if (viewPager.getAdapter().getCount() != childCount) {
            throw new IllegalArgumentException("BottomBar的子View数量必须和ViewPager条目数量一致");
        }
        for (int i = 0; i < childCount; i++) {
            if (getChildAt(i) instanceof BottomBarItem) {
                BottomBarItem bottomBarItem = (BottomBarItem) getChildAt(i);
                //alphaViews.add(bottomBarItem);
                //设置点击监听
                bottomBarItem.setOnClickListener(new ItemOnClickListener(i));
            } else {
                throw new IllegalArgumentException("BottomBar的子View必须是BottomBarItem");
            }
        }
        //对ViewPager添加监听
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setSelectedItem(position);
            }
        });
        setSelectedItem(viewPager.getCurrentItem());
    }


    private class ItemOnClickListener implements OnClickListener {
        private int currentIndex;

        public ItemOnClickListener(int i) {
            currentIndex = i;
        }

        @Override
        public void onClick(View v) {
            viewPager.setCurrentItem(currentIndex,false);
        }
    }

    /**
     * 设置按钮的状态
     */
    private void setSelectedItem(int index) {
        if (mLastSelectedIndex != -1) {
            getChildAt(mLastSelectedIndex).setSelected(false);
        }
        getChildAt(index).setSelected(true);
        mLastSelectedIndex =index;
    }


    private static final String STATE_INSTANCE = "instance_state";
    private static final String STATE_ITEM = "state_item";

    /**
     * @return 当View被销毁的时候，保存数据
     */
    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(STATE_INSTANCE, super.onSaveInstanceState());
        bundle.putInt(STATE_ITEM, viewPager.getCurrentItem());
        return bundle;
    }

    /**
     * @param state 用于恢复数据使用
     */
    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            setSelectedItem( bundle.getInt(STATE_ITEM));
            super.onRestoreInstanceState(bundle.getParcelable(STATE_INSTANCE));
        } else {
            super.onRestoreInstanceState(state);
        }
    }
}

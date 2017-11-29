package com.whatsmode.shopify.block.me;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by tom on 17-11-28.
 */

public class ClickRecyclerView extends RecyclerView {
    public ClickRecyclerView(Context context) {
        super(context);
    }

    public ClickRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ClickRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private long downTime;
    private boolean clickValid;

    private int mStartX;
    private int mStartY;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                downTime = System.currentTimeMillis();
                clickValid = true;
                mStartX = (int) ev.getRawX();
                mStartY = (int) ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                int endX = (int) ev.getRawX();
                int endY = (int) ev.getRawY();
                int offsetX = endX - mStartX;
                int offsetY = endY - mStartY;
                if(Math.abs(offsetX) > Math.abs(offsetY)){
                    clickValid = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (System.currentTimeMillis() - downTime < 150 && clickValid) {
                    if (mOnRecyclerClickLinstener != null) {
                        mOnRecyclerClickLinstener.onRecyclerClick();
                    }
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    private OnRecyclerClickLinstener mOnRecyclerClickLinstener;

    public interface OnRecyclerClickLinstener{
        void onRecyclerClick();
    }

    public void setOnRecyclerClickLinstener(OnRecyclerClickLinstener linstener) {
        mOnRecyclerClickLinstener = linstener;
    }
}

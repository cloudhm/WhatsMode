/**
 * author：caoyamin
 * email：yamin.cao@whatsmode.com
 * Copyright © 2016 Yedao Inc. All rights reserved.
 */

package com.whatsmode.shopify.ui.helper;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;


public class SwipeLoadingLayout extends FrameLayout implements SwipeLoadMoreTrigger, SwipeTrigger {

    public SwipeLoadingLayout(Context context) {
        this(context, null);
    }

    public SwipeLoadingLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeLoadingLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onLoadMore() {
    }

    @Override
    public void onPrepare() {
    }

    @Override
    public void onMove(int y, boolean isComplete, boolean automatic) {
    }

    @Override
    public void onRelease() {

    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onReset() {
    }
}

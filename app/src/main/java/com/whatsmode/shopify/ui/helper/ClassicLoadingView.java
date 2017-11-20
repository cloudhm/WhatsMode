/**
 * author：caoyamin
 * email：yamin.cao@whatsmode.com
 * Copyright © 2016 Yedao Inc. All rights reserved.
 */


package com.whatsmode.shopify.ui.helper;

import android.content.Context;
import android.util.AttributeSet;

import com.whatsmode.shopify.R;
import com.whatsmode.shopify.ui.widget.ProgressBarCircularIndeterminate;


public class ClassicLoadingView extends SwipeLoadingLayout {

    private ProgressBarCircularIndeterminate progressBar;

    //private int mFooterHeight;

    public ClassicLoadingView(Context context) {
        this(context, null);
    }

    public ClassicLoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClassicLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
       // mFooterHeight = getResources().getDimensionPixelOffset(R.dimen.load_more_footer_height_classic);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        progressBar = (ProgressBarCircularIndeterminate) findViewById(R.id.progressbar);
    }

    @Override
    public void onPrepare() {
        progressBar.setVisibility(VISIBLE);
    }

    @Override
    public void onMove(int y, boolean isComplete, boolean automatic) {
        if (!isComplete) {
            progressBar.setVisibility(VISIBLE);
        }
    }

    @Override
    public void onLoadMore() {
        progressBar.setVisibility(VISIBLE);
    }

    @Override
    public void onRelease() {
        progressBar.setVisibility(VISIBLE);
    }

    @Override
    public void onComplete() {
        progressBar.setVisibility(GONE);
    }

    @Override
    public void onReset() {
    }
}

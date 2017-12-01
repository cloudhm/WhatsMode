package com.whatsmode.shopify.ui.helper;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.whatsmode.library.util.DisplayUtil;
import com.whatsmode.shopify.R;

public class ToolbarHelper {

    /**
     * 初始化toolbar并适配沉淀式状态栏
     */
    public static ToolbarHolder initToolbar(@NonNull final AppCompatActivity activity, @IdRes int toolbarId, boolean canBack, @Nullable CharSequence title) {
        ToolbarHolder toolbarHolder = new ToolbarHolder();
        Toolbar toolbar = (Toolbar) activity.findViewById(toolbarId);
        toolbar.getLayoutParams().height = (int) (activity.getResources().getDimension(R.dimen.toolbar_height)) + DisplayUtil.getStateBarHeight(activity);
        toolbar.setPadding(0, DisplayUtil.getStateBarHeight(activity),0,0);
        toolbar.requestLayout();
        toolbarHolder.toolbar=toolbar;
        if (toolbar == null) {
            throw new IllegalStateException(
                    "The subclass of ToolbarActivity must contain a toolbar.");
        }
        activity.setSupportActionBar(toolbar);
        toolbarHolder.titleView= (TextView) toolbar.findViewById(R.id.toolbar_title);
        if (title != null) {
            toolbarHolder.titleView.setText(title);
        }

        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            if (canBack) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                toolbar.setNavigationIcon(VectorDrawableCompat.create(activity.getResources(), R.drawable.icon_back,activity.getTheme()));
                toolbar.setNavigationOnClickListener(v -> activity.onBackPressed());
            }

        }
        return toolbarHolder;
    }

    /**
     * 初始化toolbar不适配沉淀式状态栏
     */
    public static ToolbarHolder initToolbarNoFix(@NonNull final AppCompatActivity activity, @IdRes int toolbarId, boolean canBack, @Nullable CharSequence title) {
        ToolbarHolder toolbarHolder = new ToolbarHolder();
        Toolbar toolbar = (Toolbar) activity.findViewById(toolbarId);
        toolbarHolder.toolbar=toolbar;
        if (toolbar == null) {
            throw new IllegalStateException(
                    "The subclass of ToolbarActivity must contain a toolbar.");
        }
        activity.setSupportActionBar(toolbar);
        toolbarHolder.titleView= (TextView) toolbar.findViewById(R.id.toolbar_title);
        if (title != null) {
            toolbarHolder.titleView .setText(title);
        }

        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            if (canBack) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                toolbar.setNavigationIcon(VectorDrawableCompat.create(activity.getResources(), R.drawable.icon_back,activity.getTheme()));
                toolbar.setNavigationOnClickListener(v -> activity.onBackPressed());
            }

        }
        return toolbarHolder;
    }
    public static final class ToolbarHolder{
        public Toolbar toolbar;
        public TextView titleView;
    }
}

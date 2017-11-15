package com.whatsmode.shopify.block.main;

import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.whatsmode.shopify.R;
import com.whatsmode.shopify.mvp.MvpActivity;
import com.whatsmode.shopify.ui.helper.BaseFragmentAdapter;
import com.whatsmode.shopify.ui.helper.ToolbarHelper;
import com.whatsmode.shopify.ui.widget.BottomBar;
import com.whatsmode.shopify.ui.widget.NoScrollViewPager;

public class MainActivity extends MvpActivity<MainContact.Presenter> implements MainContact.View {

    private NoScrollViewPager vpContent;
    private Toolbar toolbar;
    private TextView toolbarTitle;
    private BottomBar bottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        vpContent = (NoScrollViewPager) findViewById(R.id.vp_content);
        vpContent.setOffscreenPageLimit(3);
        ToolbarHelper.ToolbarHolder toolbarHolder = ToolbarHelper.initToolbarNoFix(this, R.id.toolbar, false, null);
        toolbar = toolbarHolder.toolbar;
        toolbarTitle = toolbarHolder.titleView;
        bottomBar = (BottomBar)findViewById(R.id.bottomBar);
        getPresenter().initViewPage(getSupportFragmentManager());
    }

    @NonNull
    @Override
    public MainContact.Presenter createPresenter() {
        return new MainPresenter();
    }

    @Override
    public void setViewPage(BaseFragmentAdapter fragmentAdapter) {
        vpContent.setAdapter(fragmentAdapter);
        toolbarTitle.setText(fragmentAdapter.getPageTitle(vpContent.getCurrentItem()));
        vpContent.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                getPresenter().setPageSelected(position);

            }
        });
        bottomBar.attachView(vpContent);
    }

    @Override
    public void setToolbarTitle(int mPageTitle) {
        toolbarTitle.setText(mPageTitle);
    }


    @Override
    public void switch2Mode() {
        //switchMenu(menuItemSearch);
        if (toolbarTitle.getVisibility() != View.VISIBLE) {
            toolbarTitle.setVisibility(View.VISIBLE);
        }
        toolbar.setVisibility(View.VISIBLE);
    }

    @Override
    public void switch2Influence() {

    }

    @Override
    public void switch2Cart() {

    }

    @Override
    public void switch2Mine() {

    }
}

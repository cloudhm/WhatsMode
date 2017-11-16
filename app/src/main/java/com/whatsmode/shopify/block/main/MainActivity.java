package com.whatsmode.shopify.block.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.whatsmode.library.util.ScreenUtils;
import com.whatsmode.shopify.R;
import com.whatsmode.shopify.mvp.MvpActivity;
import com.whatsmode.shopify.ui.helper.BaseFragmentAdapter;
import com.whatsmode.shopify.ui.helper.ToolbarHelper;
import com.whatsmode.shopify.ui.widget.BottomBar;
import com.whatsmode.shopify.ui.widget.NoScrollViewPager;

public class MainActivity extends MvpActivity<MainContact.Presenter> implements MainContact.View, View.OnClickListener {

    private NoScrollViewPager vpContent;
    private Toolbar toolbar;
    private TextView toolbarTitle;
    private BottomBar bottomBar;
    private ImageView ivSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        vpContent = (NoScrollViewPager) findViewById(R.id.vp_content);
        vpContent.setOffscreenPageLimit(3);
        ivSearch = (ImageView) findViewById(R.id.search);
        ivSearch.setOnClickListener(this);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem menuItemSearch = menu.findItem(R.id.action_search);
        menuItemSearch.setVisible(true);
        getPresenter().setPageSelected(0); //在这里设置最初选中页
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return getPresenter().clickMenuItem(item.getItemId()) || super.onOptionsItemSelected(item);
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

    private PopupWindow popupWindow;
    @Override
    public void showSearch() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }else {
            initPopWindowView();
            popupWindow.showAsDropDown(ivSearch,0,5);
        }
    }

    private void initPopWindowView() {
        View customView = getLayoutInflater().inflate(R.layout.search_menu,
                null, false);
        popupWindow = new PopupWindow(customView, ScreenUtils.getScreenWidth(this), ScreenUtils.dip2px(this,250));
        popupWindow.setAnimationStyle(R.style.AnimationFade);
    }

    @Override
    public void showAbout() {
    }

    @Override
    public void onClick(View v) {
        getPresenter().onClickView(v);
    }
}

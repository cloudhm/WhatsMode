package com.whatsmode.shopify.block.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.innodroid.expandablerecycler.ExpandableRecyclerAdapter;
import com.whatsmode.library.util.ScreenUtils;
import com.whatsmode.shopify.R;
import com.whatsmode.shopify.ui.helper.CategoryAdapter;
import com.whatsmode.shopify.mvp.MvpActivity;
import com.whatsmode.shopify.ui.helper.BaseFragmentAdapter;
import com.whatsmode.shopify.ui.helper.SoftInputHandler;
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
        ivSearch.setVisibility(View.VISIBLE);
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
        getPresenter().setPageSelected(0);
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
            popupWindow.setFocusable(true);
            popupWindow.showAsDropDown(toolbar,0,0);
            ivSearch.setEnabled(false);
        }
    }

    private void initPopWindowView() {
        View customView = getLayoutInflater().inflate(R.layout.search_menu,null, false);
        EditText searchContent = (EditText) customView.findViewById(R.id.searchContent);
        RecyclerView recycler = (RecyclerView) customView.findViewById(R.id.recycleView);
        CategoryAdapter adapter = new CategoryAdapter(this);
        adapter.setMode(ExpandableRecyclerAdapter.MODE_ACCORDION);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(adapter);
        searchContent.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                SoftInputHandler.showInputMethodForQuery(MainActivity.this,searchContent);
            }else{
                SoftInputHandler.hideInputMethod(MainActivity.this,searchContent);
            }
        });
        searchContent.setOnEditorActionListener((v, actionId, event) -> actionId == EditorInfo.IME_ACTION_GO);
        popupWindow = new PopupWindow(customView, ScreenUtils.dip2px(this,225), ScreenUtils.getScreenHeight(this));
        popupWindow.setAnimationStyle(R.style.AnimationFade);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setOnDismissListener(() -> ivSearch.postDelayed(() -> ivSearch.setEnabled(true),1));
    }

    @Override
    public void showAbout() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
        popupWindow = null;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
    }

    @Override
    public void onClick(View v) {
        getPresenter().onClickView(v);
    }
}

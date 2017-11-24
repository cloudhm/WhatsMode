package com.whatsmode.shopify.block.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.innodroid.expandablerecycler.ExpandableRecyclerAdapter;
import com.whatsmode.library.util.ScreenUtils;
import com.whatsmode.shopify.R;
import com.whatsmode.shopify.block.cart.CartFragment;
import com.whatsmode.shopify.mvp.MvpActivity;
import com.whatsmode.shopify.ui.helper.BaseFragmentAdapter;
import com.whatsmode.shopify.ui.helper.CategoryAdapter;
import com.whatsmode.shopify.ui.helper.ToolbarHelper;
import com.whatsmode.shopify.ui.widget.BottomBar;
import com.whatsmode.shopify.ui.widget.NoScrollViewPager;

public class MainActivity extends MvpActivity<MainContact.Presenter> implements MainContact.View, View.OnClickListener {

    private NoScrollViewPager vpContent;
    private Toolbar toolbar;
    private TextView toolbarTitle;
    private BottomBar bottomBar;
    private ImageView ivMenu;
    private MenuItem menuItemSearch;
    private MenuItem menuItemDelete;
    private MenuItem mTempMenu;
    private BaseFragmentAdapter fragmentAdapter;
    private LinearLayout layoutParent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        layoutParent = (LinearLayout) findViewById(R.id.parent);
        vpContent = (NoScrollViewPager) findViewById(R.id.vp_content);
        vpContent.setOffscreenPageLimit(3);
        ivMenu = (ImageView) findViewById(R.id.menu);
        ivMenu.setVisibility(View.VISIBLE);
        ivMenu.setOnClickListener(this);
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
        menuItemSearch = menu.findItem(R.id.action_search);
        menuItemSearch.setVisible(true);
        menuItemDelete = menu.findItem(R.id.action_delete);
        getPresenter().setPageSelected(0);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return getPresenter().clickMenuItem(item.getItemId()) || super.onOptionsItemSelected(item);
    }

    @Override
    public void setViewPage(BaseFragmentAdapter fragmentAdapter) {
        this.fragmentAdapter = fragmentAdapter;
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
        switchMenu(menuItemSearch);
        if (toolbarTitle.getVisibility() != View.VISIBLE) {
            toolbarTitle.setVisibility(View.VISIBLE);
        }
        toolbar.setVisibility(View.VISIBLE);
        ivMenu.setVisibility(View.VISIBLE);
    }

    private void switchMenu(MenuItem menuItem) {
        if (mTempMenu != null) {
            mTempMenu.setVisible(false);
        }
        if (menuItem != null) {
            menuItem.setVisible(true);
            mTempMenu = menuItem;
        }
    }

    @Override
    public void switch2Influence() {
        switchMenu(menuItemSearch);
        ivMenu.setVisibility(View.VISIBLE);
    }

    @Override
    public void switch2Cart() {
        switchMenu(menuItemDelete);
        ivMenu.setVisibility(View.GONE);
    }

    @Override
    public void switch2Mine() {
        switchMenu(null);
        ivMenu.setVisibility(View.GONE);
    }

    private PopupWindow popupWindow;
    @Override
    public void showSearch() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }else {
            initPopWindowView();
            popupWindow.setFocusable(true);
//            popupWindow.showAtLocation(parentLayout,0,0);
            popupWindow.showAtLocation(layoutParent, Gravity.BOTTOM,0,0);
//            popupWindow.showAsDropDown(layoutParent,0,0);
            ivMenu.setEnabled(false);
        }
    }

    private void initPopWindowView() {
        View customView = getLayoutInflater().inflate(R.layout.category_menu,null, false);
        RecyclerView recycler = (RecyclerView) customView.findViewById(R.id.recycleView);
        CategoryAdapter adapter = new CategoryAdapter(this);
        adapter.setMode(ExpandableRecyclerAdapter.MODE_ACCORDION);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(adapter);
        popupWindow = new PopupWindow(customView, ScreenUtils.dip2px(this,225), ScreenUtils.getScreenHeight(this));
        popupWindow.setAnimationStyle(R.style.AnimationFade);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setOnDismissListener(() -> ivMenu.postDelayed(() -> ivMenu.setEnabled(true),1));
    }

    @Override
    public void showAbout() {
    }

    @Override
    public void deleteCart() {
        CartFragment item = (CartFragment) fragmentAdapter.getItem(2);
        item.deleteCartItems();
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

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        return intent;
    }
}

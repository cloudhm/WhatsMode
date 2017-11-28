package com.whatsmode.shopify.block.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.innodroid.expandablerecycler.ExpandableRecyclerAdapter;
import com.whatsmode.library.util.DensityUtil;
import com.whatsmode.library.util.ListUtils;
import com.whatsmode.library.util.PreferencesUtil;
import com.whatsmode.library.util.ScreenUtils;
import com.whatsmode.library.util.ToastUtil;
import com.whatsmode.shopify.AppNavigator;
import com.whatsmode.shopify.R;
import com.whatsmode.shopify.WhatsApplication;
import com.whatsmode.shopify.block.WebActivity;
import com.whatsmode.shopify.block.cart.CartFragment;
import com.whatsmode.shopify.block.cart.CartItem;
import com.whatsmode.shopify.block.me.StatusBarUtil;
import com.whatsmode.shopify.common.Constant;
import com.whatsmode.shopify.mvp.MvpActivity;
import com.whatsmode.shopify.ui.helper.BaseFragmentAdapter;
import com.whatsmode.shopify.ui.helper.CategoryAdapter;
import com.whatsmode.shopify.ui.helper.ToolbarHelper;
import com.whatsmode.shopify.ui.widget.BottomBar;
import com.whatsmode.shopify.ui.widget.NoScrollViewPager;

import java.io.IOException;
import java.util.List;

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
    private ImageView ivLogo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StatusBarUtil.StatusBarLightMode(this);
        vpContent = (NoScrollViewPager) findViewById(R.id.vp_content);
        vpContent.setOffscreenPageLimit(3);
        ivMenu = (ImageView) findViewById(R.id.menu);
        ivMenu.setVisibility(View.VISIBLE);
        ivMenu.setOnClickListener(this);
        ToolbarHelper.ToolbarHolder toolbarHolder = ToolbarHelper.initToolbarNoFix(this, R.id.toolbar, false, null);
        toolbar = toolbarHolder.toolbar;
        toolbarTitle = toolbarHolder.titleView;
        ivLogo = (ImageView) findViewById(R.id.logo);
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
        ivMenu.setVisibility(View.VISIBLE);
        toolbar.setVisibility(View.VISIBLE);
        ivLogo.setVisibility(View.VISIBLE);
        toolbarTitle.setVisibility(View.GONE);
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
        toolbar.setVisibility(View.VISIBLE);
        ivLogo.setVisibility(View.VISIBLE);
        toolbarTitle.setVisibility(View.GONE);
    }

    @Override
    public void switch2Cart() {
        switchMenu(menuItemDelete);
        ivMenu.setVisibility(View.GONE);
        ivLogo.setVisibility(View.GONE);
        toolbarTitle.setVisibility(View.VISIBLE);
        toolbar.setVisibility(View.VISIBLE);
        try {
            List<CartItem> cartItemList = (List<CartItem>) PreferencesUtil.getObject(WhatsApplication.getContext(), Constant.CART_LOCAL);
            toolbarTitle.setText(ListUtils.isEmpty(cartItemList) ? "My Cart" : "My Cart(" + cartItemList.size() + ")");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void switch2Mine() {
        switchMenu(null);
        toolbar.setVisibility(View.GONE);
    }

    private PopupWindow popupWindow;
    @Override
    public void showMenu() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }else {
            initPopWindowView();
            popupWindow.setFocusable(true);
            popupWindow.showAsDropDown(toolbar, 0,0);
            ivMenu.setEnabled(false);
        }
        toolbar.setVisibility(View.VISIBLE);
    }

    private void initPopWindowView() {
        View customView = getLayoutInflater().inflate(R.layout.category_menu,null, false);
        RecyclerView recycler = (RecyclerView) customView.findViewById(R.id.recycleView);
        CategoryAdapter adapter = new CategoryAdapter(this);
        adapter.setMode(ExpandableRecyclerAdapter.MODE_ACCORDION);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(adapter);
        popupWindow = new PopupWindow(customView,ScreenUtils.getScreenWidth(this)- DensityUtil.dp2px(this,65),
                ScreenUtils.getScreenHeight(this) - ScreenUtils.dip2px(this,80));
        popupWindow.setAnimationStyle(R.style.AnimationFade);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setOnDismissListener(() -> ivMenu.postDelayed(() -> ivMenu.setEnabled(true),1));
    }

    @Override
    public void showSearch() {
        AppNavigator.jumpToWebActivity(this, WebActivity.STATE_SEARCH, Constant.URL_SEARCH);
    }

    @Override
    public void deleteCart() {
        CartFragment item = (CartFragment) fragmentAdapter.getItem(2);
        String title = menuItemDelete.getTitle().toString();
        menuItemDelete.setTitle(title.equalsIgnoreCase(getString(R.string.edit)) ? R.string.done : R.string.edit);
        item.deleteCartItems(menuItemDelete.getTitle().toString());
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
        return new Intent(context, MainActivity.class);
    }

    private static final long WAIT_TIME = 2000L;
    private long TOUCH_TIME = 0;
    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - TOUCH_TIME < WAIT_TIME) {
            finish();
        } else {
            TOUCH_TIME = System.currentTimeMillis();
            ToastUtil.showToast(getString(R.string.press_again_exist));
        }
    }
}

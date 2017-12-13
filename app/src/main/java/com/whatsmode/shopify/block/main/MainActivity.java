package com.whatsmode.shopify.block.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatDelegate;
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
import com.shopify.graphql.support.ID;
import com.whatsmode.library.rx.RxBus;
import com.whatsmode.library.util.DensityUtil;
import com.whatsmode.library.util.ListUtils;
import com.whatsmode.library.util.PreferencesUtil;
import com.whatsmode.library.util.ScreenUtils;
import com.whatsmode.library.util.ToastUtil;
import com.whatsmode.shopify.AppNavigator;
import com.whatsmode.shopify.R;
import com.whatsmode.shopify.WhatsApplication;
import com.whatsmode.shopify.actionlog.ActionLog;
import com.whatsmode.shopify.block.WebActivity;
import com.whatsmode.shopify.block.account.data.AccountManager;
import com.whatsmode.shopify.block.cart.CartFragment;
import com.whatsmode.shopify.block.cart.CartItem;
import com.whatsmode.shopify.block.cart.CartRepository;
import com.whatsmode.shopify.block.cart.JumpMainTab;
import com.whatsmode.shopify.block.me.StatusBarUtil;
import com.whatsmode.shopify.block.me.event.LoginEvent;
import com.whatsmode.shopify.common.Constant;
import com.whatsmode.shopify.mvp.MvpActivity;
import com.whatsmode.shopify.ui.helper.BaseFragmentAdapter;
import com.whatsmode.shopify.ui.helper.CategoryAdapter;
import com.whatsmode.shopify.ui.helper.ToolbarHelper;
import com.whatsmode.shopify.ui.widget.BottomBar;
import com.whatsmode.shopify.ui.widget.BottomBarItem;
import com.whatsmode.shopify.ui.widget.NoScrollViewPager;
import com.zchu.log.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends MvpActivity<MainContact.Presenter> implements MainContact.View, View.OnClickListener {

    private NoScrollViewPager vpContent;
    private Toolbar toolbar;
    private TextView toolbarTitle;
    private BottomBar bottomBar;
    private BottomBarItem bottomBarItem;
    private ImageView ivMenu;
    private MenuItem menuItemSearch;
    private MenuItem menuEdit;
    private MenuItem mTempMenu;
    private BaseFragmentAdapter fragmentAdapter;
    private ImageView ivLogo;
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }
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
        ToolbarHelper.ToolbarHolder toolbarHolder = ToolbarHelper.initToolbar(this, R.id.toolbar, false, null);
        toolbar = toolbarHolder.toolbar;
        toolbarTitle = toolbarHolder.titleView;
        ivLogo = (ImageView) findViewById(R.id.logo);
        ivLogo.setVisibility(View.VISIBLE);
        bottomBar = (BottomBar)findViewById(R.id.bottomBar);
        bottomBarItem = (BottomBarItem) findViewById(R.id.cart_bottom_bar);
        getPresenter().initViewPage(getSupportFragmentManager());
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        defineCartTitle();
        //checkCartItemExist();
    }

    @Subscribe
    public void receive(JumpMainTab select){
        if (select.tabPosition == -1) {
            toolbar.setVisibility(View.VISIBLE);
            vpContent.setCurrentItem(0);
            //switch2Mode();
            //getPresenter().initViewPage(getSupportFragmentManager());
        }else{
            vpContent.setCurrentItem(select.tabPosition,false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (vpContent != null && vpContent.getCurrentItem() == 3) {
            if (!AccountManager.isLoginStatus()) {
                toolbar.setVisibility(View.GONE);
            }else{
                toolbar.setVisibility(View.VISIBLE);
            }
        }else{
            toolbar.setVisibility(View.VISIBLE);
        }
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
        menuEdit = menu.findItem(R.id.action_edit);
        menuEdit.setVisible(false);
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
        toolbar.setVisibility(View.VISIBLE);
        switchMenu(menuItemSearch);
        ivMenu.setVisibility(View.VISIBLE);
        ivLogo.setVisibility(View.VISIBLE);
        toolbarTitle.setVisibility(View.GONE);
        ActionLog.onEvent(Constant.Event.MODE);
    }

    private void switchMenu(MenuItem menuItem) {
        if (mTempMenu != null) {
            mTempMenu.setVisible(false);
        }
            mTempMenu = menuItem;
        if (menuItem == menuItemSearch) {
            menuItem.setVisible(true);
        }
    }

    @Override
    public void switch2Influence() {
        toolbar.setVisibility(View.VISIBLE);
        switchMenu(menuItemSearch);
        ivMenu.setVisibility(View.VISIBLE);
        ivLogo.setVisibility(View.VISIBLE);
        toolbarTitle.setVisibility(View.GONE);
        ActionLog.onEvent(Constant.Event.INFLUENCER);
    }

    private void checkCartItemExist() {
        try {
            List<CartItem> cartItemList = (List<CartItem>) PreferencesUtil.getObject(WhatsApplication.getContext(), Constant.CART_LOCAL);
            List<ID> ids = new ArrayList<>();
            if (!ListUtils.isEmpty(cartItemList)) {
                for (CartItem cartItem : cartItemList) {
                    ids.add(new ID(cartItem.getId()));
                }
                mPresenter.checkVariantExist(ids);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void defineCartTitle(){
        try {
            List<CartItem> cartItemList = (List<CartItem>) PreferencesUtil.getObject(WhatsApplication.getContext(), Constant.CART_LOCAL);
            int badge = 0;
            if (!ListUtils.isEmpty(cartItemList)) {
                for (CartItem cartItem : cartItemList) {
                    if(!cartItem.isSoldOut)
                    badge += cartItem.getQuality();
                }
            }
            toolbarTitle.setText(ListUtils.isEmpty(cartItemList) ? "My Cart" : "My Cart(" + badge + ")");
            bottomBarItem.setBadge(badge);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void switch2Cart() {
        switchMenu(menuEdit);
        toolbar.setVisibility(View.VISIBLE);
        ivMenu.setVisibility(View.GONE);
        ivLogo.setVisibility(View.GONE);
        toolbarTitle.setVisibility(View.VISIBLE);
        defineCartTitle();
        ActionLog.onEvent(Constant.Event.MY_CART);
    }

    @Override
    public void switch2Mine() {
        switchMenu(null);
        if (AccountManager.isLoginStatus()) {
            toolbar.setVisibility(View.VISIBLE);
        }else{
            toolbar.setVisibility(View.GONE);
        }
        toolbarTitle.setVisibility(View.GONE);
        ivMenu.setVisibility(View.GONE);
        ivLogo.setVisibility(View.GONE);
        ActionLog.onEvent(Constant.Event.ACCOUNT);
    }

    private PopupWindow popupWindow;
    @Override
    public void showMenu() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }else {
            initPopWindowView();
            popupWindow.setFocusable(true);
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
            popupWindow.showAsDropDown(toolbar, 0,0);
            ivMenu.setEnabled(false);
            ActionLog.onEvent(Constant.Event.CATEGORY);
        }
    }

    private void initPopWindowView() {
        View customView = getLayoutInflater().inflate(R.layout.category_menu,null, false);
        customView.findViewById(R.id.new_arrive).setOnClickListener(this);
        customView.findViewById(R.id.discover).setOnClickListener(this);
        customView.findViewById(R.id.sale).setOnClickListener(this);
        customView.findViewById(R.id.about_us).setOnClickListener(this);
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
        ActionLog.onEvent(Constant.Event.SREARCH);
    }

    @Override
    public void deleteCart() {
        CartFragment item = (CartFragment) fragmentAdapter.getItem(2);
        String title = menuEdit.getTitle().toString();
        menuEdit.setTitle(title.equals(getString(R.string.edit)) ? R.string.done : R.string.edit);
        item.deleteCartItems(menuEdit.getTitle().toString());
        ActionLog.onEvent(Constant.Event.DELETE);
    }

    @Override
    public void jumpToAds(int i) {
        AppNavigator.jumpToWebActivity(this,getResources().getStringArray(R.array.pop_icon_name)[i]
                ,getResources().getStringArray(R.array.pop_icon_link)[i]);
        switch (i){
            case 0:
                ActionLog.onEvent(Constant.Event.NEW_ARRIVALS);
                break;
            case 1:
                ActionLog.onEvent(Constant.Event.DISCOVER);
                break;
            case 2:
                ActionLog.onEvent(Constant.Event.SALE);
                break;
        }
    }

    @Override
    public void jumpToAboutUs() {
        AppNavigator.jumpToWebActivity(this,WebActivity.STATE_ABOUT_US,Constant.ABOUT_US);
        ActionLog.onEvent(Constant.Event.ABOUT_US);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
        popupWindow = null;
        EventBus.getDefault().unregister(this);
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

    public void refreshBottomBar(int i) {
        toolbarTitle.setText(i == 0 ? "My Cart" : "My Cart(" + i + ")");
        bottomBarItem.setBadge(i);
    }

    public void hideEdit(boolean visiable) {
        menuEdit.setVisible(visiable);
    }
}

package com.whatsmode.shopify.block.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.whatsmode.shopify.R;
import com.whatsmode.shopify.base.BaseWebFragment;
import com.whatsmode.shopify.block.cart.CartFragment;
import com.whatsmode.shopify.block.me.MyFragment;
import com.whatsmode.shopify.common.Constant;
import com.whatsmode.shopify.mvp.MvpBasePresenter;
import com.whatsmode.shopify.ui.helper.BaseFragmentAdapter;

import java.util.ArrayList;

class MainPresenter extends MvpBasePresenter<MainContact.View> implements MainContact.Presenter {


    private int mPageTitles[] = new int[]{R.string.tabFirst, R.string.tabSecond, R.string.tabThird, R.string.tabFourth};

    @Override
    public void initViewPage(FragmentManager supportFragmentManager) {
        final BaseFragmentAdapter fragmentAdapter = new BaseFragmentAdapter(supportFragmentManager);
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(BaseWebFragment.newInstance(Constant.URL_TAB_MODE));
        fragments.add(BaseWebFragment.newInstance(Constant.URL_TAB_INFLUENCE));
        fragments.add(CartFragment.newInstance());
        fragments.add(MyFragment.newInstance());
        fragmentAdapter.setFragmentPages(fragments);
        fragmentAdapter.setPageTitleArray(mPageTitles);
        if (isViewAttached()) {
            getView().setViewPage(fragmentAdapter);
        }
    }

    @Override
    public void setPageSelected(int position) {
        if (isViewAttached()) {
            getView().setToolbarTitle(mPageTitles[position]);
            switch (position) {
                case 0:
                    getView().switch2Mode();
                    break;
                case 1:
                    getView().switch2Influence();
                    break;
                case 2:
                    getView().switch2Cart();
                    break;
                case 3:
                    getView().switch2Mine();
                    break;
            }
        }
    }

    @Override
    public boolean clickMenuItem(int itemId) {
        switch (itemId) {
            case R.id.action_search:
                if (isViewAttached()) {
                    getView().showSearch();
                }
                return true;
            case R.id.action_delete:
                if (isViewAttached()) {
                    getView().deleteCart();
                }
                return true;
        }
        return false;
    }

    @Override
    public void onClickView(View v) {
        switch (v.getId()) {
            case R.id.menu:
                if (isViewAttached()) {
                    getView().showMenu();
                }
                break;
        }
    }
}

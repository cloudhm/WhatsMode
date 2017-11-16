package com.whatsmode.shopify.block.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.whatsmode.shopify.R;
import com.whatsmode.shopify.base.BaseWebFragment;
import com.whatsmode.shopify.mvp.MvpBasePresenter;
import com.whatsmode.shopify.ui.helper.BaseFragmentAdapter;

import java.util.ArrayList;

class MainPresenter extends MvpBasePresenter<MainContact.View> implements MainContact.Presenter{


    private int  mPageTitles [] = new int[]{R.string.tabFirst,R.string.tabSecond,R.string.tabThird,R.string.tabFourth};
    @Override
    public void initViewPage(FragmentManager supportFragmentManager) {
        if (isViewAttached()) {
            final BaseFragmentAdapter fragmentAdapter = new BaseFragmentAdapter(supportFragmentManager);
            ArrayList<Fragment> fragments = new ArrayList<>();
            fragments.add(BaseWebFragment.newInstance("https://www.shopify.com/domains"));
            fragments.add(BaseWebFragment.newInstance("https://github.com/"));
            fragments.add(BaseWebFragment.newInstance("http://baidu.com"));
            fragments.add(BaseWebFragment.newInstance("http://sougou.com"));
            fragmentAdapter.setFragmentPages(fragments);
            fragmentAdapter.setPageTitleArray(mPageTitles);
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
                    getView().showAbout();
                }
                return true;
        }
        return false;
    }

    @Override
    public void onClickView(View v) {
        switch (v.getId()) {
            case R.id.search:
                if (isViewAttached()) {
                    getView().showSearch();
                }
                break;
        }
    }
}

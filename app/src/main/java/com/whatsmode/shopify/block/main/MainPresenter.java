package com.whatsmode.shopify.block.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

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
            fragments.add(BaseWebFragment.newInstance(""));
            fragments.add(BaseWebFragment.newInstance(""));
            fragments.add(BaseWebFragment.newInstance(""));
            fragments.add(BaseWebFragment.newInstance(""));
            fragmentAdapter.setFragmentPages(fragments);
            fragmentAdapter.setPageTitleArray(mPageTitles);
            getView().setViewPage(fragmentAdapter);
        }
    }
}

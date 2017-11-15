package com.whatsmode.shopify.block.main;

import android.support.v4.app.FragmentManager;

import com.whatsmode.shopify.mvp.MvpPresenter;
import com.whatsmode.shopify.mvp.MvpView;
import com.whatsmode.shopify.ui.helper.BaseFragmentAdapter;

class MainContact {
    interface View extends MvpView{
        void setViewPage(BaseFragmentAdapter fragmentAdapter);

        void setToolbarTitle(int mPageTitle);

        void switch2Mode();

        void switch2Influence();

        void switch2Cart();

        void switch2Mine();
    }

    interface Presenter extends MvpPresenter<MainContact.View> {

        void initViewPage(FragmentManager supportFragmentManager);

        void setPageSelected(int position);
    }
}

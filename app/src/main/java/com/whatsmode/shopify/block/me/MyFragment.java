package com.whatsmode.shopify.block.me;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import com.whatsmode.library.util.SnackUtil;
import com.whatsmode.shopify.R;
import com.whatsmode.shopify.block.account.LoginActivity;
import com.whatsmode.shopify.block.address.AddressListActivity;
import com.whatsmode.shopify.block.address.LoadType;
import com.whatsmode.shopify.mvp.MvpFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tom on 17-11-20.
 */

public class MyFragment extends MvpFragment<MyContract.Presenter> implements MyContract.View, View.OnClickListener {

    private TabLayout mTabLayout;
    private ViewPager mVpBody;
    private ArrayList<Fragment> mFragments;

    public static MyFragment newInstance(){
        MyFragment fragment = new MyFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.avatar).setOnClickListener(this);
        view.findViewById(R.id.view_address).setOnClickListener(this);
        view.findViewById(R.id.order_history).setOnClickListener(this);
        mTabLayout = (TabLayout) view.findViewById(R.id.tabs);
        mVpBody = (ViewPager) view.findViewById(R.id.vp_body);
        initViewPager();
    }

    @NonNull
    @Override
    public MyContract.Presenter createPresenter() {
        return new MyPresenter();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_my;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.avatar:
                startActivity(new Intent(getActivity(), LoginActivity.class));
                break;
            case R.id.view_address:
                startActivity(new Intent(getActivity(), AddressListActivity.class));
                break;
            case R.id.order_history:
                mPresenter.refreshOrderList();
                break;
        }
    }

    @Override
    public void showContent(@LoadType.checker int type, @NonNull List<Order> orders) {
        SnackUtil.toastShow(getContext(),orders.toString());
    }

    @Override
    public void onError(int code, String msg) {
        SnackUtil.toastShow(getContext(),msg);
    }


    List<String> titles;
    private void initViewPager() {
        try {

            titles = new ArrayList<>();
            titles.add("Order");

            mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(0)));

            mTabLayout.setTabTextColors(getResources().getColor(R.color.white), getResources().getColor(R.color.colorAccent));


            mFragments = new ArrayList<Fragment>();
            Bundle bundle = new Bundle();
            OrderListFragment orderListFragment = new OrderListFragment();
            mFragments.add(orderListFragment);
            TabFragmentPagerAdapter tabFragmentPagerAdapter = new TabFragmentPagerAdapter(
                    getChildFragmentManager(), mFragments);
            mVpBody.setAdapter(new TabFragmentPagerAdapter(
                    getChildFragmentManager(), mFragments));
            mVpBody.setCurrentItem(0);
            mVpBody.setOnPageChangeListener(new MyOnPageChangeListener());


            mTabLayout.setupWithViewPager(mVpBody);
            mTabLayout.setTabsFromPagerAdapter(tabFragmentPagerAdapter);

        } catch (Exception e) {
            Log.e("initViewPager", "initViewPager", e);
        }

    }

    public class TabFragmentPagerAdapter extends FragmentPagerAdapter {
        ArrayList<Fragment> mFragmentsList;

        public TabFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public TabFragmentPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragmentsList) {
            super(fm);
            mFragmentsList = fragmentsList;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentsList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentsList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }

    public class TabOnClickListener implements View.OnClickListener {
        private int index = 0;

        public TabOnClickListener(int i) {
        }

        @Override
        public void onClick(View v) {
        }
    }

    ;

    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageSelected(int arg0) {

            switch (arg0) {
                case 0:


                    break;
                case 1:

                    break;
                case 2:

                    break;

            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    }
}

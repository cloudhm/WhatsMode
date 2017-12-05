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
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.whatsmode.library.exception.APIException;
import com.whatsmode.library.rx.RxBus;
import com.whatsmode.library.util.SnackUtil;
import com.whatsmode.shopify.AppNavigator;
import com.whatsmode.shopify.R;
import com.whatsmode.shopify.block.account.LoginActivity;
import com.whatsmode.shopify.block.address.Address;
import com.whatsmode.shopify.block.address.AddressListActivity;
import com.whatsmode.shopify.block.address.AddressUtil;
import com.whatsmode.shopify.block.address.LoadType;
import com.whatsmode.shopify.block.me.event.LoginEvent;
import com.whatsmode.shopify.common.KeyConstant;
import com.whatsmode.shopify.mvp.MvpFragment;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by tom on 17-11-20.
 */

public class MyFragment extends MvpFragment<MyContract.Presenter> implements MyContract.View, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemClickListener {

    private TabLayout mTabLayout;
    private ViewPager mVpBody;
    private ArrayList<Fragment> mFragments;
    private TextView mViewAddress;
    private TextView mName;
    private TextView mEmail;
    private SwipeRefreshLayout mSwipe;
    private OrderListAdapter mOrderListAdapter;
    private List<Order> mList;
    private RecyclerView mRecyclerView;
    private ViewGroup mOrdeEmpty;
    private RelativeLayout mNoLoginL;
    private Disposable mSubscribe;

    public static MyFragment newInstance(){
        MyFragment fragment = new MyFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mSwipe = (SwipeRefreshLayout) view.findViewById(R.id.swipe);
        mSwipe.setOnRefreshListener(this);
        mNoLoginL = (RelativeLayout) view.findViewById(R.id.no_login_l);
        Button createAccount = (Button) view.findViewById(R.id.create_account);
        Button logIn = (Button) view.findViewById(R.id.log_in);
        createAccount.setOnClickListener(this);
        logIn.setOnClickListener(this);

        /**/
        initRecyclerView();
        mPresenter.getCustomer();
        mPresenter.refreshOrderList();
        initLoginListener();
    }

    private void initLoginListener() {
        mSubscribe = RxBus.getInstance().register(LoginEvent.class)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(loginEvent -> {
                    mPresenter.getCustomer();
                    mPresenter.refreshOrderList();
                });
    }

    private void initRecyclerView() {
        mList = new ArrayList<>();
        mOrderListAdapter = new OrderListAdapter(
                R.layout.item_order_item, mList
        );
        View header = LayoutInflater.from(getActivity()).inflate(R.layout.header_my, null);
        View headerOrderEmpty = LayoutInflater.from(getActivity()).inflate(R.layout.header_order_empty, null);
        mOrdeEmpty = (ViewGroup) headerOrderEmpty.findViewById(R.id.order_empty);
        findView(header);
        mOrderListAdapter.addHeaderView(header,0);
        mOrderListAdapter.addHeaderView(headerOrderEmpty,1);
        mOrderListAdapter.setFragment(this);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mOrderListAdapter);
        mOrderListAdapter.setOnLoadMoreListener(this);
        mOrderListAdapter.setOnItemClickListener(this);

    }

    private void findView(View view){
        view.findViewById(R.id.avatar).setOnClickListener(this);
        mViewAddress = (TextView) view.findViewById(R.id.view_address);
        mViewAddress.setOnClickListener(this);
        mName = (TextView) view.findViewById(R.id.name);
        mEmail = (TextView) view.findViewById(R.id.email);
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
                startActivity(new Intent(getActivity(), SettingInfoActivity.class));
                break;
            case R.id.view_address:
                Intent intent = new Intent(getActivity(), AddressListActivity.class);
                intent.putExtra(KeyConstant.KEY_TYPE_ADDRESS,AddressListActivity.TYPE_VIEW);
                startActivity(intent);
                break;
            case R.id.order_history:
                ShareUtil.showShare(getActivity(),"https://whatsmode.com/","/storage/emulated/0/adv/af20c843-4081-4bc1-b7f8-b73041672e55.png","https://whatsmode.com/","https://whatsmode.com/");
                break;
            case R.id.create_account:
                AppNavigator.jumpToCreateAccount(getActivity());
                break;
            case R.id.log_in:
                AppNavigator.jumpToLogin(getActivity());
                break;
        }
    }

    public void completeRefresh() {
        if (mSwipe != null && mSwipe.isRefreshing()) {
            mSwipe.setRefreshing(false);
        }
    }

    @Override
    public void onError(int code, String msg) {
        SnackUtil.toastShow(getContext(),msg);
        completeRefresh();
        if (code == APIException.CODE_SESSION_EXPIRE) {
            //AppNavigator.jumpToLogin(getActivity());
            mNoLoginL.setVisibility(View.VISIBLE);
            mSwipe.setVisibility(View.GONE);
        }else{
            mNoLoginL.setVisibility(View.GONE);
            mSwipe.setVisibility(View.VISIBLE);
        }
    }

    private void setContentVisible(){
        mNoLoginL.setVisibility(View.GONE);
        mSwipe.setVisibility(View.VISIBLE);
    }

    @Override
    public void showCustomer(Customer customer) {
        completeRefresh();
        setContentVisible();
        if(customer == null) return;
        mName.setText(customer.getLastName() + " " + customer.getFirstName());
        mEmail.setText(customer.getEmail());
        Address defaultAddress = customer.getDefaultAddress();
        if (defaultAddress != null && defaultAddress.isDefault()) {
            String joinAddress = AddressUtil.getJoinAddress(defaultAddress);
            if (TextUtils.isEmpty(joinAddress)) {
                mViewAddress.setText("add address");
            }else {
                mViewAddress.setText(joinAddress);
            }
        }else{
            mViewAddress.setText("add address");
        }
    }

    @Override
    public void showContent(@LoadType.checker int type, @NonNull List<Order> orders) {
        if(mOrderListAdapter == null) return;
        setContentVisible();
        completeRefresh();
        if (mOrdeEmpty.getVisibility() == View.VISIBLE) {
            mOrdeEmpty.setVisibility(View.GONE);
        }
        switch (type) {
            case LoadType.TYPE_REFRESH_SUCCESS:
                if (orders.isEmpty()) {
                    mOrdeEmpty.setVisibility(View.VISIBLE);
                    //SnackUtil.toastShow(getActivity(),"order list is empty");
                    mList.clear();
                    mOrderListAdapter.notifyDataSetChanged();
                    return;
                }
                mList.clear();
                mList.addAll(orders);
                mOrderListAdapter.notifyDataSetChanged();
                //mOrderListAdapter.refresh(orders);
                break;
            case LoadType.TYPE_LOAD_MORE_SUCCESS:
                mOrderListAdapter.addData(orders);
                break;
        }
        if (Order.sHasNextPage) {
            mOrderListAdapter.loadMoreComplete();
        }else {
            mOrderListAdapter.loadMoreEnd();
        }
    }


    @Override
    public void onRefresh() {
        mPresenter.getCustomer();
        mPresenter.refreshOrderList();
    }


    @Override
    public void onLoadMoreRequested() {
        if (Order.sHasNextPage) {
            mPresenter.loadMoreOrderList();
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Order order = mList.get(position);
        Intent intent = new Intent(getActivity(),OrderDetailsActivity.class);
        intent.putExtra(KeyConstant.KEY_ORDER, order);
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mSubscribe != null && !mSubscribe.isDisposed()) {
            mSubscribe.dispose();
        }
    }
}

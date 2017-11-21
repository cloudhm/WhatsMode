package com.whatsmode.shopify.block.address;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.whatsmode.library.exception.APIException;
import com.whatsmode.library.util.SnackUtil;
import com.whatsmode.shopify.R;
import com.whatsmode.shopify.block.account.LoginActivity;
import com.whatsmode.shopify.common.KeyConstant;
import com.whatsmode.shopify.mvp.MvpActivity;

import java.security.Key;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tom on 17-11-16.
 */

public class AddressListActivity extends MvpActivity<AddressListPresenter> implements AddressListContract.View, View.OnClickListener {

    private RecyclerView mRecyclerView;

    AddressListAdapter mAddressListAdapter;

    List<Address> mList;
    private SwipeRefreshLayout mSwipe;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_list);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mSwipe = (SwipeRefreshLayout) findViewById(R.id.swipe);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        findViewById(R.id.add_address).setOnClickListener(this);

        init();
        initRecycler();
        mPresenter.refreshAddressList();
    }

    private void init(){
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("地址列表");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initRecycler() {
        mList = new ArrayList<>();
        mAddressListAdapter = new AddressListAdapter(
                R.layout.item_address_list,mList
        );
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAddressListAdapter);
        mAddressListAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (Address.sHasNextPage) {
                    mPresenter.loadModeAddressList();
                }
            }
        });
        mAddressListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

            }
        });
        mAddressListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.delete) {
                    String id = mList.get(position).getId();
                    mPresenter.deleteAddress(id);
                } else if (view.getId() == R.id.update) {
                    Intent intent = new Intent(AddressListActivity.this,AddEditAddressActivity.class);
                    intent.putExtra(KeyConstant.KEY_ADDRESS,mList.get(position));
                    intent.putExtra(KeyConstant.KEY_ADD_EDIT_ADDRESS, AddEditAddressActivity.TYPE_EDIT_ADDRESS);
                    startActivity(intent);
                }
            }
        });
        mSwipe.setOnRefreshListener(() -> {
            mPresenter.refreshAddressList();
        });
        //mAddressListAdapter.disableLoadMoreIfNotFullPage(mRecyclerView);
    }

    public void completeRefresh(){
        if (mSwipe != null && mSwipe.isRefreshing()) {
            mSwipe.setRefreshing(false);
        }
    }

    @NonNull
    @Override
    public AddressListPresenter createPresenter() {
        return new AddressListPresenter();
    }

    @Override
    public void showContent(@LoadType.checker int type, @NonNull List<Address> addresses) {
        if(mAddressListAdapter == null || isDestroyed()) return;
        completeRefresh();
        switch (type) {
            case LoadType.TYPE_REFRESH_SUCCESS:
                if (addresses.isEmpty()) {
                    SnackUtil.toastShow(this,"address list is empty");
                }
                mAddressListAdapter.refresh(addresses);
                break;
            case LoadType.TYPE_LOAD_MORE_SUCCESS:
                mAddressListAdapter.addData(addresses);
                break;
        }
        if (Address.sHasNextPage) {
            mAddressListAdapter.loadMoreComplete();
        }else {
            mAddressListAdapter.loadMoreEnd();
        }
    }

    @Override
    public void onError(int code,String msg) {
        completeRefresh();
        if (code == APIException.CODE_SESSION_EXPIRE) {
            startActivity(new Intent(this, LoginActivity.class));
        }
        SnackUtil.toastShow(this,msg);
    }

    @Override
    public void deleteSuccess() {
        SnackUtil.toastShow(this,"delete success");
        mPresenter.refreshAddressList();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_address:
                startActivity(new Intent(this,AddEditAddressActivity.class));
                break;
        }
    }
}

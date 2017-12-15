package com.whatsmode.shopify.block.address;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.whatsmode.library.exception.APIException;
import com.whatsmode.library.rx.RxBus;
import com.whatsmode.library.util.GlideCacheUtil;
import com.whatsmode.library.util.SnackUtil;
import com.whatsmode.shopify.R;
import com.whatsmode.shopify.block.account.LoginActivity;
import com.whatsmode.shopify.block.me.StatusBarUtil;
import com.whatsmode.shopify.block.me.event.LoginEvent;
import com.whatsmode.shopify.common.KeyConstant;
import com.whatsmode.shopify.mvp.MvpActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tom on 17-11-16.
 */

public class AddressListActivity extends MvpActivity<AddressListPresenter> implements AddressListContract.View, View.OnClickListener {

    private static final int REQUEST_ADD_UPDATE_ADDRESS_CODE = 0X11;

    public static final int TYPE_SELECT = 1;
    public static final int TYPE_VIEW = 2;

    private int mType = TYPE_SELECT;

    private RecyclerView mRecyclerView;

    AddressListAdapter mAddressListAdapter;

    List<Address> mList;
    private SwipeRefreshLayout mSwipe;
    private Toolbar mToolbar;
    private TextView mAddAddressesForEmpty;
    private RelativeLayout mContent;
    private LinearLayout mEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_list);
        StatusBarUtil.StatusBarLightMode(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mSwipe = (SwipeRefreshLayout) findViewById(R.id.swipe);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mAddAddressesForEmpty = (TextView) findViewById(R.id.add_addresses_for_empty);
        mContent = (RelativeLayout) findViewById(R.id.content);
        mEmpty = (LinearLayout) findViewById(R.id.empty);
        findViewById(R.id.add_address).setOnClickListener(this);
        findViewById(R.id.add_addresses_for_empty).setOnClickListener(this);
        mType = getIntent().getIntExtra(KeyConstant.KEY_TYPE_ADDRESS,TYPE_SELECT);
        init();
        initRecycler();
        mPresenter.refreshAddressList();
        showLoading();
    }

    private void init(){
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");
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
                if (mType == TYPE_SELECT) {
                    Intent intent = new Intent();
                    Address address = mList.get(position);
                    intent.putExtra(KeyConstant.KEY_EXTRA_SELECT_ADDRESS,address);
                    setResult(RESULT_OK,intent);
                    finish();
                }
            }
        });
        mAddressListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.delete) {
                    deleteAddress(position);
                } else if (view.getId() == R.id.update) {
                    Intent intent = new Intent(AddressListActivity.this,AddEditAddressActivity.class);
                    intent.putExtra(KeyConstant.KEY_ADDRESS,mList.get(position));
                    intent.putExtra(KeyConstant.KEY_ADD_EDIT_ADDRESS, AddEditAddressActivity.TYPE_EDIT_ADDRESS);
                    startActivityForResult(intent,REQUEST_ADD_UPDATE_ADDRESS_CODE);
                } else if (view.getId() == R.id.is_default) {

                    if (view instanceof CheckBox) {
                        CheckBox checkBox = ((CheckBox)(view));
                        if (checkBox.isChecked()) {
                            String id = mList.get(position).getId();
                            mPresenter.updateDefaultAddress(id);
                            showLoading();
                        }else{
                            checkBox.setChecked(true);
                        }
                    }

                }
            }
        });
        mSwipe.setOnRefreshListener(() -> {
            mPresenter.refreshAddressList();
        });
        //mAddressListAdapter.disableLoadMoreIfNotFullPage(mRecyclerView);
    }

    private void deleteAddress(int position) {
        new AlertDialog.Builder(this,R.style.DialogTheme)
                .setTitle(R.string.prompt)
                .setMessage(R.string.confirm_deletion)
                .setNegativeButton(R.string.no_, (dialogInterface, i)
                        -> dialogInterface.dismiss())
                .setPositiveButton(R.string.yes_, (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    String id = mList.get(position).getId();
                    mPresenter.deleteAddress(id);
                    showLoading();
                }).create().show();
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
        hideLoading();
        mContent.setVisibility(View.VISIBLE);
        mEmpty.setVisibility(View.GONE);
        switch (type) {
            case LoadType.TYPE_REFRESH_SUCCESS:
                if (addresses.isEmpty()) {
                    //SnackUtil.toastShow(this,"address list is empty");
                    mContent.setVisibility(View.GONE);
                    mEmpty.setVisibility(View.VISIBLE);
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
        hideLoading();
        if (code == APIException.CODE_SESSION_EXPIRE) {
            startActivity(new Intent(this, LoginActivity.class));
        }
        SnackUtil.toastShow(this,msg);
    }

    @Override
    public void deleteSuccess() {
        //SnackUtil.toastShow(this,"delete success");
        mPresenter.refreshAddressList();
        RxBus.getInstance().post(LoginEvent.singleRefresh());
    }

    @Override
    public void updateDefaultAddressSuccess() {
        mPresenter.refreshAddressList();
        RxBus.getInstance().post(LoginEvent.singleRefresh());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                setResult(RESULT_CANCELED);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_address:
            case R.id.add_addresses_for_empty:
                Intent intent = new Intent(this, AddEditAddressActivity.class);
                startActivityForResult(intent, REQUEST_ADD_UPDATE_ADDRESS_CODE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ADD_UPDATE_ADDRESS_CODE && resultCode == RESULT_OK) {
            mPresenter.refreshAddressList();
        }
    }
}

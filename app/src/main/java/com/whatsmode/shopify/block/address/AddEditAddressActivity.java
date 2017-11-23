package com.whatsmode.shopify.block.address;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.shopify.buy3.Storefront;
import com.whatsmode.library.util.SnackUtil;
import com.whatsmode.shopify.R;
import com.whatsmode.shopify.block.account.LoginActivity;
import com.whatsmode.shopify.common.KeyConstant;
import com.whatsmode.shopify.mvp.MvpActivity;

/**
 * Created by tom on 17-11-17.
 */

public class AddEditAddressActivity extends MvpActivity<AddEditAddressPresenter> implements AddEditAddressContract.View {

    public static final int TYPE_ADD_ADDRESS = 1;
    public static final int TYPE_EDIT_ADDRESS = 2;

    private EditText mFirstName;
    private EditText mLastName;
    private EditText mAddress1;
    private EditText mAddress2;
    private EditText mCity;
    private EditText mProvince;
    private EditText mCountry;
    private EditText mZip;
    private EditText mPhone;
    private Toolbar mToolbar;

    private int mType = TYPE_ADD_ADDRESS;
    private Address mAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_address);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mFirstName = (EditText) findViewById(R.id.first_name);
        mLastName = (EditText) findViewById(R.id.last_name);
        mAddress1 = (EditText) findViewById(R.id.address1);
        mAddress2 = (EditText) findViewById(R.id.address2);
        mCity = (EditText) findViewById(R.id.city);
        mProvince = (EditText) findViewById(R.id.province);
        mCountry = (EditText) findViewById(R.id.country);
        mZip = (EditText) findViewById(R.id.zip);
        mPhone = (EditText) findViewById(R.id.phone);

        mType = getIntent().getIntExtra(KeyConstant.KEY_ADD_EDIT_ADDRESS,TYPE_ADD_ADDRESS);
        mAddress = (Address) getIntent().getSerializableExtra(KeyConstant.KEY_ADDRESS);
        if (mType == TYPE_EDIT_ADDRESS && mAddress != null) {
            showAddress(mAddress);
        }

        init();
    }

    public void showAddress(Address address) {
        mFirstName.setText(address.getFirstName());
        mLastName.setText(address.getLastName());
        mAddress1.setText(address.getAddress1());
        mAddress2.setText(address.getAddress2());
        mCity.setText(address.getCity());
        mProvince.setText(address.getProvince());
        mCountry.setText(address.getCountry());
        mZip.setText(address.getZip());
        mPhone.setText(address.getPhone());
    }

    private void init(){
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("添加地址");//修改
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (mType == TYPE_ADD_ADDRESS) {
            getSupportActionBar().setTitle("添加地址");//修改
        } else if (mType == TYPE_EDIT_ADDRESS) {
            getSupportActionBar().setTitle("修改地址");//修改
        }
    }

    @NonNull
    @Override
    public AddEditAddressPresenter createPresenter() {
        return new AddEditAddressPresenter();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_address,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                createUpdateAddress();
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void createUpdateAddress() {
        String firstName = mFirstName.getText().toString();
        String lastName = mLastName.getText().toString();
        String address1 = mAddress1.getText().toString();
        String address2 = mAddress2.getText().toString();
        String city = mCity.getText().toString();
        String province = mProvince.getText().toString();
        String country = mCountry.getText().toString();
        String zip = mZip.getText().toString();
        String phone = mPhone.getText().toString();
        if (mAddress == null) {
            mAddress = new Address(null,address1,address2,city,province,null,country,null,null,firstName,lastName,null,phone,zip,null);
        }else {
            if (TextUtils.isEmpty(mAddress.getId())) {
                SnackUtil.toastShow(this,"ID can't be empty");
                return ;
            }
            mAddress.setAddress(mAddress.getId(),address1,address2,city,province,mAddress.getProvinceCode(),
                    country,mAddress.getCountryCode(),mAddress.getCompany(),firstName,lastName,mAddress.getName(),phone,zip,mAddress.getCursor());
        }

        if (isHasEmpty(mAddress)) {
            return;
        }
        if (mType == TYPE_ADD_ADDRESS) {
            mPresenter.createAddress(mAddress);
        } else if (mType == TYPE_EDIT_ADDRESS) {
            mPresenter.updateAddress(mAddress);
        }
    }

    private boolean isHasEmpty(Address address) {
        if("".equals(address.getFirstName())){
            SnackUtil.toastShow(this,"FirstName is empty");
            return true;
        }
        if("".equals(address.getLastName())){
            SnackUtil.toastShow(this,"LastName is empty");
            return true;
        }
        if("".equals(address.getAddress1())){
            SnackUtil.toastShow(this,"address1 is empty");
            return true;
        }
        if("".equals(address.getAddress2())){
            SnackUtil.toastShow(this,"address2 is empty");
            return true;
        }
        if("".equals(address.getCity())){
            SnackUtil.toastShow(this,"City is empty");
            return true;
        }
        if("".equals(address.getProvince())){
            SnackUtil.toastShow(this,"Provinc is empty");
            return true;
        }
        if("".equals(address.getCountry())){
            SnackUtil.toastShow(this,"Country is empty");
            return true;
        }
        if("".equals(address.getZip())){
            SnackUtil.toastShow(this,"Zip is empty");
            return true;
        }
        if("".equals(address.getPhone())){
            SnackUtil.toastShow(this,"Phone is empty");
            return true;
        }
        return false;
    }

    @Override
    public void addAddressSuccess(Address address) {
        SnackUtil.toastShow(this,"add address success");
        finish();
    }

    @Override
    public void updateAddressSuccess(Address address) {
        SnackUtil.toastShow(this,"update address success");
        finish();
    }

    @Override
    public void onError(int code, String msg) {
        if (msg.contains("access denied")) {
            startActivity(new Intent(this, LoginActivity.class));
        }
        SnackUtil.toastShow(this,msg);
    }
}

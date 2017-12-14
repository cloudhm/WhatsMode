package com.whatsmode.shopify.block.address;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.listener.CustomListener;
import com.shopify.buy3.Storefront;
import com.whatsmode.library.util.SnackUtil;
import com.whatsmode.library.util.Util;
import com.whatsmode.shopify.R;
import com.whatsmode.shopify.block.account.LoginActivity;
import com.whatsmode.shopify.block.address.data.Site;
import com.whatsmode.shopify.block.me.StatusBarUtil;
import com.whatsmode.shopify.common.KeyConstant;
import com.whatsmode.shopify.mvp.MvpActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tom on 17-11-17.
 */

public class AddEditAddressActivity extends MvpActivity<AddEditAddressPresenter> implements AddEditAddressContract.View, View.OnClickListener {

    public static final int TYPE_ADD_ADDRESS = 1;
    public static final int TYPE_EDIT_ADDRESS = 2;

    private static final int ACTION_SELECT_COUNTRY = 10;
    private static final int ACTION_SELECT_PROVINCES = 20;

    private EditText mFirstName;
    private EditText mLastName;
    private EditText mAddress1;
    private EditText mAddress2;
    private EditText mCity;
    private TextView mProvince;
    private TextView mCountry;
    private EditText mZip;
    private EditText mPhone;
    private Toolbar mToolbar;

    private int mType = TYPE_ADD_ADDRESS;
    private Address mAddress;
    private OptionsPickerView mOptionsPickerView;

    private List<Site> mPickerDateCountry = new ArrayList<>();
    private List<Site> mPickerDateProvinces = new ArrayList<>();
    private JSONObject mCountryJsonObject;

    private int mAction;
    private TextView mTitle;
    private LinearLayout mProvinceL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_address);
        StatusBarUtil.StatusBarLightMode(this);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mFirstName = (EditText) findViewById(R.id.first_name);
        mLastName = (EditText) findViewById(R.id.last_name);
        mAddress1 = (EditText) findViewById(R.id.address1);
        mAddress2 = (EditText) findViewById(R.id.address2);
        mCity = (EditText) findViewById(R.id.city);
        mProvince = (TextView) findViewById(R.id.province);
        mCountry = (TextView) findViewById(R.id.country);
        mZip = (EditText) findViewById(R.id.zip);
        mPhone = (EditText) findViewById(R.id.phone);
        mTitle = (TextView) findViewById(R.id.title);
        mProvinceL = (LinearLayout) findViewById(R.id.province_l);
        mProvinceL.setOnClickListener(this);
        findViewById(R.id.country_l).setOnClickListener(this);
        findViewById(R.id.done).setOnClickListener(this);

        mType = getIntent().getIntExtra(KeyConstant.KEY_ADD_EDIT_ADDRESS,TYPE_ADD_ADDRESS);
        mAddress = (Address) getIntent().getSerializableExtra(KeyConstant.KEY_ADDRESS);
        if (mType == TYPE_EDIT_ADDRESS && mAddress != null) {
            showAddress(mAddress);
        }

        init();

        initCustomOptionPicker();
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

        showHideProvince(address.getCountry());
    }

    private void init(){
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");//修改
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (mType == TYPE_ADD_ADDRESS) {
            mTitle.setText(R.string.new_address);
            getSupportActionBar().setTitle("");
        } else if (mType == TYPE_EDIT_ADDRESS) {
            mTitle.setText(R.string.update_address);
            getSupportActionBar().setTitle("");
        }

        //requesr focus
        mFirstName.requestFocus();
    }

    @NonNull
    @Override
    public AddEditAddressPresenter createPresenter() {
        return new AddEditAddressPresenter();
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

    private void createUpdateAddress() {
        String firstName = mFirstName.getText().toString().trim();
        String lastName = mLastName.getText().toString().trim();
        String address1 = mAddress1.getText().toString().trim();
        String address2 = mAddress2.getText().toString().trim();
        String city = mCity.getText().toString().trim();
        String province = mProvince.getText().toString().trim();
        String country = mCountry.getText().toString().trim();
        String zip = mZip.getText().toString().trim();
        String phone = mPhone.getText().toString().trim();
        if (mAddress == null) {
            mAddress = new Address(null,address1,address2,city,province,null,country,null,null,firstName,lastName,null,phone,zip,null);
        }else {
            if (TextUtils.isEmpty(mAddress.getId()) && mType == TYPE_EDIT_ADDRESS) {
                SnackUtil.toastShow(this,R.string.id_cant_be_empty);
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
        showLoading();
    }

    private boolean isHasEmpty(Address address) {
        if("".equals(address.getFirstName())){
            SnackUtil.toastShow(this,R.string.first_name_empty_prompt);
            return true;
        }
        if("".equals(address.getLastName())){
            SnackUtil.toastShow(this,R.string.last_name_empty_prompt);
            return true;
        }
        if("".equals(address.getAddress1())){
            SnackUtil.toastShow(this,R.string.address1_empty_prompt);
            return true;
        }
        /*if("".equals(address.getAddress2())){
            SnackUtil.toastShow(this,"address2 is empty");
            return true;
        }*/
        if("".equals(address.getCity())){
            SnackUtil.toastShow(this,R.string.city_empty_prompt);
            return true;
        }
        if("".equals(address.getCountry())){
            SnackUtil.toastShow(this,R.string.country_empty_prompt);
            return true;
        }
        if("".equals(address.getProvince()) && isEnterProvince(address.getCountry())){
            SnackUtil.toastShow(this,R.string.state_empty_prompt);
            return true;
        }
        if("".equals(address.getZip())){
            SnackUtil.toastShow(this,R.string.zip_empty_prompt);
            return true;
        }
        if("".equals(address.getPhone())){
            SnackUtil.toastShow(this,R.string.phone_empty_prompt);
            return true;
        }
        return false;
    }

    private boolean isEnterProvince(String country) {
        List<Site> province = getProvince(country);
        if (!province.isEmpty()) {
            return true;
        }
        return false;
    }

    @Override
    public void addAddressSuccess(Address address) {
        hideLoading();
        SnackUtil.toastShow(this,R.string.add_address_success);
        Intent intent = new Intent();
        intent.putExtra(KeyConstant.KEY_EXTRA_ADDRESS,mAddress);
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void updateAddressSuccess(Address address) {
        hideLoading();
        SnackUtil.toastShow(this,R.string.update_address_success);
        Intent intent = new Intent();
        intent.putExtra(KeyConstant.KEY_EXTRA_ADDRESS,mAddress);
        setResult(RESULT_OK,intent);
        finish();
    }

    @Override
    public void onError(int code, String msg) {
        hideLoading();
        if (msg.contains("access denied")) {
            startActivity(new Intent(this, LoginActivity.class));
        }
        SnackUtil.toastShow(this,msg);
    }

    private void showHideProvince(String country){
        List<Site> provinceTemp = getProvince(country);
        if (provinceTemp.isEmpty()) {
            mProvinceL.setVisibility(View.GONE);
        }else{
            mProvinceL.setVisibility(View.VISIBLE);
        }
    }

    private void initCustomOptionPicker(){
        mOptionsPickerView = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                if (mAction == ACTION_SELECT_COUNTRY) {
                    if (mPickerDateCountry.size() > options1 && options1 >= 0) {
                        Site site = mPickerDateCountry.get(options1);
                        mCountry.setText(site.data);
                        mProvince.setText("");
                        showHideProvince(site.data);
                    }
                } else if (mAction == ACTION_SELECT_PROVINCES) {
                    if (mPickerDateProvinces.size() > options1 && options1 >= 0) {
                        Site site = mPickerDateProvinces.get(options1);
                        mProvince.setText(site.data);
                    }
                }
                mOptionsPickerView.dismiss();
            }
        })
                .setLayoutRes(R.layout.picker_select_address, new CustomListener() {
                    @Override
                    public void customLayout(View v) {
                        //自定义布局中的控件初始化及事件处理
                        final TextView tvSubmit = (TextView) v.findViewById(R.id.tv_finish);
                        ImageView ivCancel = (ImageView) v.findViewById(R.id.iv_cancel);
                        tvSubmit.setOnClickListener(v1 -> mOptionsPickerView.returnData());
                        ivCancel.setOnClickListener(v12 -> mOptionsPickerView.dismiss());

                    }
                }).setSelectOptions(70)
                .build();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.province_l:
                Util.hideInputMethod(this);
                mAction = ACTION_SELECT_PROVINCES;
                String country = mCountry.getText().toString();
                if (TextUtils.isEmpty(country)) {
                    SnackUtil.toastShow(this,R.string.country_empty_prompt);
                    return;
                }
                List<Site> provinceList = getProvince(country);
                if (!provinceList.isEmpty()) {
                    mOptionsPickerView.setSelectOptions(getProvincePosition(provinceList));
                    mOptionsPickerView.setPicker(provinceList);
                    mOptionsPickerView.show();
                }
                break;
            case R.id.country_l:
                Util.hideInputMethod(this);
                mAction = ACTION_SELECT_COUNTRY;
                if (mPickerDateCountry.isEmpty()) {
                    List<Site> countryList = Site.getCountry(this);
                    mPickerDateCountry.addAll(countryList);
                }

                mOptionsPickerView.setSelectOptions(getCountryPosition());
                mOptionsPickerView.setPicker(mPickerDateCountry);
                mOptionsPickerView.show();
                break;
            case R.id.done:
                createUpdateAddress();
                break;
        }
    }

    public int getCountryPosition(){
        String countryTemp = mCountry.getText().toString();
        int position = 0;
        if (TextUtils.isEmpty(countryTemp)) {
            position = 70;
        }else{
            position = Site.getPosition(mPickerDateCountry,countryTemp);
        }
        return position;
    }

    public int getProvincePosition(List<Site> sites){
        String temp = mProvince.getText().toString();
        int position = 0;
        if (TextUtils.isEmpty(temp)) {
            return position;
        }else{
            return Site.getPosition(sites,temp);
        }
    }

    private List<Site> getProvince(String country) {
        if (mCountryJsonObject == null) {
            JSONObject province = Site.getCountriesForProvince(this, country);
            mCountryJsonObject = province;
        }
        List<Site> sites = Site.jsonObjectToList(mCountryJsonObject,country);
        mPickerDateProvinces.clear();
        mPickerDateProvinces.addAll(sites);
        return mPickerDateProvinces;
    }


}

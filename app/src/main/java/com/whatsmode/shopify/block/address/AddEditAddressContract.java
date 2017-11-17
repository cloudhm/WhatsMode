package com.whatsmode.shopify.block.address;

import com.whatsmode.shopify.mvp.MvpPresenter;
import com.whatsmode.shopify.mvp.MvpView;

/**
 * Created by tom on 17-11-17.
 */

public interface AddEditAddressContract {
    interface View extends MvpView{
        void addAddressSuccess(Address address);
        void updateAddressSuccess(Address address);
        void onError(int code,String msg);
    }
    interface Presenter extends MvpPresenter<View>{
        void createAddress(Address address);
        void updateAddress(Address address);
    }
}

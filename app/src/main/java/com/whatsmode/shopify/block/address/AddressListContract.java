package com.whatsmode.shopify.block.address;

import android.support.annotation.NonNull;

import com.whatsmode.shopify.mvp.MvpPresenter;
import com.whatsmode.shopify.mvp.MvpView;

import java.util.List;

/**
 * Created by tom on 17-11-16.
 */

public interface AddressListContract {
    interface View extends MvpView{
        void showContent(@LoadType.checker int type,@NonNull List<Address> addresses);
        void onError(int code ,String msg);
        void deleteSuccess();
    }
    interface Presenter extends MvpPresenter<View>{
        void refreshAddressList();
        void loadModeAddressList();
        void deleteAddress(String id);
    }
}

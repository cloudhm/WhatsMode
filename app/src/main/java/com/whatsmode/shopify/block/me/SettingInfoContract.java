package com.whatsmode.shopify.block.me;

import com.whatsmode.shopify.mvp.MvpPresenter;
import com.whatsmode.shopify.mvp.MvpView;

/**
 * Created by tom on 17-11-25.
 */

public interface SettingInfoContract {
    interface View extends MvpView{
        void showCustomer(Customer customer);
        void onError(int code,String msg);
        void updateSuccess();
        void signoutSuccess();
    }
    interface Presenter extends MvpPresenter<View>{
        void getCustomer();
        void setCustomer(String firsrName,String lastName,String password);
        void signout();
    }
}

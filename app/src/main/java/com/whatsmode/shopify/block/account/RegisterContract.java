package com.whatsmode.shopify.block.account;

import com.whatsmode.shopify.mvp.MvpPresenter;
import com.whatsmode.shopify.mvp.MvpView;

/**
 * Created by tom on 17-11-16.
 */

public interface RegisterContract {
    interface View extends MvpView{
        void registerSuccess();
        void registerFail(String msg);
    }
    interface Presenter extends MvpPresenter<View>{
        void register(String email,String pwd,String firstName,String lastName);
    }
}

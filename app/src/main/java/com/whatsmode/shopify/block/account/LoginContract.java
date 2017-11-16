package com.whatsmode.shopify.block.account;

import com.whatsmode.shopify.mvp.MvpPresenter;
import com.whatsmode.shopify.mvp.MvpView;

/**
 * Created by tom on 17-11-16.
 */

public interface LoginContract {
    interface View extends MvpView{
        void loginSuccess();
        void loginFail(String msg);
    }

    interface Presenter extends MvpPresenter<View>{
        void login(String email,String pwd);
    }
}

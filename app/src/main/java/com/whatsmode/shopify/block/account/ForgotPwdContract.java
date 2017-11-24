package com.whatsmode.shopify.block.account;

import com.whatsmode.shopify.mvp.MvpPresenter;
import com.whatsmode.shopify.mvp.MvpView;

/**
 * Created by tom on 17-11-20.
 */

public interface ForgotPwdContract {
    interface View extends MvpView{
        void success();
        void onError(int code,String msg);
    }
    interface Presenter extends MvpPresenter<View>{
        void recover(String email);
    }
}

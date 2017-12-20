package com.whatsmode.shopify.block.me;

import com.whatsmode.shopify.mvp.MvpPresenter;
import com.whatsmode.shopify.mvp.MvpView;

/**
 * Created by tom on 17-11-30.
 */

public interface ChangePasswordContract {
    interface View extends MvpView{
        void updateSuccess();
        void onError(int code,String msg);
    }
    interface Presenter extends MvpPresenter<View>{
        void setPassword(String currentPwd,String newPwd);
        void setName(String firstName,String lastName);
    }
}

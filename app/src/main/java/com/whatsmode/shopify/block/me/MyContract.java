package com.whatsmode.shopify.block.me;

import com.whatsmode.shopify.mvp.MvpPresenter;
import com.whatsmode.shopify.mvp.MvpView;

/**
 * Created by tom on 17-11-20.
 */

public interface MyContract {
    interface View extends MvpView{

    }
    interface Presenter extends MvpPresenter<View>{

    }
}

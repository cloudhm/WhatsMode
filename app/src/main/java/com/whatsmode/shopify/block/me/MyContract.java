package com.whatsmode.shopify.block.me;

import android.support.annotation.NonNull;

import com.whatsmode.shopify.block.address.LoadType;
import com.whatsmode.shopify.mvp.MvpPresenter;
import com.whatsmode.shopify.mvp.MvpView;

import java.util.List;


/**
 * Created by tom on 17-11-20.
 */

public interface MyContract {
    interface View extends MvpView{
        void onError(int code ,String msg);
        void showCustomer(Customer customer);
        void showContent(@LoadType.checker int type, @NonNull List<Order> orders);
    }
    interface Presenter extends MvpPresenter<View>{
        void getCustomer();
        void refreshOrderList();
        void loadMoreOrderList();
    }
}

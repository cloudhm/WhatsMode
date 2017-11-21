package com.whatsmode.shopify.block.me;

import android.support.annotation.NonNull;

import com.whatsmode.shopify.base.BaseListContract;
import com.whatsmode.shopify.base.BaseListView;
import com.whatsmode.shopify.block.address.LoadType;
import com.whatsmode.shopify.mvp.MvpPresenter;
import com.whatsmode.shopify.mvp.MvpView;

import java.util.List;

/**
 * Created by tom on 17-11-21.
 */

public interface OrderListContract {
    interface View extends MvpView{
        void showContent(@LoadType.checker int type, @NonNull List<Order> orders);
        void onError(int code ,String msg);
    }
    interface Presenter extends MvpPresenter< View>{
        void refreshOrderList();
        void loadMoreOrderList();
    }
}

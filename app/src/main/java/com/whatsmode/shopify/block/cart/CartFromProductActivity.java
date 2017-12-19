package com.whatsmode.shopify.block.cart;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.whatsmode.library.util.ListUtils;
import com.whatsmode.library.util.PreferencesUtil;
import com.whatsmode.shopify.R;
import com.whatsmode.shopify.WhatsApplication;
import com.whatsmode.shopify.base.BaseActivity;
import com.whatsmode.shopify.common.Constant;
import com.whatsmode.shopify.ui.helper.ToolbarHelper;

import java.util.List;
public class CartFromProductActivity extends BaseActivity {

    private MenuItem menuEdit;
    CartFragment mFragment;
    private ToolbarHelper.ToolbarHolder toolbarHolder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_from_web);
        toolbarHolder = ToolbarHelper.initToolbar(this, R.id.toolbar, true, defineCartTitle(0));
        toolbarHolder.titleView.setVisibility(View.VISIBLE);
        mFragment = new CartFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, mFragment)
                .commit();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mFragment != null) {
            mFragment.saveCart();
        }
    }

    public String defineCartTitle(int badges){
        if (badges != 0) {
            toolbarHolder.titleView.setText("My Cart(" + badges + ")");
            return "My Cart(" + badges + ")";
        }
        try {
            List<CartItem> cartItemList = (List<CartItem>) PreferencesUtil.getObject(WhatsApplication.getContext(), Constant.CART_LOCAL);
            int badge = 0;
            if (!ListUtils.isEmpty(cartItemList)) {
                for (CartItem cartItem : cartItemList) {
                    if(!cartItem.isSoldOut)
                    badge += cartItem.getQuality();
                }
            }
            return ListUtils.isEmpty(cartItemList) ? "My Cart" : "My Cart(" + badge + ")";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        menuEdit = menu.findItem(R.id.action_edit);
        menuEdit.setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return clickMenuItem(item.getItemId()) || super.onOptionsItemSelected(item);
    }

    private boolean clickMenuItem(int itemId) {
        String title = menuEdit.getTitle().toString();
        menuEdit.setTitle(title.equals(getString(R.string.edit)) ? R.string.done : R.string.edit);
        mFragment.deleteCartItems(menuEdit.getTitle().toString());
        return true;
    }

    public void hideEdit(boolean visible) {
        menuEdit.setVisible(visible);
    }

}

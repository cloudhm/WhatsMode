package com.whatsmode.shopify.block.main;

import android.support.annotation.NonNull;
import android.os.Bundle;

import com.whatsmode.shopify.R;
import com.whatsmode.shopify.mvp.MvpActivity;
import com.whatsmode.shopify.ui.helper.BaseFragmentAdapter;
import com.whatsmode.shopify.ui.widget.NoScrollViewPager;

public class MainActivity extends MvpActivity<MainContact.Presenter> implements MainContact.View {

    private NoScrollViewPager vpContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        vpContent = (NoScrollViewPager) findViewById(R.id.vp_content);
        vpContent.setOffscreenPageLimit(3);
        getPresenter().initViewPage(getSupportFragmentManager());
    }

    @NonNull
    @Override
    public MainContact.Presenter createPresenter() {
        return new MainPresenter();
    }

    @Override
    public void setViewPage(BaseFragmentAdapter fragmentAdapter) {
        vpContent.setAdapter(fragmentAdapter);
    }
}

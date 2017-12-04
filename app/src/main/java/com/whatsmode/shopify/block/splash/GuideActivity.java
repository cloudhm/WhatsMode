package com.whatsmode.shopify.block.splash;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.whatsmode.library.util.PreferencesUtil;
import com.whatsmode.shopify.AppNavigator;
import com.whatsmode.shopify.R;
import com.whatsmode.shopify.common.KeyConstant;

/**
 * Created by tom on 17-12-4.
 */

public class GuideActivity extends AppCompatActivity {

    private ViewPager mViewPager;

    private static final int[] sGuide = {R.drawable.guide_1,R.drawable.guide_2,R.drawable.guide_3};
    private LayoutInflater mInflater;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_guide);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mInflater = LayoutInflater.from(this);
        init();
    }

    private void init(){
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setAdapter(new GuideAdapter());
    }

    class GuideAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return sGuide.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View inflate = mInflater.inflate(R.layout.item_guide, null);
            ImageView imageView = (ImageView) inflate.findViewById(R.id.image_view);
            Button start = (Button) inflate.findViewById(R.id.start);
            if(position == sGuide.length - 1){
                start.setVisibility(View.VISIBLE);
                start.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PreferencesUtil.putBoolean(GuideActivity.this, KeyConstant.KEY_IS_FIRST_START,false);
                        AppNavigator.jumpToMain(GuideActivity.this);
                        finish();
                    }
                });
            }else{
                start.setVisibility(View.GONE);
            }
            Glide.with(GuideActivity.this).load(sGuide[position]).into(imageView);
            container.addView(inflate);
            return inflate;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View)object);
        }
    }
}

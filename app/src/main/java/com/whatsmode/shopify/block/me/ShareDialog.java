package com.whatsmode.shopify.block.me;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.whatsmode.library.util.FileUtil;
import com.whatsmode.library.util.SerializableUtil;
import com.whatsmode.library.util.SnackUtil;
import com.whatsmode.shopify.R;
import com.whatsmode.shopify.common.Constant;
import com.zhy.base.fileprovider.FileProvider7;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import cn.sharesdk.facebook.Facebook;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.instagram.Instagram;
import cn.sharesdk.whatsapp.WhatsApp;

/**
 * Created by tom on 17-12-13.
 */

public class ShareDialog extends Dialog implements View.OnClickListener {

    private Platform.ShareParams mParams;
    private Context mContext;
    private String mLink;

    public ShareDialog(@NonNull Context context) {
        this(context,R.style.Dialog_Fullscreen);
    }

    public ShareDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        show();
        Window window = getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        window.setGravity(Gravity.CENTER);
        window.setContentView(R.layout.dialog_share);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.FILL_PARENT;
        lp.height = WindowManager.LayoutParams.FILL_PARENT;
        window.setAttributes(lp);
        init();
    }

    private void init() {
        findViewById(R.id.close).setOnClickListener(this);
        findViewById(R.id.facebook).setOnClickListener(this);
        findViewById(R.id.ins).setOnClickListener(this);
        findViewById(R.id.snapchat).setOnClickListener(this);
        findViewById(R.id.whatsapp).setOnClickListener(this);
        findViewById(R.id.url).setOnClickListener(this);
    }

    public void setShareData(String text, String imagePath, String url,String link){
        mParams = new Platform.ShareParams();
        mParams.setText(text);
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        mParams.setImageUrl("");//"http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg"
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        mParams.setImagePath(imagePath);//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        mParams.setUrl(url);

        mLink = link;
    }

    private void share(String name){
        if(mParams == null) return;
        if (name == Instagram.NAME) {
            checkImagePath(mParams);
        }
        Platform platform = ShareSDK.getPlatform(name);
        platform.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                SnackUtil.toastShow(mContext,R.string.ssdk_oks_share_completed);
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                SnackUtil.toastShow(mContext,R.string.ssdk_oks_share_failed);
            }

            @Override
            public void onCancel(Platform platform, int i) {
                SnackUtil.toastShow(mContext,R.string.ssdk_oks_share_canceled);
            }
        });
        platform.share(mParams);
    }

    private void checkImagePath(Platform.ShareParams params) {
        String imagePath = params.getImagePath();
        File file = new File(imagePath);
        if (!file.exists()) {
            String absolutePath = SerializableUtil.getSerializableFile(Constant.SHARE_DEFAULT_IMAGE_PATH, Constant.SHARE_DEFAULT_IMAGE_NAME).getAbsolutePath();
            File share = new File(absolutePath);
            if (!share.exists() || share.length() == 0) {
                copyImage(absolutePath);
            }
            params.setImagePath(absolutePath);
        }
    }

    private void copyImage(String absolutePath) {
        try {
            FileUtil.writeFile(mContext.getAssets().open("share.png"),absolutePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        if(mParams != null)
        switch (view.getId()) {
            case R.id.facebook:
                share(Facebook.NAME);
                break;
            case R.id.ins:
                share(Instagram.NAME);
                break;
            case R.id.snapchat:
                checkImagePath(mParams);
                ShareUtil.shareSystem(mContext, FileProvider7.getUriForFile(mContext, new File(mParams.getImagePath())));
                break;
            case R.id.whatsapp:
                share(WhatsApp.NAME);
                break;
            case R.id.url:
                ShareUtil.clipboar(mContext,mLink);
                SnackUtil.toastShow(mContext,R.string.url_copy_success);
                break;
            case R.id.close:
                break;
        }
        dismiss();
    }
}

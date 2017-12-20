package com.whatsmode.shopify.block.me;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.View;

import com.whatsmode.library.util.Util;
import com.whatsmode.shopify.R;
import com.zhy.base.fileprovider.FileProvider7;

import java.io.File;
import java.sql.SQLOutput;

import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by tom on 17-11-25.
 */

public class ShareUtil {
    public static void showShare(Context context,String text, String imagePath, String url,String link) {

        System.out.println("--------------------------imagePath:"+imagePath);
        System.out.println("---------------------------:"+new File(imagePath).exists());
        imagePath += ".jpg";
        //onekeyShare(context,text,imagePath,url,link);
        customShare(context,text,imagePath,url,link);
    }

    private static void customShare(Context context,String text, String imagePath, String url,String link){
        ShareDialog dialog = new ShareDialog(context);
        dialog.setShareData(text,imagePath,url,link);
    }

    private static void onekeyShare(Context context,String text, String imagePath, String url,String link){
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网、QQ和QQ空间使用
        //oks.setTitle("标题");
        // titleUrl是标题的网络链接，仅在Linked-in,QQ和QQ空间使用
        oks.setTitleUrl("https://whatsmode.com/");
        // text是分享文本，所有平台都需要这个字段
        oks.setText(text);
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        oks.setImageUrl("");//"http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg"
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        oks.setImagePath(imagePath);//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(url);
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        //oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        //oks.setSite("ShareSDK");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        //oks.setSiteUrl("http://sharesdk.cn");

        // 构造一个图标
        Bitmap snapchat = BitmapFactory.decodeResource(context.getResources(), R.drawable.ssdk_snapchat);
        View.OnClickListener listenerSnap = new View.OnClickListener() {
            public void onClick(View v) {
                shareSystem(context, FileProvider7.getUriForFile(context, new File(imagePath)));
            }
        };
        oks.setCustomerLogo(Util.scaleBitmap(snapchat,0.54f),context.getString(R.string.snapchat), listenerSnap);

        // 构造一个图标
        Bitmap enableLogo = BitmapFactory.decodeResource(context.getResources(), R.drawable.ssdk_url);
        View.OnClickListener listener = new View.OnClickListener() {
            public void onClick(View v) {
                clipboar(context,link);
            }
        };
        oks.setCustomerLogo(Util.scaleBitmap(enableLogo,0.54f), context.getString(R.string.url), listener);

        // 启动分享GUI
        oks.show(context);
    }

    public static void clipboar(Context context,String link){
        //获取剪贴板管理器：
        ClipboardManager cm = (ClipboardManager)context. getSystemService(Context.CLIPBOARD_SERVICE);
        // 创建普通字符型ClipData
        ClipData mClipData = ClipData.newPlainText("Label", link);
        // 将ClipData内容放到系统剪贴板里。
        cm.setPrimaryClip(mClipData);
    }

    public static void shareSystem(Context context, Uri uri) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.setType("image/*");
        context.startActivity(
                Intent.createChooser(shareIntent, context.getString(R.string.share_)));
    }

    public static void shareFull(Context context){
        ShareDialog dialog = new ShareDialog(context);
        dialog.show();
    }
}

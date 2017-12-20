package com.whatsmode.shopify.ui.helper;

import android.os.Environment;
import android.text.TextUtils;

import com.whatsmode.library.util.ToastUtil;
import com.whatsmode.shopify.common.Constant;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class SDFileHelper {


    public SDFileHelper() {
    }
    private static OkHttpClient client;

    //Glide保存图片
    public static void savePicture(final String suffix, String url) {
        //url = "https://img14.360buyimg.com/da/jfs/t4024/266/968344120/18745/d610233c/5863a879Nb8f02aeb.jpg";
        if (TextUtils.isEmpty(url)) {
            return;
        }
        client = new OkHttpClient();
        final Request request = new Request.Builder().get()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code() != 200) {
                    return;
                }
                InputStream in = response.body().byteStream();
                String filePath = Constant.ROOTPATH_IMAGE_CACHE;
                File dir1 = new File(filePath);
                if (!dir1.exists()) {
                    dir1.mkdirs();
                }
                String fileName = filePath + "/" + suffix + ".jpg";
                FileOutputStream output = new FileOutputStream(fileName);
                byte[] temp = new byte[1024];
                int length = 0;
                // 源文件读取一部分内容
                while ((length = in.read(temp)) != -1) {
                    // 目标文件写入一部分内容
                    output.write(temp, 0, length);
                    output.flush();
                }
                //将bytes写入到输出流中
                output.flush();
                in.close();
                output.close();
                //关闭输出流
            }
        });
    }

    //往SD卡写入文件的方法
    public static void savaFileToSD(String filename, byte[] bytes) throws Exception {
        //如果手机已插入sd卡,且app具有读写sd卡的权限
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String filePath = Environment.getExternalStorageDirectory().getCanonicalPath() + "/budejie";
            File dir1 = new File(filePath);
            if (!dir1.exists()) {
                dir1.mkdirs();
            }
            filename = filePath + "/" + filename + ".jpg";
            //这里就不要用openFileOutput了,那个是往手机内存中写数据的
            FileOutputStream output = new FileOutputStream(filename);
            output.write(bytes);
            //将bytes写入到输出流中
            output.close();
            //关闭输出流
            ToastUtil.showToast("图片已成功保存到" + filePath);
        } else ToastUtil.showToast( "SD卡不存在或者不可读写");
    }

    public static String getFilePath(long currentTimeStep) {
        return Constant.ROOTPATH_IMAGE_CACHE + File.separator + currentTimeStep;
    }
}

package com.whatsmode.library.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import org.joda.time.DateTime;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by tom on 17-11-22.
 */

public class Util {
    /**
     * 验证是否是邮箱
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        //电子邮件
        String check = "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";

        Pattern regex = Pattern.compile(check);
        Matcher matcher = regex.matcher(email);
        boolean isMatched = matcher.matches();
        return isMatched;
    }

    /**
     * 控制软键盘显示与消失
     * @param context
     * @param visible
     */
    public static void inputMethodVisible(Context context, boolean visible) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, visible ? InputMethodManager.HIDE_NOT_ALWAYS : InputMethodManager.RESULT_SHOWN);
    }

    /**
     * 软键盘hide
     * @param activity
     */
    public static void hideInputMethod(Activity activity){
        if (null != activity.getCurrentFocus()) {
            InputMethodManager mInputMethodManager = (InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE);
            mInputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED);
        }
    }

    public static boolean isSoftShowing(Activity activity){
        //获取当前屏幕内容的高度
        int screenHeight = activity.getWindow().getDecorView().getHeight();
        //获取View可见区域的bottom
        Rect rect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);

        return screenHeight - rect.bottom != 0;
    }

    public static final String[] sManth = {"January","February","March","April","May","June","July","August","September","October","November","December"};

    public static String dateTimeToString(DateTime dateTime){
        String s = dateTime.toString(" dd,yyyy HH:mm a");
        int monthOfYear = dateTime.getMonthOfYear();
        return sManth[monthOfYear - 1] + s;
    }

    public static String convertInputStreamToString(InputStream  is) throws Exception{
        byte[] buf = new byte[1024];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        for (int i; (i = is.read(buf)) != -1;) {
            baos.write(buf, 0, i);
        }
        String data = baos.toString("UTF-8");
        return data;
    }

    /**
     * 按比例缩放图片
     *
     * @param origin 原图
     * @param ratio  比例
     * @return 新的bitmap
     */
    public static Bitmap scaleBitmap(Bitmap origin, float ratio) {
        if (origin == null) {
            return null;
        }
        int width = origin.getWidth();
        int height = origin.getHeight();
        Matrix matrix = new Matrix();
        matrix.preScale(ratio, ratio);
        Bitmap newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false);
        if (newBM.equals(origin)) {
            return newBM;
        }
        origin.recycle();
        return newBM;
    }
}

package com.whatsmode.library.util;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;

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
}

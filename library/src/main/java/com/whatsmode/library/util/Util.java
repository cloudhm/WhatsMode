package com.whatsmode.library.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
}

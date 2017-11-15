package com.whatsmode.library.util;

public class PinyinUtil {
    //汉字返回拼音，字母原样返回，为小写
    public static String getPinYin(String input) {
        String result = HanziToPinyin.getInstance().getSelling(input);
        return result;
    }

    public static String getFirstLetter(String input){
        return HanziToPinyin.getInstance().getFirstLetter(input);
    }
}

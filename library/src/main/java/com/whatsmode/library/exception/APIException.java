package com.whatsmode.library.exception;

import android.support.annotation.NonNull;

import com.whatsmode.library.rx.Util;

import java.util.List;

public class APIException extends RuntimeException {

    public static final int CODE_COMMON_EXCEPTION = 0;
    public static final int CODE_SESSION_EXPIRE = 2;

    private int code;

    private String message;

    public APIException(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public APIException(int code,@NonNull final List<String> messages){
        this(code,formatMessage(Util.checkNotEmpty(messages, "messages can't be empty")));
    }

    private static String formatMessage(final List<String> messages) {
        StringBuilder builder = new StringBuilder();
        for (final String message : messages) {
            if (builder.length() > 0) {
                builder.append('\n');
            }
            builder.append(message);
        }
        return builder.toString();
    }

    public int getCode(){
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}

package com.whatsmode.library.exception;

import android.support.annotation.NonNull;

import com.whatsmode.library.rx.Util;

import java.util.List;

public class APIException extends RuntimeException {

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



    @Override
    public String getMessage() {
        return message;
    }
}

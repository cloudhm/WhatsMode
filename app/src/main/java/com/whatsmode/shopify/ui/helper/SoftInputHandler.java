package com.whatsmode.shopify.ui.helper;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class SoftInputHandler {

    public static void showInputMethodForQuery(final Context context,final View view) {
        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
        }
    }

    public static void hideInputMethod(final Context context, final View view) {

        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(),InputMethodManager.SHOW_FORCED);
        }

    }
}

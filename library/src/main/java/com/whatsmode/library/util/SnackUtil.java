package com.whatsmode.library.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

/**
 * Created by tom on 17-11-16.
 */

public class SnackUtil {
    public static void toastShow(Context context, CharSequence text){
        Toast.makeText(context,text,Toast.LENGTH_SHORT).show();
    }

    public static void toastShow(Context context, @StringRes int resId) {
        Toast.makeText(context,resId,Toast.LENGTH_SHORT).show();
    }

    public static void snackShow(@NonNull View view, @NonNull CharSequence text){
        Snackbar.make(view,text,Toast.LENGTH_SHORT).show();
    }

    public static void snackShow(@NonNull View view, @StringRes int resId){
        Snackbar.make(view,resId,Toast.LENGTH_SHORT).show();
    }
}

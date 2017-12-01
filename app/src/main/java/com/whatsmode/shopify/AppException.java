package com.whatsmode.shopify;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.SystemClock;
import android.util.Log;

import com.whatsmode.library.util.FileUtils;
import com.zchu.log.Logger;


/**
 * 应用程序异常类：用于捕获异常和收集崩溃信息
 */
public class AppException implements Thread.UncaughtExceptionHandler {
    /**
     * 系统默认的UncaughtException处理
     */
    private Thread.UncaughtExceptionHandler mDefaultHandler;

    private AppException() {
        this.mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
    }


    /**
     * 获取APP异常崩溃处理对象
     */
    public static AppException getAppExceptionHandler() {
        return new AppException();
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        Logger.e(ex);
        if (!handleException(ex) && mDefaultHandler != null) {//如果没有处理异常则交给系统处理

            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            //AppNavigator.showCrashActivity(ModeApplication.getAppContext(), ex.getMessage(), getCrashReport(ModeApplication.getAppContext(), ex));
            FileUtils.writeLog(WhatsApplication.getContext().getExternalFilesDir("log")
                    .getAbsolutePath(), SystemClock.currentThreadTimeMillis() + ".log",
                    getCrashReport(WhatsApplication.getContext(), ex));
            System.exit(0);// 关闭已奔溃的app进程
        }
    }

    /**
     * 自定义异常处收集错误信息&发送错误报
     *
     * @return true:处理了该异常信息;否则返回false
     */
    private boolean handleException(final Throwable ex) {


        return true;
    }

    /**
     * 获取APP崩溃异常报告
     *
     * @param ex
     * @return
     */
    private String getCrashReport(Context context, Throwable ex) {

        StringBuffer exceptionStr = new StringBuffer();
        PackageInfo pinfo;
        try {
            pinfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            exceptionStr.append("Version: " + pinfo.versionName + "\n");
            exceptionStr.append("VersionCode: " + pinfo.versionCode + "\n");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            exceptionStr.append("the application not found \n");
        }
        exceptionStr.append("Android: " + android.os.Build.VERSION.RELEASE + "(" + android.os.Build.MODEL + ")\n");
        String stackTraceString = Log.getStackTraceString(ex);
        if (stackTraceString != null && stackTraceString.length() > 0) {
            exceptionStr.append(stackTraceString + "\n");
        } else {
            exceptionStr.append("Exception: " + ex.getMessage() + "\n");
            StackTraceElement[] elements = ex.getStackTrace();
            for (int i = 0; i < elements.length; i++) {
                exceptionStr.append(elements[i].toString() + "\n");
            }
        }


        return exceptionStr.toString();
    }

}

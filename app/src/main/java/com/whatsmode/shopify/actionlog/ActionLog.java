package com.whatsmode.shopify.actionlog;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.whatsmode.shopify.R;
import com.whatsmode.shopify.WhatsApplication;
import com.whatsmode.shopify.block.account.data.AccountManager;
import com.whatsmode.shopify.common.Constant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2017/9/2.
 */
public class ActionLog {
    private boolean mIsReady = true;
    private int MAX_PENDING_EVENT = 10;
    private ArrayList<LogParam> mPendingEvents = new ArrayList<>(MAX_PENDING_EVENT);

    private static Hashtable<String,String> mLogTable = new Hashtable<>();
    private static Hashtable<String,String> mOptionalLogTable = new Hashtable<>();//controlled by remote

    public static final boolean IS_ACTION = true;

    static {

    }

    private ActionLog(){

    }
    public static ActionLog getInstance(){
        return ActionLogInner.instance;
    }
    public void logEventByFlurry(LogParam param){

        logFromGoogle(param);
        logFromFirebase(param);
    }

    private void logFromGoogle(LogParam param) {
        //HitBuilders.ScreenViewBuilder screenViewBuilder = new HitBuilders.ScreenViewBuilder();
        HitBuilders.EventBuilder eventBuilder = new HitBuilders.EventBuilder();
        eventBuilder.setCategory(param.getEventId());
        HashMap<String, String> params = param.getParams();
        if(params != null) {
            Set<Map.Entry<String, String>> entries = params.entrySet();
            for (Map.Entry<String, String> entry : entries) {
                eventBuilder.setCustomDimension(dimensionTransformation(entry.getKey()), entry.getValue());
            }
        }
        getDefaultTracker().send(eventBuilder.build());
    }

    private void logFromFirebase(LogParam param){
        Bundle bundle = new Bundle();
        HashMap<String, String> params = param.getParams();
        if(params != null) {
            Set<Map.Entry<String, String>> entries = params.entrySet();
            for (Map.Entry<String, String> entry : entries) {
                bundle.putString(entry.getKey(),entry.getValue());
            }
        }
        getFirebaseAnalytics().logEvent(param.getEventId(), bundle);
    }

    /**
     * 维度转化
     * @param key
     * @return
     */
    private int dimensionTransformation(String key) {
        switch (key) {

            //TODO
            default:
                return 0;
        }
    }

    public static void setUserId(String userIdStr){

    }
    private static String getRealEventId(String eventId){
        String realEventId = mLogTable.get(eventId);
        if(TextUtils.isEmpty(eventId)){
            realEventId = mOptionalLogTable.get(eventId);
        }
        return realEventId;
    }
    public static void onEventWithLocalName(String eventId){
        String realEventId = getRealEventId(eventId);
        if(!TextUtils.isEmpty(realEventId)) {
            getInstance().onEventInner(eventId, null);
        }
    }
    public static void onEventWithLocalName(LogParam logParam){
        if(!IS_ACTION) return;
        String realEventId = getRealEventId(logParam.getEventId());
        if(!TextUtils.isEmpty(realEventId)) {
            getInstance().onScreenInner(logParam.getEventId());
            getInstance().onEventInner(realEventId, logParam.setEventId(realEventId));
        }
    }

    private void onScreenInner(String eventId) {
        //Screen
        getDefaultTracker().setScreenName(eventId);
        getDefaultTracker().send(new HitBuilders.ScreenViewBuilder().build());
    }

    public static void onEvent(String eventId){
        if(!IS_ACTION) return;
        if(!TextUtils.isEmpty(eventId)) {
            getInstance().onEventInner(eventId, null);
        }
    }
    public static void onEvent(LogParam logParam){
        if(!IS_ACTION) return;
        String realEventId = logParam.getEventId();
        if(!TextUtils.isEmpty(realEventId)) {
            getInstance().onEventInner(realEventId, logParam.setEventId(realEventId));
        }
    }
    public static void onStartTimeEvent(LogParam logParam){
        getInstance().onStartTimeEventInner(logParam);
    }
    public static void onEndTimeEvent(String eventId){
        getInstance().onEndTimeEventInner(eventId);
    }
    private void onStartTimeEventInner(LogParam logParam){
        String realEventId = getRealEventId(logParam.getEventId());
        if(TextUtils.isEmpty(realEventId)){
            return;
        }
        if(logParam==null){
            logParam = new LogParam(realEventId);
        }
        //logParam.setParam(Constant.Param.EMAIL, AccountManager.getUsername());

    }
    private void onEndTimeEventInner(String eventId){
        String realEventId = getRealEventId(eventId);
        if(!TextUtils.isEmpty(realEventId)){

        }
    }
    private void onEventInner(String eventId,LogParam logParam){
        if(logParam==null){
            logParam = new LogParam(eventId);
        }
        //logParam.setParam(Constant.Param.EMAIL, AccountManager.getUsername());

        if(mIsReady){
            logEventByFlurry(logParam);
        }else {
            synchronized (this){
                if(mPendingEvents.size()<MAX_PENDING_EVENT) {
                    mPendingEvents.add(logParam);
                }
            }
        }
    }

    static class ActionLogInner{
        private static ActionLog instance = new ActionLog();
    }

    private Tracker mTracker;


    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(WhatsApplication.getContext());
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            mTracker = analytics.newTracker(R.xml.global_tracker);
        }
        return mTracker;
    }

    private FirebaseAnalytics mFirebaseAnalytics;

    synchronized public FirebaseAnalytics getFirebaseAnalytics(){
        if (mFirebaseAnalytics == null) {
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(WhatsApplication.getContext());
        }
        return mFirebaseAnalytics;
    }
}

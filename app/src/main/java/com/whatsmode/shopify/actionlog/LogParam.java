package com.whatsmode.shopify.actionlog;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/9/2.
 */
public class LogParam {
    private String mEventId;
    private HashMap<String,String> mParams = new HashMap<>();

    public LogParam(String eventId){
        mEventId = eventId;
    }
    public LogParam setEventId(String eventId){
        mEventId = eventId;
        return this;
    }
    public LogParam setParam(String key,String value){
        mParams.put(key,value);
        return this;
    }
    public LogParam setParam(String key,long value){
        mParams.put(key,value+"");
        return this;
    }
    public LogParam setParams(Map<String,String> params){
        for (Map.Entry<String, String> param : params.entrySet()) {
            mParams.put(param.getKey(),param.getValue());
        }
        return this;
    }

    public String getEventId() {
        return mEventId;
    }

    public HashMap<String, String> getParams() {
        return mParams;
    }
}

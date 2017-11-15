package com.earlydata.library.util;

import com.google.gson.Gson;

/**
 * Created by zchu on 17-1-11.
 */

public class GsonProvide {


    private GsonProvide(){}


    private static class GsonHoder{
        private static final  Gson gsonIns=new Gson();
    }

    public static Gson getGson(){
        return GsonHoder.gsonIns;
    }
}

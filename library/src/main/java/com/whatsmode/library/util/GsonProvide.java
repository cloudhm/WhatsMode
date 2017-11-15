package com.whatsmode.library.util;

import com.google.gson.Gson;

public class GsonProvide {


    private GsonProvide(){}


    private static class GsonHoder{
        private static final  Gson gsonIns=new Gson();
    }

    public static Gson getGson(){
        return GsonHoder.gsonIns;
    }
}

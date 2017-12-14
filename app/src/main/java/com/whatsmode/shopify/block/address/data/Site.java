package com.whatsmode.shopify.block.address.data;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.bigkoo.pickerview.model.IPickerViewData;
import com.whatsmode.library.util.Util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tom on 17-11-30.
 */

public class Site implements IPickerViewData {

    public String data;

    public Site(String data) {
        this.data = data;
    }

    @Override
    public String getPickerViewText() {
        return data;
    }

    /**
     * 获取国家列表(带有省份)
     * @param context
     * @param country
     * @return
     */
    public static JSONObject getCountriesForProvince(Context context,@NonNull String country){
        try {
            InputStream is = context.getAssets().open("countries.json");
            String json = Util.convertInputStreamToString(is);
            JSONObject jsonObject = new JSONObject(json);

            return jsonObject;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JSONObject();
    }

    /**
     *从国家列表中获取省份
     * @param jsonObject
     * @param country
     * @return
     */
    public static List<Site> jsonObjectToList(JSONObject jsonObject, String country) {
        List<Site> sites = new ArrayList<>();
        if (jsonObject != null) {
            JSONObject countryJson = jsonObject.optJSONObject(country);
            if (countryJson != null) {
                JSONArray provinces = countryJson.optJSONArray("provinces");
                if (provinces != null && provinces.length() > 0) {
                    for (int i = 0; i < provinces.length(); i++) {
                        sites.add(new Site(provinces.optString(i)));
                    }
                }
            }
        }
        return sites;
    }

    /**
     * 获取国家列表
     * @param context
     * @return
     */
    public static  List<Site> getCountry(Context context){
        try {
            InputStream is = context.getAssets().open("country.json");
            String json = Util.convertInputStreamToString(is);
            JSONArray jsonArray = new JSONArray(json);
            List<Site> sites = jsonArrayToList(jsonArray);
            return sites;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    /**
     * json to object
     * @param jsonArray
     * @return
     */
    public static  List<Site> jsonArrayToList(JSONArray jsonArray) {
        List<Site> sites = new ArrayList<>();
        if (jsonArray != null && jsonArray.length() > 0) {
            for (int i = 0; i < jsonArray.length(); i++) {
                String s = jsonArray.optString(i);
                sites.add(new Site(s));
            }
        }
        return sites;
    }

    public static int getPosition(List<Site> sites,String name){
        if (sites != null && !sites.isEmpty()) {
            for (int i = 0; i < sites.size(); i++) {
                Site site = sites.get(i);
                if (TextUtils.equals(site.data, name)) {
                    return i;
                }
            }
        }
        return 0;
    }
}

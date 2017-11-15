package com.whatsmode.library.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.HashMap;

public class PhotoUtil {

    /**获取图片
     * "thumbnail_path"对应真实图片路径值，键"image_path"对应缩略图路径值
     * */
    public static ArrayList<HashMap<String, String>> getAllPictures(Context context){
        ArrayList<HashMap<String, String>> pictureList = new ArrayList<>();
        HashMap<String, String> pictureMap;
        ContentResolver cr = context.getContentResolver();
        //先得到缩略图的URL和对应的图片id
        Cursor cursor = cr.query(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Thumbnails.IMAGE_ID, MediaStore.Images.Thumbnails.DATA},
                null, null, null);
        if(cursor == null){
            return pictureList;
        }
        if(cursor.moveToFirst()){
            do{
                pictureMap = new HashMap<>();
                pictureMap.put("image_id", cursor.getInt(0)+"");
                pictureMap.put("thumbnail_path", cursor.getString(1));
//                Log.e("thumbnail_path",cursor.getString(1));
                pictureList.add(pictureMap);
            }while (cursor.moveToNext());
                cursor.close();
        }

        //再得到正常图片的path
        for (int i = 0; i < pictureList.size(); i++) {
            pictureMap = pictureList.get(i);
            String media_id = pictureMap.get("image_id");
            cursor = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    new String[]{
                            MediaStore.Images.Media.DATA
                    },
                    MediaStore.Audio.Media._ID + "=" + media_id,
                    null,
                    null
            );
            if (cursor.moveToFirst()) {
                do {
                    pictureMap.put("image_path", cursor.getString(0));
//                    Log.e("image_path",cursor.getString(0));
                    pictureList.set(i, pictureMap);
                } while (cursor.moveToNext());
                cursor.close();
            }
        }
        HashMap<String, String> map = null;
        for (int i = 0; i < pictureList.size(); i++) {
            pictureMap = pictureList.get(i);
            String imagePath = pictureMap.get("image_path");
            if(null == imagePath){
                map = pictureMap;
            }

        }
        pictureList.remove(map);
        return pictureList;
    }
}

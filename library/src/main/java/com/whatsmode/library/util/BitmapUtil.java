package com.whatsmode.library.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.graphics.drawable.VectorDrawableCompat;

import java.io.ByteArrayOutputStream;

public class BitmapUtil {


    /**
     * convert Bitmap to byte array
     */
    public static byte[] bitmapToByte(Bitmap b) {
        ByteArrayOutputStream o = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.PNG, 100, o);
        return o.toByteArray();
    }

    /**
     * convert byte array to Bitmap
     */
    public static Bitmap byteToBitmap(byte[] b) {
        return (b == null || b.length == 0) ? null : BitmapFactory.decodeByteArray(b, 0, b.length);
    }

    /**
     * convert Drawable to Bitmap
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        return drawable == null ? null : ((BitmapDrawable) drawable).getBitmap();
    }

    public static BitmapDrawable vectorToBitmapDrawable(Context ctx, @DrawableRes int resVector) {
        return new BitmapDrawable(ctx.getResources(), vectorToBitmap(ctx, resVector));
    }

    /**
     * convert Bitmap to Drawable
     */
    public static Drawable bitmapToDrawable(Resources res, Bitmap bitmap) {
        return bitmap == null ? null : new BitmapDrawable(res, bitmap);
    }


    public static Bitmap vectorToBitmap(Context ctx, @DrawableRes int resVector) {
        Drawable drawable = VectorDrawableCompat.create(ctx.getResources(), resVector, null);
        //Drawable drawable = AppCompatDrawableManager.get().getDrawable(ctx, resVector);
        Bitmap b = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        drawable.setBounds(0, 0, c.getWidth(), c.getHeight());
        drawable.draw(c);
        return b;
    }


    /**
     * scale image
     */
    public static Bitmap scaleImageTo(Bitmap org, int newWidth, int newHeight) {
        return scaleImage(org, (float) newWidth / org.getWidth(), (float) newHeight / org.getHeight());
    }

    /**
     * scale image
     */
    public static Bitmap scaleImage(Bitmap org, float scaleWidth, float scaleHeight) {
        if (org == null) {
            return null;
        }
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(org, 0, 0, org.getWidth(), org.getHeight(), matrix, true);
    }


    public static Bitmap getRoundBitmap(Bitmap scaleBitmapImage) {
        int targetWidth = 1000;
        int targetHeight = 1000;
        Bitmap targetBitmap = Bitmap.createBitmap(targetWidth, targetHeight,
                Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(targetBitmap);
        Path path = new Path();
        path.addCircle(((float) targetWidth - 1) / 2,
                ((float) targetHeight - 1) / 2,
                (Math.min(((float) targetWidth), ((float) targetHeight)) / 2),
                Path.Direction.CCW);

        canvas.clipPath(path);
        Bitmap sourceBitmap = scaleBitmapImage;
        canvas.drawBitmap(sourceBitmap, new Rect(0, 0, sourceBitmap.getWidth(),
                sourceBitmap.getHeight()), new Rect(0, 0, targetWidth,
                targetHeight), null);
        return targetBitmap;
    }

    public static Drawable getRoundDrawable(Drawable d) {
        Bitmap b = getRoundBitmap(((BitmapDrawable) d).getBitmap());
        return new BitmapDrawable(b);
    }

    public static Drawable getNewDrawable(Drawable drawable) {
        return drawable.getConstantState().newDrawable();
    }

    public static  int[] getBitmapScaleSize(int outWidth, int outHeight, int maxWidth, int maxHeight, int minWidth, int minHeight) {
        int[] size = new int[2];
        BitmapScaleSize imageSize = new BitmapScaleSize();
        if (outWidth / maxWidth > outHeight / maxHeight) {//
            if (outWidth >= maxWidth) {//
                size[0] = maxWidth;
                size[1]= outHeight * maxWidth / outWidth;
            } else {
                size[0] = outWidth;
                size[1] = outHeight;
            }
            if (outHeight < minHeight) {
                size[1]= minHeight;
                int width = outWidth * minHeight / outHeight;
                if (width > maxWidth) {
                    size[0] = maxWidth;
                } else {
                    size[0] = width;
                }
            }
        } else {
            if (outHeight >= maxHeight) {
                size[1]= maxHeight;
                size[0] = outWidth * maxHeight / outHeight;
            } else {
                size[1]= outHeight;
                size[0] = outWidth;
            }
            if (outWidth < minWidth) {
                size[0] = minWidth;
                int height = outHeight * minWidth / outWidth;
                if (height > maxHeight) {
                    size[1]= maxHeight;
                } else {
                    size[1]= height;
                }
            }
        }
        return size;
    }

/*    public static BitmapScaleSize getBitmapScaleSize(Bitmap bitmap, int maxWidth, int maxHeight, int minWidth, int minHeight) {
        BitmapScaleSize imageSize = new BitmapScaleSize();
        if (null == bitmap || bitmap.isRecycled()) {
            return imageSize;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] byteTmp = baos.toByteArray();
        try {
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(byteTmp, 0, byteTmp.length, bitmapOptions);
        int outWidth = bitmapOptions.outWidth;
        int outHeight = bitmapOptions.outHeight;
        return getBitmapScaleSize(outWidth, outHeight, maxWidth, maxHeight, minWidth, minHeight);

    }*/

    public static class BitmapScaleSize {

        public BitmapScaleSize(int height, int width) {
            this.height = height;
            this.width = width;
        }

        public BitmapScaleSize() {
        }

        public int width;
        public int height;
    }

    public static int[] getImageWidthHeight(String path){
        BitmapFactory.Options options = new BitmapFactory.Options();

        /**
         * 最关键在此，把options.inJustDecodeBounds = true;
         * 这里再decodeFile()，返回的bitmap为空，但此时调用options.outHeight时，已经包含了图片的高了
         */
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options); // 此时返回的bitmap为null
        /**
         *options.outHeight为原始图片的高
         */
        return new int[]{options.outWidth,options.outHeight};
    }

}

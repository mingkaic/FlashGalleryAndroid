package com.chen.mingkai.flashgallery;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;

public class PictureUtils {
    public static Bitmap getScaledBitMap(String path, Activity activity) {
        Point size = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(size);

        return getScaledBitMap(path, size.x, size.y);
    }

    public static Bitmap getScaledBitMap(String path, int destWidth, int destHeight) {
        // obtains input dimensions
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        float srcWidth = options.outWidth;
        float srcHeight = options.outHeight;

        // scale
        int inSampleSize = 1;
        if (srcHeight > destHeight || srcWidth > destWidth) { // scale down
            inSampleSize = Math.round(srcHeight/destHeight);
        } else {
            inSampleSize = Math.round(srcWidth/destWidth);
        }

        options = new BitmapFactory.Options();
        options.inSampleSize = inSampleSize;

        return BitmapFactory.decodeFile(path, options);
    }
}

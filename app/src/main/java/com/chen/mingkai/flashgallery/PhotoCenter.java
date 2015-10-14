package com.chen.mingkai.flashgallery;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.chen.mingkai.flashgallery.PhotoDbSchema.PhotoTable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// photo model control (singleton)
public class PhotoCenter {
    private static PhotoCenter sPhotoCenter;

    // private List<Photo> mPhotos;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static PhotoCenter get(Context context) {
        if (null == sPhotoCenter) {
            sPhotoCenter = new PhotoCenter(context);
        }
        return sPhotoCenter;
    }

    private PhotoCenter(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new PhotoBaseHelper(mContext).getWritableDatabase();
    }

    public void addPhoto(Photo photo) {
        // mPhotos.add(photo);
        ContentValues values = getContentValues(photo);
        mDatabase.insert(PhotoTable.NAME, null, values);
    }

    public void updatePhoto(Photo photo) {
        String uuidString = photo.getId().toString();
        ContentValues values = getContentValues(photo);

        mDatabase.update(PhotoTable.NAME, values, PhotoTable.Cols.UUID + " =?", new String[]{uuidString});
    }

    public void deletePhoto(Photo photo) {
        String uuidString = photo.getId().toString();
        mDatabase.delete(PhotoTable.NAME, PhotoTable.Cols.UUID + " =?", new String[]{uuidString});
    }

    // return the entire list
    public List<Photo> getPhotos() {
        List<Photo> photos = new ArrayList();

        PhotoCursorWrapper cursor = queryPhoto(null, null);

        try {
            cursor.moveToFirst();
            while (false == cursor.isAfterLast()) {
                photos.add(cursor.getPhoto());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return photos;
    }

    // return one photo in the list
    public Photo getPhoto(UUID id) {
        PhotoCursorWrapper cursor = queryPhoto(PhotoTable.Cols.UUID + " =?", new String[] {id.toString()});
        Photo photo = null;

        try {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                photo = cursor.getPhoto();
            }
        } finally {
            cursor.close();
        }

        return photo;
    }

    public File getPhotoFile(Photo photo) {
        File externalFilesDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        if (null != externalFilesDir) {
            externalFilesDir = new File(externalFilesDir, photo.getPhotoFilename());
        }

        return externalFilesDir;
    }

    private static ContentValues getContentValues(Photo photo) {
        ContentValues values = new ContentValues();
        values.put(PhotoTable.Cols.UUID, photo.getId().toString());
        values.put(PhotoTable.Cols.Title, photo.getTitle());
        values.put(PhotoTable.Cols.Date, photo.getDate().getTime());
        values.put(PhotoTable.Cols.Description, photo.getDescription());

        return values;
    }

    private PhotoCursorWrapper queryPhoto(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                PhotoTable.NAME,
                null, // Columns (null selects all)
                whereClause,
                whereArgs,
                null, null, null // groupBy, having, orderBy
        );

        return new PhotoCursorWrapper(cursor);
    }
}

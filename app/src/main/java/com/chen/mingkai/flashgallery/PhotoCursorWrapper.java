package com.chen.mingkai.flashgallery;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.chen.mingkai.flashgallery.PhotoDbSchema.PhotoTable;

import java.util.Date;
import java.util.UUID;

public class PhotoCursorWrapper extends CursorWrapper {
    public PhotoCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Photo getPhoto() {
        String uuidString = getString(getColumnIndex(PhotoTable.Cols.UUID));
        String title = getString(getColumnIndex(PhotoTable.Cols.Title));
        long date = getLong(getColumnIndex(PhotoTable.Cols.Date));
        String description = getString(getColumnIndex(PhotoTable.Cols.Description));

        Photo photo = new Photo(UUID.fromString(uuidString));
        photo.setTitle(title);
        photo.setDate(new Date(date));
        photo.setDescription(description);

        return photo; // for now
    }
}

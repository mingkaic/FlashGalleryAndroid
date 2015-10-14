package com.chen.mingkai.flashgallery;

import java.util.Date;
import java.util.UUID;

// photo data model
public class Photo {
    private UUID mId;
    private String mTitle;
    private Date mDate;
    private String mDescription;

    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public Photo() {
        this(UUID.randomUUID());
    }

    public Photo(UUID id) {
        mId = id;
        mDate = new Date();
    }

    public String getPhotoFilename() {
        return "IMG_"+getId().toString()+".jpg";
    }
}

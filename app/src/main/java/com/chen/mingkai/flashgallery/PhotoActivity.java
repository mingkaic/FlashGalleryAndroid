package com.chen.mingkai.flashgallery;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.UUID;

public class PhotoActivity extends SingleFragmentActivity {
    private static final String EXTRA_PHOTO_ID = "com.chen.mingkai.flashgallery.photo_id";

    public static Intent newIntent(Context packageContext, UUID photoId) {
        Intent intent = new Intent(packageContext, PhotoActivity.class);
        intent.putExtra(EXTRA_PHOTO_ID, photoId);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        UUID photoId = (UUID) getIntent().getSerializableExtra(EXTRA_PHOTO_ID);
        return PhotoFragment.newInstance(photoId);
    }
}

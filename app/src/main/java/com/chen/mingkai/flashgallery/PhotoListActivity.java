package com.chen.mingkai.flashgallery;

import android.content.Intent;
import android.support.v4.app.Fragment;

public class PhotoListActivity extends SingleFragmentActivity
        implements PhotoListFragment.Callbacks, PhotoFragment.Callbacks {
    @Override
    protected Fragment createFragment() {
        return new PhotoListFragment();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_masterdetail; // prevents master detail on phone
    }

    @Override
    public void onPhotoSelected(Photo photo) {
        if (null == findViewById(R.id.detail_fragment_container)) {
            Intent intent = PhotoPagerActivity.newIntent(this, photo.getId());
            startActivity(intent);
        } else {
            Fragment newDetail = PhotoFragment.newInstance(photo.getId());
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.detail_fragment_container, newDetail)
                    .commit();
        }
    }

    @Override
    public void onPhotoUpdate(Photo photo) {
        PhotoListFragment listFragment = (PhotoListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        listFragment.updateUI();
    }
}

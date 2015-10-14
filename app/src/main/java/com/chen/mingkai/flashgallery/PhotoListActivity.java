package com.chen.mingkai.flashgallery;

import android.support.v4.app.Fragment;

public class PhotoListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new PhotoListFragment();
    }
}

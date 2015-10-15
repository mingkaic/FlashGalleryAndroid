package com.chen.mingkai.flashgallery;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.List;
import java.util.UUID;

// flips between photo
public class PhotoPagerActivity extends AppCompatActivity implements PhotoFragment.Callbacks {
    // data models
    private static final String EXTRA_PHOTO_ID = "com.chen.mingkai.flashgallery.photointent.photo_id";

    // views
    private ViewPager mViewPager;
    private List<Photo> mPhotos;

    public static Intent newIntent(Context packageContext, UUID photoId) {
        Intent intent = new Intent(packageContext, PhotoPagerActivity.class);
        intent.putExtra(EXTRA_PHOTO_ID, photoId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_pager);

        UUID photoId = (UUID) getIntent().getSerializableExtra(EXTRA_PHOTO_ID);

        mViewPager = (ViewPager) findViewById(R.id.activity_photo_pager_view_pager);
        mPhotos = PhotoCenter.get(this).getPhotos();
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Photo photo = mPhotos.get(position);
                return PhotoFragment.newInstance(photo.getId());
            }

            @Override
            public int getCount() {
                return mPhotos.size();
            }
        });

        int i = 0;
        while (i < mPhotos.size() && false == mPhotos.get(i).getId().equals(photoId)) {
            i++;
        }
        if (mPhotos.get(i).getId().equals(photoId)) {
            mViewPager.setCurrentItem(i);
        }
    }

    @Override
    public void onPhotoUpdate(Photo photo) {

    }
}

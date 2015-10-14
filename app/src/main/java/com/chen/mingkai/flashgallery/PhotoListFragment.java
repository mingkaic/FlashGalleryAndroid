package com.chen.mingkai.flashgallery;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.List;

public class PhotoListFragment extends Fragment {
    // data models
    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";
    private static final int REQUEST_PHOTO = 1;
    private boolean mSubtitleVisible;

    // views
    private RecyclerView mPhotoRecyclerView;
    private PhotoAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    // inner classes
    private class PhotoHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private Photo mPhoto;
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private Button mDeleteBtn;

        public PhotoHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mTitleTextView = (TextView) itemView.findViewById(R.id.list_item_photo_title_text_view);
            mDateTextView = (TextView) itemView.findViewById(R.id.list_item_photo_date_text_view);
            mDeleteBtn = (Button) itemView.findViewById(R.id.list_item_delete_photo_btn);
        }

        public void bindPhoto(Photo photo) {
            mPhoto = photo;
            mTitleTextView.setText(mPhoto.getTitle());
            mDateTextView.setText(mPhoto.getDate().toString());
            mDeleteBtn.setOnClickListener((View view)->{
                PhotoCenter.get(getActivity()).deletePhoto(photo);
                updateUI();
            });
        }

        @Override
        public void onClick(View v) {
            Intent intent = PhotoPagerActivity.newIntent(getActivity(), mPhoto.getId());
            startActivity(intent);
        }
    }

    private class PhotoAdapter extends RecyclerView.Adapter<PhotoHolder> {
        private List<Photo> mPhotos;

        public PhotoAdapter(List<Photo> photos) {
            mPhotos = photos;
        }

        @Override
        public PhotoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_photo, parent, false);
            return new PhotoHolder(view);
        }

        @Override
        public void onBindViewHolder(PhotoHolder holder, int position) {
            Photo photo = mPhotos.get(position);
            holder.bindPhoto(photo);
        }

        @Override
        public int getItemCount() {
            return mPhotos.size();
        }

        public void setPhotos(List<Photo> photos) {
            mPhotos = photos;
        }
    }

    // implemented list UI methods
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo_list, container, false);
        mPhotoRecyclerView = (RecyclerView) view.findViewById(R.id.photo_recycler_view);

        mPhotoRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (null != saveInstanceState) {
            mSubtitleVisible = saveInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }

        updateUI();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible);
    }

    private void updateUI() {
        PhotoCenter photoCenter = PhotoCenter.get(getActivity());

        List<Photo> photos = photoCenter.getPhotos();

        if (null == mAdapter) {
            mAdapter = new PhotoAdapter(photos);
            mPhotoRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setPhotos(photos);
            mAdapter.notifyDataSetChanged();
        }
        updateSubtitle();
    }

    // top menu methods
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_photo_list, menu);

        // subtitle on menu
        MenuItem subtitleItem = menu.findItem(R.id.menu_item_show_subtitle);
        if (mSubtitleVisible) {
            subtitleItem.setTitle(R.string.hide_subtitle);
        } else {
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_photo:
                Photo photo = new Photo();
                PhotoCenter.get(getActivity()).addPhoto(photo);
                Intent intent = PhotoPagerActivity.newIntent(getActivity(), photo.getId());
                startActivity(intent);
                return true;
            case R.id.menu_item_show_subtitle:
                mSubtitleVisible = !mSubtitleVisible;
                getActivity().invalidateOptionsMenu();

                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateSubtitle() {
        PhotoCenter photoCenter = PhotoCenter.get(getActivity());
        int photoCount = photoCenter.getPhotos().size();
        String subtitle = getString(R.string.subtitle_format, photoCount);

        if (false == mSubtitleVisible) {
            subtitle = null;
        }

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }
}

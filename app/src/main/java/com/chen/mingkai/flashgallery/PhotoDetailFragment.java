package com.chen.mingkai.flashgallery;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import java.io.File;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class PhotoDetailFragment extends DialogFragment {
    // data model
    private static final String ARG_PHOTO = "photo";

    // widget
    private ImageView mImageView;

    public static PhotoDetailFragment newInstance(File photoFile) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_PHOTO, photoFile);

        PhotoDetailFragment fragment = new PhotoDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        File photoFile = (File) getArguments().getSerializable(ARG_PHOTO);
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_photo, null);

        mImageView = (ImageView) v.findViewById(R.id.dialog_photo_image_view);
        if (null == photoFile || false == photoFile.exists()) {
            mImageView.setImageDrawable(null);
        } else {
            Bitmap bitmap = PictureUtils.getScaledBitMap(photoFile.getPath(), getActivity());
            mImageView.setImageBitmap(bitmap);
        }

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.photo_detail_title)
                .create();
    }
}

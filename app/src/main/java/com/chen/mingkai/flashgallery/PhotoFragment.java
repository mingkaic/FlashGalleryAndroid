package com.chen.mingkai.flashgallery;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;
import java.util.Date;
import java.util.UUID;

public class PhotoFragment extends Fragment{
    // data model
    private Photo mPhoto;
    private File mPhotoFile;
    private Callbacks mCallbacks;

    public interface Callbacks {
        void onPhotoUpdate(Photo photo);
    }

    private static final String ARG_PHOTO_ID = "photo_id";
    private static final String DIALOG_DATE = "DialogDate";
    private static final String DIALOG_PHOTO = "DialogPhoto";
    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_CONTACT = 1;
    private static final int REQUEST_PHOTO = 2;
    private static final int REQUEST_DETAIL = 3;

    // widgets
    private EditText mTitleField;
    private ImageView mPhotoView;
    private Button mPhotoButton;
    private Button mDateButton;
    private EditText mDescriptionField;
    private Button mSendButton;

    public static PhotoFragment newInstance(UUID photoId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_PHOTO_ID, photoId);

        PhotoFragment fragment = new PhotoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID photoId = (UUID) getArguments().getSerializable(ARG_PHOTO_ID);
        mPhoto = PhotoCenter.get(getActivity()).getPhoto(photoId);
        mPhotoFile = PhotoCenter.get(getActivity()).getPhotoFile(mPhoto);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallbacks = (Callbacks)activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onPause() {
        super.onPause();
        PhotoCenter.get(getActivity()).updatePhoto(mPhoto); // save
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_photo, container, false);
        // wiring up the title text
        mTitleField = (EditText) v.findViewById(R.id.photo_title);
        mTitleField.setText(mPhoto.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // do nothing for now
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mPhoto.setTitle(s.toString()); // record in mPhoto object
                updatePhoto();
            }

            @Override
            public void afterTextChanged(Editable s) {
                // do nothing here too
            }
        });

        PackageManager packageManager = getActivity().getPackageManager();

        // wiring up photo
        mPhotoView = (ImageView) v.findViewById(R.id.photo_view);
        updatePhotoView();
        mPhotoView.setOnClickListener((View view) -> {
            FragmentManager manager = getFragmentManager();
            PhotoDetailFragment dialog = PhotoDetailFragment.newInstance(mPhotoFile);
            dialog.setTargetFragment(PhotoFragment.this, REQUEST_DETAIL);
            dialog.show(manager, DIALOG_PHOTO);
        });

        mPhotoButton = (Button) v.findViewById(R.id.photo_button);
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // verifying permission
        boolean canTakePhoto = null != mPhotoFile && null != captureImage.resolveActivity(packageManager);
        mPhotoButton.setEnabled(canTakePhoto);

        if (true == canTakePhoto) {
            Uri uri = Uri.fromFile(mPhotoFile);
            captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }

        mPhotoButton.setOnClickListener(view -> {
            startActivityForResult(captureImage, REQUEST_PHOTO);
        });

        // wiring date and description
        mDateButton = (Button) v.findViewById(R.id.photo_date);
        updateDate();
        mDateButton.setOnClickListener((View view) -> {
            FragmentManager manager = getFragmentManager();
            DatePickerFragment dialog = DatePickerFragment.newInstance(mPhoto.getDate());
            dialog.setTargetFragment(PhotoFragment.this, REQUEST_DATE);
            dialog.show(manager, DIALOG_DATE);
        });

        mDescriptionField = (EditText) v.findViewById(R.id.description);
        mDescriptionField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // do nothing for now
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mPhoto.setDescription(s.toString()); // record in mPhoto object
            }

            @Override
            public void afterTextChanged(Editable s) {
                // do nothing here too
            }
        });

        mSendButton = (Button) v.findViewById(R.id.photo_send);
        mSendButton.setOnClickListener((View view) -> {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent = Intent.createChooser(intent, getString(R.string.send_picture));
            startActivity(intent);
        });

        return v;
    }

    private void updateDate() {
        mDateButton.setText(mPhoto.getDate().toString());
    }

    private void updatePhotoView() {
        if (null == mPhotoFile || false == mPhotoFile.exists()) {
            mPhotoView.setImageDrawable(null);
        } else {
            Bitmap bitmap = PictureUtils.getScaledBitMap(mPhotoFile.getPath(), getActivity());
            mPhotoView.setImageBitmap(bitmap);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Activity.RESULT_OK == resultCode) {
            if (REQUEST_DATE == requestCode) {
                Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
                mPhoto.setDate(date);
                updatePhoto();
                updateDate();
            } else if (requestCode == REQUEST_PHOTO) {
                updatePhoto();
                updatePhotoView();
            }
        }
    }

    private void updatePhoto() {
        PhotoCenter.get(getActivity()).updatePhoto(mPhoto);
        mCallbacks.onPhotoUpdate(mPhoto);
    }
}

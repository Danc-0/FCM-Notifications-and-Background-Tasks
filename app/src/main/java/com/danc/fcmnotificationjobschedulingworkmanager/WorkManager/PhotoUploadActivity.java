package com.danc.fcmnotificationjobschedulingworkmanager.WorkManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.danc.fcmnotificationjobschedulingworkmanager.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;

public class PhotoUploadActivity extends AppCompatActivity {

    private static final String TAG = "PhotoUploadActivity";
    private ImageView imageView;

    // Uri indicates, where the image will be picked from
    public static Uri filePath;
    Uri data;

    // request code
    private final int PICK_IMAGE_REQUEST_CODE = 22;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_upload);
        imageView = findViewById(R.id.imageView);


    }

    public void selectImage(View view) {
        selectUserImage();

        // Create object of SharedPreferences.
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        //now get Editor
        SharedPreferences.Editor editor = sharedPref.edit();
        //put your value
        editor.putString("userName", String.valueOf(filePath));

        //commits your edits
        editor.commit();
    }

    private void selectUserImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image From..."), PICK_IMAGE_REQUEST_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // checking request code and result code
        // if request code is PICK_IMAGE_REQUEST_CODE and
        // resultCode is RESULT_OK
        // then set image in the image view
        if (requestCode == PICK_IMAGE_REQUEST_CODE
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            Log.d(TAG, "onActivityResult: I want to kno what filepath contains " + data.getData());
            // Get the Uri of data
            filePath = data.getData();

            // Setting image on image view using Bitmap, Glide, Picasso, Coin etc
            try {
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }

    public void uploadImage(View view) {
        if (filePath != null) {
            Log.d(TAG, "uploadImage: " + filePath);
            Constraints constraints = new Constraints.Builder()
                    .setRequiresCharging(true)
                    .setRequiresStorageNotLow(true)
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build();
            // 1. Define our Task here
            WorkRequest workRequest = new OneTimeWorkRequest.Builder(PhotoUploadWorker.class)
                    .build();

            // 2. Now we start the Task with this piece of code
            WorkManager.getInstance(getApplicationContext())
                    .enqueue(workRequest);
        } else {
            Toast.makeText(this, "Please Select an Image First", Toast.LENGTH_SHORT).show();
        }


    }
}
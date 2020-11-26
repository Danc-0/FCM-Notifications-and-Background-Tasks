package com.danc.fcmnotificationjobschedulingworkmanager.WorkManager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class PhotoUploadWorker extends Worker {
    private static final String TAG = "PhotoUploadWorker";
    Context myContext;
    PhotoUploadActivity photoUploadActivity;

    // instance for firebase storage and StorageReference
    FirebaseStorage storage;
    StorageReference storageReference;

    public PhotoUploadWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        //Do the Work here
        uploadImage();
//        WorkRequest uploadWorkRequest = new OneTimeWorkRequest.Builder(PhotoUploadWorker.class)
//                .build();
//        WorkManager
//                .getInstance(myContext)
//                .enqueue(uploadWorkRequest);

        //Indicate if the work has finished successfully with the Result
        return Result.success();
    }

    public void uploadImage() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String userName = sharedPref.getString("userName", "Not Available");
        Uri filePath = Uri.parse(userName);

        Log.d(TAG, "uploadImage: Uploading Photo to Google");
        Log.d(TAG, "uploadImage: ");
        // get the Firebase  storage reference
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        // Code for showing progressDialog while uploading
//        ProgressDialog progressDialog
//                = new ProgressDialog(getApplicationContext());
//        progressDialog.setTitle("Uploading...");
//        progressDialog.show();

        // Defining the child of storageReference
        StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());

        // adding listeners on upload
        // or failure of image
        ref.putFile(filePath)
                .addOnSuccessListener(
                        new OnSuccessListener<UploadTask.TaskSnapshot>() {

                            @Override
                            public void onSuccess(
                                    UploadTask.TaskSnapshot taskSnapshot) {

                                // Image uploaded successfully
                                // Dismiss dialog
//                                progressDialog.dismiss();
                                Toast
                                        .makeText(getApplicationContext(),
                                                "Image Uploaded!!",
                                                Toast.LENGTH_SHORT)
                                        .show();
                            }
                        })

                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        // Error, Image not uploaded
//                        progressDialog.dismiss();
                        Toast
                                .makeText(getApplicationContext(),
                                        "Failed " + e.getMessage(),
                                        Toast.LENGTH_SHORT)
                                .show();
                    }
                })
                .addOnProgressListener(
                        new OnProgressListener<UploadTask.TaskSnapshot>() {

                            // Progress Listener for loading
                            // percentage on the dialog box
                            @Override
                            public void onProgress(
                                    UploadTask.TaskSnapshot taskSnapshot) {
                                double progress
                                        = (100.0
                                        * taskSnapshot.getBytesTransferred()
                                        / taskSnapshot.getTotalByteCount());
//                                progressDialog.setMessage(
//                                        "Uploaded "
//                                                + (int) progress + "%");
                            }
                        });
    }
}

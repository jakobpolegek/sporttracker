package com.example.sporttracker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class VideoActivity extends AppCompatActivity {

    SharedPreferences sp;
    ApplicationMy app;

    public static final String USERNAME = "USERNAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        app = (ApplicationMy) getApplication();

        findViewById(R.id.record).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT,10);
                startActivityForResult(intent,1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1){
            //AlertDialog.Builder builder = new AlertDialog.Builder(this);
            //VideoView videoView = new VideoView(this);
            //videoView.setVideoURI(data.getData());
            //videoView.start();
            //builder.setView(videoView).show();
            sp = PreferenceManager.getDefaultSharedPreferences(app.getApplicationContext());
            StorageReference storageRef = FirebaseStorage.getInstance().getReference();
            ProgressDialog progressDialog = new ProgressDialog(VideoActivity.this);
            final StorageReference photoRef = storageRef.child("Video").child("video" + sp.getString(USERNAME,"DEFAULT VALUE "));
// add File/URI
            assert data != null;
            photoRef.putFile(data.getData())
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Upload succeeded
                            Toast.makeText(getApplicationContext(), "Upload Success...", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(VideoActivity.this,MainActivity.class));
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Upload failed
                            Toast.makeText(getApplicationContext(), "Upload failed...", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnProgressListener(
                    new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //calculating progress percentage
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                            //displaying percentage in progress dialog

                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    });


        }
    }
}
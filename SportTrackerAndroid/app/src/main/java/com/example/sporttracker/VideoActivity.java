package com.example.sporttracker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

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
                intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT,25);
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
            final StorageReference photoRef = storageRef.child("video" + app.thisUser.getDocID());
// add File/URI
            assert data != null;
            photoRef.putFile(data.getData())
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Upload succeeded
                            new postThread().execute();
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

    class postThread extends AsyncTask<Void, Void, Void> {

        private Exception exception;

        protected Void doInBackground(Void... params) {
            try {
                URL url = new URL("http://83.212.126.21:5000/processData");
                HttpURLConnection http = (HttpURLConnection)url.openConnection();
                http.setRequestMethod("POST");
                http.setDoOutput(true);
                http.setRequestProperty("Content-Type", "application/json");

                String data = "{\"idDoc\":\""+ app.thisUser.getDocID() +"\"}";
                System.out.println("IDDOC iz requesta: " + app.thisUser.getDocID());
                byte[] out = data.getBytes(StandardCharsets.UTF_8);

                OutputStream stream = http.getOutputStream();
                stream.write(out);

                System.out.println(http.getResponseCode() + " " + http.getResponseMessage());
                http.disconnect();
            }
            catch (Exception e){
                System.out.println(e);
            }

            return null;
        }


    }
}
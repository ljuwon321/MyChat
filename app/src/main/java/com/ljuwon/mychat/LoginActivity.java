package com.ljuwon.mychat;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Button;
import android.widget.FrameLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.apache.commons.lang.StringUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class LoginActivity extends AppCompatActivity {
    private static final int GALLERY_CALL = 0;

    private FrameLayout profileImageLayout;
    private CircleImageView profileImage;
    private TextInputEditText nickname;
    private SharedPreferences pref;

    private FirebaseStorage storage;

    private String nick = "";
    private final String imagePath = Environment.getExternalStorageDirectory() + "/Android/data/com.ljuwon.mychat/profile.jpg";
    private String imageUrl = "";

    private String androidID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);

        androidID = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);

        FloatingActionButton loginButton = (FloatingActionButton) findViewById(R.id.login_button);
        loginButton.setOnClickListener((view) -> {
            nick = nickname.getText().toString();

            Snackbar load = Snackbar.make(view, getString(R.string.loading), Snackbar.LENGTH_INDEFINITE);
            load.show();

            SharedPreferences.Editor editor = pref.edit();
            editor.putString("username", nick);
            editor.commit();

            uploadImageFile((exception) -> {
                load.dismiss();
                Snackbar.make(view, getString(R.string.upload_fail), Snackbar.LENGTH_SHORT).show();
            }, (taskSnapshot) -> {
                UploadTask.TaskSnapshot snapshot = (UploadTask.TaskSnapshot) taskSnapshot;

                Uri downloadUrl = snapshot.getMetadata().getDownloadUrl();
                imageUrl = downloadUrl.toString();

                Log.d("MyChat/info", imageUrl);
                Log.d("MyChat/info", nick);

                MainActivity.setImageUrl(imageUrl);
                MainActivity.setNickname(nick);

                load.dismiss();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            });
        });

        profileImageLayout = (FrameLayout) findViewById(R.id.profile_image_layout);
        profileImage = (CircleImageView) findViewById(R.id.profile_image);
        nickname = (TextInputEditText) findViewById(R.id.profile_name);

        if(!StringUtils.isBlank(pref.getString("username", "")))
            nickname.setText(pref.getString("username", ""));

        profileImageLayout.setClickable(true);
        profileImageLayout.setOnClickListener((view) -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
            intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.putExtra("crop", "true");

            intent.putExtra("outputX", 128);
            intent.putExtra("outputY", 128);
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);

            intent.putExtra("scale", true);
            intent.putExtra("noFaceDetection", true);

            startActivityForResult(intent, GALLERY_CALL);
        });

        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        profileImage.setImageBitmap(bitmap);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == GALLERY_CALL) {
            if(resultCode == LoginActivity.RESULT_OK) {
                Bundle extra = data.getExtras();
                Bitmap bitmap = extra.getParcelable("data");

                File dir = new File(Environment.getExternalStorageDirectory(), "Android/data/com.ljuwon.mychat");
                dir.mkdirs();

                File file = new File(imagePath);
                try {
                    FileOutputStream stream = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                profileImage.setImageBitmap(bitmap);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadImageFile(OnFailureListener failureListener, OnSuccessListener successListener) {
        if(storage == null) {
            storage = FirebaseStorage.getInstance();
        }

        StorageReference storageRef = storage.getReferenceFromUrl("gs://mychat-c0670.appspot.com");
        StorageReference mountainsRef = storageRef.child("profile/" + "profile_" + androidID + ".jpg");

        InputStream stream = null;
        try {
            stream = new FileInputStream(new File(imagePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        UploadTask uploadTask = mountainsRef.putStream(stream);
        uploadTask.addOnFailureListener(failureListener);
        uploadTask.addOnSuccessListener(successListener);
    }
}

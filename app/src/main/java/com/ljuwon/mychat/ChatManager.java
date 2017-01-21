package com.ljuwon.mychat;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;

public class ChatManager {
    private FirebaseStorage firebaseStorage;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    private OnChatListener listener;

    public ChatManager() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReferenceFromUrl("gs://mychat-c0670.appspot.com");

        databaseReference.child("chat").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ChatData chatData = dataSnapshot.getValue(ChatData.class);

                Log.d("MyChat/ChatManager", "t: " + chatData.getType());

                listener.onRecived(chatData);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void send(ChatData data) {
        databaseReference.child("chat").push().setValue(data);
    }

    public void sendMessage(String image_url, String name, String message, String time) {
        ChatData data = new ChatData();

        data.setImage_url(image_url);
        data.setTime(time);
        data.setMessage(message);
        data.setUserName(name);
        data.setType(ChatData.MESSAGE);

        databaseReference.child("chat").push().setValue(data);
    }

    public void sendImage(Bitmap bitmap, String image_url, String name, String time) {
        ChatData chatData = new ChatData();

        StorageReference mountainsRef = storageReference.child("chat/image/" + name + "_" + new Date().toString() + ".jpg");

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] data = stream.toByteArray();

        UploadTask uploadTask = mountainsRef.putBytes(data);
        uploadTask.addOnSuccessListener((taskSnapshot) -> {
            UploadTask.TaskSnapshot snapshot = taskSnapshot;

            Uri downloadUrl = snapshot.getMetadata().getDownloadUrl();

            chatData.setImage_url(image_url);
            chatData.setUserName(name);
            chatData.setMessage(downloadUrl.toString());
            chatData.setTime(time);
            chatData.setType(ChatData.IMAGE);

            databaseReference.child("chat").push().setValue(chatData);
        });
    }

    public void setChatListener(OnChatListener listener) {
        this.listener = listener;
    }

    public interface OnChatListener {
        void onRecived(ChatData chatData);
    }
}

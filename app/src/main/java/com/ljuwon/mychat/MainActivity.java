package com.ljuwon.mychat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.google.firebase.database.DataSnapshot;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private static final int GALLERY_CALL = 0;

    private static String user_name;
    private static String image_url;
    private String[] filter = {
            "시발", "ㅅㅂ", "시벌", "tq", "tlqkf",
            "병신", "ㅄ", "ㅂㅅ",
            "ㅗ",
    };

    private EditText chatEdit;
    private ImageButton sendButton;
    private ImageButton moreButton;
    private RecyclerView chatList;
    private static Context context;

    private ChatAdapter adapter;
    private ChatManager chatManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context = this;

        chatEdit = (EditText) findViewById(R.id.chat_edit);
        sendButton = (ImageButton) findViewById(R.id.send_button);
        moreButton = (ImageButton) findViewById(R.id.more_button);
        chatList = (RecyclerView) findViewById(R.id.chat_list);

        adapter = new ChatAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        chatList.setLayoutManager(layoutManager);
        chatList.setHasFixedSize(true);
        chatList.setAdapter(adapter);

        sendButton.setOnClickListener((view) -> {
            String chat = chatEdit.getText().toString();

            if(!chat.trim().equals("")) {
                for(String s : filter) {
                    if(chat.contains(s)) {
                        chatEdit.setText("");
                        return;
                    }
                }
                Date date = new Date();
                SimpleDateFormat CurTimeFormat = new SimpleDateFormat("aa HH:mm");
                String CurTime = CurTimeFormat.format(date);

                chatManager.sendMessage(image_url, user_name, chat, CurTime);

                chatEdit.setText("");
                chatList.scrollToPosition(adapter.getItemCount());
            }
        });

        moreButton.setOnClickListener((view) -> {
            showMoreDialog();
        });

        chatManager = new ChatManager();
        chatManager.setChatListener(chatData -> {
            adapter.addItem(chatData);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == GALLERY_CALL) {
            if(resultCode == LoginActivity.RESULT_OK) {
                try {
                    sendImage(MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void showMoreDialog() {
        AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setTitle(getString(R.string.select_type));

        LinearLayout layout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.file_select_dialog, null);

        LinearLayout image = (LinearLayout) layout.findViewById(R.id.image_layout);
        image.setClickable(true);
        image.setOnClickListener((view) -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
            intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

            startActivityForResult(intent, GALLERY_CALL);
            dialog.dismiss();
        });

        dialog.setView(layout);
        dialog.show();
    }

    private void sendImage(Bitmap bitmap) {
        Date date = new Date();
        SimpleDateFormat CurTimeFormat = new SimpleDateFormat("aa HH:mm");
        String CurTime = CurTimeFormat.format(date);

        chatManager.sendImage(bitmap, image_url, user_name, CurTime);
    }

    public static void setImageUrl(String url) {
        image_url = url;
    }
    public static void setNickname(String name) {
        user_name = name;
    }
    public static Context getContext() {
        return context;
    }
}

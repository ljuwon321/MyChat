package com.ljuwon.mychat;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import org.apache.commons.lang.StringUtils;

public class MainActivity extends AppCompatActivity {
    private ListView chat_list;
    private ImageButton send_bt;
    private EditText message;

    private static String user_name;
    private static String image_url;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private ChatAdapter adapter;

    private ClipboardManager clipboardManager;

    private ArrayList<ChatData> mListData = new ArrayList<>();
    private String[] s = {"시발", "병신", "개새끼", "느금", "ㅄ", "ㅂㅅ", "ㅗ", "노애미", "후장", "자지", "보지", "좆", "좇", "꼬추", "잠지"};
    private Toolbar toolbar;
    private String mes;
    private boolean b;
    private StringUtils StringUtils;
    private String email;
    private BitmapDrawable d;
    private BitmapDrawable d2;
    private String CurTime;

    private int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar)findViewById(R.id.chat_toolbar);
        setSupportActionBar(toolbar);

        clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

        chat_list = (ListView) findViewById(R.id.listView);
        send_bt = (ImageButton) findViewById(R.id.send);
        message = (EditText) findViewById(R.id.chat_et);

        adapter = new ChatAdapter(this, mListData);
        ScrollBottom();

        chat_list.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        chat_list.setOnItemClickListener((parent, view, position, id) -> {
            pos = position;
        });

        chat_list.setOnItemLongClickListener((parent, view, position, id) -> {
            Toast.makeText(MainActivity.this, getString(R.string.copy), Toast.LENGTH_SHORT).show();
            ChatData chatData = adapter.getItem(position);
            ClipData clipData = ClipData.newPlainText("label",chatData.getMessage());
            clipboardManager.setPrimaryClip(clipData);
            return false;
        });

        send_bt.setOnClickListener((v) -> {
            mes = message.getText().toString();

            long now2 = System.currentTimeMillis();
            Date date = new Date(now2);
            SimpleDateFormat CurTimeFormat = new SimpleDateFormat("aa HH:mm");
            CurTime = CurTimeFormat.format(date);

            b = true;

            for (int i = 0; i < s.length; i++) {

                if (mes.contains(s[i]) == true || mes.length() == 0 || StringUtils.isBlank(mes))
                    b = false;

            }
            if (b == true)
                chatdata();
        });
        databaseReference.child("chat").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ChatData chatData = dataSnapshot.getValue(ChatData.class);
                mListData.add(new ChatData(chatData.getUserName(), chatData.getMessage(), chatData.getImage_url(), chatData.getTime()));
                ScrollBottom();
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) { }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) { }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });

    }

    private void ScrollBottom() {
        chat_list.post(new Runnable() {
            @Override
            public void run() {
                chat_list.setSelection(adapter.getCount() - 1);
            }
        });
    }

    private void chatdata() {
        ChatData chatData = new ChatData(user_name, mes, image_url, CurTime);
        databaseReference.child("chat").push().setValue(chatData);
        adapter.notifyDataSetChanged();
        message.setText("");
    }

    public static void setImageUrl(String url) {
        image_url = url;
    }
    public static void setNickname(String name) {
        user_name = name;
    }
}

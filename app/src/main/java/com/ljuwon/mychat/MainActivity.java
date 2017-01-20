package com.ljuwon.mychat;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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

    public static ListView chat_list;
    ImageButton send_bt;
    EditText message;
    String user_name;
    public static String image_url;

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();
    ChatAdapter adapter;

    ClipboardManager clipboardManager;

    ArrayList<ChatData> mListData = new ArrayList<>();
    String[] s= {"시발", "병신", "개새끼", "느금", "ㅄ", "ㅂㅅ", "ㅗ", "노애미", "후장", "자지", "보지", "좆", "좇", "꼬추", "잠지"};
    Toolbar toolbar;
    String mes;
    boolean b;
    StringUtils StringUtils;
    String email;
    BitmapDrawable d;
    BitmapDrawable d2;
    String CurTime;

    int pos;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        toolbar = (Toolbar)findViewById(R.id.chat_toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        if(null != ab) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.finish();
            }
        });

        Intent intent = getIntent();
        user_name = intent.getStringExtra("nickname");
        image_url = intent.getStringExtra("profile_url");
        email = "user" + new Random().nextInt(10000) + "@gmail.com"; //계정 연동시 사용함

        clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        chat_list = (ListView) findViewById(R.id.listView);
        send_bt = (ImageButton) findViewById(R.id.send);
        message = (EditText) findViewById(R.id.chat_et);

        adapter = new ChatAdapter(this, mListData);
        ScrollBottom();

        chat_list.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        chat_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pos = position;
            }
        });

        chat_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, "클립보드에 복사되었습니다", Toast.LENGTH_SHORT).show();
                ChatData chatData = adapter.getItem(position);
                ClipData clipData = ClipData.newPlainText("label",chatData.getMessage());
                clipboardManager.setPrimaryClip(clipData);
                return false;
            }
        });

        send_bt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
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
                    }
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

}

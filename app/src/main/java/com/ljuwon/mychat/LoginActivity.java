package com.ljuwon.mychat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.lang.StringUtils;

import java.util.Random;

/**
 * Created by 주원 on 2017-01-19.
 */
public class LoginActivity extends AppCompatActivity {

    Toolbar toolbar;
    Button login_bt;
    EditText nick_et;
    EditText profile_et;
    String user_name, image_url;
    StringUtils StringUtils;
    SharedPreferences pref;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);

        toolbar = (Toolbar) findViewById(R.id.chat_toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        if (null != ab) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        login_bt = (Button)findViewById(R.id.login_bt);
        nick_et = (EditText)findViewById(R.id.nick_et);
        profile_et = (EditText)findViewById(R.id.profile_et);

        if(!StringUtils.isBlank(pref.getString("username", "")))
            nick_et.setText(pref.getString("username", ""));

        if(!StringUtils.isBlank(pref.getString("profile", "")))
            profile_et.setText(pref.getString("profile", ""));

        login_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                if(StringUtils.isBlank(nick_et.getText().toString())) {
                    user_name = "유저" + new Random().nextInt(10000);
                    image_url = profile_et.getText().toString();
                    if (StringUtils.isBlank(profile_et.getText().toString()))
                        image_url = "https://search.pstatic.net/sunny/?src=https%3A%2F%2Fcdn1.iconfinder.com%2Fdata%2Ficons%2Fcrimes-and-justice%2F100%2F14-512.png&type=b400";
                }
                else {
                    if(nick_et.getText().toString().length() > 20) {
                        Toast.makeText(LoginActivity.this, "닉네임은 최대 20글자까지 적을 수 있습니다", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else {
                        user_name = nick_et.getText().toString();
                        image_url = profile_et.getText().toString();
                        if (StringUtils.isBlank(profile_et.getText().toString()))
                            image_url = "https://search.pstatic.net/sunny/?src=https%3A%2F%2Fcdn1.iconfinder.com%2Fdata%2Ficons%2Fcrimes-and-justice%2F100%2F14-512.png&type=b400";
                    }
                }

                intent.putExtra("nickname", user_name);
                intent.putExtra("profile_url", image_url);

                SharedPreferences.Editor editor = pref.edit();
                editor.putString("username", user_name);
                editor.putString("profile", image_url);
                editor.commit();

                nick_et.setText(user_name);
                profile_et.setText(image_url);
                startActivity(intent);
            }
        });

    }

}

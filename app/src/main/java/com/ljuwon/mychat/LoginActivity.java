package com.ljuwon.mychat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        toolbar = (Toolbar) findViewById(R.id.chat_toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        if (null != ab) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        login_bt = (Button)findViewById(R.id.login_bt);
        nick_et = (EditText)findViewById(R.id.nick_et);
        profile_et = (EditText)findViewById(R.id.profile_et);

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
                    user_name = nick_et.getText().toString();
                    image_url = profile_et.getText().toString();
                    if(StringUtils.isBlank(profile_et.getText().toString()))
                        image_url = "https://search.pstatic.net/sunny/?src=https%3A%2F%2Fcdn1.iconfinder.com%2Fdata%2Ficons%2Fcrimes-and-justice%2F100%2F14-512.png&type=b400";
                }

                intent.putExtra("nickname", user_name);
                intent.putExtra("profile_url", image_url);

                nick_et.setText(user_name);
                profile_et.setText(image_url);
                startActivity(intent);
            }
        });

    }

}

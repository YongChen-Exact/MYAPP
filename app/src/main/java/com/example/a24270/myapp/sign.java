package com.example.a24270.myapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by 24270 on 2018/5/21.
 */

public class sign extends AppCompatActivity {
    private EditText useraccount;
    private EditText userpassword;
    private EditText userpasswords;
    private Button zc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign);
        useraccount = findViewById(R.id.useraccount);
        userpassword = findViewById(R.id.userpassword);
        userpasswords = findViewById(R.id.userpasswords);
        zc=findViewById(R.id.zc);
        zc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = useraccount.getText().toString();
                String password = userpassword.getText().toString();
                String passwords = userpasswords.getText().toString();
                SharedPreferences.Editor editor = getSharedPreferences("user", MODE_PRIVATE).edit();
                if (account.length() > 5 && password.length() > 5 && passwords.length() > 5) {
                    if (passwords.equals(password)) {
                        editor.putString("name", account);
                        editor.putString("key", password);
                        editor.apply();

                        Toast.makeText(getApplicationContext(), "注册成功！", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(sign.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "两次输入密码不一致哦！", Toast.LENGTH_SHORT).show();
                    }
                }
                else Toast.makeText(getApplicationContext(), "账号或密码不足有效长度！", Toast.LENGTH_SHORT).show();
            }
        });
    }
}


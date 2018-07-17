package com.example.a24270.myapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
/**
 * Created by 24270 on 2018/4/19.
 */
public class LoginActivity extends AppCompatActivity {

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private EditText accountEdit;
    private EditText passwordEdit;

    private Button login;
    private Button sign;
    private Button skip;
    private CheckBox rememberPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        accountEdit = (EditText) findViewById(R.id.account);
        passwordEdit = (EditText) findViewById(R.id.password);
        rememberPass = (CheckBox) findViewById(R.id.remember_pass);
        login = (Button) findViewById(R.id.login);
        sign = (Button)findViewById(R.id.sign);
        skip = (Button)findViewById(R.id.skip);

        boolean isRemember = pref.getBoolean("remember_password", false);
        if (isRemember) {
            String account = pref.getString("account", "");
            String password = pref.getString("password", "");
            accountEdit.setText(account);
            passwordEdit.setText(password);
            rememberPass.setChecked(true);
        }
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = accountEdit.getText().toString();
                String password = passwordEdit.getText().toString();
                SharedPreferences Pref = getSharedPreferences("user",MODE_PRIVATE);
                String name = Pref.getString("name","");
                String key = Pref.getString("key","");
                //判断账号和密码不为空，且需和数据库中的账号密码匹配
                if (account.length() == 0||password.length() == 0){
                        Toast.makeText(LoginActivity.this, "请输入账号或密码!",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    if (account.equals(name) && password.equals(key)) {
                        editor = pref.edit();
                        if (rememberPass.isChecked()) {
                            editor.putBoolean("remember_password", true);
                            editor.putString("account", account);
                            editor.putString("password", password);
                        } else {
                            editor.clear();
                        }
                        editor.apply();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "密码或账号错误",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, sign.class);
                startActivity(intent);
             //   finish();
            }
        });
    }

}
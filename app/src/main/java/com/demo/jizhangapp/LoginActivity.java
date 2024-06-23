package com.demo.jizhangapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.demo.jizhangapp.bean.User;
import com.demo.jizhangapp.db.Database;
import com.demo.jizhangapp.util.ActivityManager;
import com.demo.jizhangapp.util.SomeUtil;


public class LoginActivity extends AppCompatActivity {
    private EditText phoneEdit;
    private EditText pwdEdit;
    private TextView loginBtn;
    private ImageView backBtn;

    private TextView goRegisterBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActivityManager.getInstance().add(this);
        findViewsById();


        initListener();
    }

    private void initListener() {
        goRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void login() {
        String phone = phoneEdit.getText().toString().trim();
        String pwd = pwdEdit.getText().toString().trim();
        if (phone.isEmpty()) {
            phoneEdit.setError("请输入账号");
            return;
        }

        User registeredUser = Database.getDao().queryUserByPhone(phone);
        if (pwd.isEmpty()) {
            pwdEdit.setError("请输入密码");
            return;
        }

        if (registeredUser == null) {
            Toast.makeText(this, "此账号未注册，请先注册", Toast.LENGTH_SHORT).show();
        } else {
            if (pwd.equals(registeredUser.password)) {
                Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
                App.login(registeredUser);
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            } else {
                Toast.makeText(this, "帐户或密码不正确，请重新输入", Toast.LENGTH_SHORT).show();
            }
        }

    }


    private void findViewsById() {
        phoneEdit = findViewById(R.id.phoneEdit);
        pwdEdit = findViewById(R.id.pwdEdit);
        loginBtn = findViewById(R.id.loginBtn);
        goRegisterBtn = findViewById(R.id.goRegisterBtn);
        backBtn = findViewById(R.id.backBtn);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("TAG", "onDestroy: ");
        ActivityManager.getInstance().remove(this);
    }
}
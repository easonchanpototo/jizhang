package com.demo.jizhangapp;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.demo.jizhangapp.bean.User;
import com.demo.jizhangapp.db.Database;
import com.demo.jizhangapp.util.ActivityManager;

public class RegisterActivity extends AppCompatActivity {
    private View backBtn;
    private EditText phoneEdit;
    private EditText pwdEdit;
    private TextView registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ActivityManager.getInstance().add(this);
        findViewsById();
        initListener();
    }

    private void initListener() {

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }


    private void register() {
        String phone = phoneEdit.getText().toString().trim();
        String pwd = pwdEdit.getText().toString().trim();


        if (phone.isEmpty()) {
            phoneEdit.setError("请输入帐号");
            return;
        }
        if (pwd.isEmpty()) {
            pwdEdit.setError("请输入密码");
            return;
        }

        User registeredUser = Database.getDao().queryUserByPhone(phone);

        if (registeredUser != null) {
            Toast.makeText(this,"该帐户已注册，请更改", Toast.LENGTH_SHORT).show();
        } else {
            User user = new User();
            user.phone = phone;
            user.password = pwd;
            Database.getDao().register(user);
            Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
            finish();
        }


    }

    private void findViewsById() {
        backBtn = findViewById(R.id.backBtn);
        phoneEdit = findViewById(R.id.phoneEdit);
        pwdEdit = findViewById(R.id.pwdEdit);
        registerBtn = findViewById(R.id.registerBtn);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityManager.getInstance().remove(this);
    }
}
package com.demo.jizhangapp;

import android.app.Application;
import android.content.Context;

import com.demo.jizhangapp.bean.User;
import com.demo.jizhangapp.db.Database;
import com.demo.jizhangapp.util.PreferenceUtil;


public class App extends Application {
    static App context;
    public static User user;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        Database.init(this);
        String logger = PreferenceUtil.getInstance().get("logger", "");
        if (!logger.isEmpty()) {
            user = Database.getDao().queryUserByPhone(logger);
        }
    }


    public static Context getContext() {
        return context;
    }

    public static boolean isLogin() {
        return user != null;
    }

    public static void login(User user_) {
        user = user_;
        PreferenceUtil.getInstance().save("logger", user_.phone);
    }

    public static void logout() {
        user = null;
        PreferenceUtil.getInstance().remove("logger");
    }
}

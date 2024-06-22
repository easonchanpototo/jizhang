package com.demo.jizhangapp.db;

import android.app.Application;
import androidx.room.Room;

public class Database {
    private static MyDB db;

    public static void init(Application application) {
        if (db == null) {
            db = Room.databaseBuilder(application, MyDB.class, "b")
                    .allowMainThreadQueries()
                    .addMigrations(MyDB.MIGRATION_1_2)  // 添加迁移
                    .build();
        }
    }

    public static DatabaseDao getDao() {
        return db.getDao();
    }
}
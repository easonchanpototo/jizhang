package com.demo.jizhangapp.db;

import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.demo.jizhangapp.bean.Bill;
import com.demo.jizhangapp.bean.FinanceRecord;
import com.demo.jizhangapp.bean.User;


@androidx.room.Database(entities = {User.class, Bill.class, FinanceRecord.class}, version = 2, exportSchema = false)
public abstract class MyDB extends RoomDatabase {
    public abstract DatabaseDao getDao();

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // 向 User 表添加新列 age
            database.execSQL("ALTER TABLE User ADD COLUMN age INTEGER NOT NULL DEFAULT 0");
        }
    };
}

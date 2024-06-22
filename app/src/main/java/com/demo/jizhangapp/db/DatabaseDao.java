package com.demo.jizhangapp.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.demo.jizhangapp.bean.Bill;
import com.demo.jizhangapp.bean.FinanceRecord;
import com.demo.jizhangapp.bean.User;

import java.util.List;


@Dao
public interface DatabaseDao {

    @Insert
    void register(User user);

    @Update
    void updateUserInfo(User user);

    @Query("select * from user where phone=:phone")
    User queryUserByPhone(String phone);

    @Query("select * from user where id=:id")
    User queryUserById(int id);

    @Insert
    void addBill(Bill bill);

    @Query("select * from bill where userid = :id and description like :key and date>= :start and  date<=:end")
    List<Bill> searchBill(int id, String key, String start, String end);

    @Query("select * from bill where userid = :id and date>= :start and  date<=:end and type = :type")
    List<Bill> getDiyDate(int id, String start, String end, int type);

    @Query("SELECT * FROM bill WHERE date >= date('now', 'weekday 0', '-6 days') AND date <= date('now', 'weekday 0') and userid = :userid and type = :type")
    List<Bill> getCurrentWeekDate(int type, int userid);

    @Query("SELECT * FROM bill WHERE strftime('%Y-%m', date) = strftime('%Y-%m', 'now') and userid = :userid and type=:type")
    List<Bill> getCurrentMonthDate(int type, int userid);


    @Query("SELECT * FROM bill WHERE strftime('%Y', date) = strftime('%Y', 'now') and userid = :userid and type=:type")
    List<Bill> getCurrentYearDate(int type, int userid);

    @Query("SELECT * FROM bill WHERE strftime('%Y-%m', date) = :yyyyMM and type = :type")
    List<Bill> getYMMBill(String yyyyMM, int type);

    @Query("SELECT * FROM bill WHERE strftime('%Y', date) = :yyyy and type = :type")
    List<Bill> getYBill(String yyyy, int type);

    @Update
    void updateBill(Bill bill);

    @Delete
    void deleteBill(Bill remove);

    @Query("select * from financerecord where userid = :id and name = :name and startDate is not null and endDate is null")
    FinanceRecord getMyFinanceByFinance(int id, String name);

    @Insert
    void addFinance(FinanceRecord financeRecord);

    @Delete
    void delete(FinanceRecord myFinanceByFinance);

    @Update
    void update(FinanceRecord myFinanceByFinance);
}

package com.demo.jizhangapp;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.demo.jizhangapp.adapter.BindAdapter;
import com.demo.jizhangapp.base.BaseBindingActivity;
import com.demo.jizhangapp.bean.Bill;
import com.demo.jizhangapp.bean.Type;
import com.demo.jizhangapp.databinding.ActivityTypeBinding;
import com.demo.jizhangapp.databinding.ItemTypeBinding;
import com.demo.jizhangapp.db.Database;
import com.google.android.material.tabs.TabLayout;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TypeActivity extends BaseBindingActivity<ActivityTypeBinding> {
    public static List<Type> outList = new ArrayList<>();
    public static List<Type> inList = new ArrayList<>();

    static {
        outList.add(new Type(1, "交通", R.drawable.ic_out_type_1));
        outList.add(new Type(2, "医疗", R.drawable.ic_out_type_2));
        outList.add(new Type(3, "娱乐", R.drawable.ic_out_type_3));
        outList.add(new Type(4, "学习", R.drawable.ic_out_type_4));
        outList.add(new Type(5, "数码", R.drawable.ic_out_type_5));
        outList.add(new Type(6, "日常", R.drawable.ic_out_type_6));
        outList.add(new Type(7, "服装", R.drawable.ic_out_type_7));
        outList.add(new Type(8, "水果", R.drawable.ic_out_type_8));
        outList.add(new Type(9, "购物", R.drawable.ic_out_type_9));
        outList.add(new Type(10, "运动", R.drawable.ic_out_type_10));
        outList.add(new Type(11, "通信", R.drawable.ic_out_type_11));
        outList.add(new Type(12, "餐饮", R.drawable.ic_out_type_12));
        outList.add(new Type(13, "理财", R.drawable.ic_in_type_2));

        inList.add(new Type(1, "生活费", R.drawable.ic_in_type_1));
        inList.add(new Type(2, "理财", R.drawable.ic_in_type_2));
        inList.add(new Type(3, "奖学金", R.drawable.ic_in_type_3));
        inList.add(new Type(4, "兼职", R.drawable.ic_in_type_4));
        inList.add(new Type(5, "其他", R.drawable.ic_in_type_5));
    }

    private int current = -1;
    private BindAdapter<ItemTypeBinding, Type> adapter = new BindAdapter<ItemTypeBinding, Type>() {
        @Override
        public ItemTypeBinding createHolder(ViewGroup parent) {
            return ItemTypeBinding.inflate(getLayoutInflater(), parent, false);
        }

        @Override
        public void bind(ItemTypeBinding item, Type data, int position) {
            item.tvName.setText(data.name);
            item.ivIcon.setImageResource(data.image);
            if (current != -1 && current == position) {
                item.ivIcon.setBackgroundResource(R.drawable.shape_round_gray_50);
            } else {
                item.ivIcon.setBackgroundResource(R.drawable.shape_round_white_50);
            }
            item.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    current = position;
                    adapter.notifyDataSetChanged();
                }
            });
        }
    };
    private int type = 0;

    @Override
    protected void initListener() {
        viewBinder.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                adapter.getData().clear();
                current = -1;
                type = tab.getPosition();
                if (tab.getPosition() == 0) {
                    adapter.getData().addAll(outList);
                } else {
                    adapter.getData().addAll(inList);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewBinder.tvTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar instance = Calendar.getInstance();
                int year = instance.get(Calendar.YEAR);
                int month = instance.get(Calendar.MONTH);
                int day = instance.get(Calendar.DAY_OF_MONTH);
                new DatePickerDialog(TypeActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        instance.set(Calendar.YEAR, year);
                        instance.set(Calendar.MONTH, month);
                        instance.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        @SuppressLint("SimpleDateFormat") String date = new SimpleDateFormat("yyyy-MM-dd").format(instance.getTimeInMillis());
                        viewBinder.tvTime.setText(date);
                    }
                }, year, month, day).show();
            }
        });
        viewBinder.tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String price = viewBinder.etPrice.getText().toString();
                String description = viewBinder.etDescription.getText().toString();
                String date = viewBinder.tvTime.getText().toString();
                if (price.isEmpty()) return;
                if (description.isEmpty()) return;
                if (date.isEmpty()) return;
                if (current == -1) return;
                Bill bill = new Bill();
                bill.date = date;
                bill.description = description;
                bill.userid = App.user.id;
                bill.price = Double.parseDouble(price);
                bill.type = type;
                if (type == 0) {
                    bill.iconId = outList.get(current).id;
                    bill.typeName = outList.get(current).name;
                } else {
                    bill.iconId = inList.get(current).id;
                    bill.typeName = inList.get(current).name;
                }
                if (bill.id!=0){
                    Database.getDao().updateBill(bill);
                }else {
                    Database.getDao().addBill(bill);
                }

                toast("保存成功");
                finish();
            }
        });
    }

    Bill bill;

    @Override
    protected void initData() {
        bill = (Bill) getIntent().getSerializableExtra("bill");
        if (bill != null) {
            current = bill.iconId - 1;
            if (bill.type == 0) {
                adapter.getData().addAll(outList);
                adapter.notifyDataSetChanged();
            } else {
                adapter.getData().addAll(inList);
                adapter.notifyDataSetChanged();
            }
            viewBinder.etDescription.setText(bill.description);
            viewBinder.tvTime.setText(bill.date);
            viewBinder.etPrice.setText(bill.price + "");
        } else {
            adapter.getData().clear();
            current = -1;
            adapter.getData().addAll(outList);
            adapter.notifyDataSetChanged();
        }
        viewBinder.rvType.setAdapter(adapter);

    }
}
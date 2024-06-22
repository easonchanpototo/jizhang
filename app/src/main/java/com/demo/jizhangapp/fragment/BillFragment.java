package com.demo.jizhangapp.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.jizhangapp.App;
import com.demo.jizhangapp.TypeActivity;
import com.demo.jizhangapp.adapter.BindAdapter;
import com.demo.jizhangapp.base.BaseBindingFragment;
import com.demo.jizhangapp.bean.Bill;
import com.demo.jizhangapp.bean.Type;
import com.demo.jizhangapp.databinding.FragmentBillBinding;
import com.demo.jizhangapp.databinding.ItemRecordBinding;
import com.demo.jizhangapp.db.Database;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class BillFragment extends BaseBindingFragment<FragmentBillBinding> {
    private BindAdapter<ItemRecordBinding, Bill> adapter = new BindAdapter<ItemRecordBinding, Bill>() {
        @Override
        public ItemRecordBinding createHolder(ViewGroup parent) {
            return ItemRecordBinding.inflate(getLayoutInflater(), parent, false);
        }

        @Override
        public void bind(ItemRecordBinding item, Bill data, int position) {
            item.tvName.setText(data.description);
            item.tvPrice.setText(data.type == 0 ? ("-" + data.price) : ("+" + data.price));
            if (data.type == 0) {
                for (Type type : TypeActivity.outList) {
                    if (type.id == data.iconId) {
                        item.ivIcon.setImageResource(type.image);
                        break;
                    }
                }
            } else {
                for (Type type : TypeActivity.inList) {
                    if (type.id == data.iconId) {
                        item.ivIcon.setImageResource(type.image);
                        break;
                    }
                }
            }
            item.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(requireContext(), TypeActivity.class).putExtra("bill", data));
                }
            });
        }
    };

    @SuppressLint({"SetTextI18n", "SimpleDateFormat"})
    @Override
    protected void initData() {
        viewBinder.tvEmpty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(requireContext(),TypeActivity.class));
            }
        });
        viewBinder.rvRecord.setAdapter(adapter);
        Calendar instance = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") String date = new SimpleDateFormat("yyyy-MM-dd").format(instance.getTimeInMillis());
        viewBinder.tvStart.setText(date);
        instance.add(Calendar.DAY_OF_MONTH, 1);
        date = new SimpleDateFormat("yyyy-MM-dd").format(instance.getTimeInMillis());
        viewBinder.tvEnd.setText(date + " 止");
        viewBinder.tvStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar instance = Calendar.getInstance();
                int year = instance.get(Calendar.YEAR);
                int month = instance.get(Calendar.MONTH);
                int day = instance.get(Calendar.DAY_OF_MONTH);
                new DatePickerDialog(requireContext(), new DatePickerDialog.OnDateSetListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        instance.set(Calendar.YEAR, year);
                        instance.set(Calendar.MONTH, month);
                        instance.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        @SuppressLint("SimpleDateFormat") String date = new SimpleDateFormat("yyyy-MM-dd")
                                .format(instance.getTimeInMillis());
                        viewBinder.tvStart.setText(date);
                        refresh("%" + viewBinder.etSearch.getText().toString() + "%");
                    }
                }, year, month, day).show();
            }
        });
        viewBinder.tvEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar instance = Calendar.getInstance();
                int year = instance.get(Calendar.YEAR);
                int month = instance.get(Calendar.MONTH);
                int day = instance.get(Calendar.DAY_OF_MONTH);
                new DatePickerDialog(requireContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        instance.set(Calendar.YEAR, year);
                        instance.set(Calendar.MONTH, month);
                        instance.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        @SuppressLint("SimpleDateFormat") String date = new SimpleDateFormat("yyyy-MM-dd")
                                .format(instance.getTimeInMillis());
                        viewBinder.tvEnd.setText(date);
                        refresh("%" + viewBinder.etSearch.getText().toString() + "%");
                    }
                }, year, month, day).show();
            }
        });
        viewBinder.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                adapter.getData().clear();
                refresh("%" + s.toString() + "%");
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
                return makeMovementFlags(0, swipeFlags);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Bill remove = adapter.getData().remove(position);
                adapter.notifyItemRemoved(position);
                Database.getDao().deleteBill(remove);
                Snackbar.make(viewBinder.getRoot(), "删除成功", Snackbar.LENGTH_LONG)
                        .setAction("撤回", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                adapter.getData().add(position, remove);
                                Database.getDao().addBill(remove);
                                adapter.notifyItemInserted(position);
                            }
                        })
                        .show();
            }
        }).attachToRecyclerView(viewBinder.rvRecord);
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh("%" + "%");
    }

    private void refresh(String key) {
        String start = viewBinder.tvStart.getText().toString();
        String end = viewBinder.tvEnd.getText().toString();
        adapter.getData().clear();
        adapter.getData().addAll(Database.getDao().searchBill(App.user.id, key, start, end));
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void initListener() {

    }
}

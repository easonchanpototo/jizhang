package com.demo.jizhangapp.fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.jizhangapp.App;
import com.demo.jizhangapp.base.BaseBindingFragment;
import com.demo.jizhangapp.bean.Bill;
import com.demo.jizhangapp.databinding.FragmentChartBinding;
import com.demo.jizhangapp.db.Database;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class ChartFragment extends BaseBindingFragment<FragmentChartBinding> {
    @Override
    protected void initData() {

    }

    //理财
    @Override
    protected void initListener() {
        viewBinder.tl1.setTabData(new String[]{"本周", "本月", "本年", "自定义"});
        viewBinder.tl1.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                if (position == 0) {
                    initChart(Database.getDao().getCurrentWeekDate(0, App.user.id), viewBinder.pcOut);
                    initChart(Database.getDao().getCurrentWeekDate(1, App.user.id), viewBinder.pcIn);
                    viewBinder.llDiy.setVisibility(View.GONE);
                } else if (position == 1) {
                    initChart(Database.getDao().getCurrentMonthDate(0, App.user.id), viewBinder.pcOut);
                    initChart(Database.getDao().getCurrentMonthDate(1, App.user.id), viewBinder.pcIn);
                    viewBinder.llDiy.setVisibility(View.GONE);
                } else if (position == 2) {
                    initChart(Database.getDao().getCurrentYearDate(0, App.user.id), viewBinder.pcOut);
                    initChart(Database.getDao().getCurrentYearDate(1, App.user.id), viewBinder.pcIn);
                    viewBinder.llDiy.setVisibility(View.GONE);
                } else {
                    viewBinder.llDiy.setVisibility(View.VISIBLE);
                    initChart(Database.getDao().getDiyDate(App.user.id, viewBinder.tvStart.getText().toString().replace(" 起", ""), viewBinder.tvEnd.getText().toString().replace(" 止", ""), 0), viewBinder.pcOut);
                    initChart(Database.getDao().getDiyDate(App.user.id, viewBinder.tvStart.getText().toString().replace(" 起", ""), viewBinder.tvEnd.getText().toString().replace(" 止", ""), 1), viewBinder.pcIn);
                }
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
        initChart(Database.getDao().getCurrentWeekDate(0, App.user.id), viewBinder.pcOut);
        initChart(Database.getDao().getCurrentWeekDate(1, App.user.id), viewBinder.pcIn);
        initDiy();
    }

    @SuppressLint({"SimpleDateFormat", "SetTextI18n"})
    private void initDiy() {
        Calendar instance = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") String date = new SimpleDateFormat("yyyy-MM-dd").format(instance.getTimeInMillis());
        viewBinder.tvStart.setText(date );
        instance.add(Calendar.DAY_OF_MONTH, 1);
        date = new SimpleDateFormat("yyyy-MM-dd").format(instance.getTimeInMillis());
        viewBinder.tvEnd.setText(date );
        viewBinder.tvStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog((TextView) v, " 起");
            }
        });
        viewBinder.tvEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog((TextView) v, " 止");
            }
        });
    }

    private void showDatePickerDialog(TextView v, String suffix) {
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
                @SuppressLint("SimpleDateFormat") String date = new SimpleDateFormat("yyyy-MM-dd").format(instance.getTimeInMillis());
                v.setText(date);
                initChart(Database.getDao().getDiyDate(App.user.id, viewBinder.tvStart.getText().toString(), viewBinder.tvEnd.getText().toString(), 0), viewBinder.pcOut);
                initChart(Database.getDao().getDiyDate(App.user.id, viewBinder.tvStart.getText().toString(), viewBinder.tvEnd.getText().toString(), 1), viewBinder.pcIn);
            }
        }, year, month, day).show();
    }

    private void initChart(List<Bill> billList, PieChart pieChart) {
        if (billList.isEmpty()) {
            pieChart.setNoDataText("这里似乎什么都没有");
            return;
        }
        Map<String, Double> billMap = new HashMap<>();
        double total = 0;
        for (Bill item : billList) {
            total += item.price;
            Double price = billMap.get(item.typeName);
            if (price == null) {
                price = 0d;
                billMap.put(item.typeName, price);
            }
            price += item.price;
            billMap.put(item.typeName, price);
        }
        if (billList.get(0).type == 0) {
            viewBinder.tvOut.setText(String.format(Locale.getDefault(), "共支出 %d笔，共 $%.2f元", billList.size(), total));
        } else {
            viewBinder.tvIn.setText(String.format(Locale.getDefault(), "共收入%d，共 $%.2f笔", billList.size(), total));
        }
        List<PieEntry> pieEntries = new ArrayList<>();
        Set<Map.Entry<String, Double>> entries = billMap.entrySet();
        for (Map.Entry<String, Double> entry : entries) {
            String key = entry.getKey();
            Double value = entry.getValue();
            pieEntries.add(new PieEntry(value.floatValue(), key));
        }

        PieDataSet dataSet = new PieDataSet(pieEntries, "类型");
        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        dataSet.setValueTextColor(Color.BLACK);
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter(pieChart));
        data.setValueTextSize(12f);
        data.setHighlightEnabled(true);
        pieChart.setUsePercentValues(true);
        pieChart.setData(data);
        pieChart.highlightValues(null);

        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                int y = (int) e.getY();
                ArrayList<String> strings = new ArrayList<>(billMap.keySet());
                if (y > strings.size() - 1 || y < 0) {
                    return;
                }
                String s = strings.get(y);

            }

            @Override
            public void onNothingSelected() {
                // Do nothing
            }
        });

        pieChart.invalidate();
    }
}

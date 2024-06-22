package com.demo.jizhangapp;


import android.annotation.SuppressLint;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.demo.jizhangapp.base.BaseBindingActivity;
import com.demo.jizhangapp.databinding.ActivityMainBinding;
import com.demo.jizhangapp.fragment.BillFragment;
import com.demo.jizhangapp.fragment.ChartFragment;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseBindingActivity<ActivityMainBinding> {

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {
        if (!App.isLogin()) {
            startActivity(LoginActivity.class);
            finish();
            return;
        }
        fragments.add(new BillFragment());
        fragments.add(new ChartFragment());

        changePage(fragments.get(0));
        viewBinder.titleTv.setText("账本");
        viewBinder.bnv.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.bill:
                        changePage(fragments.get(0));
                        viewBinder.titleTv.setText("账本");
                        break;
                    case R.id.chart:
                        changePage(fragments.get(1));
                        viewBinder.titleTv.setText("图表");
                        break;
                }
                return true;
            }
        });
    }

    private List<Fragment> fragments = new ArrayList<>();

    private void changePage(Fragment fragment) {

        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        for (Fragment item : fragments) {
            fragmentTransaction.hide(item);
        }
        if (fragment.isAdded()) {
            fragmentTransaction.show(fragment);
        } else {
            fragmentTransaction.add(R.id.fcv, fragment).show(fragment);
        }
        fragmentTransaction.commitAllowingStateLoss();
    }
}
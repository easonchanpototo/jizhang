package com.demo.jizhangapp.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewbinding.ViewBinding;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class BaseBindingFragment<T extends ViewBinding> extends Fragment {
    protected T viewBinder;

    protected void showLoading() {
        dialog.show();
    }

    protected void dismissLoading() {
        dialog.dismiss();
    }

    private ProgressDialog dialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        dialog = new ProgressDialog(requireContext());
        dialog.setMessage("加载中...");
        Type type = getClass().getGenericSuperclass();
        Type[] actualTypeArguments = ((ParameterizedType) type).getActualTypeArguments();
        Class<T> tClass = (Class<T>) actualTypeArguments[0];
        try {
            Method method = tClass.getMethod("inflate", LayoutInflater.class, ViewGroup.class, boolean.class);
            viewBinder = (T) method.invoke(null, inflater, container, false);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        initListener();
        initData();
        return viewBinder.getRoot();
    }

    protected void toast(String msg) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();
    }

    protected abstract void initData();

    public interface IntentApply {
        void apply(Intent intent);
    }

    public <T extends Activity> void startActivity(Class<T> tClass, BaseBindingActivity.IntentApply intentApply) {
        Intent intent = new Intent(getActivity(), tClass);
        if (intentApply != null) {
            intentApply.apply(intent);
        }
        startActivity(intent);
    }

    public <T extends Activity> void startActivityForResult(Class<T> tClass, BaseBindingActivity.IntentApply intentApply, int requestCode) {
        Intent intent = new Intent(getActivity(), tClass);
        if (intentApply != null) {
            intentApply.apply(intent);
        }
        startActivityForResult(intent, requestCode);

    }

    public <T extends Activity> void startActivity(Class<T> tClass) {
        startActivity(tClass, null);
    }

    protected abstract void initListener();

    protected void display$Toast(String msg) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();
        dialog.dismiss();
    }
}

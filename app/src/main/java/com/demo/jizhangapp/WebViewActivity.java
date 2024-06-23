package com.demo.jizhangapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;

import com.demo.jizhangapp.base.BaseBindingActivity;
import com.demo.jizhangapp.databinding.ActivityWebViewBinding;

import java.io.Serializable;

public class WebViewActivity extends BaseBindingActivity<ActivityWebViewBinding> {

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {
        String url = (String) getIntent().getStringExtra("url");
        WebSettings webSettings = viewBinder.webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setGeolocationEnabled(true);
        viewBinder.webView.setWebViewClient(new WebViewClient());
        viewBinder.webView.setWebChromeClient(new WebChromeClient());
        webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        viewBinder.webView.loadUrl(url);
    }
}
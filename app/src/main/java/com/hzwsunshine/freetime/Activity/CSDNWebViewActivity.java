package com.hzwsunshine.freetime.Activity;

import android.os.Bundle;
import android.transition.Explode;
import android.view.KeyEvent;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.hzwsunshine.freetime.Application.Application;
import com.squareup.leakcanary.RefWatcher;

public class CSDNWebViewActivity extends BaseActivity {

    private WebView mWebView;

    @Override
    protected void onCreated(Bundle savedInstanceState) {
//        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
//        getWindow().setExitTransition(new Explode());
//        getWindow().setSharedElementEnterTransition(new Explode());
        //内存泄露检测
        RefWatcher refWatcher = Application.getRefWatcher(this);
        refWatcher.watch(this);
        setTitle("CSDN");
        mWebView = new WebView(this);
        setView(mWebView);
        String mHtmlUrl = getIntent().getStringExtra("link");
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        //去掉放大缩小
        settings.setBuiltInZoomControls(false);
        //把所有内容放大webview等宽的一列中
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        mWebView.loadUrl(mHtmlUrl);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mWebView.canGoBack()) {
                mWebView.goBack();//返回上一WebView界面
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}

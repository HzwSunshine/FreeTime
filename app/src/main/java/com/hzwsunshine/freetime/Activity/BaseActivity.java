package com.hzwsunshine.freetime.Activity;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.hzwsunshine.freetime.R;

import butterknife.ButterKnife;

/**
 * Created by 何志伟 on 2015/11/15.
 * 封装通用的Toolbar
 */
public abstract class BaseActivity extends AppCompatActivity {


    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        initToolbar();
        //用于子类初始化View
        onCreated(savedInstanceState);
    }

    /**
     * 初始化View
     */
    protected abstract void onCreated(Bundle savedInstanceState);

    /**
     * 设置视图资源
     */
    protected void setView(int resourceId) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View childView = inflater.inflate(resourceId, null);
        setView(childView);
    }

    /**
     * 新建一个视图，并设置
     *
     * @param childView 子视图
     */
    protected void setView(View childView) {
        FrameLayout contentView = (FrameLayout) findViewById(R.id.view_baseContentView);
        contentView.addView(childView, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        ButterKnife.inject(this, childView);
    }

    /**
     * 初始化Toolbar
     */
    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitleTextColor(Color.WHITE);
        Drawable drawable = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        mToolbar.setNavigationIcon(drawable);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(v -> finish());
    }

    public View getToolbar() {
        return mToolbar == null ? null : mToolbar;
    }

}
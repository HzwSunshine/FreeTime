package com.hzwsunshine.freetime.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Created by 何志伟 on 2015/11/15.
 */
public abstract class BaseFragment extends Fragment {
    private View rootView;//定义一个缓存View
    private LayoutInflater mInflate;
    //用户是否第一次可见
    private boolean isVisibleFirst = true;
    private boolean isHiddenFirst = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView != null) {
            // 缓存的rootView需要判断是否已经被加过parent，如果有parent需要从parent删除，
            // 要不然会发生这个rootView已经有parent的错误。
            // 加缓存view的目的是，ViewPager+Fragment，滑动ViewPager时会重新初始化Fragment，
            // 这样做防止Fragment被重复初始化
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (null != parent) {
                parent.removeView(rootView);
            }
        } else {
            //设置toolbar在fragment中可用
            setHasOptionsMenu(true);
            this.mInflate = inflater;
            //初始化View，执行数据逻辑
            onCreateView(container, savedInstanceState);
        }
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    /**
     * 为Fragment设置视图
     *
     * @param layoutId  视图ID
     * @param container ViewGroup
     */
    public View setView(int layoutId, ViewGroup container) {
        View view = mInflate.inflate(layoutId, container, false);
        return setView(view);
    }

    /**
     * 新建一个视图，并设置
     */
    public View setView(View view) {
        this.rootView = view;
        ButterKnife.inject(this, view);
        return view;
    }

    /**
     * 用于子类View初始化以及数据的操作
     */
    public abstract void onCreateView(ViewGroup container, Bundle savedInstanceState);

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isVisibleFirst) {
            onVisibleOnlyOnce();
            isVisibleFirst = false;
        }
        if (isVisibleToUser) {
            onVisible();
        } else {
            onInVisible();
        }
    }

    /**
     * 每次Fragment可见时都调用
     */
    public void onVisible() {
    }

    /**
     * 每次Fragment不可见时调用
     */
    public void onInVisible() {
    }

    /**
     * 只在用户第一次可见时调用一次，以后不再执行
     */
    public void onVisibleOnlyOnce() {
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden && isHiddenFirst) {
            onHiddenChangeOnlyOnce();
            isHiddenFirst = false;
        }
        if (hidden) {
            onHidden();
        } else {
            onShow();
        }
    }

    /**
     * 每次Fragment可见时都调用
     */
    public void onShow() {
    }

    /**
     * 每次Fragment不可见时调用
     */
    public void onHidden() {
    }

    /**
     * 只在用户第一次可见时调用一次，以后不再执行
     */
    public void onHiddenChangeOnlyOnce() {
    }
}
package com.hzwsunshine.freetime.Adapter;

import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 通用的ViewHolder
 * Created by 何志伟 on 2016/1/11.
 */
public class ViewHolder {
    private SparseArray<View> viewHolder;
    private View view;

    public static ViewHolder getViewHolder(View view) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        if (viewHolder == null) {
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }
        return viewHolder;
    }

    private ViewHolder(View view) {
        this.view = view;
        viewHolder = new SparseArray<>();
        view.setTag(viewHolder);
    }

    public <T extends View> T getView(int id) {
        View childView = viewHolder.get(id);
        if (childView == null) {
            childView = view.findViewById(id);
            viewHolder.put(id, childView);
        }
        return (T) childView;
    }

    //返回ItemView
    public View getConvertView() {
        return view;
    }

    //封装返回常用的三个控件
    public TextView getTextView(int id) {
        return getView(id);
    }

    public Button getButton(int id) {
        return getView(id);
    }

    public ImageView getImageView(int id) {
        return getView(id);
    }

    //封装设置常用的两个控件
    public void setTextView(int id, CharSequence text) {
        getTextView(id).setText(text);
    }

    public void setButton(int id, CharSequence title) {
        getButton(id).setText(title);
    }

}

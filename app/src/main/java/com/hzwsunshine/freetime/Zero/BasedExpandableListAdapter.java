package com.hzwsunshine.freetime.Zero;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseExpandableListAdapter;

import java.io.Serializable;
import java.util.List;

public abstract class BasedExpandableListAdapter extends BaseExpandableListAdapter {

    private List<?> mList;
    private Context mContext;
    private LayoutInflater mLayoutInflater;

    public BasedExpandableListAdapter(List<?> list, Context mContext) {
        super();
        this.mList = list;
        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
    }


    //********************************父类必须实现的方法********************//
    @Override
    public int getGroupCount() {
        return mList != null ? mList.size() : 0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mList.get(groupPosition);
    }


    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }


    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
            /* 很重要：实现ChildView点击事件，必须返回true */
        return true;
    }

    @SuppressWarnings("unchecked")
    protected <Z extends View> Z get(View convertView, int id) {
        SparseArray<View> viewHolder = (SparseArray<View>) convertView.getTag();
        if (viewHolder == null) {
            viewHolder = new SparseArray<View>();
            convertView.setTag(viewHolder);
        }
        View childView = viewHolder.get(id);
        if (childView == null) {
            childView = convertView.findViewById(id);
            viewHolder.put(id, childView);
        }
        return (Z) childView;
    }

    protected View inflate(int pResId) {
        return mLayoutInflater.inflate(pResId, null);
    }

    public List<?> getList() {
        return mList;
    }

    public void setList(List<?> mList) {
        this.mList = mList;
    }

}

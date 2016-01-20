package com.hzwsunshine.freetime.Zero;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ExpandableListView;
import android.widget.ImageView;

import com.hzwsunshine.freetime.Activity.BaseActivity;
import com.hzwsunshine.freetime.Adapter.BaseRVAdapter;
import com.hzwsunshine.freetime.Adapter.BasedExpandableListAdapter;
import com.hzwsunshine.freetime.Adapter.ViewHolder;
import com.hzwsunshine.freetime.R;
import com.hzwsunshine.freetime.Utils.ImageLoaderUtils;
import com.hzwsunshine.freetime.Utils.ViewUtils;
import com.hzwsunshine.freetime.Views.RVGridDivider;
import com.hzwsunshine.freetime.Views.RVLinearDivider;
import com.hzwsunshine.freetime.Views.RecyclerViewDivider;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

public class Test2Activity extends BaseActivity {
    @InjectView(R.id.expandableListView)
    ExpandableListView expandableListView;

    private ExpandListViewAdapter mAdapter;

    @Override
    protected void onCreated(Bundle savedInstanceState) {
        setView(R.layout.activity_test2);
        setTitle(getString(R.string.menu_test1));

        initData();
    }

    public void initData() {
        List<List<String>> list = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            List<String> pList = null;
            for (int j = 0; j < 5; j++) {
                pList = new ArrayList<>();
                pList.add("父数据：" + j);
            }
            list.add(pList);
        }
        mAdapter = new ExpandListViewAdapter(list, this);
        expandableListView.setAdapter(mAdapter);
    }

    class ExpandListViewAdapter extends BasedExpandableListAdapter {

        public ExpandListViewAdapter(List<?> list, Context mContext) {
            super(list, mContext);
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return 0;
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return null;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            return null;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            return null;
        }
    }


}

package com.hzwsunshine.freetime.Fragment;


import android.app.ActivityOptions;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Explode;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ProgressBar;

import com.hzwsunshine.freetime.Activity.CSDNWebViewActivity;
import com.hzwsunshine.freetime.Adapter.RecycleViewAdapter_CSDN;
import com.hzwsunshine.freetime.Bean.CSDNBean;
import com.hzwsunshine.freetime.R;
import com.hzwsunshine.freetime.Utils.AnimUtils;
import com.hzwsunshine.freetime.Utils.DBUtils;
import com.hzwsunshine.freetime.Utils.HtmlUtils;
import com.hzwsunshine.freetime.Utils.HttpUtils;
import com.hzwsunshine.freetime.Utils.ResponseUtils;
import com.hzwsunshine.freetime.Utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

import FreeTime.greenDao.CSDNCache;
import butterknife.InjectView;

public class CSDNItemFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    @InjectView(R.id.beauty_recycleView)
    RecyclerView recycleView;
    @InjectView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    @InjectView(R.id.circularProgressView)
    ProgressBar progressView;

    private RecycleViewAdapter_CSDN mAdapter;
    private String url;
    List<CSDNBean> mList;
    List<CSDNBean> cacheList = new ArrayList<>();
    private boolean isLoading = false;
    //对应的page
    private static int page0 = 1;
    private static int page1 = 1;
    private static int page2 = 1;
    private static int page3 = 1;
    private static int page4 = 1;

    @Override
    public void onCreateView(ViewGroup container, Bundle savedInstanceState) {
        setView(R.layout.fragment_csdn_item, null);
        initView();
        initData();
    }

    public void initView() {
        //初始化刷新控件
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recycleView.setLayoutManager(manager);
        recycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int num = manager.findLastVisibleItemPosition();
                //只有滑动到了倒数第三个item，且没有数据在加载时,才去访问接口
                //cacheList.size()-1   是滑动到了底部 , dy>0表示在向下滑动
                if ((num == cacheList.size() - 2 || num == cacheList.size() - 1)
                        && dy > 0 && !isLoading) {
                    if (num == cacheList.size() - 1) {
                        AnimUtils.toLarge(progressView);
                    }
                    isLoading = true;
                    HttpUtils.get(url + getPage(url), new GetData(false));
                }
            }
        });
    }

    private void initData() {
        String[] key = new String[]{"news", "mobile", "sd", "programmer", "cloud"};
        for (String aKey : key) {
            String urlStr = getArguments().getString(aKey);
            if (urlStr != null) {
                url = urlStr;
                break;
            }
        }
        List<CSDNCache> list = DBUtils.queryCSDNCache(url);
        if (list.size() != 0) {
            //从缓存中获取数据，并适配数据
            for (int i = 0; i < list.size(); i++) {
                CSDNBean csdnBean = new CSDNBean();
                csdnBean.setTitle(list.get(i).getTitle());
                csdnBean.setLink(list.get(i).getLink());
                csdnBean.setDate(list.get(i).getDate());
                csdnBean.setImgLink(list.get(i).getImgLink());
                csdnBean.setContent(list.get(i).getContent());
                cacheList.add(i, csdnBean);
            }
            mAdapter = new RecycleViewAdapter_CSDN(cacheList, R.layout.item_csdn);
            recycleView.setAdapter(mAdapter);
            adapterListener(mAdapter);
            setPage(url);
        } else {//第一次进入
            HttpUtils.get(url + 1, new GetData(true));
        }
    }

    @Override
    public void onRefresh() {
        //请求新数据时永远只请求page=1的数据
        HttpUtils.get(url + 1, new GetData(true));
    }

    class GetData extends ResponseUtils {
        private boolean isRefresh;

        public GetData(boolean isRefresh) {
            this.isRefresh = isRefresh;
        }

        @Override
        public void success(String response) {
            //html解析后的list
            mList = HtmlUtils.html2list(response);
            if (isRefresh) {//正在刷新
                refreshLayout.setRefreshing(false);
                List<CSDNCache> pList = DBUtils.queryCSDNCache(url);
                if (pList.size() == 0) {//首次进入
                    for (int i = 0; i < mList.size(); i++) {
                        cacheList.add(mList.get(i));
                    }
                    mAdapter = new RecycleViewAdapter_CSDN(cacheList, R.layout.item_csdn);
                    recycleView.setAdapter(mAdapter);
                    adapterListener(mAdapter);
                    DBCache(mList);
                } else if (mList.get(0).getTitle().equals(pList.get(0).getTitle())) {
                    //简单做个判断，防止每次刷新都写入数据库
                    ViewUtils.showToast(getString(R.string.NoMoreNewData));
                } else {
                    cacheList.clear();
                    for (int i = 0; i < mList.size(); i++) {
                        cacheList.add(mList.get(i));
                    }
                    mAdapter.notifyDataSetChanged();
                    DBUtils.clearCSDNCache(url);
                    DBCache(mList);
                }
            } else {//滑动获取数据
                for (int i = 0; i < mList.size(); i++) {
                    cacheList.add(mList.get(i));
                }
                mAdapter.notifyDataSetChanged();
                AnimUtils.toSmall(progressView);
                setPage(url);
            }
            isLoading = false;
        }

        @Override
        public void failure() {
            super.failure();
            isLoading = false;
            if (refreshLayout.isRefreshing()) {
                refreshLayout.setRefreshing(false);
            }
            AnimUtils.toSmall(progressView);
        }
    }

    public void adapterListener(RecycleViewAdapter_CSDN pAdapter) {
        pAdapter.setOnItemClickListener((view, position) -> {
//            getActivity().getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
//            getActivity().getWindow().setExitTransition(new Explode());
//            getActivity().getWindow().setSharedElementEnterTransition(new Explode());
//            ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation(getActivity(),view,"");

            Intent intent = new Intent(getActivity(), CSDNWebViewActivity.class);
            intent.putExtra("link", cacheList.get(position).getLink());
            startActivity(intent);
        });
        pAdapter.setOnItemLongClickListener((view, position) -> {
            ClipboardManager cmb = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
            cmb.setText(cacheList.get(position).getLink());
            ViewUtils.showToast(getString(R.string.copyCsdnUrl));
        });
    }

    /****************************************************************************/
    private void DBCache(List<CSDNBean> list) {
        //其实也可以将list转为Json存入数据库，但在这里不适用
        for (int i = 0; i < list.size(); i++) {
            DBUtils.addCSDNCache(list.get(i).getTitle(), list.get(i).getDate(),
                    list.get(i).getImgLink(), list.get(i).getLink(),
                    list.get(i).getContent(), url);
        }
        //缓存完成后，去掉日期重复的数据
//        DBUtils.DistinctCSDNByDate();
    }


    //获取或设置page
    private int getPage(String url) {
        if (url.contains("news")) {
            return page0;
        }
        if (url.contains("mobile")) {
            return page1;
        }
        if (url.contains("sd")) {
            return page2;
        }
        if (url.contains("programmer")) {
            return page3;
        }
        if (url.contains("cloud")) {
            return page4;
        } else {
            return -1;
        }
    }

    private void setPage(String url) {
        if (url.contains("news")) {
            page0++;
        }
        if (url.contains("mobile")) {
            page1++;
        }
        if (url.contains("sd")) {
            page2++;
        }
        if (url.contains("programmer")) {
            page3++;
        }
        if (url.contains("cloud")) {
            page4++;
        }
    }

}

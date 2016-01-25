package com.hzwsunshine.freetime.Fragment;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.hzwsunshine.freetime.Activity.ShowImageActivity;
import com.hzwsunshine.freetime.Adapter.FuLiAdapter;
import com.hzwsunshine.freetime.Application.Application;
import com.hzwsunshine.freetime.Application.Interface;
import com.hzwsunshine.freetime.Bean.FuLiImageBean;
import com.hzwsunshine.freetime.R;
import com.hzwsunshine.freetime.Utils.DBUtils;
import com.hzwsunshine.freetime.Utils.HttpUtils;
import com.hzwsunshine.freetime.Utils.JsonUtils;
import com.hzwsunshine.freetime.Utils.NetWorkUtil;
import com.hzwsunshine.freetime.Utils.ResponseUtils;
import com.hzwsunshine.freetime.Utils.ViewUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import FreeTime.greenDao.ImageCache;
import butterknife.ButterKnife;
import butterknife.InjectView;

public class FuLiImageFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    @InjectView(R.id.beauty_recycleView)
    RecyclerView mRecyclerView;
    @InjectView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mRefreshLayout;

    private List<FuLiImageBean> cacheList = new ArrayList<>();
    private FuLiAdapter adapter = null;
    private static int page = 0;    //当前页
    private NetWorkUtil mReceiver;
    private FloatingActionButton mFab;
    private boolean isLoading = false;//保证只每次只访问一次网络
    private final static String urlType = "FuLiImage";

    @Override
    public void onCreateView(ViewGroup container, Bundle savedInstanceState) {
        View view = setView(R.layout.fragment_beauty_image, container);
        ButterKnife.inject(this, view);
        mFab = (FloatingActionButton) view.findViewById(R.id.fab);
        registerBroadcast();//注册网络监听广播
        initView();
        initRecyclerView();//初始化RecyclerView
        initData();
    }

    public void registerBroadcast() {
        mReceiver = new NetWorkUtil();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        getActivity().registerReceiver(mReceiver, filter);
    }

    public void initView() {
        //初始化刷新控件
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        mFab.setOnClickListener(view -> {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            mRecyclerView.scrollToPosition(0);
        });
    }

    public void initData() {
        List<ImageCache> mCacheData = DBUtils.queryImageCache(urlType);
        if (mCacheData.size() != 0) {
            //从缓存中获取数据，并适配数据
            cacheList = JsonUtils.jsonArray2list(mCacheData.get(0).getData(), FuLiImageBean.class);
            adapter = new FuLiAdapter(cacheList, R.layout.item_recyclerview_beautyimage);
            mRecyclerView.setAdapter(adapter);
            adapterListener(adapter);
            page++;
        } else {//第一次进入
            mRefreshLayout.setProgressViewOffset(false, 0, ViewUtils.dip2px(getActivity(),30));
            mRefreshLayout.setRefreshing(true);
            String url = Interface.fuliImageUrl + "&page=0&page_size=30&max_timestamp=-1&latest_viewed_ts=0";
            HttpUtils.get(url, new GetData(false));
        }
    }

    /**
     * 初始化RecyclerView
     */
    public void initRecyclerView() {
        final LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int num = manager.findLastVisibleItemPosition();
                //只有滑动到了倒数第三个item，且没有数据在加载时,才去访问接口
                //cacheList.size()-1   是滑动到了底部 , dy>0表示在向下滑动
                if ((num == cacheList.size() - 3 || num == cacheList.size() - 1)
                        && dy > 0 && !isLoading
                        && Application.connectedType == ConnectivityManager.TYPE_WIFI) {
                    String url = Interface.fuliImageUrl + "&page=" + page + "&page_size=15&max_timestamp="
                            + cacheList.get(cacheList.size() - 1).getUpdate_time() + "&latest_viewed_ts="
                            + cacheList.get(0).getUpdate_time();
                    adapter.addFooterView(getActivity(), R.layout.refresh_progress_bar, 0, 0, 0, 0);
                    isLoading = true;
                    HttpUtils.get(url, new GetData(true));
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        if (Application.connectedType == ConnectivityManager.TYPE_WIFI) {
            String url;
            if (cacheList.size() == 0) {
                //如果不判断，那么在第一次进入断网未加载出数据，再次刷新时会奔溃
                url = Interface.fuliImageUrl + "&page=0&page_size=30&max_timestamp=-1&latest_viewed_ts=0";
            } else {
                url = Interface.fuliImageUrl
                        + "&page=0&page_size=30&max_timestamp=-1&latest_viewed_ts="
                        + cacheList.get(0).getUpdate_time();
            }
            HttpUtils.get(url, new GetData(false));
        } else if (Application.connectedType == ConnectivityManager.TYPE_MOBILE) {
            ViewUtils.showToast(getString(R.string.not_allow_refresh));
            mRefreshLayout.setRefreshing(false);
        }else{
            ViewUtils.showToast(getString(R.string.not_connect_netWork));
            mRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
        //在MainActivity销毁时解绑广播
        getActivity().unregisterReceiver(mReceiver);
    }

    public void adapterListener(FuLiAdapter pAdapter) {
        pAdapter.setOnItemClickListener((view, position) -> {
            Intent intent = new Intent(getActivity(), ShowImageActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("imgUrl", cacheList.get(position).getWpic_large());
            bundle.putString("imgW", cacheList.get(position).getWpic_m_width());
            bundle.putString("imgH", cacheList.get(position).getWpic_m_height());
            intent.putExtras(bundle);
            startActivity(intent);
        });
    }

    class GetData extends ResponseUtils {
        private boolean isGetDataByScroll = false;

        public GetData(boolean isGetDataByScroll) {
            this.isGetDataByScroll = isGetDataByScroll;
        }

        @Override
        public void success(String response) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = (JSONArray) jsonObject.opt("items");
                List<FuLiImageBean> beautyImageList =
                        JsonUtils.jsonArray2list(jsonArray.toString(), FuLiImageBean.class);
                if (isGetDataByScroll && beautyImageList != null) {
                    //当前是滚动获取数据
                    for (int i = 0; i < beautyImageList.size(); i++) {
                        cacheList.add(beautyImageList.get(i));
                    }
                    adapter.notifyDataSetChanged();
                    adapter.removeFooterView();
                    page++;
                } else if (beautyImageList != null) {
                    //第一次进入或者正在刷新
                    List<ImageCache> mCacheData = DBUtils.queryImageCache(urlType);
                    if (mCacheData.size() == 0) {
                        mRefreshLayout.setRefreshing(false);
                        //第一次进入
                        for (int i = 0; i < beautyImageList.size(); i++) {
                            cacheList.add(beautyImageList.get(i));
                        }
                        adapter = new FuLiAdapter(cacheList, R.layout.item_recyclerview_beautyimage);
                        mRecyclerView.setAdapter(adapter);
                        adapterListener(adapter);
                        DBUtils.addImageCache(jsonArray.toString(), "", urlType);
                        page++;
                    } else {
                        //正在刷新
                        mRefreshLayout.setRefreshing(false);
                        int newDataTime = Integer.parseInt(beautyImageList.get(0).getUpdate_time());
                        int oldDataTime = Integer.parseInt(cacheList.get(0).getUpdate_time());
                        //如果获取到的第一条数据和缓存List中的第一条数据相同，则没有新数据
                        if (newDataTime == oldDataTime) {
                            ViewUtils.showToast(getString(R.string.NoMoreNewData));
                        } else {//900秒更新一条数据，哈哈，这都被我发现了
                            int newDataNum = (newDataTime - oldDataTime) / 900;
                            ViewUtils.showToast(getString(R.string.update_new_data_num).replace("***", newDataNum + ""));
                            if (newDataNum > 30) {
                                cacheList.clear();
                            } else {
                                beautyImageList = beautyImageList.subList(0, newDataNum);
                            }
                            for (int i = 0; i < beautyImageList.size(); i++) {
                                cacheList.add(i, beautyImageList.get(i));
                            }
                            adapter.notifyDataSetChanged();
                            DBUtils.clearImageCache(urlType);
                            DBUtils.addImageCache(jsonArray.toString(), "", urlType);
                        }
                    }
                }
                isLoading = false;
            } catch (JSONException e) {
                isLoading = false;
                mRefreshLayout.setRefreshing(false);
                adapter.removeFooterView();
                e.printStackTrace();
            }
        }

        @Override
        public void failure() {
            super.failure();
            isLoading = false;
            if (mRefreshLayout.isRefreshing()) {
                mRefreshLayout.setRefreshing(false);
            }
            if(adapter!=null){
                adapter.removeFooterView();
            }
        }
    }

}

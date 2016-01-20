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
import com.hzwsunshine.freetime.Adapter.BFAdapter;
import com.hzwsunshine.freetime.Application.Application;
import com.hzwsunshine.freetime.Application.Interface;
import com.hzwsunshine.freetime.Bean.BFImageBean;
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

public class BeautyFunnyFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    @InjectView(R.id.beauty_recycleView)
    RecyclerView mRecyclerView;
    @InjectView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mRefreshLayout;

    private List<BFImageBean> cacheList = new ArrayList<>();
    private BFAdapter adapter = null;
    private static int page = 1;    //当前页
    private NetWorkUtil mReceiver;
    private FloatingActionButton mFab;
    private String url;
    private String mUrlType;
    private boolean isLoading = false;//保证只每次只访问一次网络

    @Override
    public void onCreateView(ViewGroup container, Bundle savedInstanceState) {
        View view = setView(R.layout.fragment_beauty_image, container);
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
        mFab.setOnClickListener(view -> mRecyclerView.scrollToPosition(0));
    }

    public void initData() {
        mUrlType = getArguments().getString("tag");
        if (mUrlType == null) {//为了保证程序的健壮性
            mUrlType = "FunnyImage";
        }
        if (mUrlType.equals("FunnyImage")) {
            url = Interface.funnyImageUrl;
        }
        if (mUrlType.equals("BeautyImage")) {
            url = Interface.beautyImageUrl;
        }
        List<ImageCache> mCacheData = DBUtils.queryImageCache(mUrlType);
        if (mCacheData.size() != 0) {
            //从缓存中获取数据，并适配数据
            cacheList = JsonUtils.jsonArray2list(mCacheData.get(0).getData(), BFImageBean.class);
            adapter = new BFAdapter(cacheList, R.layout.item_recyclerview_beautyimage);
            mRecyclerView.setAdapter(adapter);
            adapterListener(adapter);
            page++;
        } else {//第一次进入
            mRefreshLayout.setProgressViewOffset(false, 0, ViewUtils.dip2px(getActivity(), 30));
            mRefreshLayout.setRefreshing(true);
            HttpUtils.get(url + 1, new GetData(false));
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
                        && dy > 0 && !isLoading//没有数据加载时
                        && Application.connectedType == ConnectivityManager.TYPE_WIFI) {
                    adapter.addFooterView(getActivity(), R.layout.refresh_progress_bar, 0, 0, 0, 0);
                    isLoading = true;
                    HttpUtils.get(url + page, new GetData(true));
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        //请求新数据时永远只请求page=1的数据
        if (Application.connectedType == ConnectivityManager.TYPE_WIFI) {
            HttpUtils.get(url + 1, new GetData(false));
        } else if (Application.connectedType == ConnectivityManager.TYPE_MOBILE) {
            ViewUtils.showToast(getString(R.string.not_allow_refresh));
            mRefreshLayout.setRefreshing(false);
        } else {
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

    public void adapterListener(BFAdapter pAdapter) {
        pAdapter.setOnItemClickListener((view, position) -> {
            Intent intent = new Intent(getActivity(), ShowImageActivity.class);
            intent.putExtra("imgUrl", cacheList.get(position).getPics().get(0));
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
                JSONArray jsonArray = (JSONArray) jsonObject.opt("comments");
                String totalComments = jsonObject.optString("total_comments");
                List<BFImageBean> BFImageList = JsonUtils.jsonArray2list(jsonArray.toString(), BFImageBean.class);

                List<ImageCache> mCacheData = DBUtils.queryImageCache(mUrlType);
                if (mCacheData.size() != 0 && !isGetDataByScroll && BFImageList != null) {
                    mRefreshLayout.setRefreshing(false);
                    //如果当前正在刷新，则计算刷新了多少条数据，并只将新数据加入到数据集合中
                    int dataNum;
                    dataNum = Integer.parseInt(totalComments) - Integer.parseInt(mCacheData.get(0).getTotalDataNum());
                    if (dataNum == 0) {
                        ViewUtils.showToast(getResources().getString(R.string.NoMoreNewData));
                    } else {
                        ViewUtils.showToast(getString(R.string.update_new_data_num).replace("***", dataNum + ""));
                        if (dataNum <= 25) {
                            //更新数据小于25条时才去截取list
                            BFImageList = BFImageList.subList(0, dataNum);
                        } else {
                            //更新数据大于25条时清空缓存list，重新加载数据
                            cacheList.clear();
                        }
                        //将新数据从头部插入
                        for (int i = 0; i < BFImageList.size(); i++) {
                            cacheList.add(i, BFImageList.get(i));
                        }
                        adapter.notifyDataSetChanged();
                        //每次刷新都清空以前的缓存，重新缓存数据
                        DBUtils.clearImageCache(mUrlType);
                        DBUtils.addImageCache(jsonArray.toString(), totalComments, mUrlType);
                    }
                } else if (BFImageList != null) {
                    //第一次进入或者是通过刷新获取数据
                    for (int i = 0; i < BFImageList.size(); i++) {
                        cacheList.add(BFImageList.get(i));
                    }
                    if (isGetDataByScroll) {
                        //若果是通过滑动获取数据
                        adapter.notifyDataSetChanged();
                        adapter.removeFooterView();
                    } else {//第一次进入
                        mRefreshLayout.setRefreshing(false);
                        adapter = new BFAdapter(cacheList, R.layout.item_recyclerview_beautyimage);
                        mRecyclerView.setAdapter(adapter);
                        adapterListener(adapter);
                        DBUtils.addImageCache(jsonArray.toString(), totalComments, mUrlType);
                    }
                    page++;
                }
                //表示接口访问完毕，且数据解析适配完毕
                isLoading = false;
            } catch (JSONException e) {
                isLoading = false;
                mRefreshLayout.setRefreshing(false);
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
            adapter.removeFooterView();
        }
    }
}
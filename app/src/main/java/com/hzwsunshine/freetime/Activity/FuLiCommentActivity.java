package com.hzwsunshine.freetime.Activity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hzwsunshine.freetime.Adapter.CommentAdapter;
import com.hzwsunshine.freetime.Application.Interface;
import com.hzwsunshine.freetime.Bean.CommentBean;
import com.hzwsunshine.freetime.R;
import com.hzwsunshine.freetime.Utils.CommonUtils;
import com.hzwsunshine.freetime.Utils.HttpUtils;
import com.hzwsunshine.freetime.Utils.ImageLoaderUtils;
import com.hzwsunshine.freetime.Utils.JsonUtils;
import com.hzwsunshine.freetime.Utils.ResponseUtils;
import com.hzwsunshine.freetime.Utils.ViewUtils;
import com.hzwsunshine.freetime.Views.CircleImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

public class FuLiCommentActivity extends BaseActivity {

    @InjectView(R.id.rv_comment_recyclerView)
    RecyclerView itemRecyclerView;
    @InjectView(R.id.pb_comment_progressBar)
    ProgressBar progressBar;

    private List<CommentBean> cacheList = new ArrayList<>();
    private CommentAdapter mAdapter;
    private List<CommentBean> mHotList;
    private boolean isLoading = false;
    private String mFid;
    private static int page = 1;

    @Override
    protected void onCreated(Bundle savedInstanceState) {
        setView(R.layout.activity_fu_li_comment);
        setTitle(getString(R.string.fuli_comments));
        initView();
        initData();
    }

    private void initView() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        itemRecyclerView.setLayoutManager(manager);
        itemRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int num = manager.findLastVisibleItemPosition();
                if ((num == cacheList.size() - 3 || num == cacheList.size() - 1) && dy > 0 && !isLoading) {
                    isLoading = true;
                    String maxCid = cacheList.get(cacheList.size() - 1).getCid();
                    String url = Interface.fuli_comments_moreUrl + "&max_cid=" + maxCid + "&fid=" + mFid + "&page=" + page;
                    HttpUtils.get(url, new GetData());
                }
            }
        });
    }

    private void initData() {
        mFid = getIntent().getStringExtra("fid");
        HttpUtils.get(Interface.fuli_comments_firstUrl + "&fid=" + mFid, new GetData());
    }

    class GetData extends ResponseUtils {
        @Override
        public void success(String response) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArrayItems = (JSONArray) jsonObject.opt("items");
                JSONArray jsonArrayHot = (JSONArray) jsonObject.opt("hot");
                List<CommentBean> commentList = JsonUtils.jsonArray2list(jsonArrayItems.toString(), CommentBean.class);
                if (jsonArrayHot != null) {
                    mHotList = JsonUtils.jsonArray2list(jsonArrayHot.toString(), CommentBean.class);
                }
                for (int i = 0; i < (commentList != null ? commentList.size() : 0); i++) {
                    cacheList.add(commentList.get(i));
                }
                if ((commentList != null ? commentList.size() : 0) == 0) {
                    return;
                }
                if (mAdapter == null) {
                    mAdapter = new CommentAdapter(cacheList, R.layout.item_comments_fuli);
                    itemRecyclerView.setAdapter(mAdapter);
                    mAdapter.addHeaderView(createHotComment());
                    mAdapter.setOnItemClickListener((view, position) -> createPopupWindow(view));
                } else {
                    mAdapter.notifyDataSetChanged();
                }
                isLoading = false;
                page++;
            } catch (JSONException e) {
                e.printStackTrace();
                isLoading = false;
            }
        }

        @Override
        public void failure() {
            super.failure();
            isLoading = false;
        }
    }

    private View createHotComment() {
        LinearLayout hotContent = new LinearLayout(this);
        hotContent.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        hotContent.setLayoutParams(params);

        if (mHotList.size() != 0) {
            TextView title = new TextView(this);
            title.setTextColor(Color.DKGRAY);
            title.setHeight(ViewUtils.dip2px(this, 40));
            title.setGravity(Gravity.CENTER_VERTICAL);
            title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
            title.setText(getString(R.string.fuli_hot_comment));
            hotContent.addView(title, params);
        }

        for (int i = 0; i < mHotList.size(); i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.item_comments_fuli, hotContent, false);
            view.setOnClickListener(v -> createPopupWindow(view));
            CircleImageView userIcon = (CircleImageView) view.findViewById(R.id.img_comment_userIcon);
            TextView userName = (TextView) view.findViewById(R.id.tv_comment_userName);
            TextView time = (TextView) view.findViewById(R.id.tv_comment_time);
            TextView likeNum = (TextView) view.findViewById(R.id.tv_comment_likeNum);
            TextView content = (TextView) view.findViewById(R.id.tv_comment_content);
            View line = view.findViewById(R.id.v_comment_line);
            if (i == mHotList.size() - 1) {
                line.setVisibility(View.GONE);
            }
            //适配热门评论
            ImageLoaderUtils.displayImage(userIcon, mHotList.get(i).getAuthor_avatar(),
                    R.mipmap.ic_face_black_36dp, R.mipmap.ic_face_black_36dp);
            userName.setText(mHotList.get(i).getAuthor_name());
            String timeStr = CommonUtils.timestamp2Date(mHotList.get(i).getCreated_time());
            time.setText(CommonUtils.timeFormat(timeStr));
            likeNum.setText(mHotList.get(i).getLikes());
            String text = mHotList.get(i).getContent();
            SpannableStringBuilder builder = new SpannableStringBuilder(text);
            int start = text.indexOf("@");
            int end = text.indexOf(":");
            if (start != -1 && end != -1) {
                builder.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.themeColor_blue)),
                        start-3, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                builder.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.gravy)),
                        end + 1, text.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                content.setText(builder);
            } else {
                content.setText(text);
            }
            //将热门评论的Item加入View
            hotContent.addView(view, params);
        }
        //添加最新评论
        TextView item = new TextView(this);
        item.setTextColor(Color.DKGRAY);
        item.setHeight(ViewUtils.dip2px(this, 40));
        item.setGravity(Gravity.CENTER_VERTICAL);
        item.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
        item.setText(getString(R.string.fuli_new_comment));
        hotContent.addView(item, params);

        progressBar.setVisibility(View.GONE);
        return hotContent;
    }

    private void createPopupWindow(View parent) {
        View popView = LayoutInflater.from(this).inflate(R.layout.popup_window, null, false);
        PopupWindow popupWindow = new PopupWindow(popView, ViewUtils.dip2px(this, 120),
                LinearLayoutCompat.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0));
        Button reply = (Button) popView.findViewById(R.id.btn_reply);
        reply.setOnClickListener(v -> popupWindow.dismiss());
        popupWindow.showAsDropDown(parent, ViewUtils.dip2px(this, 50),
                -parent.getHeight() + ViewUtils.dip2px(this, 40), Gravity.CENTER);
    }


}

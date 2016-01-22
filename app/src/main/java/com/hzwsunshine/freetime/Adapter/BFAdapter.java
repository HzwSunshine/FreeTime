package com.hzwsunshine.freetime.Adapter;

import android.text.TextUtils;
import android.view.View;

import com.hzwsunshine.freetime.Bean.BFImageBean;
import com.hzwsunshine.freetime.R;
import com.hzwsunshine.freetime.Utils.CommonUtils;
import com.hzwsunshine.freetime.Utils.ImageLoaderUtils;

import java.util.List;

/**
 * Created by 何志伟 on 2016/1/12.
 */
public class BFAdapter extends BaseRVAdapter {
    private List<BFImageBean> mList;

    public BFAdapter(List<?> list, int itemLayoutId) {
        super(list, itemLayoutId);
        this.mList = (List<BFImageBean>) list;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String imgUrl = mList.get(position).getPics().get(0);
        ImageLoaderUtils.displayImage(holder.getImageView(R.id.img), imgUrl,
                R.drawable.fuli_image, R.drawable.fuli_image);
        holder.getImageView(R.id.img).setAdjustViewBounds(true);
        holder.setTextView(R.id.tv_time, CommonUtils.timeFormat(mList.get(position).getComment_date()));
        holder.setTextView(R.id.tv_like, mList.get(position).getVote_positive());//喜欢人数
        holder.setTextView(R.id.tv_dislike, mList.get(position).getVote_positive());//不喜欢人数
        holder.setTextView(R.id.tv_userName, mList.get(position).getComment_author());//用户名
        String str = mList.get(position).getText_content().replace("\r\n", "");
        if (TextUtils.isEmpty(str)) {
            holder.getTextView(R.id.tv_contentText).setVisibility(View.GONE);
        } else {
            holder.getTextView(R.id.tv_contentText).setVisibility(View.VISIBLE);
            holder.getTextView(R.id.tv_contentText).setText(str);
        }
    }
}


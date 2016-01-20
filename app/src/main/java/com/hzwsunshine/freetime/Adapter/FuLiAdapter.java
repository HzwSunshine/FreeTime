package com.hzwsunshine.freetime.Adapter;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;

import com.hzwsunshine.freetime.Activity.FuLiCommentActivity;
import com.hzwsunshine.freetime.Bean.FuLiImageBean;
import com.hzwsunshine.freetime.R;
import com.hzwsunshine.freetime.Utils.CommonUtils;
import com.hzwsunshine.freetime.Utils.ImageLoaderUtils;
import com.hzwsunshine.freetime.Utils.ViewUtils;

import java.util.List;

/**
 * Created by 何志伟 on 2016/1/12.
 */
public class FuLiAdapter extends BaseRVAdapter {
    private List<FuLiImageBean> mList;

    public FuLiAdapter(List<?> list, int itemLayoutId) {
        super(list, itemLayoutId);
        this.mList = (List<FuLiImageBean>) list;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String imgUrl = mList.get(position).getWpic_middle();
        int imageWidth = Integer.parseInt(mList.get(position).getWpic_m_width());
        int imageHeight = Integer.parseInt(mList.get(position).getWpic_m_height());
        int imageViewWidth = ViewUtils.getScreenWH()[0] -
                ViewUtils.dip2px(holder.getImageView(R.id.img).getContext(), 44);
        ViewGroup.LayoutParams layoutParams = holder.getImageView(R.id.img).getLayoutParams();
        layoutParams.height = imageHeight * imageViewWidth / imageWidth;
        holder.getImageView(R.id.img).setLayoutParams(layoutParams);
        ImageLoaderUtils.displayImage(holder.getImageView(R.id.img), imgUrl,
                R.mipmap.defaultimage, R.mipmap.defaultimage);
        //设置不用的控件属性
        holder.getTextView(R.id.tv_userName).setVisibility(View.GONE);
        holder.getTextView(R.id.tv_commentNum).setVisibility(View.VISIBLE);
        //设置时间
        String time = CommonUtils.timestamp2Date(mList.get(position).getUpdate_time());
        holder.setTextView(R.id.tv_time, CommonUtils.timeFormat(time));
        String[] isLike = mList.get(position).getLikes().split("\\.");
        holder.setTextView(R.id.tv_like, isLike[0]);//喜欢人数
        holder.setTextView(R.id.tv_dislike, isLike[1]);//不喜欢人数
        holder.setTextView(R.id.tv_contentText, mList.get(position).getWbody());//内容
        holder.setTextView(R.id.tv_commentNum, mList.get(position).getComments());//评论数
        holder.getTextView(R.id.tv_commentNum).setOnClickListener(v -> {
            Intent intent = new Intent(holder.getTextView(R.id.tv_commentNum).getContext(), FuLiCommentActivity.class);
            intent.putExtra("fid", mList.get(position).getWid());
            holder.getTextView(R.id.tv_commentNum).getContext().startActivity(intent);
        });
    }

}
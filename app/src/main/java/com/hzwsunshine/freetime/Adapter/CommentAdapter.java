package com.hzwsunshine.freetime.Adapter;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;

import com.hzwsunshine.freetime.Bean.CommentBean;
import com.hzwsunshine.freetime.R;
import com.hzwsunshine.freetime.Utils.CommonUtils;
import com.hzwsunshine.freetime.Utils.ImageLoaderUtils;

import java.util.List;

/**
 * Created by 何志伟 on 2016/1/13.
 */
public class CommentAdapter extends BaseRVAdapter {
    private List<CommentBean> mList;

    public CommentAdapter(List<?> list, int itemLayoutId) {
        super(list, itemLayoutId);
        this.mList = (List<CommentBean>) list;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setTextView(R.id.tv_comment_userName, mList.get(position).getAuthor_name());
        String time = CommonUtils.timestamp2Date(mList.get(position).getCreated_time());
        holder.setTextView(R.id.tv_comment_time, CommonUtils.timeFormat(time));
        holder.setTextView(R.id.tv_comment_likeNum, mList.get(position).getLikes());
        ImageLoaderUtils.displayImage(holder.getImageView(R.id.img_comment_userIcon),
                mList.get(position).getAuthor_avatar(), R.drawable.fuli_image, R.drawable.fuli_image);
        //设置评论
        String text = mList.get(position).getContent();
        SpannableStringBuilder builder = new SpannableStringBuilder(text);
        int start = text.indexOf("@");
        int end = text.indexOf(":");
        if (start != -1 && end != -1) {
            builder.setSpan(new ForegroundColorSpan(holder.getImageView(R.id.img_comment_userIcon)
                            .getContext().getResources().getColor(R.color.themeColor_blue)),
                    start - 3, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            builder.setSpan(new ForegroundColorSpan(holder.getImageView(R.id.img_comment_userIcon)
                            .getContext().getResources().getColor(R.color.gravy)),
                    end + 1, text.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            holder.setTextView(R.id.tv_comment_content, builder);
        } else {
            holder.setTextView(R.id.tv_comment_content, mList.get(position).getContent());
        }
    }
}

package com.hzwsunshine.freetime.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hzwsunshine.freetime.Bean.CSDNBean;
import com.hzwsunshine.freetime.R;
import com.hzwsunshine.freetime.Utils.ImageLoaderUtils;

import java.util.List;

/**
 * Created by 何志伟 on 2015/11/5.
 * 留下一个原生的Adapter，作为对比和参考
 */
public class RecycleViewAdapter_CSDN extends RecyclerView.Adapter<RecycleViewAdapter_CSDN.ViewHolder> {

    private List<CSDNBean> mList;
    private int mLayoutId;

    public RecycleViewAdapter_CSDN(List<CSDNBean> list, int layoutId) {
        this.mList = list;
        this.mLayoutId = layoutId;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(mLayoutId, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        String imgUrl = mList.get(position).getImgLink();
        if (imgUrl == null) {
            imgUrl = "www.023085023580358023.jpg";
        }
        ImageLoaderUtils.displayImage(holder.imgTitle, imgUrl, R.mipmap.defaultimage, R.mipmap.defaultimage);
        //设置时间
        holder.time.setText(mList.get(position).getDate());
        //标题
        holder.title.setText(mList.get(position).getTitle());
        //简介
        holder.details.setText(mList.get(position).getContent());
        //RecyclerView的item点击事件
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(v -> {
                int position1 = holder.getLayoutPosition();
                mOnItemClickListener.OnItemClickListener(holder.itemView, position1);
            });
        }
        if (mOnItemLongClickListener != null) {
            holder.itemView.setOnLongClickListener(v -> {
                int position1 = holder.getLayoutPosition();
                mOnItemLongClickListener.OnItemLongClickListener(holder.itemView, position1);
                return false;
            });
        }

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgTitle;
        private TextView title, details, time;

        public ViewHolder(View itemView) {
            super(itemView);
            imgTitle = (ImageView) itemView.findViewById(R.id.img_imageView);
            title = (TextView) itemView.findViewById(R.id.tv_csdn_title);
            details = (TextView) itemView.findViewById(R.id.tv_csdn_details);
            time = (TextView) itemView.findViewById(R.id.tv_csdn_time);
        }
    }

    /*-----------------------解决RecyclerView无Item的点击事件---------------------------------------*/

    /**
     * RecyclerView  Item点击事件的回调接口
     */
    public interface OnItemClickListener {
        void OnItemClickListener(View view, int position);
    }

    public interface OnItemLongClickListener {
        void OnItemLongClickListener(View view, int position);
    }

    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.mOnItemLongClickListener = onItemLongClickListener;
    }
    /*-----------------------解决RecyclerView无Item的点击事件---------------------------------------*/
}

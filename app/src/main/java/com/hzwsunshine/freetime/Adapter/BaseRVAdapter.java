package com.hzwsunshine.freetime.Adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * RecyclerView 通用适配器，适用于LinearLayout，GridLayout和StaggeredGridLayout的水平方式和垂直方式
 * Created by 何志伟 on 2016/1/11.
 */
public abstract class BaseRVAdapter extends RecyclerView.Adapter<BaseRVAdapter.RVHolder> {

    private List<?> mList;
    private int mItemLayoutId;

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_NORMAL = 1;
    private static final int TYPE_FOOTER = 2;
    private View mHeaderView;
    private View mFooterView;

    public BaseRVAdapter(List<?> list, int itemLayoutId) {
        this.mList = list;
        this.mItemLayoutId = itemLayoutId;
    }

    /**
     * 添加新建的HeaderView
     */
    public void addHeaderView(View headerView) {
        //这里只允许添加一个HeaderView，也可以添加多个，但嫌麻烦
        if (mHeaderView != null) {
            try {
                throw new Exception(" HeaderView already exist ");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }
        mHeaderView = headerView;
        notifyItemInserted(0);
    }

    /**
     * 添加xml布局文件的HeaderView
     */
    public void addHeaderView(Context context, int LayoutId,
                              int marginLeft, int marginTop, int marginRight, int marginBottom) {
        if (mHeaderView != null) {
            try {
                throw new Exception(" HeaderView already exist ");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }
        //这里由于LayoutInflater没有ViewGroup而引起HeaderView根布局layout属性失效（失效的原因是：
        // layout属性用于指定一个View在ViewGroup中的各个属性，如位置大小...，但当前ViewGroup为null
        // 所以失效）的问题，有多种解决方案
        //1. 可以在头布局文件的根布局再添加一个ViewGroup
        //2. 代码指定布局文件的layout属性，如大小和layout_margin,layout_gravity等等
        //3. 直接指定一个ViewGroup，水平低下，这个还不知道该指定哪个ViewGroup
        RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT);
        params.setMargins(marginLeft, marginTop, marginRight, marginBottom);
        View headerView = LayoutInflater.from(context).inflate(LayoutId, null);
        headerView.setLayoutParams(params);
        addHeaderView(headerView);
    }

    public void removeHeaderView() {
        if (mHeaderView != null) {
            mHeaderView = null;
            notifyDataSetChanged();
        }
    }

    public View getHeaderView() {
        return mHeaderView;
    }

    /**
     * 添加新建的FooterView
     */
    public void addFooterView(View footerView) {
        if (mFooterView != null) {
            try {
                throw new Exception(" FooterView already exist ");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }
        mFooterView = footerView;
        if (mHeaderView != null) {
            notifyItemInserted(mList.size() + 1);
        } else {
            notifyItemInserted(mList.size());
        }
    }

    /**
     * 添加xml布局文件的FooterView
     */
    public void addFooterView(Context context, int LayoutId,
                              int marginLeft, int marginTop, int marginRight, int marginBottom) {
        if (mFooterView != null) {
            try {
                throw new Exception(" FooterView already exist ");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }
        RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(marginLeft, marginTop, marginRight, marginBottom);
        View footerView = LayoutInflater.from(context).inflate(LayoutId, null);
        footerView.setLayoutParams(params);
        addFooterView(footerView);
    }

    public void removeFooterView() {
        if (mFooterView != null) {
            mFooterView = null;
            notifyDataSetChanged();
        }
    }

    public View getFooterView() {
        return mFooterView;
    }

    @Override
    public int getItemViewType(int position) {
        if (mHeaderView == null && mFooterView == null) {
            return TYPE_NORMAL;
        }
        if (position == 0) {
            return mHeaderView == null ? TYPE_NORMAL : TYPE_HEADER;
        }
        if (mHeaderView != null && position == mList.size() + 1) {
            return mFooterView == null ? TYPE_NORMAL : TYPE_FOOTER;
        }
        if (mHeaderView == null && position == mList.size()) {
            return mFooterView == null ? TYPE_NORMAL : TYPE_FOOTER;
        }
        return TYPE_NORMAL;
    }

    @Override
    public RVHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mHeaderView != null && viewType == TYPE_HEADER) {
            return new RVHolder(mHeaderView);
        }
        if (mFooterView != null && viewType == TYPE_FOOTER) {
            return new RVHolder(mFooterView);
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(mItemLayoutId, parent, false);
        return new RVHolder(view);
    }

    @Override
    public void onBindViewHolder(RVHolder holder, int position) {
        if (getItemViewType(position) == TYPE_HEADER || getItemViewType(position) == TYPE_FOOTER) {
            return;
        }
        onBindViewHolder(holder.getViewHolder(), getPosition(holder));
        //RecyclerView的item点击事件
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.OnItemClick(holder.itemView, getPosition(holder));
                }
            });
        }
        //RecyclerView的item长点击事件,不常用，先屏蔽
//        if (mOnItemLongClickListener != null) {
//            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//                    int position1 = holder.getLayoutPosition();
//                    mOnItemLongClickListener.OnItemLongClickListener(holder.itemView, position1);
//                    return false;
//                }
//            });
//        }
    }

    public abstract void onBindViewHolder(ViewHolder holder, int position);

    /**
     * GridLayout(GridView)的特殊处理
     */
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = (GridLayoutManager) manager;
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (getItemViewType(position) == TYPE_HEADER || getItemViewType(position) == TYPE_FOOTER) {
                        return gridManager.getSpanCount();
                    }
                    return 1;
                }
            });
        }
    }

    /**
     * StaggeredGridLayout(瀑布流)的特殊处理
     */
    @Override
    public void onViewAttachedToWindow(RVHolder holder) {
        super.onViewAttachedToWindow(holder);
        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        if (layoutParams != null && layoutParams instanceof StaggeredGridLayoutManager.LayoutParams) {
            if ((mHeaderView == null && holder.getLayoutPosition() == mList.size()) ||
                    (mHeaderView != null && holder.getLayoutPosition() == mList.size() + 1)) {
                StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) layoutParams;
                params.setFullSpan(true);
            }
        }
        if (mHeaderView != null && layoutParams != null &&
                layoutParams instanceof StaggeredGridLayoutManager.LayoutParams && holder.getLayoutPosition() == 0) {
            StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) layoutParams;
            params.setFullSpan(true);
        }
    }

    private int getPosition(RVHolder holder) {
        int position = holder.getLayoutPosition();
        return mHeaderView == null ? position : position - 1;
    }

    @Override
    public int getItemCount() {
        if (mHeaderView != null && mFooterView != null) {
            return mList.size() + 2;
        } else if (mHeaderView != null || mFooterView != null) {
            return mList.size() + 1;
        } else {
            return mList.size();
        }
    }

    class RVHolder extends RecyclerView.ViewHolder {
        private ViewHolder mViewHolder;

        public RVHolder(View itemView) {
            super(itemView);
            if (itemView == mHeaderView || itemView == mFooterView) {
                return;
            }
            mViewHolder = ViewHolder.getViewHolder(itemView);
        }

        public ViewHolder getViewHolder() {
            return mViewHolder;
        }
    }

    /*-----------------------解决RecyclerView无Item的点击事件-----------------------------*/
    public interface OnItemClickListener {
        void OnItemClick(View view, int position);
    }

    public interface OnItemLongClickListener {
        void OnItemLongClick(View view, int position);
    }

    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.mOnItemLongClickListener = onItemLongClickListener;
    }
    /*-----------------------解决RecyclerView无Item的点击事件----------------------------*/

}

//该适配器还存在的问题：
//  1.Item只能添加xml布局文件，不能添加代码创建的View
//  2.其他问题暂未发现
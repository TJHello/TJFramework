package com.tjbaobao.framework.ui;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.View;

import com.tjbaobao.framework.base.BaseItemDecoration;
import com.tjbaobao.framework.base.BaseRecyclerAdapter;

/**
 * Created by TJbaobao on 2018/1/6.
 */

public class BaseRecyclerView extends RecyclerView {

    private BaseRecyclerAdapter.OnItemClickListener mOnItemClickListener ;

    public BaseRecyclerView(Context context) {
        this(context,null);
    }

    public BaseRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public BaseRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void toListView()
    {
        this.setLayoutManager(new LinearLayoutManager(getContext()));
    }
    public void toListView(int orientation, boolean reverseLayout)
    {
        this.setLayoutManager(new LinearLayoutManager(getContext(),orientation,reverseLayout));
    }

    public void toGridView(int spanCount)
    {
        this.setLayoutManager(new GridLayoutManager(getContext(),spanCount));
    }
    public void toGridView(int spanCount,int orientation, boolean reverseLayout)
    {
        this.setLayoutManager(new GridLayoutManager(getContext(),spanCount,orientation,reverseLayout));
    }

    public void toStaggeredGridView(int spanCount)
    {
        toStaggeredGridView(spanCount,StaggeredGridLayoutManager.VERTICAL);
    }

    public void toStaggeredGridView(int spanCount,int orientation)
    {
        this.setLayoutManager(new StaggeredGridLayoutManager(spanCount,orientation));
    }

    public void addListViewItemDecoration()
    {
        this.addItemDecoration(BaseItemDecoration.getLineVerticalDecoration(BaseItemDecoration.DEF_SPACING));
    }

    public void addGridAverageCenterDecoration(int spacingVertical,int spacingHorizontal)
    {
        this.addItemDecoration(BaseItemDecoration.getGridCenterDecoration(spacingVertical,spacingHorizontal));
    }

    public abstract static class BaseViewHolder extends RecyclerView.ViewHolder
    {
        public BaseViewHolder(View itemView) {
            super(itemView);
            onInitView(itemView);
        }

        public abstract void onInitView(View itemView);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        if(adapter instanceof BaseRecyclerAdapter)
        {
            ((BaseRecyclerAdapter)adapter).setOnItemClickListener(mOnItemClickListener);
        }
    }

    public BaseRecyclerAdapter.OnItemClickListener getOnItemClickListener() {
        return mOnItemClickListener;
    }

    public void setOnItemClickListener(BaseRecyclerAdapter.OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
        Adapter adapter = getAdapter();
        if(adapter!=null)
        {
            if(adapter instanceof BaseRecyclerAdapter)
            {
                ((BaseRecyclerAdapter)adapter).setOnItemClickListener(mOnItemClickListener);
            }
        }
    }
}

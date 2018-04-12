package com.tjbaobao.framework.ui;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.View;

import com.tjbaobao.framework.base.BaseGridLayoutManager;
import com.tjbaobao.framework.base.BaseItemDecoration;
import com.tjbaobao.framework.base.BaseLinearLayoutManager;
import com.tjbaobao.framework.base.BaseRecyclerAdapter;
import com.tjbaobao.framework.base.BaseStaggeredGridLayoutManager;

/**
 * RecyclerView的二次封装
 * 简单切换普通ListView、GridView、StaggeredGridView，只需要to一下
 * Created by TJbaobao on 2018/1/6.
 */

@SuppressWarnings("unused")
public class BaseRecyclerView<Holder extends BaseRecyclerView.BaseViewHolder,Info> extends RecyclerView {

    private BaseRecyclerAdapter.OnItemClickListener<Holder,Info> mOnItemClickListener ;

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
        this.setLayoutManager(new BaseLinearLayoutManager(getContext()));
    }

    public void toListView(int orientation, boolean reverseLayout)
    {
        this.setLayoutManager(new BaseLinearLayoutManager(getContext(),orientation,reverseLayout));
    }

    public void toGridView(int spanCount)
    {
        this.setLayoutManager(new BaseGridLayoutManager(getContext(),spanCount));
    }

    public void toGridView(int spanCount,int orientation, boolean reverseLayout)
    {
        this.setLayoutManager(new BaseGridLayoutManager(getContext(),spanCount,orientation,reverseLayout));
    }

    public void toStaggeredGridView(int spanCount)
    {
        toStaggeredGridView(spanCount,StaggeredGridLayoutManager.VERTICAL);
    }

    public void toStaggeredGridView(int spanCount,int orientation)
    {
        this.setLayoutManager(new BaseStaggeredGridLayoutManager(spanCount,orientation));
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

    public void setAdapter(BaseRecyclerAdapter<Holder,Info> adapter)
    {
        adapter.setOnItemClickListener(mOnItemClickListener);
        super.setAdapter(adapter);
    }

    public BaseRecyclerAdapter.OnItemClickListener getOnItemClickListener() {
        return mOnItemClickListener;
    }

    public void setOnItemClickListener(BaseRecyclerAdapter.OnItemClickListener<Holder,Info> onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
        Adapter adapter = getAdapter();
        if(adapter!=null)
        {
            if(adapter instanceof BaseRecyclerAdapter)
            {
                ((BaseRecyclerAdapter<Holder,Info>)adapter).setOnItemClickListener(mOnItemClickListener);
            }
        }
    }
}

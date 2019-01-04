package com.tjbaobao.framework.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Parcelable;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.tjbaobao.framework.base.BaseGridLayoutManager;
import com.tjbaobao.framework.base.BaseItemDecoration;
import com.tjbaobao.framework.base.BaseLinearLayoutManager;
import com.tjbaobao.framework.base.BaseRecyclerAdapter;
import com.tjbaobao.framework.base.BaseStaggeredGridLayoutManager;
import com.tjbaobao.framework.entity.base.BaseListInfo;

import java.util.List;

/**
 * RecyclerView的二次封装
 * 简单切换普通ListView、GridView、StaggeredGridView，只需要to一下
 * Created by TJbaobao on 2018/1/6.
 */

@SuppressWarnings("unused")
public class BaseRecyclerView<Holder extends BaseRecyclerView.BaseViewHolder, Info> extends RecyclerView {

    private BaseRecyclerAdapter.OnItemClickListener<Holder, Info> mOnItemClickListener;

    public BaseRecyclerView(Context context) {
        this(context, null);
    }

    public BaseRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void toListView() {
        this.setLayoutManager(new BaseLinearLayoutManager(getContext()));
    }

    public void toListView(int orientation, boolean reverseLayout) {
        this.setLayoutManager(new BaseLinearLayoutManager(getContext(), orientation, reverseLayout));
    }

    public void toGridView(int spanCount) {
        this.setLayoutManager(new BaseGridLayoutManager(getContext(), spanCount));
    }

    public void toGridView(int spanCount, int orientation, boolean reverseLayout) {
        this.setLayoutManager(new BaseGridLayoutManager(getContext(), spanCount, orientation, reverseLayout));
    }

    public void toStaggeredGridView(int spanCount) {
        toStaggeredGridView(spanCount, StaggeredGridLayoutManager.VERTICAL);
    }

    public void toStaggeredGridView(int spanCount, int orientation) {
        this.setLayoutManager(new BaseStaggeredGridLayoutManager(spanCount, orientation));
    }

    public void addListViewItemDecoration() {
        this.addItemDecoration(BaseItemDecoration.getLineVerticalDecoration(BaseItemDecoration.DEF_SPACING));
    }

    public void addGridAverageCenterDecoration(int spacingVertical, int spacingHorizontal) {
        this.addItemDecoration(BaseItemDecoration.getGridCenterDecoration(spacingVertical, spacingHorizontal));
    }

    public abstract static class BaseViewHolder extends RecyclerView.ViewHolder {

        public Object tag ;

        public BaseViewHolder(View itemView) {
            super(itemView);
            onInitView(itemView);
        }

        public abstract void onInitView(View itemView);

        public void onInitAdapter(@NonNull Adapter adapter)
        {

        }

        public void onInitAdapter(@NonNull Adapter adapter,int position)
        {
            onInitAdapter(adapter);
        }

        protected <T extends View> T findViewById(View view,@IdRes int id){
            return view.findViewById(id);
        }
    }

    public void setAdapter(@NonNull BaseRecyclerAdapter<Holder, Info> adapter) {
        adapter.setOnItemClickListener(mOnItemClickListener);
        super.setAdapter(adapter);
    }

    public BaseRecyclerAdapter.OnItemClickListener getOnItemClickListener() {
        return mOnItemClickListener;
    }

    public void setOnItemClickListener(BaseRecyclerAdapter.OnItemClickListener<Holder, Info> onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
        Adapter adapter = getAdapter();
        if (adapter != null) {
            if (adapter instanceof BaseRecyclerAdapter) {
                ((BaseRecyclerAdapter<Holder, Info>) adapter).setOnItemClickListener(onItemClickListener);
            }
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        if ( getScrollState() != SCROLL_STATE_IDLE
                && e.getAction() == MotionEvent.ACTION_DOWN
                &&getScrollYDistance() < 1 ) {
            stopScroll();
        }
        return super.onInterceptTouchEvent(e);
    }

    private int getScrollYDistance() {
        LayoutManager layoutManager = getLayoutManager();
        if(layoutManager!=null)
        {
            if (layoutManager instanceof LinearLayoutManager) {
                LinearLayoutManager mgr = (LinearLayoutManager)layoutManager;
                int position = mgr.findFirstVisibleItemPosition();
                View firstChildView = mgr.findViewByPosition(position);
                if(firstChildView!=null)
                {
                    int itemHeight = firstChildView.getHeight();
                    return position * itemHeight - firstChildView.getTop();
                }
            }
        }
        return 0;
    }

    public void setSpanSizeConfig(List<? extends BaseListInfo> infoList)
    {
        LayoutManager layoutManager = getLayoutManager();
        if(layoutManager!=null)
        {
            if(layoutManager instanceof GridLayoutManager)
            {
                GridLayoutManager manager = (GridLayoutManager) layoutManager;
                manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        if(position>=0&&position<infoList.size())
                        {
                            return infoList.get(position).getSpanSize();
                        }
                        return 1;
                    }
                });
            }
        }
    }

    @Override
    public Parcelable onSaveInstanceState() {
        return super.onSaveInstanceState();
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
    }
}

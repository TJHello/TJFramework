package com.tjbaobao.framework.base;

import android.content.Context;
import android.util.AttributeSet;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.tjbaobao.framework.utils.LogUtil;

public class BaseStaggeredGridLayoutManager extends StaggeredGridLayoutManager {

    public BaseStaggeredGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public BaseStaggeredGridLayoutManager(int spanCount, int orientation) {
        super(spanCount, orientation);
    }

    @Override
    public boolean supportsPredictiveItemAnimations() {
        return false;
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        //override this method and implement code as below
        try {
            super.onLayoutChildren(recycler, state);
        } catch (Exception e) {
            LogUtil.exception(e);
        }
    }
}

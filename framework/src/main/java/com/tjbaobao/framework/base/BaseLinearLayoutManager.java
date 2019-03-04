package com.tjbaobao.framework.base;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import com.tjbaobao.framework.utils.LogUtil;

public class BaseLinearLayoutManager extends LinearLayoutManager {
    public BaseLinearLayoutManager(Context context) {
        super(context);
    }

    public BaseLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public BaseLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean supportsPredictiveItemAnimations() {
        return false;
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            super.onLayoutChildren(recycler, state);
        } catch (Exception e) {
            LogUtil.exception(e);
        }
    }

    @Override
    public void layoutDecoratedWithMargins(View child, int left, int top, int right, int bottom) {
        try {
            super.layoutDecoratedWithMargins(child, left, top, right, bottom);
        } catch (Exception e) {
            LogUtil.exception(e);
        }
    }

    @Override
    public void layoutDecorated(View child, int left, int top, int right, int bottom) {
        try {
            super.layoutDecorated(child, left, top, right, bottom);
        } catch (Exception e) {
            LogUtil.exception(e);
        }
    }

}

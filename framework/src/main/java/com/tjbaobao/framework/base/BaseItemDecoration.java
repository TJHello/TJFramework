package com.tjbaobao.framework.base;

import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tjbaobao.framework.util.Tools;

/**
 * Created by TJbaobao on 2018/1/6.
 */

public class BaseItemDecoration extends RecyclerView.ItemDecoration {

    public static final int DEF_SPACING = Tools.dpToPx(3);

    public static final int TYPE_LINE_VERTICAL = 1;
    public static final int TYPE_LINE_HORIZONTAL = 2;
    public static final int TYPE_GRID_AVERAGE = 3;
    public static final int TYPE_GRID_AVERAGE_CENTER = 4;

    private int type = TYPE_LINE_VERTICAL;

    public static BaseItemDecoration getLineVerticalDecoration(int spacing) {
        return new BaseItemDecoration(spacing,TYPE_LINE_VERTICAL);
    }
    public static BaseItemDecoration getLineHorizontalDecoration(int spacing) {
        return new BaseItemDecoration(spacing,TYPE_LINE_HORIZONTAL);
    }

    public static BaseItemDecoration getGridCenterDecoration(int spacingVertical,int spacingHorizontal)
    {
        return new BaseItemDecoration(TYPE_GRID_AVERAGE_CENTER,spacingVertical,spacingHorizontal);
    }

    private int spacingLeft =0,spacingRight = 0,spacingBottom = 0,spacingTop =0;

    public BaseItemDecoration(int type, int spacingLeft) {
        this.type = type;
        this.spacingLeft = spacingLeft;
    }

    public BaseItemDecoration(int type, int spacingLeft, int spacingBottom) {
        this.type = type;
        this.spacingLeft = spacingLeft;
        this.spacingBottom = spacingBottom;
    }

    public BaseItemDecoration(int type, int spacingLeft, int spacingRight, int spacingBottom, int spacingTop) {
        this.type = type;
        this.spacingLeft = spacingLeft;
        this.spacingRight = spacingRight;
        this.spacingBottom = spacingBottom;
        this.spacingTop = spacingTop;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        int positon = parent.getChildAdapterPosition(view);
        int itemCount = state.getItemCount();
        if(type==TYPE_LINE_VERTICAL)
        {
            outRect.bottom = spacingLeft ;
        }
        else if(type==TYPE_LINE_HORIZONTAL)
        {
            outRect.right = spacingLeft ;
        }
        else if(type==TYPE_GRID_AVERAGE_CENTER)
        {
            if(layoutManager instanceof GridLayoutManager)
            {
                GridLayoutManager manager = (GridLayoutManager) layoutManager;
                int spanCount =manager.getSpanCount();
                int rowCount = itemCount/spanCount-1;
                if(positon/spanCount!=rowCount)
                {
                    outRect.bottom = spacingBottom;
                }
                if(positon%spanCount!=0)
                {
                    outRect.left = spacingLeft;
                }
            }
        }
    }
}

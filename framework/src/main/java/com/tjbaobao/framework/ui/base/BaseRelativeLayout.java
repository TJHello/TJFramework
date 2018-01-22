package com.tjbaobao.framework.ui.base;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by TJbaobao on 2018/1/5.
 */

public class BaseRelativeLayout extends RelativeLayout implements View.OnClickListener{

    protected Context context ;
    public int viewWidth = 0,viewHeight = 0;


    public BaseRelativeLayout(Context context) {
        this(context,null);
    }

    public BaseRelativeLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public BaseRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.initView(context, attrs, defStyleAttr);
    }


    /**
     * 初始化组件
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    private void initView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        this.context = context ;
        onInitView(context, attrs, defStyleAttr);
    }
    protected void onInitView(Context context, AttributeSet attrs, int defStyleAttr) {

    }
    public View setLayout(int layoutId)
    {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return inflater.inflate(layoutId, this,false);
    }

    @Override
    public void onClick(View v) {

    }

    /**
     * 将dip数值转化为px数值
     * @param dip
     * @return
     */
    protected int dpToPx(int dip)
    {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f);
    }
    /**
     * 判断点击的位置是否在view上
     * @param view
     * @return
     */
    protected boolean isTouchOnView(View view, MotionEvent event){
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int x = location[0];
        int y = location[1];
        if(event.getRawX() < x || event.getRawX() > (x + view.getWidth()) || event.getRawY() < y || event.getRawY() > (y + view.getHeight()))
        {
            return false;
        }
        return true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        /*this.viewWidth = w;
        this.viewHeight = h;  */
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        this.viewWidth = measureWidth(widthMeasureSpec);
        this.viewHeight = measureHeight(heightMeasureSpec);
        //setMeasuredDimension(viewWidth, viewHeight);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 计算组件宽度
     */
    private int measureWidth(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        int result = 500;
        if (specMode == MeasureSpec.AT_MOST) {
            result = specSize;
        }

        else if (specMode == MeasureSpec.EXACTLY) {

            result = specSize;
        }

        return result;
    }

    /**
     * 计算组件高度
     */
    private int measureHeight(int measureSpec) {

        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        // Default size if no limits are specified.

        int result = 500;
        if (specMode == MeasureSpec.AT_MOST) {
            result = specSize;
        } else if (specMode == MeasureSpec.EXACTLY) {

            result = specSize;
        }
        return result;
    }
}

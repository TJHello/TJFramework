package com.tjbaobao.framework.ui.base;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

public class BaseLinearLayout extends LinearLayout implements OnClickListener {

	protected Context context ;
	public int viewWidth = 0,viewHeight = 0;
	
	public BaseLinearLayout(Context context) {
		super(context);
		this.initView(context, null, 0);
	}
	
	public BaseLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.initView(context, attrs, 0);
	}
	public BaseLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
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

	private void drawTextCentent(Canvas canvas)
	{
		int padding = viewWidth/10;
		Rect rect = new Rect(padding,padding,viewWidth-padding,viewHeight-padding);//画一个矩形
		Paint rectPaint = new Paint();
		rectPaint.setColor(Color.BLUE);
		rectPaint.setStyle(Paint.Style.FILL);
		canvas.drawRect(rect, rectPaint);
		Paint textPaint = new Paint();
		textPaint.setColor(Color.WHITE);
		textPaint.setTextSize(50);
		textPaint.setStyle(Paint.Style.FILL);
		//该方法即为设置基线上那个点究竟是left,center,还是right  这里我设置为center
		textPaint.setTextAlign(Paint.Align.CENTER);
		Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
		float top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
		float bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom

		int baseLineY = (int) (rect.centerY() - top/2 - bottom/2);//基线中间点的y轴计算公式
		canvas.drawText("A",rect.centerX(),baseLineY,textPaint);
	}

}

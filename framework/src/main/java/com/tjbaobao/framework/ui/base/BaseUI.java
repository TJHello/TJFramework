package com.tjbaobao.framework.ui.base;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.tjbaobao.framework.imp.HandlerToolsImp;
import com.tjbaobao.framework.utils.BaseHandler;

public class BaseUI extends View implements HandlerToolsImp{

	protected Context context ;
	protected int viewWidth,viewHeight ;
	
	public BaseUI(Context context) {
		this(context,null);
	}
	public BaseUI(Context context, AttributeSet attrs) {
		this(context, attrs,0);
	}
	public BaseUI(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView(context, attrs, defStyleAttr);
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
		return inflater.inflate(layoutId,null);
	}
	
	protected int getColorForRes(int resID)
	{
		return context.getResources().getColor(resID);
	}



    public class UILinearLayout extends BaseUI
	{
		public UILinearLayout(Context context) {
			super(context);
		}
		public UILinearLayout(Context context, AttributeSet attrs) {
			super(context, attrs);
		}
		public UILinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
			super(context, attrs, defStyleAttr);
		}
	}
	
	/**
	 * 计算xml布局里的view大小
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		this.viewWidth = measureWidth(widthMeasureSpec);  
        this.viewHeight = measureHeight(heightMeasureSpec);  
        setMeasuredDimension(viewWidth, viewHeight);
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
		int result = 500;
		if (specMode == MeasureSpec.AT_MOST) {
			result = specSize;
		} else if (specMode == MeasureSpec.EXACTLY) {

			result = specSize;
		}
		return result;
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

	protected BaseHandler handler = new BaseHandler(new HandlerCallback());

	private class HandlerCallback implements Handler.Callback
	{
		@Override
		public boolean handleMessage(Message msg) {
			onHandleMessage(msg,msg.what,msg.obj);
			return false;
		}
	}

	@Override
	public void onHandleMessage(Message msg, int what, Object obj) {

	}

	@Override
	public void sendMessage(int what) {
		handler.sendMessage(what);

	}

	@Override
	public void sendMessage(int what, Object obj) {
		handler.sendMessage(what,obj);
	}

	@Override
	public void sendMessage(int what, Object obj, int arg1) {
		handler.sendMessage(what,obj,arg1);
	}
}

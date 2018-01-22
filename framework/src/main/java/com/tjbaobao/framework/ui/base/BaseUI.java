package com.tjbaobao.framework.ui.base;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

public class BaseUI extends View {

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

	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			onHandleMessage(msg.what,msg.obj,msg);
			super.handleMessage(msg);
		}
	};
	protected void onHandleMessage(int what,Object obj,Message msg)
	{};

	/**
	 * 向Handler发送消息
	 * @param what
	 * @param obj
	 */
	protected void sendMessage(int what,Object obj)
	{
		Message msg = new Message();
		msg.what = what ;
		msg.obj = obj ;
		handler.sendMessage(msg);
	}
	protected void sendMessage(int what,Object obj,int arg1)
	{
		Message msg = new Message();
		msg.what = what ;
		msg.obj = obj ;
		msg.arg1 = arg1;
		handler.sendMessage(msg);
	}
}

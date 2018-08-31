package com.tjbaobao.framework.ui.base;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.tjbaobao.framework.imp.HandlerToolsImp;
import com.tjbaobao.framework.utils.BaseHandler;

@SuppressWarnings("unused")
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
		if(inflater!=null)
		{
			return inflater.inflate(layoutId,null);
		}
		return null;
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		if(getWidth()>0&&getHeight()>0&&viewWidth==0||viewHeight==0)
		{
			viewWidth = getWidth();
			viewHeight = getHeight();
		}
	}

	protected int getColorForRes(int resID)
	{
		return context.getResources().getColor(resID);
	}

	/**
	 * 将dip数值转化为px数值
	 * @param dip dip
	 * @return px
	 */
	protected float dpToPx(float dip)
	{
		final float scale = context.getResources().getDisplayMetrics().density;
		return dip * scale + 0.5f;
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

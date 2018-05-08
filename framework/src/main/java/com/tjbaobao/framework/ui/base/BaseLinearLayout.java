package com.tjbaobao.framework.ui.base;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.tjbaobao.framework.imp.HandlerToolsImp;
import com.tjbaobao.framework.utils.BaseHandler;

@SuppressWarnings("unused")
public class BaseLinearLayout extends LinearLayout implements HandlerToolsImp {

	protected Context context ;
	protected int viewWidth,viewHeight ;

	public BaseLinearLayout(Context context) {
		this(context,null);
	}
	public BaseLinearLayout(Context context, AttributeSet attrs) {
		this(context, attrs,0);
	}
	public BaseLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
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

	protected int getColorForRes(int resID)
	{
		return context.getResources().getColor(resID);
	}


	/**
	 * 将dip数值转化为px数值
	 * @param dip dip
	 * @return px
	 */
	protected float dpToPx(int dip)
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

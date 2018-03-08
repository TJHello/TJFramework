package com.tjbaobao.framework.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.WindowManager;

import com.tjbaobao.framework.imp.ActivityToolsImp;
import com.tjbaobao.framework.imp.HandlerToolsImp;
import com.tjbaobao.framework.utils.ActivityTools;
import com.tjbaobao.framework.utils.BaseHandler;

public class BaseFragmentActivity extends FragmentActivity implements View.OnClickListener,ActivityToolsImp,HandlerToolsImp{
	protected Context context ;
	protected Activity activity ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.context = this ;
		this.activity = this ;
	}


	protected BaseHandler handler = new BaseHandler(new HandlerCallback());

	private class HandlerCallback implements Handler.Callback {
		@Override
		public boolean handleMessage(Message message) {
			onHandleMessage(message,message.what,message.obj);
			return false;
		}
	}

	@Override
	public int getColorById(int id) {
		return ActivityTools.getColorById(activity,id);
	}

	@Override
	public String getStringById(int id) {
		return ActivityTools.getStringById(activity,id);
	}

	@Override
	public void finish(int resultCode) {
		ActivityTools.finish(activity,resultCode);
	}

	@Override
	public void startActivity(Class<? extends Activity> mClass) {
		ActivityTools.startActivity(activity,mClass);
	}

	@Override
	public void startActivityAndFinish(Class<? extends Activity> mClass) {
		ActivityTools.startActivityAndFinish(activity,mClass);
	}

	@Override
	public void startActivity(Class<? extends Activity> mClass, String[] keys, Object... values) {
		ActivityTools.startActivityAndFinish(activity,mClass,keys,values);
	}

	@Override
	public void startActivityAndFinish(Class<? extends Activity> mClass, String[] keys, Object... values) {
		ActivityTools.startActivityAndFinish(activity,mClass,keys,values);
	}

	@Override
	public void startActivityAndFinish(Intent intent) {
		ActivityTools.startActivityAndFinish(activity,intent);
	}

	@Override
	public void startActivityForResult(Class<? extends Activity> mClass, int requestCode) {
		ActivityTools.startActivityForResult(activity,mClass,requestCode);
	}

	@Override
	public void startActivityForResult(Class<? extends Activity> mClass, int requestCode, String[] keys, Object... values) {
		ActivityTools.startActivityForResult(activity,mClass,requestCode,keys,values);
	}

	/**
	 * 接受到的Handler的消息
	 * @param msg Message
	 * @param what what
	 * @param obj obj
	 */
	public void onHandleMessage(Message msg,int what,Object obj){}

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

	@Override
	public int getBarHeight() {
		return ActivityTools.getBarHeight(activity);
	}

	@Override
	public void immersiveStatusBar() {
		ActivityTools.immersiveStatusBar(activity);
	}

	@Override
	public void fullScreen() {
		ActivityTools.fullScreen(activity);
	}

	@Override
	public void hideVirtualKey() {
		ActivityTools.hideVirtualKey(activity);
	}

	@Override
	public void onClick(View v) {}
}

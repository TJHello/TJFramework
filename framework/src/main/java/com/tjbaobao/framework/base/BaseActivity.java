package com.tjbaobao.framework.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.tjbaobao.framework.imp.ActivityToolsImp;
import com.tjbaobao.framework.imp.HandlerToolsImp;
import com.tjbaobao.framework.utils.ActivityTools;
import com.tjbaobao.framework.utils.BaseHandler;


/**
 * Activity基类
 * Created by TJbaobao on 2017/9/8.
 *
 * 主要功能亮点:
 * 1、常用工具使用
 * 2、快捷启动Activity
 * @author TJbaobao
 */
@SuppressWarnings("unused")
public class BaseActivity extends AppCompatActivity implements View.OnClickListener,ActivityToolsImp,HandlerToolsImp {
	protected BaseActivity context ;
	private boolean isDestroy = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
	}

	protected BaseHandler handler = new BaseHandler(new HandlerCallback());

	private class HandlerCallback implements Handler.Callback {
		@Override
		public boolean handleMessage(Message message) {
			if(isFinishing()||isDestroy){
				return false;
			}
			onHandleMessage(message,message.what,message.obj);
			return false;
		}
	}


	public BaseActivity getActivity(){
		return context;
	}

	@Override
	public int getColorById(int id) {
		return ActivityTools.getColorById(this,id);
	}

	@Override
	public String getStringById(int id) {
		return ActivityTools.getStringById(this,id);
	}

	@Override
	public void finish(int resultCode) {
		ActivityTools.finish(getActivity(),resultCode);
	}

	@Override
	public void startActivity(Class<? extends Activity> mClass) {
		ActivityTools.startActivity(getActivity(),mClass);
	}

	@Override
	public void startActivityAndFinish(Class<? extends Activity> mClass) {
		ActivityTools.startActivityAndFinish(getActivity(),mClass);
	}

	@Override
	public void startActivity(Class<? extends Activity> mClass, String[] keys, Object... values) {
		ActivityTools.startActivity(getActivity(),mClass,keys,values);
	}

	@Override
	public void startActivityAndFinish(Class<? extends Activity> mClass, String[] keys, Object... values) {
		ActivityTools.startActivityAndFinish(getActivity(),mClass,keys,values);
	}

	@Override
	public void startActivityAndFinish(Intent intent) {
		ActivityTools.startActivityAndFinish(getActivity(),intent);
	}

	@Override
	public void startActivityForResult(Class<? extends Activity> mClass, int requestCode) {
		ActivityTools.startActivityForResult(getActivity(),mClass,requestCode);
	}

	@Override
	public void startActivityForResult(Class<? extends Activity> mClass, int requestCode, String[] keys, Object... values) {
		ActivityTools.startActivityForResult(getActivity(),mClass,requestCode,keys,values);
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
		return ActivityTools.getBarHeight(getActivity());
	}

	@Override
	public void immersiveStatusBar() {
		ActivityTools.immersiveStatusBar(getActivity());
	}

	@Override
	public void fullScreen() {
		ActivityTools.fullScreen(getActivity());
	}

	@Override
	public void hideVirtualKey() {
		ActivityTools.hideVirtualKey(getActivity());
	}

	@Override
	public void onClick(View v) {}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		isDestroy = true;
		handler.removeCallbacksAndMessages(null);
	}

	/**
	 * 判断Activity是否已经走到了onDestroy生命周期
	 * @return boolean
	 */
	public boolean isDestroy() {
		return isDestroy;
	}
}

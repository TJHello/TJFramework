package com.tjbaobao.framework.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.ColorRes;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class BaseActivity extends AppCompatActivity implements View.OnClickListener{
	protected Context context ;
	protected Activity activity ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.context = this ;
		this.activity = this ;
	}
	/**
	 * 通过id获取颜色
	 * @param id
	 * @return
	 */
	protected int getColorById(@ColorRes int id)
	{
		return this.getResources().getColor(id);
	}
	/**
	 * 通过id获取字符串
	 * @param id
	 * @return
	 */
	protected String getStringById(@StringRes int id)
	{
		return this.getResources().getString(id);
	}
	protected void finish(int resultCode)
	{
		setResult(resultCode);
		this.finish();
	}
	/**
	 * 沉浸式状态栏
	 */
	protected void immersiveStatusBar()
	{
		if (VERSION.SDK_INT >= VERSION_CODES.KITKAT)
		{
			this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		}
	}
	/**
	 * 设置全屏
	 */
	protected void fullScreen()
	{
		// 隐藏状态栏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		/*getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
		//       设置屏幕始终在前面，不然点击鼠标，重新出现虚拟按键
		getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
			@Override
			public void onSystemUiVisibilityChange(int visibility) {
				setHideVirtualKey(getWindow());
			}
		});*/

	}

	private void setHideVirtualKey(Window window)
	{
		int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE|
				//布局位于状态栏下方
				View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION|
				//全屏
				View.SYSTEM_UI_FLAG_FULLSCREEN|
				//隐藏导航栏
				View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|
				View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
		if (VERSION.SDK_INT>=19){
			uiOptions |= 0x00001000;
		}else{
			uiOptions |= View.SYSTEM_UI_FLAG_LOW_PROFILE;
		}
		window.getDecorView().setSystemUiVisibility(uiOptions);
	}
	/**
	 * UI更新
	 */
	protected Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			BaseActivity.this.onHandleMessage(msg,msg.what,msg.obj);
			super.handleMessage(msg);
		};
	};
	/**
	 * 接受到的Handler的消息
	 * @param msg
	 * @param what
	 * @param obj
	 */
	protected void onHandleMessage(Message msg,int what,Object obj)
	{
		
	}
	
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

	//TimerTask启动与停止
	protected void stopTimerTask(Timer timer,TimerTask timerTask)
	{
		if(timerTask!=null)
		{
			timerTask.cancel();
			timerTask = null;
		}
		if(timer!=null)
		{
			timer.cancel();
			timer = null;
		}
	}
	protected void startTimerTask(Timer timer,TimerTask timerTask,Date when)
	{
		if(timer!=null&&timerTask!=null)
			timer.schedule(timerTask, when);
	}
	protected void startTimerTask(Timer timer,TimerTask timerTask,long period)
	{
		if(timer!=null&&timerTask!=null)
			timer.schedule(timerTask, period);
	}
	protected void startTimerTask(Timer timer,TimerTask timerTask,Date when,long period)
	{
		if(timer!=null&&timerTask!=null)
			timer.schedule(timerTask, when, period);
	}
	protected void startTimerTask(Timer timer,TimerTask timerTask,long delay,long period)
	{
		if(timer!=null&&timerTask!=null)
			timer.schedule(timerTask, delay, period);
	}
	
	//启动Activity
	protected void startActivity(Class<? extends Activity> mClass)
	{
		Intent intent = new Intent(this,mClass);
		this.startActivity(intent);
	}
	protected void startActivityAndFinish(Class<? extends Activity> mClass)
	{
		startActivity(mClass);
		this.finish();
	}
	protected void startActivity(Class<? extends Activity> mClass,String[] names,String ... values)
	{
		Intent intent = new Intent(this,mClass);
		int i=0;
		for(String name:names)
		{
			intent.putExtra(name, values[i]);
			i++;
		}
		this.startActivity(intent);
	}
	protected void startActivityAndFinish(Class<? extends Activity> mClass,String[] names,String ... values)
	{
		startActivity(mClass,names,values);
		this.finish();
	}
	protected void startActivityAndFinish(Intent intent)
	{
		startActivity(intent);
		this.finish();
	}
	protected void startActivityForResult(Class<? extends Activity> mClass,int requestCode)
	{
		Intent intent = new Intent(this,mClass);
		startActivityForResult(intent, requestCode);
	}
	protected void startActivityForResult(Class<? extends Activity> mClass,int requestCode,String[] names,String ... values)
	{
		Intent intent = new Intent(this,mClass);
		int i=0;
		for(String name:names)
		{
			intent.putExtra(name, values[i]);
			i++;
		}
		startActivityForResult(intent, requestCode);
	}
	
	/**
	 * 获取状态栏高度
	 * @return
	 */
	protected int getBarHeight()
	{
		int statusBarHeight = -1;  
		//获取status_bar_height资源的ID  
		int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");  
		if (resourceId > 0) {
		    //根据资源ID获取响应的尺寸值
			statusBarHeight = getResources().getDimensionPixelSize(resourceId);
		}
		return statusBarHeight;
	}

	@Override
	public void onClick(View v) {
	}
}

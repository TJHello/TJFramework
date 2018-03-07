package com.tjbaobao.framework.base;


import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class BaseFragment extends Fragment{
	protected Context context ;
	protected Activity activity ;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		this.context = this.getActivity();
		this.activity = this.getActivity();
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	/**
	 * 通过id获取颜色
	 * @param id
	 * @return 颜色值
	 */
	@SuppressWarnings("deprecation")
	protected int getColorById(int id)
	{
		final int version = Build.VERSION.SDK_INT;
		return context.getResources().getColor(id);
	}
	/**
	 * 通过id获取字符串
	 * @param id
	 * @return 字符串
	 */
	protected String getStringById(int id)
	{
		return this.getResources().getString(id);
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
			Intent intent = new Intent(activity,mClass);
			activity.startActivity(intent);
		}
		protected void startActivityAndFinish(Class<? extends Activity> mClass)
		{
			startActivity(mClass);
			activity.finish();
		}
		protected void startActivity(Class<? extends Activity> mClass,String[] names,String ... values)
		{
			Intent intent = new Intent(activity,mClass);
			int i=0;
			for(String name:names)
			{
				intent.putExtra(name, values[i]);
				i++;
			}
			activity.startActivity(intent);
		}
		protected void startActivityAndFinish(Class<? extends Activity> mClass,String[] names,String ... values)
		{
			startActivity(mClass,names,values);
			activity.finish();
		}
		protected void startActivityAndFinish(Intent intent)
		{
			startActivity(intent);
			activity.finish();
		}
		protected void startActivityForResult(Class<? extends Activity> mClass,int requestCode)
		{
			Intent intent = new Intent(activity,mClass);
			startActivityForResult(intent, requestCode);
		}
		protected void startActivityForResult(Class<? extends Activity> mClass,int requestCode,String[] names,String ... values)
		{
			Intent intent = new Intent(activity,mClass);
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
		if(getActivity()==null)
		{
			return 0;
		}
		int statusBarHeight = -1;
		//获取status_bar_height资源的ID
		int resourceId = getActivity().getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			//根据资源ID获取响应的尺寸值
			statusBarHeight = getActivity().getResources().getDimensionPixelSize(resourceId);
		}
		return statusBarHeight;
	}
		
}

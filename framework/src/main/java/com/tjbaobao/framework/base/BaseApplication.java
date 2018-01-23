package com.tjbaobao.framework.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import com.tjbaobao.framework.utils.ExecuteLog;
import com.tjbaobao.framework.utils.Tools;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.Thread.UncaughtExceptionHandler;

public class BaseApplication extends Application {
	private static Context context ;
	private static int foregroundActivities =0;
	private MyUncaughtExceptionHandler handler;
	private UncaughtExceptionHandler defaultHandler;
	
	@Override
	public void onCreate() {
		super.onCreate();
		context = this.getApplicationContext();
		
		//监听activity生命周期
		registerActivityLifecycleCallbacks(new MyActivityLifecycleCallbacks());
		
		// 获取默认未捕获异常处理器
		defaultHandler = Thread.getDefaultUncaughtExceptionHandler();

		// 设置未捕获异常处理器
		handler = new MyUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(handler);
	}
	public static Context getContext() {
		return context;
	}
	/**
	 * activity生命周期回调接口
	 * @author TJbaobao
	 *
	 */
	private class MyActivityLifecycleCallbacks implements ActivityLifecycleCallbacks
	{

		@Override
		public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
			
		}

		@Override
		public void onActivityStarted(Activity activity) {
			foregroundActivities++;
		}

		@Override
		public void onActivityResumed(Activity activity) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onActivityPaused(Activity activity) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onActivityStopped(Activity activity) {
			foregroundActivities--;
		}

		@Override
		public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onActivityDestroyed(Activity activity) {
			// TODO Auto-generated method stub
			
		}
		
	}

	public static boolean isAppRunInBackground()
	{
		if(foregroundActivities==0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * 未捕获异常处理器
	 * @author TJbaobao
	 *
	 */
	private class MyUncaughtExceptionHandler implements UncaughtExceptionHandler
	{
		/**
		 *  处理未捕获异常
		 */
		@Override
		public void uncaughtException(Thread thread, Throwable ex)
		{
			try
			{
				//获取并记录异常日志
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				PrintStream ps = new PrintStream(baos);
				ex.printStackTrace(ps);
				byte[] data = baos.toByteArray();
				String sLog = "程序出错："+new String(data);
				Tools.printLog(sLog);
				ExecuteLog.writeErrorException(sLog);
				data = null;
				ps.close();
				baos.close();
			}
			catch (Exception e)
			{

			}
			//让默认未捕获异常处理器来处理未捕获异常
			defaultHandler.uncaughtException(thread, ex);
		}
	}
}

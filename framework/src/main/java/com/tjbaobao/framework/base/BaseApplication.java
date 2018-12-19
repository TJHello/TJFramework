package com.tjbaobao.framework.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import com.tjbaobao.framework.utils.ExecuteLog;
import com.tjbaobao.framework.utils.LogUtil;
import com.tjbaobao.framework.utils.Tools;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.Thread.UncaughtExceptionHandler;

@SuppressWarnings("unused")
public class BaseApplication extends Application {

	public static Context context ;
	private static int foregroundActivities =0;
	private MyUncaughtExceptionHandler handler;
	private static boolean isDebug ;
	private static UncaughtExceptionHandler defHandler ;
	public static String dexPage = null;
	private static boolean isAutoHandlerException = true;

	/**
	 * 初始化框架
	 * @param application application
	 */
	public static void init(Application application)
	{
		context = application.getBaseContext();
		//监听activity生命周期
		application.registerActivityLifecycleCallbacks(new MyActivityLifecycleCallbacks());
		// 设置未捕获异常处理器
		if(isAutoHandlerException){
			defHandler = Thread.getDefaultUncaughtExceptionHandler();
			Thread.setDefaultUncaughtExceptionHandler(new MyUncaughtExceptionHandler());
		}
		Boolean isDebug = (Boolean) Tools.getAppMetaData("FW_IS_DEBUG",false);
		if(isDebug!=null)
		{
			LogUtil.setDebug(isDebug);
		}
	}

	@Override
	public void onCreate() {
		super.onCreate();
		init(this);
	}

	/**
	 * 获取全局静态Context
	 * (通常用于工具类型以及一些简单的例如Toast的UI更新上，不建议用使用该context进行大量任务以及一些可能会造成内存溢出的地方)
	 * @return context
	 */
	public static Context getContext() {
		return context;
	}

	/**
	 * activity生命周期回调接口
	 * @author TJbaobao
	 *
	 */
	private static class MyActivityLifecycleCallbacks implements ActivityLifecycleCallbacks
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

	/**
	 * 判断App是否是后台状态
	 * @return true代表是，false代表不是
	 */
	public static boolean isAppRunInBackground()
	{
		return foregroundActivities==0;
	}

	/**
	 * 未捕获异常处理器
	 * @author TJbaobao
	 *
	 */
	private static class MyUncaughtExceptionHandler implements UncaughtExceptionHandler
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
			catch (Exception ignored)
			{

			}
			//让默认未捕获异常处理器来处理未捕获异常
			defHandler.uncaughtException(thread, ex);
		}
	}

	public static void setDexPage(String page)
	{
		dexPage = page;
	}

	public static void setIsAutoHandlerException(boolean isAuto){
		isAutoHandlerException = isAuto;
	}
}

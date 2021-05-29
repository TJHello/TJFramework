package com.tjbaobao.framework.utils;


import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Looper;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.ColorRes;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.tjbaobao.framework.R;
import com.tjbaobao.framework.base.BaseApplication;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@SuppressWarnings("ALL")
public class
Tools {
	private static Context context = null;
	static{
		context = BaseApplication.getContext();

	}
	/**
	 *输出日志
	 */
	public static void printLog(String msg)
	{
		LogUtil.i(msg);
	}
	
	/**
	 * 休眠
	 * @param time 单位秒
	 */
	public static void sleep(long time)
	{
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			LogUtil.exception(e);
		}
	}
	public static void setOnclickBackground(final View view)
	{
		setOnclickBackground(view,true);
	}
	@SuppressLint("ClickableViewAccessibility")
	public static void setOnclickBackground(final View view, boolean hasBackground)
	{
		if(hasBackground)
		{
			view.setBackgroundResource(R.drawable.fw_ripple);
		}
		view.setOnTouchListener((View v, MotionEvent event) -> {
            if(event.getAction()==MotionEvent.ACTION_DOWN)
            {
                view.setAlpha(0.7f);
            }
            else if(event.getAction()==MotionEvent.ACTION_UP)
            {
                view.setAlpha(1.0f);
            }
            else if(event.getAction()==MotionEvent.ACTION_CANCEL)
            {
                view.setAlpha(1.0f);
            }
            return false;
        });
	}
	
	public static void showToast(String text)
	{
		showToast(text,Toast.LENGTH_SHORT);
	}
	/**
	 * 显示一个Toast消息
	 * @param text 内容
	 * @param duration 时长
	 */

	public static void showToast(String text,int duration)
	{
		if(context==null) return;
		Toast.makeText(context, text, duration).show();
	}

	/**
	 * 将dip数值转化为px数值
	 * @param dip
	 * @return
	 */
	public static float dpToPx(float dip)
	{
		if(context!=null)
		{
			final float scale = context.getResources().getDisplayMetrics().density;
			return dip * scale + 0.5f;
		}
		return 0f;
	}

	public static int dpToPx(int dip)
	{
		if(context!=null)
		{
			final float scale = context.getResources().getDisplayMetrics().density;
			return (int) (dip * scale + 0.5f);
		}
		return 0;
	}

	public static int spToPx(float spValue) {
		if(context!=null)
		{
			final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
			return (int)(spValue * fontScale + 0.5f);
		}
		else
		{
			return 0;
		}
	}

	public static boolean rate()
	{
		return rate(DeviceUtil.getPackageName());
	}

	public static boolean rate(String pkg)
	{
		if(context==null) return false;
		try{
			Uri uri = Uri.parse("market://details?id="+pkg);
			Intent intentRate = new Intent(Intent.ACTION_VIEW,uri);
			ComponentName comp = new ComponentName("com.android.vending", "com.google.android.finsky.activities.LaunchUrlHandlerActivity");
			intentRate.setComponent(comp);
			intentRate.setData(uri);
			intentRate.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intentRate);
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	public static String getResString(@StringRes int id)
	{
		if(id==-1||context==null)
		{
			return "";
		}
		return context.getResources().getString(id);
	}

	public static int getResColor(@ColorRes int id)
	{
		if(context==null)
		{
			return -1;
		}
		return context.getResources().getColor(id);
	}

	public static void setLParamsFilWidth(View view, Drawable drawable)
	{
		int nWidth = view.getWidth();
		int nHeight = nWidth * drawable.getIntrinsicHeight() / drawable.getIntrinsicWidth();
		android.widget.LinearLayout.LayoutParams rpImgView = (android.widget.LinearLayout.LayoutParams)view.getLayoutParams();
		rpImgView.height = nHeight;
		view.setLayoutParams(rpImgView);
	}
	/**
	 * 获取屏幕宽度
	 * @return 屏幕宽度
	 */
	public static int getScreenWidth()
	{
		if(context==null)
		{
			return 0;
		}
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;
	}
	
	/**
	 * 获取屏幕高度
	 * @return 屏幕高度
	 */
	public static int getScreenHeight()
	{
		if(context==null)
		{
			return 0;
		}
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dm);
		return dm.heightPixels;
	}

	@Nullable
	public static InputStream getAssetsInputSteam(String fileName)
	{
		if(context!=null)
		{
			AssetManager assetManager = null;
			try {
				assetManager = context.getAssets();
				if(assetManager!=null)
				{
					return assetManager.open(fileName);
				}
			} catch (IOException e) {
				LogUtil.exception(e);
			}
		}
		return null;
	}

	@Nullable
	public static InputStream getResInputSteam(String fileName)
	{
		if(context!=null)
		{
			return context.getResources().openRawResource(getResourceIdByFilter(fileName));
		}
		return null;
	}

	public static int getResouseIdByRef(String name){
		if(context==null) return 0;
		ApplicationInfo appInfo = context.getApplicationInfo();
		return context.getResources().getIdentifier(name, "drawable", appInfo.packageName);
    }  

    public static int getResourceIdByFilter(String name) {
		if(context==null) return 0;
        Resources res = context.getResources();  
        return res.getIdentifier(name, "raw", context.getPackageName());  
    }  


	public static Bitmap getViewDrawableCache(View view)
	{
		view.setDrawingCacheEnabled(true);
		/*view.measure(
				View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),

				View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));*/

		view.layout(0, 0, view.getMeasuredWidth(),

				view.getMeasuredHeight());



		view.buildDrawingCache();

		Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());// 注意:这地方必须特别注意
		return  bitmap;
	}

	public static boolean feedback(Context context,String email,String subject,String content){
		if(context==null) return false;
		Intent intent = new Intent(Intent.ACTION_SENDTO);
		intent.setType("message/rfc822");
		String uriText = String.format("mailto:%s?subject=%s&body=%s", email, subject, content);
		intent.setData(Uri.parse(uriText));
		if (intent.resolveActivity(context.getPackageManager()) != null) {
			context.startActivity(intent);
			return true;
		}else{
			return false;
		}
	}

	public static boolean checkApkExist(String packageName){
		try {
			if (TextUtils.isEmpty(packageName))
				return false;
			if(context==null) return false;
			ApplicationInfo info = context.getPackageManager()
					.getApplicationInfo(packageName,
							PackageManager.GET_UNINSTALLED_PACKAGES);
			return true;
		} catch (PackageManager.NameNotFoundException e) {
			return false;
		}
	}

	private static long lastClickTime=0 ;
	public static long CAN_ONCLICK_TIME = 550L;
	public static boolean cantOnclik() {
		return cantOnclik(CAN_ONCLICK_TIME);
	}
	public static boolean cantOnclik(long time) {
		long timeN = System.currentTimeMillis();
		long timeD = timeN - lastClickTime;
		if ( 0 < timeD && timeD < time) {
			return true;
		}
		lastClickTime = timeN;
		return false;
	}

	public static boolean isMainThread() {
		return Looper.getMainLooper() == Looper.myLooper();
	}

	/**
	 * 判断当前设备是手机还是平板，代码来自 Google I/O App for Android
	 * @param context 上下文容器
	 * @return 平板返回 True，手机返回 False
	 */
	public static boolean isPad() {
		if(context==null) return false;
		return (context.getResources().getConfiguration().screenLayout
				& Configuration.SCREENLAYOUT_SIZE_MASK)
				>= Configuration.SCREENLAYOUT_SIZE_LARGE;
	}

	@Nullable
	public static Object getAppMetaData(String key,Object def)
	{
		if(context==null) return def;
		ApplicationInfo appInfo = null;
		try {
			appInfo = context.getPackageManager()
					.getApplicationInfo(context.getPackageName(),
							PackageManager.GET_META_DATA);
			if(appInfo!=null&&appInfo.metaData!=null)
			{
				if(def==null||def instanceof String)
				{
					return appInfo.metaData.getString(key, (String) def);
				}
				else if(def instanceof Integer)
				{
					return appInfo.metaData.getInt(key, (Integer) def);
				}
				else if(def instanceof Float)
				{
					return appInfo.metaData.getFloat(key, (Float) def);
				}
				else if(def instanceof Long)
				{
					return appInfo.metaData.getLong(key, (Long) def);
				}
				else if(def instanceof Boolean)
				{
					return appInfo.metaData.getBoolean(key, (Boolean) def);
				}
			}
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
}

package com.tjbaobao.framework.utils;


import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Looper;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.tjbaobao.framework.R;
import com.tjbaobao.framework.base.BaseApplication;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.List;


@SuppressWarnings("ALL")
public class Tools {
	private static Toast toast;
	private static Context context = BaseApplication.getContext();
	private static SharedPreferences pref ;
	static{
		if(context!=null)
		pref = context.getSharedPreferences("app", 0);
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
		} catch (InterruptedException ignored) {
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
		if(toast==null)
		{
			toast = Toast.makeText(context, text, duration);
		}
		else
		{
			toast.setText(text);
		}
		toast.show();
	}

	public static Object getSharedPreferencesValue(String key,Object defValue)
	{
		if(defValue==null||defValue instanceof String)
		{
			return pref.getString(key, (String) defValue);
		}
		else if(defValue instanceof Integer)
		{
			return pref.getInt(key, (Integer) defValue);
		}
		else if(defValue instanceof Float)
		{
			return pref.getFloat(key, (Float) defValue);
		}
		else if(defValue instanceof Long)
		{
			return pref.getLong(key, (Long) defValue);
		}
		else if(defValue instanceof Boolean)
		{
			return pref.getBoolean(key, (Boolean) defValue);
		}
		return null;
	}

	public static void setSharedPreferencesValue(String key,Object value)
	{
		SharedPreferences.Editor editor = pref.edit();
		if(value==null||value instanceof String)
		{
			editor.putString(key, (String) value);
		}
		else if(value instanceof Integer)
		{
			editor.putInt(key, (Integer) value);
		}
		else if(value instanceof Float)
		{
			editor.putFloat(key, (Float) value);
		}
		else if(value instanceof Long)
		{
			editor.putLong(key, (Long) value);
		}
		else if(value instanceof Boolean)
		{
			editor.putBoolean(key, (Boolean) value);
		}
		editor.commit();
	}
	/**
	 * 将dip数值转化为px数值
	 * @param dip
	 * @return
	 */
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
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
	}

	public static boolean rate()
	{
		return rate(DeviceUtil.getPackageName());
	}

	public static boolean rate(String pkg)
	{
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
			try {
				return context.getAssets().open(fileName);
			} catch (IOException e) {
				e.printStackTrace();
				LogUtil.e(e.getMessage());
			}
			catch (Exception e)
			{
				e.printStackTrace();
				LogUtil.e(e.getMessage());
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
        try {  
            Field field = R.drawable.class.getField(name);
            return field.getInt(new R.drawable());  
        } catch (IllegalAccessException e) {  
            e.printStackTrace();  
            return 0;  
        } catch (NoSuchFieldException e) {  
            e.printStackTrace();  
            return 0;  
        }  
    }  

    public static int getResourceIdByFilter(String name) {
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

	public static boolean feedback(Context context,String email,String content){
		Intent intent=new Intent(Intent.ACTION_SENDTO);
		intent.setData(Uri.parse("mailto:"+email));
		List<ResolveInfo> resInfos = context.getPackageManager().queryIntentActivities(intent, 0);
		if(!resInfos.isEmpty()){
			intent.putExtra(Intent.EXTRA_SUBJECT, content);
			context.startActivity(intent);
			return true;
		}else {
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
	public static boolean canOnclik() {
		long time = System.currentTimeMillis();
		long timeD = time - lastClickTime;
		if ( 0 < timeD && timeD < 550) {
			return true;
		}
		lastClickTime = time;
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
		return (context.getResources().getConfiguration().screenLayout
				& Configuration.SCREENLAYOUT_SIZE_MASK)
				>= Configuration.SCREENLAYOUT_SIZE_LARGE;
	}
}

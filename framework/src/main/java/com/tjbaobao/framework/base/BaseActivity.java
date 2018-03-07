package com.tjbaobao.framework.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.annotation.ColorRes;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.tjbaobao.framework.utils.BaseHandler;

import java.io.Serializable;


/**
 * Activity基类
 * Created by TJbaobao on 2017/9/8.
 *
 * 主要功能亮点:
 * 1、常用工具使用
 * 2、快捷启动Activity
 *
 */
@SuppressWarnings("unused")
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
	 * @param id 颜色资源id
	 * @return 颜色值
	 */
	protected int getColorById(@ColorRes int id)
	{
		return this.getResources().getColor(id);
	}

	/**
	 * 通过id获取字符串
	 * @param id 字符串资源id
	 * @return 字符串
	 */
	protected String getStringById(@StringRes int id)
	{
		return this.getResources().getString(id);
	}

	/**
	 * 带resultCode的finish
	 * @param resultCode 返回结果
	 */
	protected void finish(int resultCode)
	{
		setResult(resultCode);
		this.finish();
	}
	/**
	 * 沉浸式状态栏
	 *
	 * 使用需要在布局中配合增加以下参数
	 * android:windowTranslucentStatus=“true"
	 * android:fitsSystemWindows="true"
	 *
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
        int uiOptions = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE|
                    //布局位于状态栏下方
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION|
                    //全屏
                    View.SYSTEM_UI_FLAG_FULLSCREEN|
                    //隐藏导航栏
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        }
        if (VERSION.SDK_INT>=19){
			uiOptions |= 0x00001000;
		}else{
			uiOptions |= View.SYSTEM_UI_FLAG_LOW_PROFILE;
		}
		window.getDecorView().setSystemUiVisibility(uiOptions);
	}


	protected BaseHandler handler = new BaseHandler(new HandlerCallback());

	private class HandlerCallback implements Handler.Callback {

		@Override
		public boolean handleMessage(Message message) {
			BaseActivity.this.onHandleMessage(message,message.what,message.obj);
			return false;
		}
	}

	/**
	 * 接受到的Handler的消息
	 * @param msg Message
	 * @param what what
	 * @param obj obj
	 */
	protected void onHandleMessage(Message msg,int what,Object obj)
	{

	}

	/**
	 * 向Handler发送消息
	 * @param what what
	 */
	protected void sendMessage(int what)
	{
		Message msg = handler.obtainMessage();
		msg.what = what ;
		handler.sendMessage(msg);
	}

	/**
	 * 向Handler发送消息
	 * @param what what
	 * @param obj obj
	 */
    protected void sendMessage(int what, Object obj)
	{
		Message msg = handler.obtainMessage();
		msg.what = what ;
		msg.obj = obj ;
		handler.sendMessage(msg);
	}
	protected  void sendMessage(int what,Object obj,int arg1)
	{
		Message msg = handler.obtainMessage();
		msg.what = what ;
		msg.obj = obj ;
		msg.arg1 = arg1;
		handler.sendMessage(msg);
	}

	/**
	 * 快捷启动Activity
	 * @param mClass Activity.class
	 */
	protected void startActivity(Class<? extends Activity> mClass)
	{
		Intent intent = new Intent(this,mClass);
		this.startActivity(intent);
	}

	/**
	 * 快捷启动Activity并且finish当前Activity
	 * @param mClass Activity.class
	 */
	protected void startActivityAndFinish(Class<? extends Activity> mClass)
	{
		startActivity(mClass);
		this.finish();
	}

	/**
	 * 快捷启动Activity并且传入参数
	 * @param mClass Activity.class
	 * @param keys keys
	 * @param values values
	 */
	protected void startActivity(Class<? extends Activity> mClass,String[] keys,Object ... values)
	{
		Intent intent = new Intent(this,mClass);
		int i=0;
		for(String name:keys)
		{
			Object value = values[i];
			if (value instanceof Integer) {
				intent.putExtra(name, (Integer)value);
			}
			else if(value instanceof Byte)
			{
				intent.putExtra(name, (Byte)value);
			}
			else if(value instanceof Long)
			{
				intent.putExtra(name, (Long)value);
			}
			else if(value instanceof Float)
			{
				intent.putExtra(name, (Float)value);
			}
			else if(value instanceof Integer[])
			{
				intent.putExtra(name, (Integer[])value);
			}
			else if(value instanceof Short)
			{
				intent.putExtra(name, (Short)value);
			}
			else if(value instanceof Bundle)
			{
				intent.putExtra(name, (Bundle)value);
			}
			else if(value instanceof Byte[])
			{
				intent.putExtra(name, (Byte[])value);
			}
			else if(value instanceof char[])
			{
				intent.putExtra(name, (char[])value);
			}
			else if(value instanceof Double)
			{
				intent.putExtra(name, (Double)value);
			}
			else if(value instanceof String[])
			{
				intent.putExtra(name, (String[])value);
			}
			else if(value instanceof Boolean)
			{
				intent.putExtra(name, (Boolean)value);
			}
			else if(value instanceof Float[])
			{
				intent.putExtra(name, (Float[])value);
			}
			else if(value instanceof Short[])
			{
				intent.putExtra(name, (Short[])value);
			}
			else if(value instanceof Long[])
			{
				intent.putExtra(name, (Long[])value);
			}
			else if(value instanceof Parcelable)
			{
				intent.putExtra(name, (Parcelable)value);
			}
			else if(value instanceof Parcelable[])
			{
				intent.putExtra(name, (Parcelable[])value);
			}
			else if(value instanceof CharSequence)
			{
				intent.putExtra(name, (CharSequence)value);
			}
			else if(value instanceof CharSequence[])
			{
				intent.putExtra(name, (CharSequence[])value);
			}
			else if(value instanceof Serializable)
			{
				intent.putExtra(name, (Serializable)value);
			}
			i++;
		}
		this.startActivity(intent);
	}

	/**
	 * 快捷启动Activity并且传入参数然后finish
	 * @param mClass Activity.class
	 * @param names keys
	 * @param values values
	 */
	protected void startActivityAndFinish(Class<? extends Activity> mClass,String[] names,Object ... values)
	{
		startActivity(mClass,names,values);
		this.finish();
	}

	/**
	 * 快捷启动Activity并且finish
	 * @param intent intent
	 */
	protected void startActivityAndFinish(Intent intent)
	{
		startActivity(intent);
		this.finish();
	}

	/**
	 * 通过ForResult方式快捷启动Activity
	 * @param mClass Activity.class
	 * @param requestCode 请求码
	 */
	protected void startActivityForResult(Class<? extends Activity> mClass,int requestCode)
	{
		Intent intent = new Intent(this,mClass);
		startActivityForResult(intent, requestCode);
	}

	/**
	 * 通过ForResult方式快捷启动Activity并且携带参数
	 * @param mClass Activity.class
	 * @param requestCode 请求码
	 * @param keys keys
	 * @param values values
	 */
	protected void startActivityForResult(Class<? extends Activity> mClass,int requestCode,String[] keys,Object ... values)
	{
		Intent intent = new Intent(this,mClass);
		int i=0;
		for(String name:keys)
		{
			Object value = values[i];
			if (value instanceof Integer) {
				intent.putExtra(name, (Integer)value);
			}
			else if(value instanceof Byte)
			{
				intent.putExtra(name, (Byte)value);
			}
			else if(value instanceof Long)
			{
				intent.putExtra(name, (Long)value);
			}
			else if(value instanceof Float)
			{
				intent.putExtra(name, (Float)value);
			}
			else if(value instanceof Integer[])
			{
				intent.putExtra(name, (Integer[])value);
			}
			else if(value instanceof Short)
			{
				intent.putExtra(name, (Short)value);
			}
			else if(value instanceof Bundle)
			{
				intent.putExtra(name, (Bundle)value);
			}
			else if(value instanceof Byte[])
			{
				intent.putExtra(name, (Byte[])value);
			}
			else if(value instanceof char[])
			{
				intent.putExtra(name, (char[])value);
			}
			else if(value instanceof Double)
			{
				intent.putExtra(name, (Double)value);
			}
			else if(value instanceof String[])
			{
				intent.putExtra(name, (String[])value);
			}
			else if(value instanceof Boolean)
			{
				intent.putExtra(name, (Boolean)value);
			}
			else if(value instanceof Float[])
			{
				intent.putExtra(name, (Float[])value);
			}
			else if(value instanceof Short[])
			{
				intent.putExtra(name, (Short[])value);
			}
			else if(value instanceof Long[])
			{
				intent.putExtra(name, (Long[])value);
			}
			else if(value instanceof Parcelable)
			{
				intent.putExtra(name, (Parcelable)value);
			}
			else if(value instanceof Parcelable[])
			{
				intent.putExtra(name, (Parcelable[])value);
			}
			else if(value instanceof CharSequence)
			{
				intent.putExtra(name, (CharSequence)value);
			}
			else if(value instanceof CharSequence[])
			{
				intent.putExtra(name, (CharSequence[])value);
			}
			else if(value instanceof Serializable)
			{
				intent.putExtra(name, (Serializable)value);
			}
			i++;
		}
		startActivityForResult(intent, requestCode);
	}

	
	/**
	 * 获取状态栏高度
	 * @return 返回高度 -1代表失败
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

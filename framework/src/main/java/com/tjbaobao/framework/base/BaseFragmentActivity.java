package com.tjbaobao.framework.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.WindowManager;

public class BaseFragmentActivity extends FragmentActivity implements View.OnClickListener{
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
	protected int getColorById(int id)
	{
		return this.getResources().getColor(id);
	}
	/**
	 * 通过id获取字符串
	 * @param id
	 * @return
	 */
	protected String getStringById(int id)
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
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}

	@Override
	public void onClick(View v) {

	}
}

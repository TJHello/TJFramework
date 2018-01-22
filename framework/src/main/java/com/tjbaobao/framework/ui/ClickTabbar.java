package com.tjbaobao.framework.ui;


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tjbaobao.framework.R;
import com.tjbaobao.framework.entity.ui.Tab;
import com.tjbaobao.framework.listener.OnTabListener;

import java.util.ArrayList;


public class ClickTabbar extends LinearLayout implements OnClickListener {
	
	private Context context ;
	private static final int DEFAULT_LAYOUT_ID = R.layout.fw_click_tabbar_layout;
	private ArrayList<Tab> tabList = new ArrayList<>();
	private int defaultPosition = -1 ;
	private String defaultTag = null;
	private OnTabListener onTabListener = null;
	private int ivId,tvId ;
	public ClickTabbar(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context, null, -1);
	}
	public ClickTabbar(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView(context, attrs, defStyleAttr);
	}
	

	/**
	 * 初始化组件
	 * @param context
	 * @param attrs
	 * @param defStyleAttr
	 */
	private void initView(Context context, AttributeSet attrs, int defStyleAttr)
	{
		this.context = context ;
		//setDefaultLayout();
	}
	
	/**
	 * 添加tab
	 * @param tab
	 */
	public void addTab(Tab tab)
	{
		addTab(tab,R.layout.fw_tabbar_tab_layout,R.id.iv_tab_img,R.id.tv_tab_text);
	}
	public void addTab(Tab tab,int itemLayoutId,int ivId,int tvId)
	{
		this.ivId = ivId;
		this.tvId = tvId;
		int position = tabList.size() ;
		String tag = tab.getTag();
		tab.setPosition(position);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View tabView = inflater.inflate(itemLayoutId, this,false);
		ImageView imageView = null ;
		TextView textView = null;
		if(ivId!=-1){
			imageView = (ImageView) tabView.findViewById(ivId);
		}
		if(tvId!=-1)
		{
			textView = (TextView) tabView.findViewById(tvId);
			if(textView!=null&&tab.getText()!=null){
				textView.setText(tab.getText());
			}
		}
		
		//如果是默认值
		if((defaultPosition!=-1&&defaultPosition==position)
				||(defaultTag!=null&&tag!=null&&defaultTag.equals(tag)))
		{
			if(imageView!=null){
				imageView.setImageResource(tab.getImgOnId());
			}
			if(textView!=null){
				if(tab.getTextOnColor()!=-1){
					textView.setTextColor(tab.getTextOnColor());
				}
			}
		}
		else
		{
			if(imageView!=null){
				imageView.setImageResource(tab.getImgOffId());
			}
			if(textView!=null){
				if(tab.getTextOnColor()!=-1){
					textView.setTextColor(tab.getTextOffColor());
				}
			}
		}
		//设置标签
		if(tag!=null)
		{
			if(tabView!=null)
			{
				tabView.setTag(tab.getTag());
			}
		}
		else
		{
			tab.setTag(position+"");
			if(tabView!=null)
			{
				tabView.setTag(tab.getTag());
			}
		}
		if(tabView!=null){
			tabView.setOnClickListener(this);
			this.addView(tabView);
		}
		tabList.add(tab);
		this.invalidate();
	}
	
	public void closeAllTab()
	{
		for(Tab tab:tabList)
		{
			View tabView = this.findViewWithTag(tab.getTag());
			ImageView imageView = (ImageView) tabView.findViewById(ivId);
			TextView textView = (TextView) tabView.findViewById(tvId);
			imageView.setImageResource(tab.getImgOffId());
			if(textView!=null&&tab.getText()!=null)
			{
				textView.setText(tab.getText());
			}
			if(textView!=null&&tab.getText()!=null)
			{
				textView.setTextColor(tab.getTextOffColor());
			}
		}
	}
	
	public void openTab(Tab tab)
	{
		View tabView = this.findViewWithTag(tab.getTag());
		ImageView imageView = (ImageView) tabView.findViewById(ivId);
		TextView textView = (TextView) tabView.findViewById(tvId);
		imageView.setImageResource(tab.getImgOnId());
		if(textView!=null){
			textView.setText(tab.getText());
		}
		if(textView!=null&&tab.getTextOnColor()!=-1){
			textView.setTextColor(tab.getTextOnColor());
		}
		if(onTabListener!=null)
		{
			onTabListener.onTabClick(tab.getTag(),tab.getPosition());
		}
	}

	/**
	 * 通过序号设置默认tab
	 * @param position
	 */
	public void setDefaultTab(int position)
	{
		defaultPosition = position;
		openTab(position);
	}
	
	/**
	 * 通过标签设置默认tab
	 * @param tag
	 */
	public void setDefaultTab(String tag)
	{
		defaultTag = tag ;
		opendTab(tag);
	}

	/**
	 * 通过序号打开tab
	 * @param position
	 */
	public void openTab(int position)
	{
		if(position>=0&&position<tabList.size()-1)
		{
			Tab tab = tabList.get(position);
			openTab(tab);
		}
	}
	
	/**
	 * 通过标签打开tab
	 * @param tag
	 */
	public void opendTab(String tag)
	{
		if(tag!=null)
		{
			for(Tab tab : tabList)
			{
				if(tab.getTag().equals(tag))
				{
					openTab(tab);
					break;
				}
			}
		}
	}

	/**
	 * 重新设置组件布局
	 * @param layoutId
	 */
	public void setLayout(int layoutId)
	{
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(layoutId, this);
	}
	private void setDefaultLayout()
	{
		setLayout(DEFAULT_LAYOUT_ID);
	}

	public void setOnTabListener(OnTabListener onTabListener) {
		this.onTabListener = onTabListener;
	}
	
	@Override
	public void onClick(View v) {
		String tag = (String) v.getTag();
		if(tag!=null)
		{
			for(Tab tab:tabList)
			{
				if(tab.getTag().equals(tag))
				{
					closeAllTab();
					openTab(tab);
					break;
				}
			}
		}
	}
}

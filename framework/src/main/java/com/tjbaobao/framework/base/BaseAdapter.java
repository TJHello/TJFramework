package com.tjbaobao.framework.base;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

@SuppressWarnings({"unused", "WeakerAccess"})
public class BaseAdapter extends android.widget.BaseAdapter {

	protected Context context ;
	protected ArrayList<?> arrayList ;
	protected int resId ;
	public BaseAdapter(Context context,ArrayList<?> arrayList,int resId) {
		this.context = context ;
		this.arrayList = arrayList ;
		this.resId = resId;
	}
	@Override
	public int getCount() {
		return arrayList.size();
	}

	@Override
	public Object getItem(int position) {
		return arrayList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		return convertView;
	}
}

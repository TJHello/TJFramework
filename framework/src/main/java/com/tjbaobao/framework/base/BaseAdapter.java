package com.tjbaobao.framework.base;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Adapter基类
 * Created by TJbaobao on 2017/9/8.
 *
 *
 * @deprecated 该类已经不建议使用，可以使用{@link BaseRecyclerAdapter}代替，
 *
 * 				同时，已经不建议使用ListView,可以使用{@link com.tjbaobao.framework.ui.BaseRecyclerView}代替
 *
 */
@SuppressWarnings({"unused", "WeakerAccess"})
@Deprecated
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

package com.tjbaobao.framework.entity.base;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 作者:TJbaobao
 * 时间:2018/9/21  10:32
 * 说明:
 * 使用：
 */
public class BaseListInfo {

    private int type ;
    private Object info ;
    private int spanSize ;
    private transient RecyclerView.Adapter adapter ;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Object getInfo() {
        return info;
    }

    public void setInfo(Object info) {
        this.info = info;
    }

    public int getSpanSize() {
        return spanSize;
    }

    public void setSpanSize(int spanSize) {
        this.spanSize = spanSize;
    }

    @Nullable
    public RecyclerView.Adapter getAdapter() {
        return adapter;
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        this.adapter = adapter;
    }
}

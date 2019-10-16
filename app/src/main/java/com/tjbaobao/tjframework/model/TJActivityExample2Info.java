package com.tjbaobao.tjframework.model;


import androidx.recyclerview.widget.RecyclerView;

import com.tjbaobao.framework.entity.base.BaseListInfo;

/**
 * 作者:TJbaobao
 * 时间:2018/9/21  10:52
 * 说明:
 * 使用：
 */
public class TJActivityExample2Info extends BaseListInfo {

    public static final int TYPE_ITEM = 0;

    public static final int TYPE_TITLE = 1;

    public static final int TYPE_LIST = 2;

    public TJActivityExample2Info(TJActivityExample1Info info) {
        setInfo(info);
        setType(TYPE_ITEM);
        setSpanSize(1);
    }

    public TJActivityExample2Info(AppItemTitleInfo info,int spanSize)
    {
        setInfo(info);
        setType(TYPE_TITLE);
        setSpanSize(spanSize);
    }

    public TJActivityExample2Info(RecyclerView.Adapter adapter, int spanSize) {
        setAdapter(adapter);
        setSpanSize(spanSize);
        setType(TYPE_LIST);
    }
}

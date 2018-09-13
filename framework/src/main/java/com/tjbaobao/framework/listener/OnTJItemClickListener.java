package com.tjbaobao.framework.listener;

import android.support.annotation.NonNull;
import android.view.View;

/**
 * 作者:TJbaobao
 * 时间:2018/8/13  14:28
 * 说明:
 * 使用：
 */
public interface OnTJItemClickListener<T> {

    default void onItemClick(@NonNull T info){}

    default void onItemClick(int type, @NonNull T info){
        onItemClick(info);
    }

    default void onItemClick(int type, @NonNull T info, @NonNull View view){
        onItemClick(type,info);
    }
}

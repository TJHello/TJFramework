package com.tjbaobao.framework.listener;

import android.view.View;

import androidx.annotation.NonNull;

/**
 * 作者:TJbaobao
 * 时间:2018/12/19  16:43
 * 说明:
 * 使用：
 */
public interface OnTJHolderItemClickListener<Info> {

    void onClick(@NonNull View view,@NonNull Info info,int position);

}

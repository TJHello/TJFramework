package com.tjbaobao.framework.listener;

import android.content.DialogInterface;
import android.view.View;

/**
 * 作者:TJbaobao
 * 时间:2018/9/6  10:54
 * 说明:
 * 使用：
 */
public interface OnTJDialogListener {

    /**
     * 按钮点击监听
     * @param view view
     * @return 自定义的状态，入不需要可返回人和数字，例如0
     */
    int onTJClick(View view);

    default void onDismiss(DialogInterface dialog, int state){}

    default void onShow(DialogInterface dialog,int state){}
}

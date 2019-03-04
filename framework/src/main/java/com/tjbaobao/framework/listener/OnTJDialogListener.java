package com.tjbaobao.framework.listener;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
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
     * @return 自定义的状态，如不需要可返回任何数字，例如0
     */
    default int onTJClick(@NonNull View view){
        return 0;
    }

    default void onDismiss(DialogInterface dialog, int state){}

    default void onShow(DialogInterface dialog,int state){}

    default void onBtContinueClick(@NonNull View view){}

    default void onBtCancelClick(@NonNull View view){}

    default void onBtCloseClick(@NonNull View view){}
}

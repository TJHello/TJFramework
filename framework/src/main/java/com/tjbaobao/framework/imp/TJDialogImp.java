package com.tjbaobao.framework.imp;

import android.support.annotation.IdRes;
import android.view.View;

/**
 * 作者:TJbaobao
 * 时间:2018/7/18  18:24
 * 说明:
 * 使用：
 */
public interface TJDialogImp {

    void onWinBgClick(View view);

    void onWinBoxClick(View view);

    void onTitleClick(View view);

    void onBtContinueClick(View view);

    void onBtCancelClick(View view);

    void onBtCloseClick(View view);

    void destroy();

    void isCantClose();

    @IdRes
    int getViewWinBgId();

    @IdRes
    int getViewWinBoxId();

    @IdRes
    int getViewWinTitleId();

    @IdRes
    int getViewWinBtContinue();

    @IdRes
    int getViewWinBtCancel();

    @IdRes
    int getViewWinBtClose();
}

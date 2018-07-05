package com.tjbaobao.framework.listener;

public interface OnProgressBarListener {

    void onProgressChanged(float progress);

    void onStartTrackingTouch();

    void onStopTrackingTouch();
}

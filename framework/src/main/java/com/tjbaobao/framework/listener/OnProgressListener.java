package com.tjbaobao.framework.listener;

/**
 * Created by TJbaobao on 2018/1/12.
 */

public interface OnProgressListener {

    void  onProgress(float progress, boolean isFinish);

    default void onProgress(long sizeProgress, long sizeTotal)
    {
        onProgress((float)sizeProgress/(float)sizeTotal,sizeProgress==sizeTotal||sizeProgress==-1);
    }

    default void onProgress(Object tag,long sizeProgress, long sizeTotal )
    {
        onProgress((float)sizeProgress/(float)sizeTotal,sizeProgress==sizeTotal||sizeProgress==-1);
    }
}

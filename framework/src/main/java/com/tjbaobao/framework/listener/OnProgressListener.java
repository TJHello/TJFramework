package com.tjbaobao.framework.listener;

/**
 * Created by TJbaobao on 2018/1/12.
 */

public abstract class OnProgressListener {

    public long length = 0;
    public Object tag = null;

    public abstract void onProgress(float progress, boolean isFinish);

    public void onProgress(long sizeProgress, long sizeTotal)
    {
        onProgress(tag,sizeProgress,sizeTotal);
    }

    public void onProgress(long sizeProgress)
    {
        onProgress(sizeProgress,length);
    }

    public void onProgress(Object tag,long sizeProgress, long sizeTotal )
    {
        onProgress((float)sizeProgress/(float)sizeTotal,sizeProgress==sizeTotal||sizeProgress==-1);
    }
}

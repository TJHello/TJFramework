package com.tjbaobao.framework.listener;

/**
 * 作者:TJbaobao
 * 时间:2018/7/16  13:56
 * 说明:
 * 使用：
 */
public abstract class RxTimerTaskListener {

    public abstract void run(long time);

    public void runUI(long time){}
}

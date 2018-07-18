package com.tjbaobao.framework.imp;

import com.tjbaobao.framework.listener.RxTimerTaskListener;

/**
 * 作者:TJbaobao
 * 时间:2018/7/16  10:29
 * 说明:
 * 使用：
 */
public interface TimerTaskImp {

    void start(long delay);

    void start(long delay,long period);

    void start(long delay, RxTimerTaskListener rxTimerTaskListener);

    void start(long delay,long period,RxTimerTaskListener rxTimerTaskListener);

    void stop();

}

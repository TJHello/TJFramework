package com.tjbaobao.framework.utils;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 定时器封装类，使用简单，解决了timer有时不能停止的问题。
 * 使用方法:
 * BaseTimerTask timerTask = new BaseTimerTask();
 * timerTask.startTimer(...);//启动
 * timerTask.stopTimer();//停止
 *
 * Created by TJbaobao on 2017/8/13.
 */

@SuppressWarnings({"unused", "WeakerAccess"})
public abstract class BaseTimerTask {
    private Timer timer ;
    private MyTimerTask mTimerTask ;
    protected volatile boolean isCancel = true;

    private class MyTimerTask extends TimerTask
    {
        @Override
        public void run() {
            if(!isCancel)
            {
                BaseTimerTask.this.run();
            }
        }
    }

    public BaseTimerTask startTimer(long delay)
    {
        stopTimer();
        isCancel = false;
        timer = new Timer();
        mTimerTask = new MyTimerTask();
        timer.schedule(mTimerTask,delay);
        return this;
    }
    public BaseTimerTask startTimer()
    {
        return startTimer(0);
    }
    public BaseTimerTask startTimer(Date time)
    {
        stopTimer();
        isCancel = false;
        timer = new Timer();
        mTimerTask = new MyTimerTask();
        timer.schedule(mTimerTask,time);
        return this;
    }
    public BaseTimerTask startTimer(long delay,long period)
    {
        stopTimer();
        isCancel = false;
        timer = new Timer();
        mTimerTask = new MyTimerTask();
        timer.schedule(mTimerTask,delay,period);
        return this;
    }
    public BaseTimerTask startTimer(Date firstTime,long period)
    {
        stopTimer();
        timer = new Timer();
        mTimerTask = new MyTimerTask();
        timer.schedule(mTimerTask,firstTime,period);
        return this;
    }

    public void stopTimer()
    {
        isCancel = true;
        if(timer!=null)
        {
            timer.cancel();
            timer = null;
        }
        if(mTimerTask!=null)
        {
            mTimerTask.cancel();
            mTimerTask = null;
        }
    }
    public abstract void run();

    public void cancel()
    {
        isCancel = true;
    }
}

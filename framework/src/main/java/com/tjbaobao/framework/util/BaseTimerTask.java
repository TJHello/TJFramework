package com.tjbaobao.framework.util;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by TJbaobao on 2017/8/13.
 */

public abstract class BaseTimerTask {
    private Timer timer ;
    private MyTimerTask mTimerTask ;
    protected boolean isCancel = false;

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
        isCancel = false;
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

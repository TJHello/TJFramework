package com.tjbaobao.framework.utils;

import android.support.annotation.NonNull;

import com.tjbaobao.framework.imp.TimerTaskImp;
import com.tjbaobao.framework.listener.RxTimerTaskListener;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 作者:TJbaobao
 * 时间:2018/7/16  10:28
 * 说明:
 * 使用：
 */
@SuppressWarnings("unused")
public class RxTimerTask implements TimerTaskImp{

    private Consumer<Long> consumer ;
    private Disposable disposable ;

    @Override
    public void start(long delay) {
        if(consumer==null)
        {
            consumer = getTimeConsumer();
        }
        disposable = Observable.interval(delay, TimeUnit.MILLISECONDS).subscribe(consumer);
    }

    @Override
    public void start(long delay, long period) {
        if(consumer==null)
        {
            consumer = getTimeConsumer();
        }
        disposable = Observable.interval(delay,period, TimeUnit.MILLISECONDS).subscribe(consumer);
    }

    @Override
    public void start(long delay, RxTimerTaskListener rxTimerTaskListener) {
        this.rxTimerTaskListener = rxTimerTaskListener;
        if(consumer==null)
        {
            consumer = getTimeConsumer();
        }
        disposable = Observable.interval(delay, TimeUnit.MILLISECONDS, Schedulers.trampoline()).subscribe(consumer);
    }

    @Override
    public void start(long delay, long period, RxTimerTaskListener rxTimerTaskListener) {
        this.rxTimerTaskListener = rxTimerTaskListener;
        if(consumer==null)
        {
            consumer = getTimeConsumer();
        }
        disposable = Observable.interval(delay, TimeUnit.MILLISECONDS).subscribe(consumer);
    }

    @Override
    public void stop() {
        if(disposable!=null)
        {
            if(!disposable.isDisposed())
            {
                disposable.dispose();
            }
        }
    }

    @NonNull
    private Consumer<Long> getTimeConsumer()
    {
        return aLong -> {
            if(rxTimerTaskListener!=null)
            {
                rxTimerTaskListener.run(aLong);
//                RxJavaUtil.runOnUI(() -> {
//                    if(rxTimerTaskListener!=null)
//                    {
//                        rxTimerTaskListener.runUI(aLong);
//                    }
//                });
            }
        };
    }

    private RxTimerTaskListener rxTimerTaskListener ;
    public void setRxTimerTaskListener(RxTimerTaskListener rxTimerTaskListener) {
        this.rxTimerTaskListener = rxTimerTaskListener;
    }
}

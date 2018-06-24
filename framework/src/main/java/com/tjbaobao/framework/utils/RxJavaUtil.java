package com.tjbaobao.framework.utils;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@SuppressWarnings({"unused", "UnusedReturnValue"})
public class RxJavaUtil {

    public interface IOTask<T>{
        default T onIOThreadBack(){
            onIOThread();
            return null;
        }

        void onIOThread();
    }

    public interface ThreadTask<T>{
        default T onNewThreadBack(){
            onNewThread();
            return null;
        }
        void onNewThread();
    }

    public interface UITask<T>{
        default void onUIThread(T t){
            onUIThread();
        }
        void onUIThread();
    }

    public static class RxTask<T> implements UITask<T>,IOTask<T>,ThreadTask<T>{

        private T t ;

        @Override
        public T onIOThreadBack() {
            onIOThread();
            return null;
        }

        @Override
        public void onIOThread() {

        }

        @Override
        public T onNewThreadBack() {
            onNewThread();
            return null;
        }

        @Override
        public void onNewThread() {

        }

        @Override
        public void onUIThread(T t) {
            onUIThread();
        }

        @Override
        public void onUIThread() {

        }

        public void setT(T t)
        {
            this.t = t;
        }

        public T getT()
        {
            return t;
        }
    }

    /**
     * 在IO线程中运行
     * @param task {@link IOTask}
     * @param <T> 对象
     * @return {@link Disposable}
     */
    public static <T> Disposable runOnIOThread(final IOTask<T> task) {
        return Observable.just(task)
                .observeOn(Schedulers.io())
                .subscribe(IOTask::onIOThread);
    }

    public static <T> Disposable runOnNewThread(final ThreadTask<T> task) {
        return Observable.just(task)
                .observeOn(Schedulers.newThread())
                .subscribe(ThreadTask::onNewThread);
    }

    /**
     * 在UI线程中运行
     * @param task {@link UITask}
     * @param <T> 对象
     * @return {@link Disposable}
     */
    public static <T> Disposable runOnUI(final UITask<T> task) {
        return Observable.just(task)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(UITask::onUIThread);
    }

    /**
     * IO线程转UI线程
     * @param task {@link RxTask}
     * @param <T> 对象
     * @return {@link Disposable}
     */
    public static <T> Disposable runOnIOToUI(final RxTask<T> task) {
        return Observable.create((ObservableOnSubscribe<RxTask<T>>) emitter -> {
            T t = task.onIOThreadBack();
            task.setT(t);
            emitter.onNext(task);
            emitter.onComplete();
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(tRxTask -> tRxTask.onUIThread(tRxTask.t));
    }
}

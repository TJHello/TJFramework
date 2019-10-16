package com.tjbaobao.framework.imp;

import android.app.Activity;
import android.content.Intent;

import androidx.annotation.ColorRes;
import androidx.annotation.StringRes;

/**
 * Activity常用工具接口规范
 * Created by TJbaobao on 2018/3/8.
 */

@SuppressWarnings("unused")
public interface ActivityToolsImp {

    /**
     * 通过id获取颜色
     * @param id 颜色资源id
     * @return 颜色值
     */
    int getColorById(@ColorRes int id);

    /**
     * 通过id获取字符串
     * @param id 字符串资源id
     * @return 字符串
     */
    String getStringById(@StringRes int id);

    /**
     * 带resultCode的finish
     * @param resultCode 返回结果
     */
    void finish(int resultCode);

    /**
     * 快捷启动Activity
     * @param mClass Activity.class
     */
    void startActivity(Class<? extends Activity> mClass);

    /**
     * 快捷启动Activity并且finish当前Activity
     * @param mClass Activity.class
     */
    void startActivityAndFinish(Class<? extends Activity> mClass);

    /**
     * 快捷启动Activity并且传入参数
     * @param mClass Activity.class
     * @param keys keys
     * @param values values
     */
    void startActivity(Class<? extends Activity> mClass,String[] keys,Object ... values);

    /**
     * 快捷启动Activity并且传入参数然后finish
     * @param mClass Activity.class
     * @param names keys
     * @param values values
     */
    void startActivityAndFinish(Class<? extends Activity> mClass,String[] names,Object ... values);

    /**
     * 快捷启动Activity并且finish
     * @param intent intent
     */
     void startActivityAndFinish(Intent intent);

    /**
     * 通过ForResult方式快捷启动Activity
     * @param mClass Activity.class
     * @param requestCode 请求码
     */
    void startActivityForResult(Class<? extends Activity> mClass,int requestCode);

    /**
     * 通过ForResult方式快捷启动Activity并且携带参数
     * @param mClass Activity.class
     * @param requestCode 请求码
     * @param keys keys
     * @param values values
     */
    void startActivityForResult(Class<? extends Activity> mClass,int requestCode,String[] keys,Object ... values);

    /**
     * 获取状态栏高度
     * @return 返回高度 -1代表失败
     */
    int getBarHeight();

    /**
     * 沉浸式状态栏
     *
     * 使用需要在布局中配合增加以下参数
     * android:windowTranslucentStatus=“true"
     * android:fitsSystemWindows="true"
     *
     */
    void immersiveStatusBar();

    /**
     * 设置全屏
     */
    void fullScreen();

    /**
     * 隐藏虚拟按键以及全屏
     */
    void hideVirtualKey();

}

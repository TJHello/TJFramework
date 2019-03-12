package com.tjbaobao.framework.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import java.io.Serializable;

/**
 * Activity工具类
 * Created by TJbaobao on 2018/3/8.
 */

@SuppressWarnings("unused")
public class  ActivityTools{

    /**
     * 通过id获取颜色
     * @param id 颜色资源id
     * @return 颜色值
     */
    public static int getColorById(@Nullable Activity activity,@ColorRes int id)
    {
        if(activity==null) return -1;
        return activity.getResources().getColor(id);
    }

    /**
     * 通过id获取字符串
     * @param id 字符串资源id
     * @return 字符串
     */
    public static String getStringById(@Nullable Activity activity,@StringRes int id)
    {
        if(activity==null) return null;
        return activity.getResources().getString(id);
    }

    /**
     * 带resultCode的finish
     * @param resultCode 返回结果
     */
    public static void finish(@Nullable Activity activity, int resultCode)
    {
        if(activity==null) return ;
        activity.setResult(resultCode);
        activity.finish();
    }

    /**
     * 快捷启动Activity
     * @param mClass Activity.class
     */
    public static void startActivity(@Nullable Activity activity,Class<? extends Activity> mClass)
    {
        if(activity==null) return ;
        Intent intent = new Intent(activity,mClass);
        activity.startActivity(intent);
    }

    /**
     * 快捷启动Activity并且finish当前Activity
     * @param mClass Activity.class
     */
    public static void startActivityAndFinish(@Nullable Activity activity,Class<? extends Activity> mClass)
    {
        if(activity==null) return ;
        startActivity(activity,mClass);
        activity.finish();
    }

    /**
     * 快捷启动Activity并且传入参数
     * @param mClass Activity.class
     * @param keys keys
     * @param values values
     */
    public static void startActivity(@Nullable Activity activity,Class<? extends Activity> mClass,String[] keys,Object ... values)
    {
        if(activity==null) return ;
        Intent intent = new Intent(activity,mClass);
        int i=0;
        for(String name:keys)
        {
            Object value = values[i];
            if (value instanceof Integer) {
                intent.putExtra(name, (Integer)value);
            }
            else if(value instanceof Byte)
            {
                intent.putExtra(name, (Byte)value);
            }
            else if(value instanceof Long)
            {
                intent.putExtra(name, (Long)value);
            }
            else if(value instanceof Float)
            {
                intent.putExtra(name, (Float)value);
            }
            else if(value instanceof Integer[])
            {
                intent.putExtra(name, (Integer[])value);
            }
            else if(value instanceof Short)
            {
                intent.putExtra(name, (Short)value);
            }
            else if(value instanceof Bundle)
            {
                intent.putExtra(name, (Bundle)value);
            }
            else if(value instanceof Byte[])
            {
                intent.putExtra(name, (Byte[])value);
            }
            else if(value instanceof char[])
            {
                intent.putExtra(name, (char[])value);
            }
            else if(value instanceof Double)
            {
                intent.putExtra(name, (Double)value);
            }
            else if(value instanceof String[])
            {
                intent.putExtra(name, (String[])value);
            }
            else if(value instanceof Boolean)
            {
                intent.putExtra(name, (Boolean)value);
            }
            else if(value instanceof Float[])
            {
                intent.putExtra(name, (Float[])value);
            }
            else if(value instanceof Short[])
            {
                intent.putExtra(name, (Short[])value);
            }
            else if(value instanceof Long[])
            {
                intent.putExtra(name, (Long[])value);
            }
            else if(value instanceof Parcelable)
            {
                intent.putExtra(name, (Parcelable)value);
            }
            else if(value instanceof Parcelable[])
            {
                intent.putExtra(name, (Parcelable[])value);
            }
            else if(value instanceof CharSequence)
            {
                intent.putExtra(name, (CharSequence)value);
            }
            else if(value instanceof CharSequence[])
            {
                intent.putExtra(name, (CharSequence[])value);
            }
            else if(value instanceof Serializable)
            {
                intent.putExtra(name, (Serializable)value);
            }
            i++;
        }
        activity.startActivity(intent);
    }

    /**
     * 快捷启动Activity并且传入参数然后finish
     * @param mClass Activity.class
     * @param keys keys
     * @param values values
     */
    public static void startActivityAndFinish(@Nullable Activity activity,Class<? extends Activity> mClass,String[] keys,Object ... values)
    {
        if(activity==null) return ;
        startActivity(activity,mClass,keys,values);
        activity.finish();
    }

    /**
     * 快捷启动Activity并且finish
     * @param intent intent
     */
    public static void startActivityAndFinish(@Nullable Activity activity,Intent intent)
    {
        if(activity==null) return ;
        activity.startActivity(intent);
        activity.finish();
    }

    /**
     * 通过ForResult方式快捷启动Activity
     * @param mClass Activity.class
     * @param requestCode 请求码
     */
    public static void startActivityForResult(@Nullable Activity activity,Class<? extends Activity> mClass,int requestCode)
    {
        if(activity==null) return ;
        Intent intent = new Intent(activity,mClass);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 通过ForResult方式快捷启动Activity并且携带参数
     * @param mClass Activity.class
     * @param requestCode 请求码
     * @param keys keys
     * @param values values
     */
    public static void startActivityForResult(@Nullable Activity activity,Class<? extends Activity> mClass,int requestCode,String[] keys,Object ... values)
    {
        if(activity==null) return ;
        Intent intent = new Intent(activity,mClass);
        int i=0;
        for(String name:keys)
        {
            Object value = values[i];
            if (value instanceof Integer) {
                intent.putExtra(name, (Integer)value);
            }
            else if(value instanceof Byte)
            {
                intent.putExtra(name, (Byte)value);
            }
            else if(value instanceof Long)
            {
                intent.putExtra(name, (Long)value);
            }
            else if(value instanceof Float)
            {
                intent.putExtra(name, (Float)value);
            }
            else if(value instanceof Integer[])
            {
                intent.putExtra(name, (Integer[])value);
            }
            else if(value instanceof Short)
            {
                intent.putExtra(name, (Short)value);
            }
            else if(value instanceof Bundle)
            {
                intent.putExtra(name, (Bundle)value);
            }
            else if(value instanceof Byte[])
            {
                intent.putExtra(name, (Byte[])value);
            }
            else if(value instanceof char[])
            {
                intent.putExtra(name, (char[])value);
            }
            else if(value instanceof Double)
            {
                intent.putExtra(name, (Double)value);
            }
            else if(value instanceof String[])
            {
                intent.putExtra(name, (String[])value);
            }
            else if(value instanceof Boolean)
            {
                intent.putExtra(name, (Boolean)value);
            }
            else if(value instanceof Float[])
            {
                intent.putExtra(name, (Float[])value);
            }
            else if(value instanceof Short[])
            {
                intent.putExtra(name, (Short[])value);
            }
            else if(value instanceof Long[])
            {
                intent.putExtra(name, (Long[])value);
            }
            else if(value instanceof Parcelable)
            {
                intent.putExtra(name, (Parcelable)value);
            }
            else if(value instanceof Parcelable[])
            {
                intent.putExtra(name, (Parcelable[])value);
            }
            else if(value instanceof CharSequence)
            {
                intent.putExtra(name, (CharSequence)value);
            }
            else if(value instanceof CharSequence[])
            {
                intent.putExtra(name, (CharSequence[])value);
            }
            else if(value instanceof Serializable)
            {
                intent.putExtra(name, (Serializable)value);
            }
            i++;
        }
        activity.startActivityForResult(intent, requestCode);
    }


    /**
     * 获取状态栏高度
     * @return 返回高度 -1代表失败
     */
    public static int getBarHeight(@Nullable Activity activity)
    {
        if(activity==null) return -1;
        int statusBarHeight = -1;
        //获取status_bar_height资源的ID
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight = activity.getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }

    /**
     * 沉浸式状态栏
     *
     * 使用需要在布局中配合增加以下参数
     * android:windowTranslucentStatus=“true"
     * android:fitsSystemWindows="true"
     *
     */
    public static void immersiveStatusBar(@Nullable Activity activity)
    {
        if(activity==null) return ;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    /**
     * 设置全屏
     */
    public static void fullScreen(@Nullable Activity activity)
    {
        if(activity==null) return ;
        // 隐藏状态栏
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * 隐藏虚拟按键以及全屏
     */
    public static void hideVirtualKey(@Nullable Activity activity)
    {
        if(activity==null) return ;
        Window window = activity.getWindow();
        int uiOptions = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE|
                    //布局位于状态栏下方
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION|
                    //全屏
                    View.SYSTEM_UI_FLAG_FULLSCREEN|
                    //隐藏导航栏
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        }
        if (Build.VERSION.SDK_INT>=19){
            uiOptions |= 0x00001000;
        }else{
            uiOptions |= View.SYSTEM_UI_FLAG_LOW_PROFILE;
        }
        window.getDecorView().setSystemUiVisibility(uiOptions);
    }

}

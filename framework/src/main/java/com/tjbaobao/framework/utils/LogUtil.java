package com.tjbaobao.framework.utils;

import android.util.Log;

import com.tjbaobao.framework.BuildConfig;

/**
 * Log封装类
 * 发布时自动停止日志输出
 * Created by TJbaobao on 2018/3/8.
 */

public class LogUtil {
    private static final String TAG = "TJFramework";
    private static final int TYPE_I = 0;
    private static final int TYPE_E = 1;
    private static final int TYPE_W = 2;
    private static final int TYPE_V = 3;
    private static final int TYPE_D = 4;

    public static void i(String log)
    {
        util(log,TYPE_I);
    }
    public static void e(String log)
    {
        util(log,TYPE_E);
    }
    public static void w(String log)
    {
        util(log,TYPE_W);
    }
    public static void v(String log)
    {
        util(log,TYPE_V);
    }
    public static void d(String log)
    {
        util(log,TYPE_D);
    }

    private static void util(String msg,int type)
    {
        if(!BuildConfig.DEBUG)
        {
            return ;
        }
        int length = msg.length();
        int max = 1024;
        if(length>max)
        {
            for(int i=0;i<length;)
            {
                int j = i+max;
                if(j<=length)
                {
                    log(msg.substring(i,j),type);
                }
                else
                {
                    log(msg.substring(i,length),type);
                }
                i=j;
            }
        }
        else
        {
            log(msg,type);
        }
    }

    private static void log(String msg,int type)
    {
        String tag = TAG;
        switch (type)
        {
            case TYPE_I:
                Log.i(tag,msg);
                break;
            case TYPE_E:
                Log.e(tag,msg);
                break;
            case TYPE_W:
                Log.w(tag,msg);
                break;
            case TYPE_V:
                Log.v(tag,msg);
                break;
            case TYPE_D:
                Log.d(tag,msg);
                break;
        }
    }
}

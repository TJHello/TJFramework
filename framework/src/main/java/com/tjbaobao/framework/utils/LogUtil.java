package com.tjbaobao.framework.utils;

import android.util.Log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

/**
 * Log封装类
 * 可以通过在AndroidManifest.xml-application中配置<meta-data android:name="FW_IS_DEBUG" android:value="true" />来决定日志输出的开关
 * Created by TJbaobao on 2018/3/8.
 */

@SuppressWarnings("unused")
public class LogUtil {
    private static boolean IS_DEBUG = false;
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

    public static void exception(Exception e)
    {
        util(getStackTrace(e),TYPE_E);
    }

    private static void util(String msg,int type)
    {
        if(!IS_DEBUG)
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

    /**
     * 获取详细的异常链信息--精准定位异常位置
     *
     * @param aThrowable {@link Throwable}
     * @return String
     */
    public static String getStackTrace(Throwable aThrowable) {
        final Writer result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(result);
        aThrowable.printStackTrace(printWriter);
        return result.toString();
    }

    public static void setDebug(boolean isDebug)
    {
        IS_DEBUG = isDebug;
    }
}

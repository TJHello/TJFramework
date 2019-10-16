package com.tjbaobao.framework.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.tjbaobao.framework.base.BaseApplication;

import java.util.HashMap;
import java.util.Map;

/**
 * 字体管理器
 * 自带缓存管理，避免生成多个typeface对象，造成内存泄漏。
 * Created by TJbaobao on 2017/6/22.
 */

@SuppressWarnings({"unused", "WeakerAccess"})
public class FontManager {

    private static final Map<String,Typeface> typefaceMap = new HashMap<>();

    public static void changeFonts(@NonNull ViewGroup viewGroup,@NonNull String fontPath) {
        Typeface tf = getTypeface(fontPath);
        changeFonts(viewGroup,tf);
    }

    public static void changeFonts(@NonNull ViewGroup viewGroup,Typeface typeface)
    {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View v = viewGroup.getChildAt(i);
            if (v instanceof TextView) {
                ((TextView) v).setTypeface(typeface);
            }else if (v instanceof ViewGroup) {
                changeFonts((ViewGroup) v,typeface);
            }
        }
    }

    public static void changeFont(@NonNull View view,@NonNull String fontPath)
    {
        Typeface tf = getTypeface(fontPath);
        changeFont(view,tf);
    }

    public static void changeFont(@NonNull View view,Typeface typeface)
    {
        if (view instanceof TextView) {
            ((TextView) view).setTypeface(typeface);
        }
    }

    public static void changeFonts(@NonNull Activity activity, String fontPath)
    {
        try
        {
            Window window = activity.getWindow();
            if(window!=null)
            {
                View view = window.getDecorView() ;
                if(view instanceof ViewGroup)
                {
                    changeFonts((ViewGroup) view,fontPath);
                }
            }
        }
        catch (Exception ignored){}
    }

    public static void changeFonts(@NonNull Activity activity,Typeface typeface)
    {
        try
        {
            Window window = activity.getWindow();
            if(window!=null)
            {
                View view = window.getDecorView() ;
                if(view instanceof ViewGroup)
                {
                    view.post(() -> changeFonts((ViewGroup) view,typeface));
                }
            }
        }
        catch (Exception ignored){}
    }

    public static Typeface getTypeface(@NonNull String fontPath)
    {
        Context context = BaseApplication.getContext();
        if(context!=null)
        {
            if(typefaceMap.containsKey(fontPath)){
                return typefaceMap.get(fontPath);
            }
            Typeface typeface = Typeface.createFromAsset(context.getAssets(),fontPath);
            typefaceMap.put(fontPath,typeface);
            return typeface;
        }
        return null;
    }

}

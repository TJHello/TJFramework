package com.tjbaobao.framework.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tjbaobao.framework.base.BaseApplication;

import static com.tjbaobao.framework.base.BaseApplication.context;

/**
 * 字体管理器
 * Created by TJbaobao on 2017/6/22.
 */

@SuppressWarnings({"unused", "WeakerAccess"})
public class FontManager {

    public static void changeFonts(@NonNull ViewGroup viewGroup, String fontPath) {
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

    public static void changeFont(View view,String fontPath)
    {
        Typeface tf = getTypeface(fontPath);
        changeFont(view,tf);
    }

    public static void changeFont(View view,Typeface typeface)
    {
        if (view instanceof TextView) {
            ((TextView) view).setTypeface(typeface);
        }
    }

    public static Typeface getTypeface(String fontPath)
    {
        Context context = BaseApplication.getContext();
        if(context!=null)
        {
            return  Typeface.createFromAsset(context.getAssets(),fontPath);
        }
        return null;
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

}

package com.tjbaobao.framework.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tjbaobao.framework.base.BaseApplication;

import static com.tjbaobao.framework.base.BaseApplication.context;

/**
 * Created by TJbaobao on 2017/6/22.
 */

@SuppressWarnings({"unused", "WeakerAccess"})
public class FontManager {

    public static void changeFonts(ViewGroup viewGroup, String fontPath) {

        Typeface tf = getTypeface(fontPath);
        changeFonts(viewGroup,tf);
    }

    public static void changeFonts(ViewGroup viewGroup,Typeface typeface)
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
            Typeface tf = Typeface.createFromAsset(context.getAssets(),
                    fontPath);
            return tf;
        }
        return null;
    }

    public static void changeFonts(Activity activity,String fontPath)
    {
        changeFonts( (ViewGroup) activity.getWindow().getDecorView(),fontPath);
    }
    public static void changeFonts(Activity activity,Typeface typeface)
    {
        changeFonts( (ViewGroup) activity.getWindow().getDecorView(),typeface);
    }



}

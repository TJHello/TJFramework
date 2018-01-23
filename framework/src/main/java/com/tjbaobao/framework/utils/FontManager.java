package com.tjbaobao.framework.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by TJbaobao on 2017/6/22.
 */

public class FontManager {
    public static void changeFonts(ViewGroup root, Context act, String fontPath) {

        Typeface tf = Typeface.createFromAsset(act.getAssets(),
                fontPath);

        for (int i = 0; i < root.getChildCount(); i++) {
            View v = root.getChildAt(i);
            if (v instanceof TextView) {
                ((TextView) v).setTypeface(tf);
            } else if (v instanceof Button) {
                ((Button) v).setTypeface(tf);
            } else if (v instanceof EditText) {
                ((EditText) v).setTypeface(tf);
            } else if (v instanceof ViewGroup) {
                changeFonts((ViewGroup) v, act,fontPath);
            }
        }

    }

    public static void changeFonts(Context context,View view,String fontPath)
    {
        Typeface tf = Typeface.createFromAsset(context.getAssets(),
                fontPath);
        if (view instanceof TextView) {
            ((TextView) view).setTypeface(tf);
        } else if (view instanceof Button) {
            ((Button) view).setTypeface(tf);
        } else if ( view instanceof EditText) {
            ((EditText) view).setTypeface(tf);
        }
    }

    public static Typeface getTypeface(Context context,String fontPath)
    {
        return Typeface.createFromAsset(context.getAssets(),
                fontPath);
    }

}

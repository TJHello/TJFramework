package com.tjbaobao.framework.util;

import android.view.MotionEvent;

/**
 * Created by TJbaobao on 2017/8/21.
 */

public class TouchUtil {

    public void onTouchEvent(MotionEvent event)
    {
        int pointerCount = event.getPointerCount();

    }

    public abstract class OnTouchUtilListener
    {
        public abstract void onTouch();
    }
}

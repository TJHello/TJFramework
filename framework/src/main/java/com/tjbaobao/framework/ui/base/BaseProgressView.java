package com.tjbaobao.framework.ui.base;

import android.content.Context;
import android.util.AttributeSet;

import com.tjbaobao.framework.imp.ProgressViewImp;
import com.tjbaobao.framework.listener.OnProgressBarListener;

/**
 * 规范进度条类的组件
 */
public abstract class BaseProgressView extends BaseUI implements ProgressViewImp {

    public BaseProgressView(Context context) {
        super(context);
    }

    public BaseProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onInitView(Context context, AttributeSet attrs, int defStyleAttr) {

    }

    @Override
    public void setProgress(float progress) {

    }

    @Override
    public void setOnProgressBarListener(OnProgressBarListener onProgressBarListener) {

    }
}

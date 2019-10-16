package com.tjbaobao.framework.ui.base;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.tjbaobao.framework.listener.OnImgRecycledListener;
import com.tjbaobao.framework.utils.LogUtil;

/**
 *
 * Created by TJbaobao on 2018/3/20.
 */

@SuppressWarnings("unused")
public class BaseImageView extends AppCompatImageView {

    public BaseImageView(Context context) {
        super(context);
    }

    public BaseImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        try {
            super.onDraw(canvas);
        }catch (Exception ignored){
            LogUtil.e(ignored.getMessage());
            if(onImgRecycledListener!=null)
            {
                onImgRecycledListener.onRecycled(this);
            }
        }
    }

    private OnImgRecycledListener onImgRecycledListener ;
    public void setOnImgRecycledListener(OnImgRecycledListener onImgRecycledListener) {
        this.onImgRecycledListener = onImgRecycledListener;
    }

}

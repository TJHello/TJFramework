package com.tjbaobao.framework.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

/**
 *
 * Created by TJbaobao on 2017/9/11.
 */

public class ImageTipView extends AppCompatImageView {

    private int imageOnId =-1,imageOffId=-1 ;
    private int tipNum = 0;
    private int tipBg = Color.parseColor("#5a9e77");
    private boolean isShowTip = true;

    public ImageTipView(Context context) {
        super(context);
    }

    public ImageTipView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageTipView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private Paint paint = new Paint();
    private boolean isInitData = false;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(canvas==null)
        {
            return ;
        }
        if(!isInitData)
        {
            if(paint==null)
            {
                paint = new Paint();
            }
            paint.setAntiAlias(true);
            isInitData = true;
        }
        if(isShowTip)
        {
            int viewWidth = getWidth();
            float r = viewWidth/4f/2f;
            float leftX = viewWidth-r*3;
            float topY =  viewWidth/5f*2f;
            float textSize = tipNum>9?r*0.9f:r*2*0.8f;
            paint.setTextSize(textSize);
            paint.setColor(tipBg);
            canvas.drawCircle(leftX,topY,r,paint);
            paint.setColor(Color.WHITE);
            drawTextCenter(canvas,(int)(leftX-r),(int)(topY-r),(int)(leftX+r),(int)(topY+r),tipNum+"");
        }

    }

    private void drawTextCenter(Canvas canvas,int left,int top,int right,int bottom,String text)
    {
        Rect targetRect = new Rect(left, top, right, bottom);
        Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        int baseline = (targetRect.bottom + targetRect.top - fontMetrics.bottom - fontMetrics.top) / 2;
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(text, targetRect.centerX(), baseline, paint);
    }

    public int getImageOnId() {
        return imageOnId;
    }

    public void setImageOnId(int imageOnId) {
        this.imageOnId = imageOnId;
    }

    public int getImageOffId() {
        return imageOffId;
    }

    public void setImageOffId(int imageOffId) {
        this.imageOffId = imageOffId;
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        if(selected)
        {
            if(imageOnId!=-1)
            {
                setImageResource(imageOnId);
            }
        }
        else
        {
            if(imageOffId!=-1)
            {
                setImageResource(imageOffId);
            }
        }
    }

    public int getTipNum() {
        return tipNum;
    }

    public void setTipNum(int tipNum) {
        this.tipNum = tipNum;
        invalidate();
    }

    public boolean isShowTip() {
        return isShowTip;
    }

    public void setShowTip(boolean showTip) {
        isShowTip = showTip;
        invalidate();
    }

    public void setTipBg(@ColorInt int tipBg) {
        this.tipBg = tipBg;
    }
}

package com.tjbaobao.framework.util;


import android.graphics.PointF;

/**
 * Created by TJbaobao on 2017/8/11.
 */

public class AnimAlgorithm {

    public static PointF translationAlgorithm(float nowX, float nowY, float endX, float endY, float step)
    {
        float distanceX = endX-nowX;
        float distanceY = endY-nowY;
        float ratio = Math.abs(distanceX/distanceY);
        if(distanceX==0&&distanceY==0)
        {
            return new PointF(endX,endY);
        }
        float runY =Math.abs ((float) Math.sqrt(step*step/(1+ratio*ratio)));
        float runX = ratio*runY;
        if(distanceX>0)
        {
            nowX += runX;
        }
        else
        {
            nowX -=runX;
        }
        if(distanceY>0)
        {
            nowY +=runY;
        }
        else
        {
            nowY -=runY;
        }
        if(distanceX>0)
        {
            if(nowX>=endX)
            {
                nowX = endX;
                nowY = endY;
            }
        }
        else
        {
            if(nowX<=endX)
            {
                nowX = endX;
                nowY = endY;
            }
        }
        if(distanceY>0)
        {
            if(nowY>=endY)
            {
                nowX = endX;
                nowY = endY;
            }
        }
        else
        {
            if(nowY<=endY)
            {
                nowX = endX;
                nowY = endY;
            }
        }

        return new PointF(nowX,nowY);
    }
}

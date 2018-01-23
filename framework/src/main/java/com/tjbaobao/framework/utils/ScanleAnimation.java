package com.tjbaobao.framework.utils;

/**
 * Created by TJbaobao on 2017/7/26.
 */
public class ScanleAnimation extends  AnimArithmetic
{
    float fromX,toX,fromY,toY ;
    int repeatCout = 1,nowRepeatCout = 0;
    int moveCout = 0;
    float factor = 0.1f;
    boolean isFinish = false;
    float jumpScanle = 0.2f;
    float jumpSpeed1 = 1.0f,jumpSpeed2 = 2.0f,jumpSpeed3 = 0.6f;

    public void initialize(float fromX,float toX,float fromY,float toY)
    {
        this.fromX = fromX;
        this.toX = toX;
        this.fromY = fromY;
        this.toY = toY;
    }
    public float getScaleX(float nowScanleX)
    {
        if(isFinish)
        {
            return nowScanleX;
        }
        moveCout++;
        float absolute = Math.abs(toX-fromX);
        float moveX = absolute*factor;
        if(nowRepeatCout==0)
        {
            if(animListener!=null)
            {
                animListener.stage1();
            }
            if(toX>fromX)
            {
                if(nowScanleX<=toX+toX*jumpScanle)
                {
                    nowScanleX+=moveX*jumpSpeed1;
                }
                else
                {
                    nowRepeatCout++;
                }
            }
            else
            {
                if(nowScanleX>=toX-toX*jumpScanle)
                {
                    nowScanleX-=moveX*jumpSpeed1;
                }
                else
                {
                    nowRepeatCout++;
                }

            }
        }
        else if(nowRepeatCout==1)
        {
            if(animListener!=null)
            {
                animListener.stage2();
            }
            if(toX>fromX)
            {
                if(nowScanleX>fromX-fromX*jumpScanle)
                {
                    nowScanleX-=moveX*jumpSpeed2;
                }
                else
                {
                    nowRepeatCout++;
                }
            }
            else
            {
                if(nowScanleX<fromX+fromX*jumpScanle)
                {
                    nowScanleX+=moveX*jumpSpeed2;
                }
                else
                {
                    nowRepeatCout++;
                }
            }
        }
        else if(nowRepeatCout==2)
        {
            if(animListener!=null)
            {
                animListener.stage3();
            }
            if(toX>fromX)
            {
                if(nowScanleX<fromX)
                {
                    nowScanleX+=moveX*jumpSpeed3;
                }
                else
                {
                    nowScanleX = fromX;
                    nowRepeatCout++;
                }
            }
            else
            {
                if(nowScanleX>fromX)
                {
                    nowScanleX-=moveX*jumpSpeed3;
                }
                else
                {
                    nowScanleX = fromX;
                    nowRepeatCout++;
                }
            }
        }
        if(repeatCout*3 <=nowRepeatCout)
        {
            isFinish = true;
        }


        return nowScanleX;
    }
    public void getScaleY(float nowScanleY)
    {

    }

    public int getRepeatCout() {
        return repeatCout;
    }

    public void setRepeatCout(int repeatCout) {
        this.repeatCout = repeatCout;
    }

    public boolean isFinish() {
        return isFinish;
    }
}
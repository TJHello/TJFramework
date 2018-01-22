package com.tjbaobao.framework.entity;

/**
 * Created by TJbaobao on 2017/12/28.
 */

public class PointF{
    public float X ;
    public float Y ;

    public PointF() {
    }

    public PointF(float x, float y) {
        X = x;
        Y = y;
    }

    public void set(float x,float y)
    {
        X = x;
        Y = y;
    }

    public float getX() {
        return X;
    }

    public void setX(float x) {
        X = x;
    }

    public float getY() {
        return Y;
    }

    public void setY(float y) {
        Y = y;
    }
}

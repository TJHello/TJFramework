package com.tjbaobao.framework.entity;

/**
 *
 * Created by TJbaobao on 2017/12/28.
 */

public class PointF{
    public float x ;
    public float y ;

    public PointF() {
    }

    public PointF(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void set(float x,float y)
    {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return this.x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return this.y;
    }

    public void setY(float y) {
        this.y = y;
    }
}

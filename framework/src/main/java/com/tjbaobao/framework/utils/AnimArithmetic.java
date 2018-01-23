package com.tjbaobao.framework.utils;

/**
 * Created by TJbaobao on 2017/7/26.
 */

public class AnimArithmetic {
    public AnimArithmetic()
    {

    }

    public AnimListener animListener ;
    public interface AnimListener
    {
        public void stage1();
        public void stage2();
        public void stage3();
    }

    public AnimListener getAnimListener() {
        return animListener;
    }

    public void setAnimListener(AnimListener animListener) {
        this.animListener = animListener;
    }
}

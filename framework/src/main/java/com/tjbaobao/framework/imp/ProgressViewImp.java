package com.tjbaobao.framework.imp;

import com.tjbaobao.framework.listener.OnProgressBarListener;

public interface ProgressViewImp {

    void setProgress(float progress);

    void setOnProgressBarListener(OnProgressBarListener onProgressBarListener) ;
}

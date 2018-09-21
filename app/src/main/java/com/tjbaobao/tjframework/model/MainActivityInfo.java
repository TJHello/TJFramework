package com.tjbaobao.tjframework.model;

import android.app.Activity;

/**
 * Created by TJbaobao on 2018/2/9.
 */

public class MainActivityInfo {

    public String title;

    public String subTitle ;

    public Class<? extends Activity> activityClass ;

    public String name;

    public MainActivityInfo(String title,String subTitle,Class<? extends Activity> activityClass) {
        this.title = title;
        this.activityClass = activityClass;
        this.name = activityClass.getSimpleName();
        this.subTitle = subTitle;
    }
}

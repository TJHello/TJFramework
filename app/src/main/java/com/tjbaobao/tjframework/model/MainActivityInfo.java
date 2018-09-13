package com.tjbaobao.tjframework.model;

import android.app.Activity;

/**
 * Created by TJbaobao on 2018/2/9.
 */

public class MainActivityInfo {

    public String description;

    public Class<? extends Activity> activityClass ;

    public String name;

    public MainActivityInfo(String description,Class<? extends Activity> activityClass) {
        this.description = description;
        this.activityClass = activityClass;
        this.name = activityClass.getSimpleName();
    }
}

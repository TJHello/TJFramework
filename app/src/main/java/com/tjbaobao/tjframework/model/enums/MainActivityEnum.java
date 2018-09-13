package com.tjbaobao.tjframework.model.enums;

import android.app.Activity;

import com.tjbaobao.tjframework.activity.example.TJActivityExampleActivity;

/**
 * 作者:TJbaobao
 * 时间:2018/9/13  11:39
 * 说明:
 * 使用：
 */
public enum MainActivityEnum {

    TJActivity(TJActivityExampleActivity.class,"TJActivity");

    public Class<? extends Activity> activityClass ;
    public String description ;

    MainActivityEnum(Class<? extends Activity> activityClass,String description) {
        this.activityClass = activityClass;
        this.description = description;
    }
}

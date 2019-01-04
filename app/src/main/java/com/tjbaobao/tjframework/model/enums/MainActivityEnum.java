package com.tjbaobao.tjframework.model.enums;

import android.app.Activity;

import com.tjbaobao.tjframework.activity.example.*;

/**
 * 作者:TJbaobao
 * 时间:2018/9/13  11:39
 * 说明:
 * 使用：
 */
public enum MainActivityEnum {

    TJActivity1(TJActivityExample1Activity.class,"TJActivity1","普通列表"),
    TJActivity2(TJActivityExample2Activity.class,"TJActivity2","高级列表"),
    TJActivity3(TJActivityExample3Activity.class,"TJActivity3","快速列表"),
    TJDialog1(TJDialogExample1Activity.class,"TJDialog","自定义弹窗"),
    DataBaseHelper(DataBaseExampleActivity.class,"DataBaseHelper","数据库解决方案"),
    ;

    public Class<? extends Activity> activityClass ;
    public String title;
    public String subTitle ;

    MainActivityEnum(Class<? extends Activity> activityClass,String title,String subTitle) {
        this.activityClass = activityClass;
        this.title = title;
        this.subTitle = subTitle;
    }
}

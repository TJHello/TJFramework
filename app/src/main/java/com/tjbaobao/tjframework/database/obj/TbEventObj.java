package com.tjbaobao.tjframework.database.obj;

/**
 * 作者:TJbaobao
 * 时间:2018/9/25  11:04
 * 说明:
 * 使用：
 */
public class TbEventObj {

    public static final String EVENT_START_ACTIVITY = "startActivity";

    public static final String EVENT_TEST = "test";

    private String code ;
    private String name ;
    private String path ;
    private String event ;
    private String createTime ;
    private String changeTime ;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getChangeTime() {
        return changeTime;
    }

    public void setChangeTime(String changeTime) {
        this.changeTime = changeTime;
    }
}

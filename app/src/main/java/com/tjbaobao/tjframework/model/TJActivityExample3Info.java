package com.tjbaobao.tjframework.model;

import com.tjbaobao.framework.entity.base.BaseListInfo;
import com.tjbaobao.tjframework.model.enums.TJActivityExample3Enum;

/**
 * 作者:TJbaobao
 * 时间:2019/1/3  11:57
 * 说明:
 * 使用：
 */
public class TJActivityExample3Info extends BaseListInfo {

    public static final int TITLE_SWITCH = 0;
    public static final int TITLE_MENU = 1;
    public static final int TITLE_NONE = 2;
    public static final int LIST_MUSIC = 3;
    public static final int LIST_ANIM = 4;
    public static final int LAYOUT_TUTORIAL = 5;
    public static final int MARGIN = 6;

    public TJActivityExample3Enum example3Enum ;
    public boolean isSelect ;

    public TJActivityExample3Info(int type) {
        setSpanSize(1);
        setType(type);
    }
}

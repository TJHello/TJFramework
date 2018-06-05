package com.tjbaobao.framework.imp;

import android.database.sqlite.SQLiteDatabase;

/**
 *
 * Created by TJbaobao on 2018/3/23.
 */

public interface DataBaseImp {

    /**
     * 数据库第一次创建
     * @param db db
     */
    void onCreate(SQLiteDatabase db);

    /**
     * 数据库版本更新
     * @param db db
     * @param oldVersion 旧的版本号
     * @param newVersion 新的版本号
     */
    void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion);
}

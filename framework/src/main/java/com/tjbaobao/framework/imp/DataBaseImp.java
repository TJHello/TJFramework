package com.tjbaobao.framework.imp;

import android.database.sqlite.SQLiteDatabase;

/**
 *
 * Created by TJbaobao on 2018/3/23.
 */

public interface DataBaseImp {

    void onCreate(SQLiteDatabase db);

    void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion);
}

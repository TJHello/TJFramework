package com.tjbaobao.framework.database.dao;

import android.content.ContentValues;
import android.database.Cursor;

import com.tjbaobao.framework.base.BaseApplication;
import com.tjbaobao.framework.database.DataBaseHelper;
import com.tjbaobao.framework.utils.LogUtil;

/**
 * Created by TJbaobao on 2017/9/12.
 */

@SuppressWarnings({"WeakerAccess", "unused"})
public class TbBaseDAO {
    private static DataBaseHelper mDataBaseHelper = null;

    static{
        create();
    }

    public static DataBaseHelper create()
    {
        if(mDataBaseHelper==null)
        {
            try {
                mDataBaseHelper = DataBaseHelper.create(BaseApplication.getContext());
            } catch (Exception e) {
                LogUtil.e(e.getMessage());
            }
        }
        return mDataBaseHelper;
    }

    protected static Cursor query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit)
    {
        return DataBaseHelper.query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
    }
    public static Cursor query(String table, String[] columns, String selection, String[] selectionArgs)
    {
        return DataBaseHelper.query(table, columns, selection, selectionArgs,null,null,null,null);
    }
    protected static Cursor rawQuery(String sql, String[] selectionArgs)
    {
        return DataBaseHelper.rawQuery(sql, selectionArgs);
    }
    protected static long insert(String tbName, String nullColumnHack, ContentValues cValue)
    {
        return DataBaseHelper.insert(tbName, nullColumnHack, cValue);
    }

    protected static int  update(String table, ContentValues values, String whereClause, String[] whereArgs)
    {
        return create().getWritableDatabase().update(table, values, whereClause, whereArgs);
    }
    protected static void execSQL(String sql)
    {
         create().getWritableDatabase().execSQL(sql);
    }

    protected static Cursor rawQuery(String sql)
    {
        return DataBaseHelper.rawQuery(sql, null);
    }

    protected static long delete(String table, String whereClause, String[] whereArgs)
    {
        return create().getWritableDatabase().delete(table, whereClause, whereArgs);
    }

    protected static int getIntByColumn(Cursor cursor, String column)
    {
        return cursor.getInt(cursor.getColumnIndex(column));
    }
    protected static String getStringByColumn(Cursor cursor, String column)
    {
        return cursor.getString(cursor.getColumnIndex(column));
    }
    protected static float getFloatByColumn(Cursor cursor, String column)
    {
        return cursor.getFloat(cursor.getColumnIndex(column));
    }
    protected static byte[] getBlobByColumn(Cursor cursor, String column)
    {
        return cursor.getBlob(cursor.getColumnIndex(column));
    }
    protected static long getLongByColumn(Cursor cursor,String column)
    {
        return cursor.getLong(cursor.getColumnIndex(column));
    }


    public static void close()
    {
        create().close();
        mDataBaseHelper= null;
    }
}

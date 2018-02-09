package com.tjbaobao.framework.database.dao;

import android.content.ContentValues;
import android.database.Cursor;

import com.tjbaobao.framework.base.BaseApplication;
import com.tjbaobao.framework.database.BaseDataBaseHelper;

/**
 * Created by TJbaobao on 2017/9/12.
 */

public class TbBaseDAO {
    private static BaseDataBaseHelper mDataBaseHelper = null;

    private static BaseDataBaseHelper create()
    {
        if(mDataBaseHelper==null)
        {
            try {
                mDataBaseHelper = BaseDataBaseHelper.create(BaseApplication.getContext());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return mDataBaseHelper;
    }

    protected static Cursor query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit)
    {
        return create().query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
    }
    public static Cursor query(String table, String[] columns, String selection, String[] selectionArgs)
    {
        return create().query(table, columns, selection, selectionArgs,null,null,null,null);
    }
    protected static Cursor rawQuery(String sql, String[] selectionArgs)
    {
        return create().rawQuery(sql, selectionArgs);
    }
    protected static long insert(String tbName, String nullColumnHack, ContentValues cValue)
    {
        return create().insert(tbName, nullColumnHack, cValue);
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
        return create().rawQuery(sql, null);
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


    public static void close()
    {
        create().close();
        mDataBaseHelper= null;
    }
}

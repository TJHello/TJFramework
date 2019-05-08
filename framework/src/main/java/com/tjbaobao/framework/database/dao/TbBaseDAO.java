package com.tjbaobao.framework.database.dao;

import android.content.ContentValues;
import android.database.Cursor;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.tjbaobao.framework.base.BaseApplication;
import com.tjbaobao.framework.database.DataSet;
import com.tjbaobao.framework.database.TJDataBaseHelper;
import com.tjbaobao.framework.utils.LogUtil;

import java.util.List;

/**
 * Created by TJbaobao on 2017/9/12.
 */

@SuppressWarnings({"WeakerAccess", "unused"})
public class TbBaseDAO {
    private static TJDataBaseHelper mDataBaseHelper = null;

    static{
        create();
    }

    public static TJDataBaseHelper create()
    {
        if(mDataBaseHelper==null)
        {
            try {
                mDataBaseHelper = TJDataBaseHelper.create(BaseApplication.getContext());
            } catch (Exception e) {
                LogUtil.e(e.getMessage());
            }
        }
        return mDataBaseHelper;
    }

    protected static Cursor queryCursor(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit)
    {
        return TJDataBaseHelper.queryCursor(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
    }

    public static Cursor queryCursor(String table, String[] columns, String selection, String[] selectionArgs)
    {
        return TJDataBaseHelper.queryCursor(table, columns, selection, selectionArgs,null,null,null,null);
    }

    protected static Cursor rawQueryCursor(String sql, String[] selectionArgs)
    {
        return TJDataBaseHelper.rawQueryCursor(sql, selectionArgs);
    }

    protected static Cursor rawQueryCursor(String sql)
    {
        return TJDataBaseHelper.rawQueryCursor(sql, null);
    }

    protected static long insert(String tbName, String nullColumnHack, ContentValues cValue)
    {
        return TJDataBaseHelper.insert(tbName, nullColumnHack, cValue);
    }

    protected static void insertTransaction(@Nullable String tbName, @Nullable String nullColumnHack, @NonNull List<ContentValues> cValueList){
        TJDataBaseHelper.insertTransaction(tbName,nullColumnHack,cValueList);
    }

    protected static int  update(String table, ContentValues values, String whereClause, String[] whereArgs)
    {
        return TJDataBaseHelper.update(table, values, whereClause, whereArgs);
    }

    protected static void execSQL(String sql)
    {
        TJDataBaseHelper.execSQL(sql);
    }

    protected static List<DataSet> rawQuery(String sql)
    {
        return TJDataBaseHelper.rawQuery(sql, null);
    }

    protected static List<DataSet> query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit)
    {
        return TJDataBaseHelper.query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
    }

    public static List<DataSet> query(String table, String[] columns, String selection, String[] selectionArgs)
    {
        return TJDataBaseHelper.query(table, columns, selection, selectionArgs,null,null,null,null);
    }

    protected static long delete(String table, String whereClause, String[] whereArgs)
    {
        return TJDataBaseHelper.delete(table, whereClause, whereArgs);
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

package com.tjbaobao.framework.database.dao;

import android.content.ContentValues;
import android.database.Cursor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tjbaobao.framework.base.BaseApplication;
import com.tjbaobao.framework.database.DataSet;
import com.tjbaobao.framework.database.TJDataBaseHelper;
import com.tjbaobao.framework.utils.LogUtil;

import java.util.List;

/**
 * Created by TJbaobao on 2017/9/12.
 */

@SuppressWarnings({"WeakerAccess", "unused", "SameParameterValue"})
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

    protected static Cursor queryCursor(@NonNull String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit)
    {
        return TJDataBaseHelper.queryCursor(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
    }

    public static Cursor queryCursor(@NonNull String table, String[] columns, String selection, String[] selectionArgs)
    {
        return TJDataBaseHelper.queryCursor(table, columns, selection, selectionArgs,null,null,null,null);
    }

    protected static Cursor rawQueryCursor(@NonNull String sql,@Nullable String[] selectionArgs)
    {
        return TJDataBaseHelper.rawQueryCursor(sql, selectionArgs);
    }

    protected static Cursor rawQueryCursor(@NonNull String sql)
    {
        return TJDataBaseHelper.rawQueryCursor(sql, null);
    }

    protected static long insert(@NonNull String tbName, String nullColumnHack, ContentValues cValue)
    {
        return TJDataBaseHelper.insert(tbName, nullColumnHack, cValue);
    }

    protected static void insertTransaction(@NonNull String tbName, @Nullable String nullColumnHack, @NonNull List<ContentValues> cValueList){
        TJDataBaseHelper.insertTransaction(tbName,nullColumnHack,cValueList);
    }

    protected static int  update(@NonNull String table,@NonNull ContentValues values, String whereClause, String[] whereArgs)
    {
        return TJDataBaseHelper.update(table, values, whereClause, whereArgs);
    }

    protected static void execSQL(@NonNull String sql)
    {
        TJDataBaseHelper.execSQL(sql);
    }

    protected static List<DataSet> rawQuery(@NonNull String sql)
    {
        return TJDataBaseHelper.rawQuery(sql, null);
    }

    protected static List<DataSet> query(@NonNull String table,@NonNull String[] columns
            ,@Nullable String selection,@Nullable String[] selectionArgs,@Nullable String groupBy
            ,@Nullable String having,@Nullable String orderBy,@Nullable String limit)
    {
        return TJDataBaseHelper.query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
    }

    public static List<DataSet> query(@NonNull String table,@Nullable String[] columns,@Nullable String selection,@Nullable String[] selectionArgs)
    {
        return TJDataBaseHelper.query(table, columns, selection, selectionArgs,null,null,null,null);
    }

    protected static long delete(@NonNull String table,@Nullable String whereClause,@Nullable String[] whereArgs)
    {
        return TJDataBaseHelper.delete(table, whereClause, whereArgs);
    }

    protected static int getIntByColumn(@NonNull Cursor cursor,@NonNull String column)
    {
        int index = cursor.getColumnIndex(column);
        if(index>=0){
            return cursor.getInt(index);
        }
        return 0;
    }
    @Nullable
    protected static String getStringByColumn(@NonNull Cursor cursor,@NonNull String column)
    {
        int index = cursor.getColumnIndex(column);
        if(index>=0){
            return cursor.getString(index);
        }
        return null;
    }
    protected static float getFloatByColumn(@NonNull Cursor cursor,@NonNull String column)
    {
        int index = cursor.getColumnIndex(column);
        if(index>=0){
            return cursor.getFloat(index);
        }
        return 0f;
    }
    @Nullable
    protected static byte[] getBlobByColumn(@NonNull Cursor cursor,@NonNull String column)
    {
        int index = cursor.getColumnIndex(column);
        if(index>=0){
            return cursor.getBlob(index);
        }
        return null;
    }
    protected static long getLongByColumn(@NonNull Cursor cursor,@NonNull String column)
    {
        int index = cursor.getColumnIndex(column);
        if(index>=0){
            return cursor.getLong(index);
        }
        return 0L;
    }

    public static void close()
    {
        create().close();
        mDataBaseHelper= null;
    }
}

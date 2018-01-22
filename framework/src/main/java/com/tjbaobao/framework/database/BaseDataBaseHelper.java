package com.tjbaobao.framework.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.tjbaobao.framework.util.DeviceUtil;

/**
 * Created by TJbaobao on 2017/9/12.
 */

public class BaseDataBaseHelper extends SQLiteOpenHelper {
    private static BaseDataBaseHelper mDataBaseHelper = null ;
    public static BaseDataBaseHelper create(Context context)
    {
        if(mDataBaseHelper==null)
        {
            ApplicationInfo appInfo = null;
            try {
                appInfo = context.getPackageManager()
                        .getApplicationInfo(DeviceUtil.getPackageName(),
                                PackageManager.GET_META_DATA);
                String dbName=appInfo.metaData.getString("database_name");
                int dbVersion = appInfo.metaData.getInt("database_version",1);
                mDataBaseHelper = new BaseDataBaseHelper(context,dbName,null,dbVersion);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return mDataBaseHelper;
    }



    public BaseDataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public BaseDataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        onCreateFileTb(db);
    }

    public void onCreateFileTb(SQLiteDatabase db)
    {
        StringBuilder sbSQL = new StringBuilder();
        sbSQL.append("create table tb_file ");
        sbSQL.append("( ");
        sbSQL.append("code varchar(36) ,");//唯一标识
        sbSQL.append("url varchar(255),");//下载链接
        sbSQL.append("path varchar(255),");//文件路径
        sbSQL.append("prefix varchar(8) ,");//后缀
        sbSQL.append("create_time varchar(19),");//创建时间
        sbSQL.append("change_time varchar(19)");//修改时间
        sbSQL.append(");");
        db.execSQL(sbSQL.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void execSQL(String sql)
    {
        if(mDataBaseHelper!=null)
        {
            getReadableDatabase().execSQL(sql);
        }
    }

    public  static Cursor query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit)
    {
        if(mDataBaseHelper!=null)
        {
            return mDataBaseHelper.getReadableDatabase().query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
        }
        return null;
    }
    public Cursor query(String table, String[] columns, String selection, String[] selectionArgs)
    {
        if(mDataBaseHelper!=null)
        {
            return getReadableDatabase().query(table, columns, selection, selectionArgs,null,null,null,null);
        }
       return null;
    }

    public static Cursor rawQuery(String sql, String[] selectionArgs)
    {
        if(mDataBaseHelper!=null)
        {
            return mDataBaseHelper.getReadableDatabase().rawQuery(sql, selectionArgs);
        }
        return null;
    }

    public static long insert(String tbName, String nullColumnHack, ContentValues cValue)
    {
        if(mDataBaseHelper!=null)
        {
            return mDataBaseHelper.getReadableDatabase().insert(tbName,nullColumnHack,cValue);
        }
        return 0;
    }

    public void close()
    {
        if(mDataBaseHelper!=null)
        {
            getReadableDatabase().close();
        }
        mDataBaseHelper = null;
    }
}

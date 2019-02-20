package com.tjbaobao.framework.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.tjbaobao.framework.imp.DataBaseImp;
import com.tjbaobao.framework.utils.DeviceUtil;

import java.util.List;

import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;

/**
 * 数据库管理
 * Created by TJbaobao on 2017/9/12.
 *
 * 使用方法：
 * //在AndroidManifest-Application中增加
 * <meta-data android:name="database_name" android:value="TjFramework" />//数据库名称
 * <meta-data android:name="database_version" android:value="1"/>//数据库版本
 *
 * 在自己的Application-onCreate()里面为BaseDataBaseHelper设置监听器。
 * setDataBaseImp然后在对应的地方创建表以及做数据库更新的处理即可
 *
 *
 */

public class DataBaseHelper extends SQLiteOpenHelper {

    private static DataBaseImp dataBaseImp ;
    private static DataBaseHelper mDataBaseHelper = null ;
    public static DataBaseHelper create(Context context) throws Exception {
        if(mDataBaseHelper==null)
        {
            ApplicationInfo appInfo;
            try {
                appInfo = context.getPackageManager()
                        .getApplicationInfo(DeviceUtil.getPackageName(),
                                PackageManager.GET_META_DATA);
                if(appInfo.metaData!=null)
                {
                    String dbName=appInfo.metaData.getString("database_name",null);
                    int dbVersion = appInfo.metaData.getInt("database_version",1);
                    if(dbName!=null)
                    {
                        mDataBaseHelper = new DataBaseHelper(context,dbName,null,dbVersion);
                    }
                    else
                    {
                        throw new Exception("未配置database_name和database_version");
                    }
                }
                else
                {
                    throw new Exception("未配置database_name和database_version");
                }
            } catch (PackageManager.NameNotFoundException e) {
                throw new Exception("未配置database_name和database_version");
            }
        }
        return mDataBaseHelper;
    }

    private DataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        onCreateFileTb(db);
        if(dataBaseImp!=null)
        {
            dataBaseImp.onCreate(db);
        }
    }

    private void onCreateFileTb(SQLiteDatabase db)
    {
        String sbSQL = "create table tb_file " +
                "( " +
                "code varchar(36) ," +//唯一标识
                "url varchar(255)," +//下载链接
                "path varchar(255)," +//文件路径
                "prefix varchar(8) ," +//后缀
                "create_time varchar(19)," +//创建时间
                "change_time varchar(19)" +//修改时间
                ");";
        db.execSQL(sbSQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(dataBaseImp!=null)
        {
            dataBaseImp.onUpgrade(db,oldVersion,newVersion);
        }
    }

    /**
     * 运行SQL语句
     * @param sql sql
     */
    public void execSQL(String sql)
    {
        if(mDataBaseHelper!=null)
        {
            getReadableDatabase().execSQL(sql);
        }
    }

    public static Cursor query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit)
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

    /**
     * 通过事务插入数据
     * @param tbName 表名
     * @param nullColumnHack 插入字段 可为null,null代表全部字段
     * @param cValueList 插入values
     */
    public static void insertTransaction(@NonNull String tbName, @Nullable String nullColumnHack, @NonNull List<ContentValues> cValueList)
    {
        if(mDataBaseHelper!=null)
        {
            SQLiteDatabase db = mDataBaseHelper.getWritableDatabase();
            db.beginTransaction();
            for(ContentValues contentValues:cValueList)
            {
                db.insert(tbName,nullColumnHack,contentValues);
            }
            db.setTransactionSuccessful();
        }
    }

    public void close()
    {
        if(mDataBaseHelper!=null)
        {
            getReadableDatabase().close();
        }
        mDataBaseHelper = null;
    }

    public static DataBaseImp getDataBaseImp() {
        return dataBaseImp;
    }

    public static void setDataBaseImp(DataBaseImp dataBaseImp) {
        DataBaseHelper.dataBaseImp = dataBaseImp;
    }
}

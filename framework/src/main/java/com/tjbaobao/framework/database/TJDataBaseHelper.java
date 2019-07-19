package com.tjbaobao.framework.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.tjbaobao.framework.imp.DataBaseImp;
import com.tjbaobao.framework.utils.DeviceUtil;
import com.tjbaobao.framework.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者:TJbaobao
 * 时间:2019/3/12  16:49
 * 说明:
 * 使用：
 */
@SuppressWarnings("WeakerAccess")
public class TJDataBaseHelper extends SQLiteOpenHelper {

    private static DataBaseImp dataBaseImp ;
    private static TJDataBaseHelper mDataBaseHelper = null ;

    public TJDataBaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public TJDataBaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version, @Nullable DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    public static TJDataBaseHelper create(Context context) throws Exception {
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
                        mDataBaseHelper = new TJDataBaseHelper(context,dbName,null,dbVersion);
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

    @Override
    public void onCreate(SQLiteDatabase db) {
        onCreateFileTb(db);
        if(dataBaseImp!=null) {
            dataBaseImp.onCreate(db);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(dataBaseImp!=null)
        {
            dataBaseImp.onUpgrade(db,oldVersion,newVersion);
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

    public static void execSQL(String sql){
        if(mDataBaseHelper!=null) {
            mDataBaseHelper.getDatabase().execSQL(sql);
        }
    }

    public static List<DataSet> query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit)
    {
        synchronized (lock){
            if(mDataBaseHelper!=null) {
                try (Cursor cursor = mDataBaseHelper.getDatabase()
                        .query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit)) {
                    return toDataSet(cursor);
                } catch (Exception e) {
                    LogUtil.exception(e);
                }
            }
            return null;
        }
    }

    public static List<DataSet> query(String table, String[] columns, String selection, String[] selectionArgs)
    {
        synchronized (lock){
            if(mDataBaseHelper!=null)
            {
                try(Cursor cursor = mDataBaseHelper.getDatabase().query(table, columns, selection, selectionArgs,null,null,null,null)){
                    return toDataSet(cursor);
                } catch (Exception e) {
                    LogUtil.exception(e);
                }
            }
            return null;
        }
    }

    public static List<DataSet> rawQuery(String sql, String[] selectionArgs)
    {
        synchronized (lock){
            if(mDataBaseHelper!=null)
            {
                try(Cursor cursor = mDataBaseHelper.getDatabase().rawQuery(sql, selectionArgs)) {
                    return toDataSet(cursor);
                } catch (Exception e) {
                    LogUtil.exception(e);
                }
            }
            return null;
        }
    }

    public static List<DataSet> rawQuery(String sql)
    {
        synchronized (lock){
            if(mDataBaseHelper!=null)
            {
                try(Cursor cursor = mDataBaseHelper.getDatabase().rawQuery(sql, null)) {
                    return toDataSet(cursor);
                } catch (Exception e) {
                    LogUtil.exception(e);
                }
            }
            return null;
        }
    }

    public static int update(String table, ContentValues values, String whereClause, String[] whereArgs) {
        if(mDataBaseHelper!=null)
        {
            return mDataBaseHelper.getDatabase().update(table, values,whereClause,whereArgs);
        }
        return -1;
    }

    public static Cursor queryCursor(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit)
    {
        if(mDataBaseHelper!=null) {
            return  mDataBaseHelper.getDatabase()
                    .query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
        }
        return null;
    }

    public static Cursor queryCursor(String table, String[] columns, String selection, String[] selectionArgs)
    {
        if(mDataBaseHelper!=null)
        {
            return mDataBaseHelper.getDatabase().query(table, columns, selection, selectionArgs,null,null,null,null);
        }
        return null;
    }

    public static Cursor rawQueryCursor(String sql, String[] selectionArgs)
    {
        if(mDataBaseHelper!=null)
        {
            return mDataBaseHelper.getDatabase().rawQuery(sql, selectionArgs);
        }
        return null;
    }

    public static Cursor rawQueryCursor(String sql)
    {
        if(mDataBaseHelper!=null)
        {
            return mDataBaseHelper.getDatabase().rawQuery(sql, null);
        }
        return null;
    }

    public static long insert(String tbName, String nullColumnHack, ContentValues cValue)
    {
        if(mDataBaseHelper!=null)
        {
            return mDataBaseHelper.getDatabase().insert(tbName,nullColumnHack,cValue);
        }
        return 0;
    }

    public static void insertTransaction(@NonNull String tbName, @Nullable String nullColumnHack, @NonNull List<ContentValues> cValueList)
    {
        if(mDataBaseHelper!=null)
        {
            SQLiteDatabase db = mDataBaseHelper.getDatabase();
            db.beginTransaction();
            try{
                for(ContentValues contentValues:cValueList)
                {
                    db.insert(tbName,nullColumnHack,contentValues);
                }
            }finally {
                db.setTransactionSuccessful();
                db.endTransaction();
            }
        }
    }

    public static long delete(String table, String whereClause, String[] whereArgs){
        if(mDataBaseHelper!=null){
            return mDataBaseHelper.getDatabase().delete(table,whereClause,whereArgs);
        }
        return -1;
    }

    private static List<DataSet> toDataSet(@NonNull Cursor cursor){
        List<DataSet> dataSetList = new ArrayList<>();
        while (cursor.moveToNext()){
            DataSet dataSet = new DataSet();
            int columnIndex = 0;
            for(String name: cursor.getColumnNames()){
                int type = cursor.getType(columnIndex);
                switch (type){
                    case Cursor.FIELD_TYPE_NULL:{
                        dataSet.set(name,null);
                    }
                    case Cursor.FIELD_TYPE_BLOB:{
                        dataSet.setBytes(name,cursor.getBlob(columnIndex));
                    }
                    case Cursor.FIELD_TYPE_FLOAT:{
                        dataSet.setFloat(name,cursor.getFloat(columnIndex));
                    }
                    case Cursor.FIELD_TYPE_INTEGER:{
                        dataSet.setInt(name,cursor.getInt(columnIndex));
                    }
                    case Cursor.FIELD_TYPE_STRING:{
                        dataSet.setString(name,cursor.getString(columnIndex));
                    }
                }
                columnIndex++;
            }
            dataSetList.add(dataSet);
        }
        return dataSetList;
    }

    private static final byte[] lock = new byte[0];

    public SQLiteDatabase getDatabase(){
        synchronized (lock){
            return getWritableDatabase();
        }
    }

    public static void setDataBaseImp(DataBaseImp dataBaseImp) {
        TJDataBaseHelper.dataBaseImp = dataBaseImp;
    }

    public static void destroy(){
        synchronized (lock){
            if(mDataBaseHelper!=null){
                mDataBaseHelper.getWritableDatabase().close();
            }
        }
    }

}

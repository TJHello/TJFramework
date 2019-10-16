package com.tjbaobao.tjframework.database.dao;

import android.content.ContentValues;
import android.database.Cursor;
import androidx.annotation.Nullable;

import com.tjbaobao.framework.database.DataSet;
import com.tjbaobao.framework.database.dao.TbBaseDAO;
import com.tjbaobao.framework.utils.DateTimeUtil;
import com.tjbaobao.tjframework.database.obj.TbEventObj;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者:TJbaobao
 * 时间:2018/9/25  11:05
 * 说明:
 * 使用：
 */
public class TbEventDAO extends TbBaseDAO {

    private static final String tbName = "tb_event";

    //region======================常规方法======================

    @Nullable
    private static List<TbEventObj> getEvents(String sql)
    {
        List<TbEventObj> list = null;
        Cursor cursor = rawQueryCursor(sql);
        if(cursor!=null)
        {
            try{
                list = new ArrayList<>();
                while (cursor.moveToNext())
                {
                    TbEventObj obj = new TbEventObj();
                    obj.setCode(getStringByColumn(cursor,"code"));
                    obj.setName(getStringByColumn(cursor,"name"));
                    obj.setPath(getStringByColumn(cursor,"path"));
                    obj.setEvent(getStringByColumn(cursor,"event"));
                    obj.setCreateTime(getStringByColumn(cursor,"create_time"));
                    obj.setChangeTime(getStringByColumn(cursor,"change_time"));
                    list.add(obj);
                }
            }finally {
                cursor.close();
            }
        }
        return list;
    }

    @Nullable
    private static List<TbEventObj> getEventsNew(String sql)
    {
        List<TbEventObj> list = null;
        List<DataSet> dataSetList = rawQuery(sql);
        if(dataSetList!=null)
        {
            list = new ArrayList<>();
            for(DataSet dataSet : dataSetList){
                TbEventObj obj = new TbEventObj();
                obj.setCode(dataSet.getString("code"));
                obj.setName(dataSet.getString("name"));
                obj.setPath(dataSet.getString("path"));
                obj.setEvent(dataSet.getString("event"));
                obj.setCreateTime(dataSet.getString("create_time"));
                obj.setChangeTime(dataSet.getString("change_time"));
                list.add(obj);
            }
        }
        return list;
    }

    @Nullable
    public static List<TbEventObj> getDataList()
    {
        String sql = "Select * From "+tbName ;
        return getEvents(sql);
    }

    @Nullable
    public static List<TbEventObj> getDataListNew()
    {
        String sql = "Select * From "+tbName ;
        return getEventsNew(sql);
    }

    @Nullable
    public static TbEventObj getData(String code)
    {
        String sql = "Select * From "+tbName +" Where `code`='"+code+"'";
        return getNo1(sql);
    }

    public static long add(TbEventObj obj)
    {
        ContentValues values = new ContentValues();
        values.put("code", obj.getCode());
        values.put("name",obj.getName());
        values.put("path",obj.getPath());
        values.put("event",obj.getEvent());
        values.put("create_time", DateTimeUtil.getNowMsTime());
        values.put("change_time", DateTimeUtil.getNowMsTime());
        return insert(tbName,null,values);
    }

    public static void addTransaction(List<TbEventObj> objList){
        List<ContentValues> contentValuesList = new ArrayList<>();
        for(TbEventObj obj : objList){
            ContentValues values = new ContentValues();
            values.put("code", obj.getCode());
            values.put("name",obj.getName());
            values.put("path",obj.getPath());
            values.put("event",obj.getEvent());
            values.put("create_time", DateTimeUtil.getNowMsTime());
            values.put("change_time", DateTimeUtil.getNowMsTime());
            contentValuesList.add(values);
        }
        insertTransaction(tbName,null,contentValuesList);
    }

    public static void delAll(){
        delete(tbName,null,null);
    }

    public static long change(TbEventObj obj)
    {
        ContentValues values = new ContentValues();
        values.put("name",obj.getName());
        values.put("path",obj.getPath());
        values.put("event",obj.getEvent());
        values.put("create_time", obj.getCreateTime());
        values.put("change_time", DateTimeUtil.getNowMsTime());
        return update(tbName,values,"code=?",new String[]{obj.getCode()});
    }

    @Nullable
    private static TbEventObj getNo1(String sql)
    {
        List<TbEventObj> list = getEvents(sql);
        if(list!=null)
        {
            if(list.size()>0)
            {
                return list.get(0);
            }
        }
        return null;
    }

    //endregion

    //region======================自定义方法======================

    @Nullable
    public static List<TbEventObj> getDataListByName(String name)
    {
        String sql = "Select * From "+tbName +" Where `name`='"+name+"'";
        return getEvents(sql);
    }

    //endregion
}

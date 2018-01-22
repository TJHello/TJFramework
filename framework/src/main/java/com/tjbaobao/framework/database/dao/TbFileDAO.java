package com.tjbaobao.framework.database.dao;

import android.content.ContentValues;
import android.database.Cursor;

import com.tjbaobao.framework.database.obj.TbFileObj;
import com.tjbaobao.framework.util.DateTimeUtil;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by TJbaobao on 2017/10/25.
 */

public class TbFileDAO extends TbBaseDAO {
    private static final String tbName = "tb_file";
    private static ArrayList<TbFileObj> getFiles(String sql)
    {
        ArrayList<TbFileObj> files = null;
        Cursor cursor = rawQuery(sql);
        if(cursor!=null)
        {
            files = new ArrayList<>();
            while (cursor.moveToNext())
            {
                TbFileObj filesObj = new TbFileObj();
                filesObj.setCode(getStringByColumn(cursor,"code"));
                filesObj.setUrl(getStringByColumn(cursor,"url"));
                filesObj.setPath(getStringByColumn(cursor,"path"));
                filesObj.setPrefix(getStringByColumn(cursor,"prefix"));
                filesObj.setCreateTime(getStringByColumn(cursor,"create_time"));
                filesObj.setChangeTime(getStringByColumn(cursor,"change_time"));
                files.add(filesObj);
            }
            cursor.close();
        }
        return files;
    }
    public static ArrayList<TbFileObj> getFiles()
    {
        String sql = "Select * From "+tbName;
        return getFiles(sql);
    }

    public static long addFile(TbFileObj obj)
    {
        if(getFileByUrl(obj.getUrl())!=null)
        {

            return -1;
        }
        ContentValues values = new ContentValues();
        values.put("code", obj.getCode());
        values.put("url",obj.getUrl());
        values.put("path",obj.getPath());
        values.put("prefix",obj.getPrefix());
        values.put("create_time", DateTimeUtil.getNowMsTime());
        values.put("change_time", DateTimeUtil.getNowMsTime());
        return insert(tbName, null, values);
    }

    public static long addFile(String url, String path, String prefix)
    {
        TbFileObj fileObj = new TbFileObj();
        fileObj.setCode(UUID.randomUUID().toString());
        fileObj.setUrl(url);
        fileObj.setPath(path);
        fileObj.setPrefix(prefix);
        return addFile(fileObj);
    }

    public static TbFileObj getFileByUrl(String url)
    {
        Cursor cursor = query(tbName,null,"url=?",new String[]{url});
        if(cursor!=null)
        {
            if(cursor.moveToNext())
            {
                TbFileObj filesObj = new TbFileObj();
                filesObj.setCode(getStringByColumn(cursor,"code"));
                filesObj.setUrl(getStringByColumn(cursor,"url"));
                filesObj.setPath(getStringByColumn(cursor,"path"));
                filesObj.setPrefix(getStringByColumn(cursor,"prefix"));
                filesObj.setCreateTime(getStringByColumn(cursor,"create_time"));
                filesObj.setChangeTime(getStringByColumn(cursor,"change_time"));
                cursor.close();
                return filesObj;
            }
            cursor.close();
        }
        return  null;
    }

    public static String sqliteEscape(String keyWord){
        keyWord = keyWord.replace("/", "//");
        keyWord = keyWord.replace("'", "''");
        keyWord = keyWord.replace("[", "/[");
        keyWord = keyWord.replace("]", "/]");
        keyWord = keyWord.replace("%", "/%");
        keyWord = keyWord.replace("&","/&");
        keyWord = keyWord.replace("_", "/_");
        keyWord = keyWord.replace("(", "/(");
        keyWord = keyWord.replace(")", "/)");
        return keyWord;
    }

    public static String getFilePathByUrl(String url)
    {
        TbFileObj fileObj =getFileByUrl(url);
        if(fileObj!=null)
        {
            return fileObj.getPath();
        }
        return null;
    }

    public static long changeTimeByCode(String code)
    {
        ContentValues values = new ContentValues();
        values.put("change_time", DateTimeUtil.getNowMsTime());
        return update(tbName, values, "code=?", new String[]{code});
    }

    private static TbFileObj getFileNO1(String sql)
    {
        ArrayList<TbFileObj> tbFiles = getFiles(sql);
        if(tbFiles!=null)
        {
            if(tbFiles.size()>0)
            {
                return tbFiles.get(0);
            }
        }
        return null;
    }
}

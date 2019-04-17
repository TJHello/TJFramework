package com.tjbaobao.framework.database;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DataSet {

    private Map<String,Object> map = new HashMap<>();

    public void setInt(String key,int value){
        map.put(key,value);
    }

    public void setString(String key,String value){
        map.put(key,value);
    }

    public void setFloat(String key,float value){
        map.put(key,value);
    }

    public void setDouble(String key,double value){
        map.put(key,value);
    }

    public void setLong(String key,long value){
        map.put(key,value);
    }

    public void setBytes(String key,byte[] bytes){
        map.put(key,bytes);
    }

    public int getInt(String key){
        Object value =  map.get(key);
        if(value!=null){
            return Integer.valueOf(String.valueOf(value)) ;
        }
        return 0;
    }

    public String getString(String key){
        return (String) map.get(key);
    }

    public float getFloat(String key){
        Object value =  map.get(key);
        if(value!=null){
            return Float.valueOf(String.valueOf(value)) ;
        }
        return 0f;
    }

    public double getDouble(String key){
        Object value =  map.get(key);
        if(value!=null){
            return Double.valueOf(String.valueOf(value)) ;
        }
        return 0;
    }

    public long getLong(String key){
        Object value =  map.get(key);
        if(value!=null){
            return Long.valueOf(String.valueOf(value)) ;
        }
        return 0;
    }

    @Nullable
    public byte[] getBytes(String key){
        Object value = map.get(key);
        if(value!=null){
            return String.valueOf(value).getBytes();
        }
        return null;
    }

    @Nullable
    public <T> T get(String key){
        Object value =  map.get(key);
        if(value!=null){
            return (T) value;
        }
        return null;

    }

    public void set(String key,Object value){
        map.put(key,value);
    }

    public Set<String> keySet()
    {
        return map.keySet();
    }

    public Collection<Object> values(){
        return map.values();
    }



}

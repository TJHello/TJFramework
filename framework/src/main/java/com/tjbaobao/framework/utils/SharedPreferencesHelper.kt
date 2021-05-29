package com.tjbaobao.framework.utils

import android.content.Context
import android.content.SharedPreferences
import java.util.concurrent.Executors

/**
 * 作者:天镜baobao
 * 时间:2020/5/19  18:39
 * 说明:允许使用，但请遵循Apache License 2.0
 * 使用：
 * Copyright 2020/5/19 天镜baobao
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
object SharedPreferencesHelper {

    /**
     * 1、值保存到内存里面
     * 2、保存的值需要有一个最大缓存数
     * 3、保存的时候做个最低时间间隔限制
     */


    lateinit var shared : SharedPreferences
    private val cacheMap : MutableMap<String,Any> = mutableMapOf()
    private val threadPool = Executors.newFixedThreadPool(3)


    @JvmStatic
    fun init(context: Context){
        shared = context.getSharedPreferences("app",Context.MODE_PRIVATE)
    }

    @JvmStatic
    fun setValue(key:String,value:String?){
        setCacheValue(key,value)
        runThreadPool{
            val editor: SharedPreferences.Editor = shared.edit()
            editor.putString(key,value)
            editor.commit()
        }
    }

    @JvmStatic
    fun setValue(key:String,value:MutableSet<String>?){
        setCacheValue(key,value)
        runThreadPool {
            val editor: SharedPreferences.Editor = shared.edit()
            editor.putStringSet(key,value)
            editor.commit()
        }
    }
    @JvmStatic
    fun setValue(key:String,value:Float){
        setCacheValue(key,value)
        runThreadPool {
            val editor: SharedPreferences.Editor = shared.edit()
            editor.putFloat(key,value)
            editor.commit()
        }
    }
    @JvmStatic
    fun setValue(key:String,value:Int){
        setCacheValue(key,value)
        runThreadPool {
            val editor: SharedPreferences.Editor = shared.edit()
            editor.putInt(key,value)
            editor.commit()
        }
    }
    @JvmStatic
    fun setValue(key:String,value:Long){
        setCacheValue(key,value)
        runThreadPool {
            val editor: SharedPreferences.Editor = shared.edit()
            editor.putLong(key,value)
            editor.commit()

        }
    }
    @JvmStatic
    fun setValue(key:String,value:Boolean){
        setCacheValue(key,value)
        runThreadPool{
            val editor: SharedPreferences.Editor = shared.edit()
            editor.putBoolean(key,value)
            editor.commit()
        }
    }

    @JvmStatic
    fun setAny(key:String,value:Any?){
        setCacheValue(key,value)
        runThreadPool{
            val editor: SharedPreferences.Editor = shared.edit()
            when{
                value==null||value is String->{
                    editor.putString(key,value as String?)
                }
                value is Float->{
                    editor.putFloat(key,value)
                }
                value is Long->{
                    editor.putLong(key,value)
                }
                value is Int->{
                    editor.putInt(key,value)
                }
                value is Boolean->{
                    editor.putBoolean(key,value)
                }
            }
            editor.commit()
        }
    }

    @JvmStatic
    fun getValue(key:String,def:String?):String?{
        var value = getCacheValue(key)
        if(value==null){
            value =  shared.getString(key,def)
            setCacheValue(key,value)
        }
        return value as String?
    }
    @JvmStatic
    fun getValue(key:String,def:MutableSet<String>):MutableSet<String>{
        var value = getCacheValue(key)
        if(value==null){
            value =  shared.getStringSet(key,def)
            setCacheValue(key,value)
        }
        return value as MutableSet<String>
    }
    @JvmStatic
    fun getValue(key:String,def:Float):Float{
        var value = getCacheValue(key)
        if(value==null){
            value = shared.getFloat(key,def)
            setCacheValue(key,value)
        }
        return value as Float
    }
    @JvmStatic
    fun getValue(key:String,def:Long):Long{
        var value = getCacheValue(key)
        if(value==null){
            value = shared.getLong(key,def)
            setCacheValue(key,value)
        }
        return value as Long
    }
    @JvmStatic
    fun getValue(key:String,def:Int):Int{
        var value = getCacheValue(key)
        if(value==null){
            value = shared.getInt(key,def)
            setCacheValue(key,value)
        }
        return value as Int
    }
    @JvmStatic
    fun getValue(key:String,def:Boolean):Boolean{
        var value = getCacheValue(key)
        if(value==null){
            value = shared.getBoolean(key,def)
            setCacheValue(key,value)
        }
        return value as Boolean
    }

    @JvmStatic
    fun getAny(key: String,def:Any?):Any?{
        var value = getCacheValue(key)
        if(value==null){
            when{
                def==null||def is String->{
                    value = shared.getString(key,def as String?)
                }
                def is Float->{
                    value = shared.getFloat(key,def)
                }
                def is Long->{
                    value = shared.getLong(key,def)
                }
                def is Int->{
                    value = shared.getInt(key,def)
                }
                def is Boolean->{
                    value = shared.getBoolean(key,def)
                }
            }
            setCacheValue(key,value)
        }
        return value
    }

    private fun runThreadPool(runnable: Runnable){
        threadPool.execute(runnable)
    }

    private fun setCacheValue(key: String, value:Any?){
        if(value!=null){
            cacheMap[key] = value
        }else{
            cacheMap.remove(key)
        }
    }

    private fun getCacheValue(key: String):Any?{
        return cacheMap[key]
    }
}
package com.tjbaobao.framework.utils

import android.animation.Animator
import android.animation.ValueAnimator
import android.graphics.Bitmap
import android.graphics.Rect
import android.view.ViewPropertyAnimator
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tjbaobao.framework.listener.TJAnimatorListener
import java.io.File

/**
 * 作者:TJbaobao
 * 时间:2019/8/16  11:14
 * 说明:
 * 使用：
 */
class KotlinCodeSugar {


}

fun ViewPropertyAnimator.animOnEnd(function:()->Unit):ViewPropertyAnimator{
    setListener(object :TJAnimatorListener(){
        override fun onAnimationEnd(animation: Animator?) {
            function()
        }
    })
    return this
}

fun ViewPropertyAnimator.cleanAnimListener():ViewPropertyAnimator{
    setListener(null)
    return this
}

fun ValueAnimator.onEnd(function:()->Unit):ValueAnimator{
    addListener(object :TJAnimatorListener(){
        override fun onAnimationEnd(animation: Animator?) {
            function()
        }
    })
    return this
}

inline fun <reified T> Gson.fromJson(jsonStr:String?):T?{
    return fromJson(jsonStr, genericType<T>())
}

inline fun <reified T> genericType() = object: TypeToken<T>() {}.type


inline fun File.foreachFile(function: (File) -> Unit){
    val fileList = listFiles()
    if(fileList!=null){
        for(file in fileList){
            function(file)
        }
    }
}

fun Rect.set(bitmap: Bitmap){
    set(0,0,bitmap.width,bitmap.height)
}
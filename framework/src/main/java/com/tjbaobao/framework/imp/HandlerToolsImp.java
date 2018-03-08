package com.tjbaobao.framework.imp;

import android.os.Message;

/**
 * Handler常用方法接口规范
 * Created by TJbaobao on 2018/3/8.
 */

@SuppressWarnings("unused")
public interface HandlerToolsImp {

    /**
     * 接受到的Handler的消息
     * @param msg Message
     * @param what what
     * @param obj obj
     */
    void onHandleMessage(Message msg, int what, Object obj);

    /**
     * 向Handler发送消息
     * @param what what
     */
    void sendMessage(int what);

    /**
     * 向Handler发送消息
     * @param what what
     * @param obj obj
     */
    void sendMessage(int what, Object obj);

    /**
     * 向Handler发送消息
     * @param what what
     * @param obj obj
     * @param arg1 arg1
     */
    void sendMessage(int what,Object obj,int arg1);
}

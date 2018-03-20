package com.tjbaobao.framework.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by TJbaobao on 2017/7/4.
 */

public class ValueTools {

    public static void saveObject(String path,Object object){
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            File file = new File(path);
             fos = new FileOutputStream(file);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(object);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (oos != null){
                try {
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static Object getObject(String path){
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = new FileInputStream(new File(path));
            ois = new ObjectInputStream(fis);
            return ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            //这里是读取文件产生异常
        } finally {
            if (fis != null){
                try {
                    fis.close();
                } catch (IOException e) {
                    //fis流关闭异常
                    e.printStackTrace();
                }
            }
            if (ois != null){
                try {
                    ois.close();
                } catch (IOException e) {
                    //ois流关闭异常
                    e.printStackTrace();
                }
            }
        }
        //读取产生异常，返回null
        return null;
    }

    public static Object getObject(byte[] data)
    {
        Object object = null;
        ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(data);
        try {
            ObjectInputStream inputStream = new ObjectInputStream(arrayInputStream);
            object  = inputStream.readObject();
            inputStream.close();
            arrayInputStream.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            String sOut = "";
            StackTraceElement[] trace = e.getStackTrace();
            for (StackTraceElement s : trace) {
                sOut += "\tat " + s + "\r\n";
            }
        }
        return object;
    }

    public static byte[] toBytes(Object object)
    {
        byte[] data = null;
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(arrayOutputStream);
            objectOutputStream.writeObject(object);
            objectOutputStream.flush();
            data = arrayOutputStream.toByteArray();
            objectOutputStream.close();
            arrayOutputStream.close();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return data;
    }

    public static int valueOf(String value)
    {
        try
        {
            return Integer.valueOf(value);
        }
        catch (Exception e)
        {
            return 0;
        }
    }
}

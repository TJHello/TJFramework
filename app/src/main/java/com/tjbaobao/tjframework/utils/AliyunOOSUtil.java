package com.tjbaobao.tjframework.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tjbaobao.framework.utils.FileDownloader;
import com.tjbaobao.framework.utils.FileUtil;
import com.tjbaobao.framework.utils.ImageDownloader;
import com.tjbaobao.framework.utils.OKHttpUtil;
import com.tjbaobao.framework.utils.RxJavaUtil;
import com.tjbaobao.framework.utils.Tools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * 作者:TJbaobao
 * 时间:2018/9/29  10:19
 * 说明:
 * 使用：
 */
public class AliyunOOSUtil {

    private static final String BucketName = "tjbaobao";
    private static final String ObjectNameHome = "TJFramework";
    private static final String ServerIP = "s.imczm.com:8080";
    private static final String ServerUrl = "http://" + ServerIP + "/TJFramework/AliyunOSSServlet";

    private static final Map<String, String> mapUrl = new HashMap<>();
    private static final String AliyunUrlKey = "Aliyun_Url_Key";
    private static final String[] objectNames = new String[]{"lakes-02_m_2.jpg","lakes-04_m_2.jpg","lakes-07_2.jpg"};

    public static void init() {
        initCache();
        update();
    }

    @Nullable
    public static String getUrl(@NonNull String name)
    {
        if(mapUrl.containsKey(name))
        {
            return mapUrl.get(name);
        }
        else
        {
            update();
        }
        return null;
    }

    private static void initCache() {
        String urlValues = (String) Tools.getSharedPreferencesValue(AliyunUrlKey, null);
        if (urlValues != null) {
            try {
                JSONArray jsonArray = new JSONArray(urlValues);
                int arraySize = jsonArray.length();
                for (int i = 0; i < arraySize; i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    if (jsonObject != null) {
                        String key = jsonObject.getString("key");
                        String url = jsonObject.getString("url");
                        if (key != null && url != null) {
                            mapUrl.put(key, url);
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private static void update()
    {
        RxJavaUtil.runOnIOThread(() -> {
            for(String name:objectNames)
            {
                String httpUrl = ServerUrl+"?bucketName="+BucketName+"&objectName="+ObjectNameHome+"/"+name;
                Tools.printLog("httpUrl="+httpUrl);
                String imageUrl = OKHttpUtil.doGet(httpUrl);
                if(imageUrl!=null)
                {
                    if(mapUrl.containsKey(name))
                    {
                        String urlCache = mapUrl.get(name);
                        if(!imageUrl.equals(urlCache))
                        {
                            mapUrl.put(name,imageUrl);
                            String cacheFilePath = FileDownloader.getFilePath(urlCache);
                            FileUtil.delFileIfExists(cacheFilePath);
                        }
                    }
                    else
                    {
                        mapUrl.put(name,imageUrl);
                    }
                }
            }
            JSONArray jsonArray = new JSONArray();
            for(String key:mapUrl.keySet())
            {
                String url = mapUrl.get(key);
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("key",key);
                    jsonObject.put("url",url);
                    jsonArray.put(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            Tools.setSharedPreferencesValue(AliyunUrlKey,jsonArray.toString());
        });
    }

}

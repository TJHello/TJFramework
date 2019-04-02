package com.tjbaobao.framework.utils;


import android.support.annotation.NonNull;

import com.tjbaobao.framework.listener.OnProgressListener;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.*;

/**
 *对OKHttp的封装
 * 1、支持文件下载上传进度条。
 * 2、支持文件下载二级缓存，避免文件损坏
 * 3、接入cookie功能。
 *
 * Created by TJbaobao on 2018/1/12.
 */

@SuppressWarnings({"unused", "WeakerAccess"})
public class OKHttpUtil {

    private static CookieJar userCookieJar = null;
    private static OkHttpClient.Builder builder = new OkHttpClient.Builder();
    private static final OkHttpClient mOkHttpClient = builder
            .cookieJar(new TJCookieJar())
            .connectTimeout(3,TimeUnit.SECONDS)//设置连接超时时间
            .build();
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final String CACHE_SUFFIX = ".cache";
    public static boolean isUseCacheFile = true;//是否使用缓冲文件来降低文件损坏概率
    static {
    }

    private static final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();
    private static class TJCookieJar implements CookieJar {

        @Override
        public void saveFromResponse(@NonNull HttpUrl url, @NonNull List<Cookie> cookies) {
            if(userCookieJar==null){
                cookieStore.put(url.host(), cookies);
            }else{
                userCookieJar.saveFromResponse(url,cookies);
            }
        }

        @Override
        public List<Cookie> loadForRequest(@NonNull HttpUrl url) {
            if(userCookieJar==null){
                List<Cookie> cookies = cookieStore.get(url.host());
                return cookies != null ? cookies : new ArrayList<>();
            }else{
                return userCookieJar.loadForRequest(url);
            }
        }
    }

    private static Response execute(Request request){
        try {
            return  mOkHttpClient.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void enqueue(Request request, Callback callback)
    {
        mOkHttpClient.newCall(request).enqueue(callback);
    }

    private static String executeString(Request request)
    {
        try {
            Response response = execute(request);
            if(response!=null&&response.isSuccessful())
            {
                ResponseBody body =  response.body();
                if(body!=null)
                {
                    return body.string();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String doGet(String url)
    {
        Request request = new Request.Builder().url(url).build();
        return executeString(request);
    }

    public static void doGet(String url, Callback callback)
    {
        Request request = new Request.Builder().url(url).build();
        enqueue(request,callback);
    }

    public static String doPost(String url, String json)  {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        return executeString(request);
    }

    public static void doPost(String url, String json, Callback callback)
    {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        enqueue(request,callback);
    }

    public static boolean download(String url, String path)
    {
        return download(url,path,null);
    }

    public static boolean download(String url, String path, OnProgressListener onProgressListener)
    {
        Request request = new Request.Builder().url(url).build();
        Response response = execute(request);
        if(response!=null&&response.isSuccessful())
        {
            ResponseBody body = response.body();
            if(body!=null)
            {
                if(onProgressListener!=null)
                {
                    onProgressListener.length =  body.contentLength();
                }
                String outPath = isUseCacheFile?path+CACHE_SUFFIX:path;
                InputStream inputStream = body.byteStream();
                if(FileUtil.Writer.writeFile(inputStream,outPath,onProgressListener))
                {
                    if(isUseCacheFile){
                        return FileUtil.rename(new File(outPath), new File(path));
                    }else{
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static class TJInterceptor implements Interceptor
    {
        @Override
        public Response intercept(@NonNull Chain chain) throws IOException {
            Response originalResponse = chain.proceed(chain.request());
            return originalResponse.newBuilder()
                    .body(new TJResponseBody(originalResponse.body()))
                    .build();
        }
    }

    public static String upload(String url, String path)
    {
        return upload(url,path,null);
    }

    public static String upload(String url, String path, OnProgressListener onProgressListener)
    {
        RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"),path);
        TJRequestBody tjRequestBody = new TJRequestBody(fileBody,onProgressListener);
        Request request = new Request.Builder().url(url).post(tjRequestBody).build();
       return executeString(request);
    }

    public static OkHttpClient.Builder getBuilder(){
        return builder;
    }

    public static OkHttpClient.Builder setCookieJar(CookieJar jar){
        builder.cookieJar(jar);
        return builder;
    }

}

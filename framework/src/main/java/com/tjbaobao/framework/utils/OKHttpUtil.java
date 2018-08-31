package com.tjbaobao.framework.utils;


import android.support.annotation.NonNull;

import com.tjbaobao.framework.listener.OnProgressListener;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 *
 * Created by TJbaobao on 2018/1/12.
 */

@SuppressWarnings({"unused", "WeakerAccess"})
public class OKHttpUtil {

    private static final OkHttpClient mOkHttpClient =  new OkHttpClient.Builder()
            .connectTimeout(3,TimeUnit.SECONDS)//设置连接超时时间
            .build();
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final String CACHE_SUFFIX = ".cache";
    static {
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
                InputStream inputStream = body.byteStream();
                if(FileUtil.Writer.writeFile(inputStream,path+CACHE_SUFFIX,onProgressListener))
                {
                    return FileUtil.rename(new File(path+CACHE_SUFFIX), new File(path));
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

}

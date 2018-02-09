package com.tjbaobao.framework.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.tjbaobao.framework.entity.BitmapConfig;
import com.tjbaobao.framework.listener.OnProgressListener;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImageDownloader {
	private final static int maxMemory = (int) (Runtime.getRuntime().maxMemory() );
	private final static int cacheSize = maxMemory / 10;
	private static LruCache<String, Bitmap> imageLruCache = new LruCache<String, Bitmap>(cacheSize){
		protected int sizeOf(String key, Bitmap value) {
			if(value!=null&&!value.isRecycled())
			{
				return value.getByteCount() ;
			}
			return 0;
		}
		@Override
		protected void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap newValue) {
			super.entryRemoved(evicted, key, oldValue, newValue);
			if (evicted && oldValue != null&&!oldValue.isRecycled()){
				oldValue.recycle();
			}
		}
	};
	private ExecutorService cachedThreadPool  = Executors.newFixedThreadPool(3);

	private static final ImageDownloader imageDownloader = new ImageDownloader();
	private static FileDownloader fileDownloader = FileDownloader.getInstance();
    private static BaseHandler baseHandler ;
    private ImageDownloader(){
    }

    public static ImageDownloader getInstance()
    {
        baseHandler = new BaseHandler();
        return imageDownloader;
    }

    public BitmapConfig load(String path,ImageView imageView)
    {
        return load(path,imageView,null);
    }

    public BitmapConfig load(String path, ImageView imageView, OnProgressListener onProgressListener)
    {
        if(path==null)
        {
            return null;
        }
        if(path.indexOf("http")==0)
        {
            return loadHttpImage(path,imageView,onProgressListener);
        }
        else
        {
            return loadLocalImage(path,imageView,onProgressListener);
        }
    }

    private BitmapConfig loadLocalImage(String path,ImageView imageView, OnProgressListener onProgressListener)
    {
        BitmapConfig bitmapConfig = ImageUtil.getBitmapConfig(path);
        startLoadThread(path,imageView,onProgressListener);
        return bitmapConfig;
    }

    private BitmapConfig loadHttpImage(String url,ImageView imageView, OnProgressListener onProgressListener)
    {
        startLoadThread(url,imageView,onProgressListener);
        return null;
    }

    private void startLoadThread(String url,ImageView imageView,OnProgressListener onImageLoaderListener)
    {
        WeakReference<ImageView> viewWeakReference = new WeakReference<>(imageView);
        cachedThreadPool.execute(new LoadRunnable(url,viewWeakReference,onImageLoaderListener));
    }

    private class LoadRunnable implements Runnable
    {
        private String url ;
        private WeakReference<ImageView> viewWeakReference ;
        private OnProgressListener onProgressListener;

        LoadRunnable(String url, WeakReference<ImageView> viewWeakReference,OnProgressListener onProgressListener) {
            this.url = url;
            this.viewWeakReference = viewWeakReference;
            this.onProgressListener = onProgressListener;
        }
        @Override
        public void run() {
            final Bitmap bitmapCache = getCacheImage(url);//从缓存中获取
            if(!ImageUtil.isOk(bitmapCache))
            {
                if(url.indexOf("http")==0)
                {
                    //网络连接
                    String path = downloadImage(url,onProgressListener);
                    Bitmap bitmap = loadLocalImage(path);
                    if (ImageUtil.isOk(bitmap)) {
                        onSuccess(url,path,bitmap,viewWeakReference);
                    }
                    else
                    {
                        onFail(url);
                    }
                }
                else
                {
                    //本地路径
                    Bitmap bitmap = loadLocalImage(url);
                    if(!ImageUtil.isOk(bitmapCache))
                    {
                        //本地图片读取出错
                        String path = downloadImage(url, onProgressListener);
                        Bitmap bitmapTry = loadLocalImage(path);
                        if (ImageUtil.isOk(bitmapTry))
                        {
                            onSuccess(url,path,bitmapTry,viewWeakReference);
                        }
                        else
                        {
                            onFail(url);
                        }
                    }
                    else
                    {
                        onSuccess(url,url,bitmap,viewWeakReference);
                    }
                }
            }
            else
            {
                onSuccess(url,url,bitmapCache,viewWeakReference);
            }
        }

        private String downloadImage(String url,OnProgressListener onProgressListener)
        {
            String prefix = FileUtil.getPrefix(url);
            if(prefix==null)
            {
                prefix = "png";
            }
            String path = ConstantUtil.getImageFilesPath()+ UUID.randomUUID().toString()+"."+prefix;
            return fileDownloader.downloadExecute(url,path,onProgressListener);
        }

        private Bitmap getCacheImage(String url)
        {
            return imageLruCache.get(url);
        }

        private Bitmap loadLocalImage(String path)
        {
            Bitmap bitmap = ImageUtil.getBitmapByAssets(path);
            if(bitmap==null||bitmap.isRecycled()||!ImageUtil.isOk(path))
            {
                FileUtil.delFileIfExists(path);
                return null;
            }
            return bitmap;
        }

        private void onFail(String url)
        {
            baseHandler.post(() -> {
                if(onImageLoaderListener!=null)
                {
                    onImageLoaderListener.onFail(url);
                }
            });
        }
        private void onSuccess(String url,String path,Bitmap bitmap,WeakReference<ImageView> viewWeakReference)
        {
            final ImageView imageView = viewWeakReference.get();
            if(imageView!=null)
            {
                baseHandler.post(() ->
                {
                    imageView.setImageBitmap(bitmap);
                    if(onImageLoaderListener!=null)
                    {
                        onImageLoaderListener.onSuccess(url,path,bitmap);
                    }
                });
            }
        }
    }

    public OnImageLoaderListener onImageLoaderListener ;

    public OnImageLoaderListener getOnImageLoaderListener() {
        return onImageLoaderListener;
    }

    public void setOnImageLoaderListener(OnImageLoaderListener onImageLoaderListener) {
        this.onImageLoaderListener = onImageLoaderListener;
    }

    public interface OnImageLoaderListener
    {
        void onSuccess(String url,String path,Bitmap bitmap);

        void onFail(String url);
    }

}

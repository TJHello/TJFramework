package com.tjbaobao.framework.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.tjbaobao.framework.entity.BitmapConfig;
import com.tjbaobao.framework.listener.OnProgressListener;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
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
    private static final List<Image> imageList = new ArrayList<>();

    private ImageDownloader(){
    }

    public static ImageDownloader getInstance()
    {
        baseHandler = new BaseHandler();
        return imageDownloader;
    }

    public BitmapConfig load(String url,ImageView imageView)
    {
        return load(url,imageView,null);
    }

    public BitmapConfig load(String url, ImageView imageView, OnProgressListener onProgressListener)
    {
        if(url==null)
        {
            return null;
        }
        Image image = findImage(imageView);
        image.setUrl(url);
        image.setViewWeakReference(imageView);

        if(url.indexOf("http")==0)
        {
            return loadHttpImage(url,onProgressListener);
        }
        else
        {
            return loadLocalImage(url,onProgressListener);
        }
    }

    private BitmapConfig loadLocalImage(String path, OnProgressListener onProgressListener)
    {
        BitmapConfig bitmapConfig = ImageUtil.getBitmapConfig(path);
        startLoadThread(path,onProgressListener);
        return bitmapConfig;
    }

    private BitmapConfig loadHttpImage(String url,OnProgressListener onProgressListener)
    {
        startLoadThread(url,onProgressListener);
        return null;
    }

    private void startLoadThread(String url,OnProgressListener onImageLoaderListener)
    {
        cachedThreadPool.execute(new LoadRunnable(url,onImageLoaderListener));
    }

    private class LoadRunnable implements Runnable
    {
        private String url ;
        private OnProgressListener onProgressListener;

        LoadRunnable(String url,OnProgressListener onProgressListener) {
            this.url = url;
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
                        onSuccess(url,path,bitmap);
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
                            onSuccess(url,path,bitmapTry);
                        }
                        else
                        {
                            onFail(url);
                        }
                    }
                    else
                    {
                        onSuccess(url,url,bitmap);
                    }
                }
            }
            else
            {
                onSuccess(url,url,bitmapCache);
            }
        }

        private Bitmap getCacheImage(String url)
        {
            return imageLruCache.get(url);
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

        private void onSuccess(String url,String path,Bitmap bitmap)
        {
            final List<Image> images = findImage(url);
            for(Image image:images)
            {
                final ImageView imageView = image.getViewWeakReference().get();
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
                imageList.remove(image);
            }
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

    }

    private class Image
    {
        String url;
        WeakReference<ImageView> viewWeakReference ;

        String getUrl() {
            return url;
        }

        void setUrl(String url) {
            this.url = url;
        }

        WeakReference<ImageView> getViewWeakReference() {
            return viewWeakReference;
        }

        void setViewWeakReference(ImageView imageView) {
            this.viewWeakReference = new WeakReference<>(imageView);
        }
    }

    @NonNull
    private static Image findImage(ImageView imageView)
    {
        synchronized (imageList)
        {
            cleanNullImage();
            for(Image image:imageList)
            {
                if(image!=null)
                {
                    ImageView iv = image.getViewWeakReference().get();
                    if(iv!=null&&iv.equals(imageView))
                    {
                        return image;
                    }
                }
            }
            Image image = imageDownloader.new Image();
            imageList.add(image);
            return image;
        }
    }
    private static void cleanNullImage()
    {
        for(int i=imageList.size()-1;i>=0;i--)
        {
            Image image = imageList.get(i);
            if(image==null)
            {
                imageList.remove(i);
            }
        }
    }

    private static List<Image> findImage(String url)
    {
        List<Image> images = new ArrayList<>(imageList.size());
        synchronized (imageList)
        {
            for (Image image : imageList) {
                ImageView iv = image.getViewWeakReference().get();
                if (iv != null && image.getUrl().equals(url)) {
                    images.add(image);
                }
            }
        }
        return images;
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

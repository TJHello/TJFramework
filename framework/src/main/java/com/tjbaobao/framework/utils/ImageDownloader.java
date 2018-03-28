package com.tjbaobao.framework.utils;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.LruCache;
import android.widget.ImageView;

import com.tjbaobao.framework.entity.BitmapConfig;
import com.tjbaobao.framework.listener.OnProgressListener;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SuppressWarnings("unused")
public class ImageDownloader {
	private final static int maxMemory = (int) (Runtime.getRuntime().maxMemory() );
	private final static int cacheSize = maxMemory / 6;
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
	private ExecutorService cachedThreadPool  = Executors.newFixedThreadPool(5);
	private static FileDownloader fileDownloader = FileDownloader.getInstance();
    private static BaseHandler baseHandler ;
    private static final List<Image> imageList = new ArrayList<>();
    private static final List<QueueInfo> queuePoolList = Collections.synchronizedList(new ArrayList<>());
    private static final List<String> downloadList = Collections.synchronizedList(new ArrayList<>());
    private static boolean isStop = false;
    private Bitmap defaultBitmap = null;
    private static int imageWidth = 0,imageHeight = 0;

    private ImageDownloader(){
        Bitmap bitmap = Bitmap.createBitmap(100, 100,Bitmap.Config.ARGB_8888);
        bitmap.eraseColor(Color.parseColor("#FDFDFD"));//填充颜色
        defaultBitmap = bitmap  ;
    }

    public static ImageDownloader getInstance()
    {
        isStop = false;
        imageWidth = 0 ;
        imageHeight = 0;
        baseHandler = new BaseHandler();
        return new ImageDownloader();
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
        isStop = false;
        Image image = findImage(url);
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
        isStop = false;
        BitmapConfig bitmapConfig = new BitmapConfig();
        if(defaultBitmap!=null)
        {
            bitmapConfig.setWidth(defaultBitmap.getWidth());
            bitmapConfig.setHeight(defaultBitmap.getHeight());
        }
        else
        {
            bitmapConfig.setHeight(500);
            bitmapConfig.setWidth(500);
        }
        startLoadThread(path,onProgressListener);
        return bitmapConfig;
    }

    private BitmapConfig loadHttpImage(String url,OnProgressListener onProgressListener)
    {
        startLoadThread(url,onProgressListener);
        BitmapConfig bitmapConfig = new BitmapConfig();
        if(defaultBitmap!=null)
        {
            bitmapConfig.setWidth(defaultBitmap.getWidth());
            bitmapConfig.setHeight(defaultBitmap.getHeight());
            return bitmapConfig;
        }

        return null;
    }

    private void startLoadThread(final String url,final OnProgressListener onImageLoaderListener)
    {
        new Thread(() -> {
            final Bitmap bitmapCache = getCacheImage(url);//从缓存中获取
            if(!ImageUtil.isOk(bitmapCache))
            {
                if(url.indexOf("http")!=0)
                {
                    //本地路径
                    Bitmap bitmap = loadLocalImage(url);
                    if(!ImageUtil.isOk(bitmapCache))
                    {
                        //本地图片读取出错
                        runInQueue(url,onImageLoaderListener);
                    }
                    else
                    {
                        onSuccess(url,url,bitmap);
                    }
                }
                else
                {
                    runInQueue(url,onImageLoaderListener);
                }
            }
            else
            {
                onSuccess(url,null,bitmapCache);
            }
        }).start();
    }

    private void runInQueue(String url,OnProgressListener onImageLoaderListener)
    {
        QueueInfo queueInfo = new QueueInfo(url,null,onImageLoaderListener);
        queuePoolList.remove(queueInfo);
        queuePoolList.add(queueInfo);
        if(downloaderQueuePool==null)
        {
            downloaderQueuePool = new DownloaderQueuePool();
        }
        downloaderQueuePool.startTimer(0,100);
    }

    private DownloaderQueuePool downloaderQueuePool ;
    private class DownloaderQueuePool extends BaseTimerTask
    {
        @Override
        public BaseTimerTask startTimer(long delay, long period) {
            synchronized (this)
            {
                if(isCancel)
                {
                    return super.startTimer(delay, period);
                }
                return this;
            }
        }

        @Override
        public void run() {
            synchronized (queuePoolList)
            {
                synchronized (downloadList)
                {
                    int length = queuePoolList.size();
                    if(length>0)
                    {
                        for (int i = length-1; i >= 0; i--)
                        {
                            if(downloadList.size()<8)
                            {
                                try{
                                    QueueInfo queueInfo = queuePoolList.get(i);
                                    if(!mapDownload.containsKey(queueInfo.getUrl())||!mapDownload.get(queueInfo.getUrl()))
                                    {
                                        mapDownload.put(queueInfo.getUrl(),true);
                                        cachedThreadPool.execute(new LoadRunnable(queueInfo));
                                        downloadList.add(queueInfo.getUrl());
                                    }
                                    queuePoolList.remove(i);
                                }
                                catch (Exception ignored){}
                            }
                            else
                            {
                                for(int j = downloadList.size()-1; j >= 0; j--)
                                {
                                    if(j<downloadList.size()) {
                                        try {
                                            String url = downloadList.get(j);
                                            if(!mapDownload.containsKey(url)||!mapDownload.get(url))
                                            {
                                                downloadList.remove(url);
                                            }
                                        } catch (Exception ignored)
                                        {

                                        }
                                    }
                                }
                            }
                        }
                    }
                    else
                    {
                        this.stopTimer();
                    }
                }
            }
        }
    }

    public void stop()
    {
        isStop = true;
        if(downloaderQueuePool!=null)
        {
            downloaderQueuePool.stopTimer();
        }
        queuePoolList.clear();
        downloadList.clear();
    }

    public boolean isStop() {
        return isStop;
    }

    public void setDefaultImgSize(int width,int height)
    {
        imageWidth = width ;
        imageHeight  = height ;
    }

    public void setDefaultBitmap(Bitmap bitmap)
    {
        this.defaultBitmap = bitmap;
    }

    public void remove(String key)
    {
        if(imageLruCache!=null)
        {
            try {
                Bitmap bitmap = imageLruCache.get(key+imageWidth+"_"+imageHeight);
                imageLruCache.remove(key+imageWidth+"_"+imageHeight);
                Image image = findImage(key);
                if(image!=null)
                {
                    ImageView imageView = image.getImageView();
                    if(imageView!=null)
                    {
                        imageView.setImageBitmap(null);
                    }
                }
//                ImageUtil.recycled(bitmap);
            }
            catch (Exception ignored){}
        }
    }

    /**
     * 释放资源
     */
    public static void release()
    {
        if(imageLruCache!=null)
        {
            if (imageLruCache.size() > 0)
            {
                imageLruCache.evictAll();
            }
        }
    }

    @SuppressWarnings("unused")
    private class QueueInfo
    {
        String url ;
        String path;
        OnProgressListener onProgressListener;

        QueueInfo(String url, String path, OnProgressListener onProgressListener) {
            this.url = url;
            this.path = path;
            this.onProgressListener = onProgressListener;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public OnProgressListener getOnProgressListener() {
            return onProgressListener;
        }

        public void setOnProgressListener(OnProgressListener onProgressListener) {
            this.onProgressListener = onProgressListener;
        }
    }

    private volatile Map<String ,Boolean> mapDownload = new HashMap<>();
    private class LoadRunnable implements Runnable
    {
        private String url ;
        private OnProgressListener onProgressListener;


        LoadRunnable(QueueInfo queueInfo)
        {
            this(queueInfo.url,queueInfo.onProgressListener);
        }

        LoadRunnable(String url,OnProgressListener onProgressListener) {
            this.url = url;
            this.onProgressListener = onProgressListener;
        }
        @Override
        public void run() {
            if(isStop)
            {
                while (downloadList.contains(url))
                {
                    downloadList.remove(url);
                }
                return ;
            }
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
                        FileUtil.delFileIfExists(path);
                        path = downloadImage(url,onProgressListener);
                        bitmap = loadLocalImage(path);
                        if (ImageUtil.isOk(bitmap)) {
                            onSuccess(url,path,bitmap);
                        }
                        else
                        {
                            onFail(url);
                        }
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
            while (downloadList.contains(url))
            {
                downloadList.remove(url);
            }
            mapDownload.put(url,false);
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



    }

    private Bitmap loadLocalImage(String path)
    {
        Bitmap bitmap ;
        if(imageWidth!=0&&imageHeight!=0)
        {
            bitmap = ImageUtil.compressImageRGB(path,imageWidth,imageHeight);
//            Bitmap bitmap2 = ImageUtil.matrixBitmapRGB(bitmap,imageWidth,imageHeight);
//            ImageUtil.recycled(bitmap);
//            bitmap = bitmap2;
        }
        else
        {
            bitmap = ImageUtil.getBitmap(path);
        }
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

    private void onSuccess(String url,String path,Bitmap bitmap)
    {
        setCacheImage(url,bitmap);
        final List<Image> images = findImages(url);
        for(Image image:images)
        {
            final ImageView imageView = image.getViewWeakReference().get();
            if(image.getUrl().equals(url))
            {
                baseHandler.post(() ->
                {
                    if(imageView!=null)
                    {
                        imageView.setImageBitmap(bitmap);
                    }
                    if(onImageLoaderListener!=null)
                    {
                        onImageLoaderListener.onSuccess(url,path,bitmap);
                    }
                });
            }
            imageList.remove(image);
        }
    }

    private void setCacheImage(String url,Bitmap bitmap)
    {
        if(getCacheImage(url+imageWidth+"_"+imageHeight)==null)
        {
            if(imageLruCache!=null&&bitmap!=null)
            {
                imageLruCache.put(url+imageWidth+"_"+imageHeight, bitmap);
            }
        }
    }
    private Bitmap getCacheImage(String url)
    {
        if(imageLruCache!=null&&url!=null)
        {
            return imageLruCache.get(url+imageWidth+"_"+imageHeight);
        }
        return null;
    }

    private static class Image
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

        ImageView getImageView()
        {
            if(viewWeakReference!=null)
            {
                return viewWeakReference.get();
            }
            return null;
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
            for(int i=imageList.size()-1;i>=0;i--)
            {
                if(i<imageList.size())
                {
                    Image image = imageList.get(i);
                    if(image!=null)
                    {
                        ImageView iv = image.getViewWeakReference().get();
                        if(iv!=null&&iv.equals(imageView))
                        {
                            return image;
                        }
                    }
                }
            }
            Image image = new Image();
            imageList.add(image);
            return image;
        }
    }

    private static Image findImage(String url)
    {
        synchronized (imageList)
        {
            cleanNullImage();
            for(int i=imageList.size()-1;i>=0;i--)
            {
                if(i<imageList.size())
                {
                    try
                    {
                        Image image = imageList.get(i);
                        if(image!=null)
                        {
                            if(image.getUrl()!=null&&image.getUrl().equals(url))
                            {
                                return image;
                            }
                        }
                    }
                    catch (Exception ignored)
                    {

                    }
                }
            }
            Image image = new Image();
            imageList.add(image);
            return image;
        }
    }

    private static void cleanNullImage()
    {
        for(int i=imageList.size()-1;i>=0;i--)
        {
            if(i<imageList.size())
            {
                Image image = imageList.get(i);
                if(image==null)
                {
                    imageList.remove(i);
                }
            }
        }
    }

    private static List<Image> findImages(String url)
    {
        List<Image> images = new ArrayList<>(imageList.size());
        synchronized (imageList)
        {
            for(int i=imageList.size()-1;i>=0;i--)
            {
                if(i<imageList.size())
                {
                    try{
                        Image image = imageList.get(i);
                        if(image!=null)
                        {
                            if (image.getUrl().equals(url)) {
                                images.add(image);
                            }
                        }
                    }catch (Exception ignored)
                    {

                    }
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

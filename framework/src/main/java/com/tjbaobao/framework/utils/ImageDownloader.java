package com.tjbaobao.framework.utils;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

/**
 * 图片下载器-支持磁盘缓存功能-支持优先下载队列-支持列表图片加载，防止混乱-支持图片压缩裁切-支持设置缓存大小
 *
 * 使用方法：
 * ImageDownloader imageDownloader = ImageDownloader.getInstance();
 * imageDownloader.setDefaultImgSize(int width,int height);//设置默认大小，如果不设置，会加载原图。
 * imageDownloader.load(String url,ImageView imageView);//加载图片,url可以是本地路径，可以是在线链接，可以是assets路径
 *
 */
@SuppressWarnings("unused")
public class ImageDownloader {
	private final static int maxMemory = (int) (Runtime.getRuntime().maxMemory() );
	private static int cacheSize = maxMemory / 7;
	private static LruCache<String, Bitmap> imageLruCache ;

	private ExecutorService cachedThreadPool  = Executors.newFixedThreadPool(3);
	private static FileDownloader fileDownloader = FileDownloader.getInstance();
    private BaseHandler baseHandler = new BaseHandler();
    private final List<Image> imageList = new ArrayList<>();
    private final List<QueueInfo> queuePoolList = Collections.synchronizedList(new ArrayList<>());
    private final List<String> downloadList = Collections.synchronizedList(new ArrayList<>());
    private boolean isStop;
    private Bitmap defaultBitmap ;
    private int imageWidth,imageHeight;
    private static boolean isStrictMode = false;//严格模式

    private ImageDownloader(){
        isStop = false;
        imageWidth = 0 ;
        imageHeight = 0;
        if(imageLruCache==null)
        {
            imageLruCache = new LruCache<String, Bitmap>(cacheSize){
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
                    if(isStrictMode)
                    {
                        if (evicted && oldValue != null&&!oldValue.isRecycled()){
                            oldValue.recycle();
                        }
                    }
                }
            };
        }
    }

    public static ImageDownloader getInstance()
    {
        return new ImageDownloader();
    }

    public BitmapConfig load(String url,ImageView imageView)
    {
        return load(url,imageView,null);
    }

    /**
     *
     * @param url url
     * @param imageView imageView
     * @param onProgressListener 进度监听器
     * @return 没有什么用了
     */
    @Nullable
    public BitmapConfig load(String url, ImageView imageView, OnProgressListener onProgressListener)
    {
        if(url==null)
        {
            return null;
        }
        isStop = false;
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

    @Nullable
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

    @Nullable
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
        cachedThreadPool.execute(new Thread(() -> {
            final Bitmap bitmapCache = getCacheImage(url);//从缓存中获取
            if(!ImageUtil.isOk(bitmapCache))
            {
                if(url.indexOf("http")!=0)
                {
                    //本地路径
                    Bitmap bitmap = loadLocalImage(url);
                    if(!ImageUtil.isOk(bitmap))
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
        }));
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

    /**
     * 停止所有下载任务，清空等待下载队列
     */
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

    /**
     * 设置默认图片大小
     * @param width width
     * @param height height
     */
    public void setDefaultImgSize(int width,int height)
    {
        imageWidth = width ;
        imageHeight  = height ;
    }

    /**
     * 设置默认图片，如果加载失败会使用这个图片
     * @param bitmap bitmap
     */
    public void setDefaultBitmap(Bitmap bitmap)
    {
        this.defaultBitmap = bitmap;
    }

    /**
     * 清除某个地址的内存缓存
     * @param key url
     */
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
            if(ImageUtil.isOk(bitmap))
            {
                float width = (float)imageWidth*0.8f;
                float height = width*(float)bitmap.getHeight()/(float)bitmap.getWidth();
                Bitmap bitmap2 = ImageUtil.matrixBitmapRGB(bitmap,width,height);
                if(!bitmap.equals(bitmap2))
                {
                    ImageUtil.recycled(bitmap);
                }
                bitmap = bitmap2;
            }
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
            final ImageView imageView = image.getImageView();
            if(image.getUrl().equals(url)&&imageView!=null)
            {
                synchronized(this)
                {
                    baseHandler.post(() ->
                    {
                        imageView.setImageBitmap(bitmap);
                        imageList.remove(image);
                    });
                }
            }
        }
        baseHandler.post(() -> {
            if(onImageLoaderListener!=null)
            {
                onImageLoaderListener.onSuccess(url,path,bitmap);
            }
        });

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
    private Image findImage(ImageView imageView)
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
                        ImageView iv = image.getImageView();
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

    private Image findImage(String url)
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

    private void cleanNullImage()
    {
        synchronized (imageList)
        {
            for(int i=imageList.size()-1;i>=0;i--)
            {
                if(i<imageList.size())
                {
                    Image image = imageList.get(i);
                    if(image==null)
                    {
                        if(i<imageList.size())
                        {
                            imageList.remove(i);
                        }
                    }
                }
            }
        }
    }

    private List<Image> findImages(String url)
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

    /**
     * 设置缓存大小
     * @param cacheSize 1*1024*1024 == 1M
     */
    public static void setCacheSize(int cacheSize) {
        ImageDownloader.cacheSize = cacheSize;
    }

    public static int getCacheSize() {
        return cacheSize;
    }

    /**
     * 是否启动严格模式，启动严格模式之后，超出缓存池的图片会被主动recycle
     * @param isStrictMode {true:开启,false:关闭}
     */
    public static void setIsStrictMode(boolean isStrictMode) {
        ImageDownloader.isStrictMode = isStrictMode;
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

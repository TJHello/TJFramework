package com.tjbaobao.framework.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.LruCache;
import android.widget.ImageView;

import com.tjbaobao.framework.base.BaseApplication;
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
@SuppressWarnings("ALL")
public class ImageDownloader {
	private final static int maxMemory = (int) (Runtime.getRuntime().maxMemory() );
	private static int cacheSize = maxMemory / 7;
	private static LruCache<String, Bitmap> imageLruCache ;
	private static int downloadThreadNum = 3;
    private static int loadThreadNum = 3;
    private static boolean isMergeUrlParameter = false;//是否将不同参数的链接当做同一个文件链接

	private ExecutorService downloadThreadPool  = Executors.newFixedThreadPool(downloadThreadNum);
	private ExecutorService localThreadPool = Executors.newFixedThreadPool(loadThreadNum);
	private static FileDownloader fileDownloader = FileDownloader.getInstance();
    private BaseHandler baseHandler = new BaseHandler();
    private final List<Image> imageList = new ArrayList<>();
    private final List<QueueInfo> queuePoolList = Collections.synchronizedList(new ArrayList<>());
    private final List<String> downloadList = Collections.synchronizedList(new ArrayList<>());
    private boolean isStop;
    private Bitmap defaultBitmap ;
    private int imageWidth ,imageHeight;
    private static boolean isStrictMode = false;//严格模式
    private static boolean isSizeStrictMode = false;//尺寸严格模式
    private static boolean isCacheTempSizeBitmap = false;//是否缓存临时的尺寸图
    private static boolean isAutoDelErrorBitmap = false;//是否自动删除读取失败的图片(防止图片损坏)
    private ImageResolver imageResolver = new TJImageResolver();

    private ImageDownloader(){
        isStop = false;
        imageWidth  = DeviceUtil.getScreenWidth()/2;
        imageHeight = DeviceUtil.getScreenWidth()/2;
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

    @NonNull
    public static ImageDownloader getInstance()
    {
        return new ImageDownloader();
    }

    public void load(String url,ImageView imageView)
    {
        load(url,imageView,null);
    }

    /**
     *
     * @param url url
     * @param imageView imageView
     * @param onProgressListener 进度监听器
     * @return 没有什么用了
     */
    public void load(String url, ImageView imageView, OnProgressListener onProgressListener)
    {
        if(url==null)  return;
        isStop = false;
        Image image = findImage(imageView);
        image.setUrl(url);
        image.setTag(isMergeUrlParameter?FileUtil.formatUrl(url):url);
        image.setViewWeakReference(imageView);
        if(isHttp(url))
        {
            loadHttpImage(url,onProgressListener);
        }
        else
        {
            loadLocalImage(url,onProgressListener);
        }
    }

    public void load(int resId,ImageView imageView){
        load(resId,imageView,null);
    }

    public void load(int resId,ImageView imageView,@Nullable OnProgressListener onProgressListener){
        isStop = false;
        Image image = findImage(imageView);
        image.setUrl(""+resId);
        image.setTag(""+resId);
        image.setViewWeakReference(imageView);
        loadResImage(resId,onProgressListener);
    }


    @NonNull
    private void loadLocalImage(@NonNull String path,@Nullable OnProgressListener onProgressListener)
    {
        startLoadThread(path,onProgressListener);
    }

    @Nullable
    private void loadHttpImage(@NonNull String url,@Nullable OnProgressListener onProgressListener)
    {
        startLoadThread(url,onProgressListener);
    }

    private void loadResImage(int resId,@Nullable OnProgressListener onProgressListener){
        startLoadThread(resId,onProgressListener);
    }

    private boolean isHttp(@NonNull String url)
    {
        return url.indexOf("http")==0;
    }

    private void startLoadThread(@NonNull final String url,@Nullable final OnProgressListener onProgressListener)
    {
        localThreadPool.execute(() -> {
            final Bitmap bitmapCache = getCacheImage(url);//从缓存中获取
            if(!ImageUtil.isOk(bitmapCache))
            {
                String path ;
                if(isHttp(url))
                {
                    path = fileDownloader.getCache(url);
                }
                else
                {
                    path = url;
                }
                utilRes(url,path,imageWidth,imageHeight,onProgressListener);
            }
            else
            {
                onSuccess(url,null,bitmapCache);
                if(onProgressListener!=null)
                {
                    onProgressListener.onProgress(1f,true);
                }
            }
        });
    }

    private void startLoadThread(@NonNull final int resId,@Nullable final OnProgressListener onProgressListener)
    {
        localThreadPool.execute(() -> {
            String url = ""+resId;
            final Bitmap bitmapCache = getCacheImage(url);//从缓存中获取
            if(!ImageUtil.isOk(bitmapCache))
            {
                utilRes(url,resId,imageWidth,imageHeight,onProgressListener);
            }
            else
            {
                onSuccess(url,null,bitmapCache);
                if(onProgressListener!=null)
                {
                    onProgressListener.onProgress(1f,true);
                }
            }
        });
    }

    private void postToUtilRes(String url,String path,ImageView iv,OnProgressListener onProgressListener)
    {
        final int width = iv.getWidth();
        final int height = iv.getHeight();
        RxJavaUtil.runOnIOThread(new RxJavaUtil.IOTask<Object>() {
            @Override
            public void onIOThread() {
                utilRes(url,path,width,height,onProgressListener);
            }
        });
    }

    private void runInQueue(@NonNull String url,@Nullable OnProgressListener onImageLoaderListener)
    {
        QueueInfo queueInfo = new QueueInfo(url,null,onImageLoaderListener);
        queuePoolList.remove(queueInfo);
        queuePoolList.add(queueInfo);
        if(downloaderQueuePool==null)
        {
            downloaderQueuePool = new DownloaderQueuePool();
        }
        downloaderQueuePool.startTimer();
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
                                        downloadThreadPool.execute(new LoadRunnable(queueInfo));
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
     * 刷新下载队列
     */
    private void refQueuePool()
    {
        downloaderQueuePool.startTimer();
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

    /**
     * 清除监听器
     */
    public void cleanListener()
    {
        onImageLoaderListener = null;
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
                key = isMergeUrlParameter?FileUtil.formatUrl(key):key;
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
        @NonNull
        String url ;
        @Nullable
        String path;
        @Nullable
        OnProgressListener onProgressListener;

        QueueInfo(@NonNull String url, @Nullable String path,@Nullable OnProgressListener onProgressListener) {
            this.url = url;
            this.path = path;
            this.onProgressListener = onProgressListener;
        }

        @NonNull
        public String getUrl() {
            return url;
        }

        public void setUrl(@NonNull String url) {
            this.url = url;
        }

        @Nullable
        public String getPath() {
            return path;
        }

        public void setPath(@Nullable String path) {
            this.path = path;
        }

        @Nullable
        public OnProgressListener getOnProgressListener() {
            return onProgressListener;
        }

        public void setOnProgressListener(@Nullable OnProgressListener onProgressListener) {
            this.onProgressListener = onProgressListener;
        }
    }

    private volatile Map<String ,Boolean> mapDownload = new HashMap<>();
    private class LoadRunnable implements Runnable
    {
        @NonNull
        private String url ;

        @Nullable
        private OnProgressListener onProgressListener;


        LoadRunnable(QueueInfo queueInfo)
        {
            this(queueInfo.url,queueInfo.onProgressListener);
        }

        LoadRunnable(@NonNull String url,@Nullable OnProgressListener onProgressListener) {
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
                String path ;
                if(isHttp(url))
                {
                    //网络链接
                    path = downloadImage(url,onProgressListener);

                }
                else
                {
                    path = url;
                }
                Image image = findImage(url);
                if(image!=null)
                {
                    Bitmap bitmap = loadLocalImage(url,path,imageWidth,imageHeight);
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
                    Bitmap bitmap = loadLocalImage(url,path,imageWidth,imageHeight);
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
                onSuccess(url,url,bitmapCache);
            }
            while (downloadList.contains(url))
            {
                downloadList.remove(url);
            }
            mapDownload.put(url,false);
            refQueuePool();
        }


        @Nullable
        private String downloadImage(@NonNull String url,@Nullable OnProgressListener onProgressListener)
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

    @Nullable
    private Bitmap loadLocalImage(@NonNull String url,@Nullable String path,int width,int height)
    {
        if(path==null||"".equals(path))
        {
            return null;
        }
        String prefix = "."+FileUtil.getPrefix(path);
        String tempSizeBitmapPath = path.replace(prefix,"")+"_"+width+"_"+height+prefix;
        if(isCacheTempSizeBitmap&&FileUtil.exists(tempSizeBitmapPath)){
            Bitmap bitmap = imageResolver.onResolver(url,tempSizeBitmapPath,width,height);
            if(bitmap==null||bitmap.isRecycled())
            {
                if(isAutoDelErrorBitmap){
                    FileUtil.delFileIfExists(tempSizeBitmapPath);
                }
                bitmap = imageResolver.onResolver(url,path,width,height);
                if(bitmap==null||bitmap.isRecycled())
                {
                    if(isAutoDelErrorBitmap){
                        FileUtil.delFileIfExists(tempSizeBitmapPath);
                    }
                    return null;
                }else{
                    ImageUtil.saveBitmap(bitmap,tempSizeBitmapPath);
                }
            }
            return bitmap;
        }
        else
        {
            Bitmap bitmap = imageResolver.onResolver(url,path,width,height);
            if(bitmap==null||bitmap.isRecycled())
            {
                if(isAutoDelErrorBitmap){
                    FileUtil.delFileIfExists(tempSizeBitmapPath);
                }
                return null;
            }
            return bitmap;
        }
    }

    @Nullable
    private Bitmap loadResImage(int resId,int width,int height){
        Bitmap bitmap = imageResolver.onResolver(""+resId,resId,width,height);
        if(bitmap==null||bitmap.isRecycled())
        {
            return null;
        }
        return bitmap;
    }

    private void utilRes(@NonNull String url,@Nullable String path,int width,int height,@Nullable OnProgressListener onProgressListener)
    {
        Bitmap bitmap = loadLocalImage(url,path,width,height);
        if(!ImageUtil.isOk(bitmap))
        {
            //本地图片读取出错
            runInQueue(url,onProgressListener);
        }
        else
        {
            onSuccess(url,path,bitmap);
            if(onProgressListener!=null)
            {
                onProgressListener.onProgress(1f,true);
            }
        }
    }

    private void utilRes(@NonNull String url,int resId,int width,int height,@Nullable OnProgressListener onProgressListener)
    {
        Bitmap bitmap = loadResImage(resId,width,height);
        if(!ImageUtil.isOk(bitmap))
        {
            //本地图片读取出错
            onFail(url);
        }
        else
        {
            onSuccess(url,null,bitmap);
            if(onProgressListener!=null)
            {
                onProgressListener.onProgress(1f,true);
            }
        }
    }

    private void onFail(@NonNull String url)
    {
        if(isStop)
        {
            return;
        }
        baseHandler.post(() -> {
            if(onImageLoaderListener!=null)
            {
                onImageLoaderListener.onFail(url);
            }
        });
    }

    private void onSuccess(@NonNull String url,@Nullable String path ,@NonNull Bitmap bitmap)
    {
        if(isStop)
        {
            return;
        }
        setCacheImage(url,bitmap);
        boolean isSetImageSuccess = false;
        final List<Image> images = findImages(url);
        for(Image image:images)
        {
            final ImageView imageView = image.getImageView();
            if(image.getUrl()!=null&&image.getUrl().equals(url))
            {
                if(imageView!=null)
                {
                    synchronized(this)
                    {
                        baseHandler.post(() ->
                        {
                            imageView.setImageBitmap(bitmap);
                            imageList.remove(image);
                            if(onImageLoaderListener!=null)
                            {
                                onImageLoaderListener.onSetImageSuccess(url);
                            }
                        });
                        isSetImageSuccess = true;
                    }
                }
                else
                {
                    LogUtil.w("ImageDownloader.onSuccess()->imageView==null");
                }
            }
        }
        if(!isSetImageSuccess)
        {
            if(onImageLoaderListener!=null)
            {
                onImageLoaderListener.onSetImageFail(url);
            }
        }
        baseHandler.post(() -> {
            if(onImageLoaderListener!=null)
            {
                onImageLoaderListener.onSuccess(url,path,bitmap);
            }
        });
    }

    private void setCacheImage(@NonNull String url,@NonNull Bitmap bitmap)
    {
        if(getCacheImage(url+imageWidth+"_"+imageHeight)==null)
        {
            if(imageLruCache!=null&&bitmap!=null)
            {
                url = isMergeUrlParameter?FileUtil.formatUrl(url):url;
                imageLruCache.put(url+imageWidth+"_"+imageHeight, bitmap);
            }
        }
    }

    @Nullable
    private Bitmap getCacheImage(@NonNull String url)
    {
        if(imageLruCache!=null&&url!=null)
        {
            url = isMergeUrlParameter?FileUtil.formatUrl(url):url;
            return imageLruCache.get(url+imageWidth+"_"+imageHeight);
        }
        return null;
    }

    private static class Image
    {
        String url;
        String tag ;

        WeakReference<ImageView> viewWeakReference ;

        String getUrl() {
            return url;
        }

        void setUrl(String url) {
            this.url = url;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
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
    private Image findImage(@Nullable ImageView imageView)
    {
        synchronized (imageList)
        {
            cleanNullImage();
            if(imageView!=null)
            {
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
            }
            Image image = new Image();
            imageList.add(image);
            return image;
        }
    }

    @NonNull
    private Image findImage(@NonNull String url)
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
                            String urlTemp = image.getUrl();
                            if(urlTemp!=null&&urlTemp.equals(url))
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

    @NonNull
    private List<Image> findImages(@NonNull String url)
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
                            if (image.getUrl()!=null&&image.getUrl().equals(url)) {
                                images.add(image);
                            }
                        }
                    }catch (Exception ignored)
                    {
                        LogUtil.exception(ignored);
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
     * 注意：开启之后，会出现利用了已经回收的Bitmap的错误，所以需要使用我提供的BaseImageView
     * 然后设置重新加载的接口，当发生崩溃的时候，重新加载图片。
     * @param isStrictMode {true:开启,false:关闭}
     */
    public static void setIsStrictMode(boolean isStrictMode) {
        ImageDownloader.isStrictMode = isStrictMode;
    }

    /**
     * 是否启动尺寸严格模式-启动之后会将图片按照比例裁剪到指定的最小尺寸
     * @param isSizeStrictMode boolean
     */
    public static void setIsSizeStrictMode(boolean isSizeStrictMode)
    {
        ImageDownloader.isSizeStrictMode = isSizeStrictMode;
    }

    public static void setDownloadThreadNum(int num){
        downloadThreadNum = num;
    }

    public static void setLoadThreadNum(int num){
        loadThreadNum = num;
    }

    public void setImageResolver(@NonNull ImageResolver imageResolver) {
        this.imageResolver = imageResolver;
    }

    public void isCacheTempSizeBitmap(Boolean value){
        isCacheTempSizeBitmap = value;
    }

    public void isAutoDelErrorBitmap(boolean value){
        isAutoDelErrorBitmap = value;
    }

    private OnImageLoaderListener onImageLoaderListener ;

    public OnImageLoaderListener getOnImageLoaderListener() {
        return onImageLoaderListener;
    }

    public void setOnImageLoaderListener(OnImageLoaderListener onImageLoaderListener) {
        this.onImageLoaderListener = onImageLoaderListener;
    }

    public interface OnImageLoaderListener
    {
        void onSuccess(@NonNull String url,@Nullable String path,@NonNull Bitmap bitmap);

        void onFail(@NonNull String url);

        default void onSetImageFail(@NonNull String url){}

        default void onSetImageSuccess(@NonNull String url){}
    }

    public interface ImageResolver{

        @Nullable
        Bitmap onResolver(@NonNull String url,@Nullable String path,int width,int height);

        @Nullable
        default Bitmap onResolver(@NonNull String url,int resId,int width,int height){
            return null;
        }
    }

    private class TJImageResolver implements ImageResolver{

        @Override
        public Bitmap onResolver(@NonNull String url,@Nullable String path, int width, int height) {
            Bitmap bitmap = null;
            if(width>0&&height>0)
            {
                String fix = FileUtil.getPrefix(path);
                if(fix!=null&&fix.equalsIgnoreCase("svg"))
                {
                    bitmap = SVGUtil.getSvgBitmap(path,width,height);
                }
                else
                {
                    if(isSizeStrictMode)
                    {
                        bitmap = ImageUtil.compressImageRGB(path,imageWidth,imageHeight);
                        if(ImageUtil.isOk(bitmap))
                        {
                            float widthTemp = (float)width;
                            float heightTemp = widthTemp*(float)bitmap.getHeight()/(float)bitmap.getWidth();
                            if((widthTemp<bitmap.getWidth()||heightTemp<bitmap.getHeight())&&widthTemp!=0&&heightTemp!=0)
                            {
                                Bitmap bitmap2 = ImageUtil.matrixBitmapRGB(bitmap,widthTemp,heightTemp);
                                if(!bitmap.equals(bitmap2))
                                {
                                    ImageUtil.recycled(bitmap);
                                }
                                bitmap = bitmap2;
                            }
                        }
                    }
                    else
                    {
                        bitmap = ImageUtil.compressImageRGB(path,imageWidth,imageHeight);
                    }
                }
            }
            else
            {
                bitmap = ImageUtil.getBitmap(path);
            }
            return bitmap;
        }

        @Override
        public Bitmap onResolver(@NonNull String url,int resId,int width,int height){
            Bitmap bitmap = null;
            if(width>0&&height>0)
            {
                if(isSizeStrictMode)
                {
                    bitmap = BitmapFactory.decodeResource(BaseApplication.getContext().getResources(),resId);
                    if(ImageUtil.isOk(bitmap))
                    {
                        float widthTemp = (float)width;
                        float heightTemp = widthTemp*(float)bitmap.getHeight()/(float)bitmap.getWidth();
                        if((widthTemp<bitmap.getWidth()||heightTemp<bitmap.getHeight())&&widthTemp!=0&&heightTemp!=0)
                        {
                            Bitmap bitmap2 = ImageUtil.matrixBitmapRGB(bitmap,widthTemp,heightTemp);
                            if(!bitmap.equals(bitmap2))
                            {
                                ImageUtil.recycled(bitmap);
                            }
                            bitmap = bitmap2;
                        }
                    }
                }
                else
                {
                    bitmap = ImageUtil.compressImage(resId,width,height);
                }
            }
            else
            {
                bitmap = BitmapFactory.decodeResource(BaseApplication.getContext().getResources(),resId);
            }
            return bitmap;
        }
    }
}

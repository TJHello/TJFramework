package com.tjbaobao.framework.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.LruCache;

import com.tjbaobao.framework.entity.BitmapConfig;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImageDownloaderUtil implements FileDownloaderUtil.OnFileDownloaderListener {
	private final static int maxMemory = (int) (Runtime.getRuntime().maxMemory() /1024);
	private final static int cacheSize = maxMemory / 4;
	private static LruCache<String, Bitmap> imageLruCache = new LruCache<String, Bitmap>(cacheSize){
		protected int sizeOf(String key, Bitmap value) {
			return value.getByteCount() / 1024;
		};
	};
	private ExecutorService cachedThreadPool  = Executors.newCachedThreadPool();
	private int imageWidth = 0,imageHeight = 0;
	private FileDownloaderUtil fileDownloaderUtil ;
	private OnImgDownloaderListener onImgDownloaderListener ;
	private Bitmap defaultBitmap = null;

	private Map<String ,String> codeMap = new HashMap<>();
	
	private class Handler_What
	{
		public static final int onCompleteImage= 1;
		public static final int onFail= 2;
		public static final int compressImage= 3;
	}
	

	
	public ImageDownloaderUtil() {
		fileDownloaderUtil = new FileDownloaderUtil();
		fileDownloaderUtil.setOnFileDownloadListener(this);
		//设置默认图片
		Bitmap bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
		bitmap.eraseColor(Color.parseColor("#FDFDFD"));//填充颜色
		defaultBitmap = bitmap  ;
	}
	
	/**
	 * 加载图片
	 * @param url
	 * @return
	 */
	public BitmapConfig loadBitmap(String url)
	{
		if(url==null||url.equals(""))
		{
			return null;
		}
		Bitmap bitmap = imageLruCacheGet(url);
		if(bitmap!=null)
		{
			Bundle bundle = new Bundle();
			bundle.putString("url", url);
			startComImageThread(url,url);
			//sendMessage(Handler_What.onCompleteImage,bitmap,bundle);
			return getBitmapConfig(bitmap);
		}
		else
		{
			if(url.indexOf("http")!=-1||url.indexOf("https")!=-1)
			{
				//加载网络图片
				return loadHttpBitmap(url);
			}
			else
			{
				//加载本地图片
				return loadLocalBitmap(url);
			}
		}
	}

	public BitmapConfig loadBitmapByCode(String url, String code)
	{
		url = url+"?fileCode="+code+"&fileType=.jpg";
		if(url==null||url.equals(""))
		{
			return null;
		}
		Bitmap bitmap = imageLruCacheGet(url);
		codeMap.put(url,code);
		if(bitmap!=null)
		{
			startComImageThread(url,url,code);
			return getBitmapConfig(bitmap);
		}
		else
		{
			if(url.indexOf("http")!=-1||url.indexOf("https")!=-1)
			{
				//加载网络图片
				return loadHttpBitmap(url);
			}
			else
			{
				//加载本地图片
				return loadLocalBitmap(url);
			}
		}
	}
	
	/**
	 * 加载网络图片
	 * @param url 图片链接
	 * @return 图片配置
	 */
	private BitmapConfig loadHttpBitmap(String url)
	{
		String prefix = FileUtil.getPrefix(url);
		String tempUrl = url;
		try {
			tempUrl = URLDecoder.decode(tempUrl,"UTF-8").replaceAll("\\+", "%20").replaceAll("/","").replaceAll(":","_").replace("\\?","_").replace("=","");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String path = ConstantUtil.getImageCachePath()+tempUrl+(prefix==null?".jpg":"");
		String downloadPath = fileDownloaderUtil.downloadFile(url,path);
		if(downloadPath!=null)
		{
			startComImageThread(url,downloadPath);
			BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
			bmpFactoryOptions.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(downloadPath, bmpFactoryOptions);
			return getBitmapConfig(bmpFactoryOptions);
		}
		else
		{
			BitmapConfig bitmapConfig = new BitmapConfig();
			bitmapConfig.setHeight(500);
			bitmapConfig.setWidth(500);
			return bitmapConfig;
		}
	}

	/**
	 * 加载本地图片
	 * @param path 图片路径
	 * @return
	 */
	private BitmapConfig loadLocalBitmap(String path)
	{
		if(FileUtil.exists(path))
		{
			BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
			bmpFactoryOptions.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(path, bmpFactoryOptions);
			startComImageThread(path,path);
			return getBitmapConfig(bmpFactoryOptions);
		}
		else
		{
			InputStream inputStream = Tools.getAssetsInputSteam(path);
			if(inputStream!=null)
			{
				BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
				bmpFactoryOptions.inJustDecodeBounds = true;
				BitmapFactory.decodeStream(inputStream,null,bmpFactoryOptions);
				startComImageThread(path,path);
				return getBitmapConfig(bmpFactoryOptions);
			}
			else
			{
				sendMessage(Handler_What.onFail, null);
			}
			return null;
		}
	}
	
	/**
	 * 压缩图片线程
	 * @author TJbaobao
	 * @time 2017年5月19日
	 */
	private class ComImageRunnable implements Runnable
	{
		private String url ,path,code;
		public ComImageRunnable(String url, String path) {
			this.url = url;
			this.path = path;
		}
		public ComImageRunnable(String url, String path, String code)
		{
			this.url = url;
			this.path = path;
			this.code = code;
		}
		@Override
		public void run() {
			Bitmap bitmap = imageLruCacheGet(path);
			if(bitmap==null&&code!=null)
			{
				bitmap = imageLruCacheGet(code);
			}
			if(bitmap==null||bitmap.isRecycled())
			{
				if(imageWidth!=0||imageHeight!=0)
				{
					bitmap = ImageUtil.compressImage(path, imageWidth, imageHeight);
				}
				else
				{
					bitmap = ImageUtil.compressImage(path);
				}
				if(bitmap!=null)
				{
					imageLruCacheSet(url, bitmap);
					if(code!=null)
					{
						imageLruCacheSet(code, bitmap);
					}
				}
			}
			if(bitmap!=null)
			{

				Bundle bundle = new Bundle();
				bundle.putString("url", url);
				bundle.putString("code",code);
				sendMessage(Handler_What.onCompleteImage,bitmap,bundle);
			}
			else
			{
				sendMessage(Handler_What.onFail,null);
			}
		}
	}
	
	/**
	 * 启动图片压缩线程
	 * @param url 图片链接
	 * @param path 图片本地地址
	 */
	private void startComImageThread(String url, String path)
	{
		cachedThreadPool.execute(new ComImageRunnable(url,path));
	}
	private void startComImageThread(String url, String path, String code)
	{
		cachedThreadPool.execute(new ComImageRunnable(url,path,code));
	}
	/**
	 * 通过图片获取图片配置信息
	 * @param bitmap
	 * @return
	 */
	public BitmapConfig getBitmapConfig(Bitmap bitmap)
	{
		if(bitmap==null)
		{
			return null;
		}
		BitmapConfig bitmapConfig = new BitmapConfig();
		bitmapConfig.setWidth(bitmap.getWidth());
		bitmapConfig.setHeight(bitmap.getHeight());
		bitmapConfig.setSize(bitmap.getByteCount());
		return bitmapConfig;
	}
	
	/**
	 * 通过图片选项获取图片信息
	 * @param bmpFactoryOptions
	 * @return
	 */
	private BitmapConfig getBitmapConfig(BitmapFactory.Options bmpFactoryOptions)
	{
		if(bmpFactoryOptions==null)
		{
			return null;
		}
		BitmapConfig bitmapConfig = new BitmapConfig();
		bitmapConfig.setWidth(bmpFactoryOptions.outWidth);
		bitmapConfig.setHeight(bmpFactoryOptions.outHeight);
		bitmapConfig.setSize(bmpFactoryOptions.inSampleSize);
		return bitmapConfig;
	}
	
	private void sendMessage(int what,Object obj)
	{
		Message msg = new Message();
		msg.what = what ;
		msg.obj = obj ;
		handler.sendMessage(msg);
	}
	
	private void sendMessage(int what, Object obj, Bundle bundle)
	{
		Message msg = new Message();
		msg.what = what ;
		msg.obj = obj ;
		msg.setData(bundle);
		handler.sendMessage(msg);
	}

	private void sendMessage(int what,Bundle bundle)
	{
		Message msg = new Message();
		msg.what = what ;
		msg.setData(bundle);
		handler.sendMessage(msg);
	}
	
	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			if(msg.what== Handler_What.compressImage)
			{
				
			}
			else if(msg.what== Handler_What.onCompleteImage)
			{
				Bitmap bitmap = (Bitmap) msg.obj;
				String url = msg.getData().getString("url");
				String code = msg.getData().getString("code");
				if(bitmap!=null)
				{
					if(onImgDownloaderListener!=null)
					{
						onImgDownloaderListener.onComplete(url, bitmap);
						onImgDownloaderListener.onComplete(code, bitmap);
					}
				}
				else
				{
					if(onImgDownloaderListener!=null)
					{
						onImgDownloaderListener.onFail(url,defaultBitmap);
					}
				}
			}
			else if(msg.what== Handler_What.onFail)
			{
				String url  = (String) msg.obj;
				if(onImgDownloaderListener!=null)
				{
					onImgDownloaderListener.onFail(url,defaultBitmap);
				}
			}
		};
	};

	@Override
	public void onComplete(String url, String path)
	{
		startComImageThread(url,path);
	}

	@Override
	public void onFail(String url) {
		sendMessage(Handler_What.onFail, null);
	}

	/**
	 * 图片下载监听器
	 * @author TJbaobao
	 * @time 2017年5月19日
	 */
	public interface OnImgDownloaderListener
	{
		public void onComplete(String url, Bitmap bitmap);
		public void onFail(String url, Bitmap defaultBitmap);
	}
	
	
	public OnImgDownloaderListener getOnImgDownloaderListener() {
		return onImgDownloaderListener;
	}

	/**
	 * 设置图片监听下载器
	 * @param onImgDownloaderListener
	 */
	public void setOnImgDownloaderListener(OnImgDownloaderListener onImgDownloaderListener) {
		this.onImgDownloaderListener = onImgDownloaderListener;
	}

	/**
	 * 设置默认图片大小-决定压缩之后的图片大小以及质量
	 * @param width
	 * @param height
	 */
	public void setDefauleImgSize(int width,int height)
	{
		this.imageWidth = width ;
		this.imageHeight  = height ;
	}
	
	/**
	 * 获取默认图片
	 * @return
	 */
	public Bitmap getDefaultBitmap() {
		return defaultBitmap;
	}
	
	/**
	 * 设置默认Bitmap
	 * @param defaultBitmap
	 */
	public void setDefaultBitmap(Bitmap defaultBitmap) {
		this.defaultBitmap = defaultBitmap;
	}
	
	public Bitmap imageLruCacheGet(String key)
	{
		if(imageLruCache!=null&&key!=null)
		{
			return imageLruCache.get(key+imageWidth+"_"+imageHeight);
		}
		return null;
	}
	
	public void imageLruCacheSet(String key, Bitmap value)
	{
		if(imageLruCacheGet(key+imageWidth+"_"+imageHeight)==null)
		{
			if(imageLruCache!=null)
			{
				imageLruCache.put(key+imageWidth+"_"+imageHeight, value);
			}
		}
	}

	public void remove(String key)
	{
		if(imageLruCache!=null)
		{
			imageLruCache.remove(key+imageWidth+"_"+imageHeight);
		}
	}

	/**
	 * 释放资源
	 */
	public void release()
	{
		if(imageLruCache!=null)
		{
			if (imageLruCache.size() > 0) 
			{
				imageLruCache.evictAll();
            }
		}
	}

	public String getCodeByUrl(String url)
	{
		return codeMap.get(url);
	}

}

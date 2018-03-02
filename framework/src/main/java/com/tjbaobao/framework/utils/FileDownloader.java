package com.tjbaobao.framework.utils;


import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.annotation.Nullable;

import com.tjbaobao.framework.database.dao.TbFileDAO;
import com.tjbaobao.framework.database.obj.TbFileObj;
import com.tjbaobao.framework.listener.OnProgressListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FileDownloader {

	private static volatile List<String> downloadList = new ArrayList<>();//下载列表
	private static volatile List<QueueInfo> queuePoolList = new ArrayList<>();//等待队列
	private static final Map<String,String> downLoadHosts = new HashMap<>();//连接与本地地址映射
	private static final FileDownloader fileDownloader = new FileDownloader();
	private static BaseHandler baseHandler ;
	private static boolean isShop = false;


	private FileDownloader(){

	}

	public static FileDownloader getInstance()
	{
		baseHandler = new BaseHandler();
		isShop = false;
		return fileDownloader;
	}

	public String download(String url,String path)
	{
		return download(url,path,null);
	}

	public String download(String url, String path, @Nullable OnProgressListener onProgressListener)
	{
		if(url==null||url.equals(""))
		{
			return null;
		}
		String outPath = getCache(url);
		if(outPath==null)
		{
			//文件不存在
			if(url.indexOf("http")==0)
			{
				startDownloadThread(url,path,onProgressListener);
				return null;
			}
			else
			{
				onSuccess(url,outPath);
				return outPath;
			}
		}
		else
		{
			onSuccess(url,outPath);
			return outPath;
		}
	}

	@Nullable
	public String downloadExecute(String url, String path, OnProgressListener onProgressListener)
	{
		if(url==null||url.equals(""))
		{
			return null;
		}
		String outPath = getCache(url);
		if(outPath==null)
		{
			if(url.indexOf("http")==0)
			{
				boolean isContains = downloadList.contains(url);
				if(!isContains)
				{
					downloadList.add(url);
					boolean isOk = OKHttpUtil.download(url,path,onProgressListener);
					downloadList.remove(url);
					if(isOk)
					{
						TbFileDAO.addFile(url,path,FileUtil.getPrefix(path));
						downLoadHosts.put(url,path);
						onSuccess(url,path);
						return path;
					}
				}
			}
			else
			{
				onSuccess(url,url);
				return url;
			}
		}
		else
		{
			onSuccess(url,outPath);
			return outPath;
		}
		onFail(url);
		return null;
	}

	public String getCache(String url)
	{
		String outPath = downLoadHosts.get(url);
		if(!FileUtil.exists(outPath))
		{
			outPath = TbFileDAO.getFilePathByUrl(url);
			if(!FileUtil.exists(outPath))
			{
				return null;
			}
		}
		return outPath;
	}

	private static final ExecutorService executorService = Executors.newFixedThreadPool(3);
	private void startDownloadThread(String url,String path,OnProgressListener onProgressListener)
	{
		QueueInfo queueInfo = new QueueInfo(url,path,onProgressListener);
		queuePoolList.add(queueInfo);
		if(downloaderQueuePool==null)
		{
			downloaderQueuePool = new DownloaderQueuePool();
		}
		downloaderQueuePool.startTimer(0,500);
	}

	private DownloaderQueuePool downloaderQueuePool ;
	private class DownloaderQueuePool extends BaseTimerTask
	{
		@Override
		public BaseTimerTask startTimer(long delay, long period) {
			if(isCancel)
			{
				return super.startTimer(delay, period);
			}
			return this;
		}

		@Override
		public void run() {
			int length = queuePoolList.size();
			if(length>0)
			{
				for (int i = length-1; i >= 0; i--)
				{
					if(downloadList.size()<3)
					{
						QueueInfo queueInfo = queuePoolList.get(i);
						if(!downloadList.contains(queueInfo.getUrl()))
						{
							executorService.execute(new DownloadRunnable(queueInfo));
							downloadList.add(queueInfo.getUrl());
							queuePoolList.remove(queueInfo);
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

	public void stop()
	{
		isShop = true;
		if(downloaderQueuePool!=null)
		{
			downloaderQueuePool.stopTimer();
		}
	}

	public static boolean isStop()
	{
		return isShop;
	}

	private class QueueInfo
	{
		String url ;
		String path;
		OnProgressListener onProgressListener;

		public QueueInfo(String url, String path, OnProgressListener onProgressListener) {
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

	private class DownloadRunnable implements Runnable {

		private String url ,path;
		private OnProgressListener onProgressListener ;

		DownloadRunnable(QueueInfo queueInfo)
		{
			this(queueInfo.url,queueInfo.path,queueInfo.onProgressListener);
		}
		DownloadRunnable(String url, String path,OnProgressListener onProgressListener) {
			this.url = url;
			this.path = path;
			this.onProgressListener = onProgressListener;
		}
		@Override
		public void run() {
			if(isShop)
			{
				return ;
			}
			boolean isOk = OKHttpUtil.download(url,path,onProgressListener);
			downloadList.remove(url);
			if(isOk)
			{
				TbFileDAO.addFile(url,path,FileUtil.getPrefix(path));
				downLoadHosts.put(url,path);
				onSuccess(url,path);
			}
			else
			{
				onFail(url);
			}
		}
	}

	private OnFileDownloadListener onFileDownloadListener ;

	public OnFileDownloadListener getOnFileDownloadListener() {
		return onFileDownloadListener;
	}

	public void setOnFileDownloadListener(OnFileDownloadListener onFileDownloadListener) {
		this.onFileDownloadListener = onFileDownloadListener;
	}

	public interface OnFileDownloadListener
	{
		void onSuccess(String url,String path);

		void onFail(String url);
	}

	private void onSuccess(String url,String path)
	{
		baseHandler.post(() -> {
			if(onFileDownloadListener!=null)
			{
				onFileDownloadListener.onSuccess(url,path);
			}
        });

	}

	private void onFail(String url)
	{
		baseHandler.post(() -> {
			if(onFileDownloadListener!=null)
			{
				onFileDownloadListener.onFail(url);
			}
		});
	}

	public static String getFilePath(String url)
	{
		if(url==null)
		{
			return null;
		}
		if(url.indexOf("http://")==0||url.indexOf("https://")==0)
		{
			TbFileObj fileObj = TbFileDAO.getFileByUrl(url);
			if(fileObj!=null)
			{
				return fileObj.getPath();
			}
			else {
				return null;
			}
		}
		return url;
	}

}

package com.tjbaobao.framework.utils;


import com.tjbaobao.framework.database.dao.TbFileDAO;
import com.tjbaobao.framework.listener.OnProgressListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FileDownloader {

	private static final List<String> downloadList = new ArrayList<>();//下载列表
	private static final Map<String,String> downLoadHosts = new HashMap<>();//连接与本地地址映射
	private static final FileDownloader fileDownloader = new FileDownloader();
	private static BaseHandler baseHandler ;

	private FileDownloader(){}

	public static FileDownloader getInstance()
	{
		baseHandler = new BaseHandler();
		return fileDownloader;
	}

	public void download(String url,String path)
	{
		download(url,path,null);
	}

	public void download(String url, String path, OnProgressListener onProgressListener)
	{
		if(url==null||url.equals(""))
		{
			return ;
		}
		String outPath = downLoadHosts.get(url);
		if(!FileUtil.exists(outPath))
		{
			outPath = TbFileDAO.getFilePathByUrl(url);
			if(!FileUtil.exists(outPath))
			{
				//文件不存在
				if(url.indexOf("http")==0)
				{
					startDownloadThread(url,path,onProgressListener);
				}
				else
				{
					onSuccess(url,outPath);
				}
			}
			else
			{
				onSuccess(url,outPath);
			}
		}
		else
		{
			onSuccess(url,outPath);
		}
	}

	public String downloadExecute(String url, String path, OnProgressListener onProgressListener)
	{
		if(url==null||url.equals(""))
		{
			return null;
		}
		String outPath = downLoadHosts.get(url);
		if(!FileUtil.exists(outPath))
		{
			outPath = TbFileDAO.getFilePathByUrl(url);
			if(!FileUtil.exists(outPath))
			{
				//文件不存在

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
							return path;
						}
					}
				}
				else
				{
					return outPath;
				}
			}
			else
			{
				return outPath;
			}
		}
		else
		{
			return outPath;
		}
		return null;
	}

	private static final ExecutorService executorService = Executors.newFixedThreadPool(3);
	private void startDownloadThread(String url,String path,OnProgressListener onProgressListener)
	{
		if(!downloadList.contains(url))
		{
			executorService.execute(new DownloadRunnable(url,path,onProgressListener));
			downloadList.add(url);
		}
	}

	private class DownloadRunnable implements Runnable {

		private String url ,path;
		private OnProgressListener onProgressListener ;

		public DownloadRunnable(String url, String path,OnProgressListener onProgressListener) {
			this.url = url;
			this.path = path;
			this.onProgressListener = onProgressListener;
		}
		@Override
		public void run() {
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

}

package com.tjbaobao.framework.utils;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.tjbaobao.framework.database.dao.TbFileDAO;
import com.tjbaobao.framework.database.obj.TbFileObj;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FileDownloaderUtil {

	private OnFileDownloaderListener onFileDownloadListener;
	private Map< String, String> urlMap = new HashMap<String, String>();
	private DownloadHandler handler = new DownloadHandler();
	public FileDownloaderUtil() {
	}
	/**
	 * 下载文件
	 * @param url 文件url
	 * @param path 文件下载位置
	 * @return
	 */
	public String downloadFile(String url,String path)
	{
		if(url==null||url.equals(""))
		{
			sendMessage(Handler_What.onFail,url,"");
			return null;
		}
		if(url.indexOf("http://")==0||url.indexOf("https://")==0)
		{
			//从内存中查找对应的文件地址
			String pathTemp = urlMap.get(url);
			if(pathTemp==null)
			{
				TbFileObj fileObj = TbFileDAO.getFileByUrl(url);
				if(fileObj!=null)
				{
					pathTemp = fileObj.getPath();
				}
				if(pathTemp!=null)
				{
					path = pathTemp;
				}
			}
			if(pathTemp!=null&&!pathTemp.equals(""))
			{
				if(FileUtil.exists(pathTemp))
				{
					//sendMessage(Handler_What.onCompleteFile,url,pathTemp);
					return pathTemp;
				}
			}
			startFileDdownloadThread(url,path);
			return null;
		}
		else
		{
			//读取本地文件
			urlMap.put(url, url);
			return url ;
		}
	}
	
	private void startFileDdownloadThread(String url,String path)
	{
		new FileDownloadThread(url,path).start();
	}
	/**
	 * 文件下载线程
	 * @author TJbaobao
	 * @time 2017年5月8日
	 */
	private class FileDownloadThread extends Thread
	{
		String url ;
		String path ;
		NetworkUtil mNetworkUtil ;
		public FileDownloadThread(String url,String path) {
			this.url = url;
			this.path = path ;
		}
		@Override
		public void run() {
			mNetworkUtil = new NetworkUtil();
			mNetworkUtil.setOnProgressLinstener(new MyOnProgressLinstener());
			boolean isOk = mNetworkUtil.downloadFileNew(url, path);
			if(isOk)
			{
				sendMessage(Handler_What.onCompleteFile,url,path);
				String prefix = FileUtil.getPrefix(url);
				TbFileDAO.addFile(url,path,prefix);
				if(prefix!=null)
				{

				}
				else
				{
					sendMessage(Handler_What.onFail,url,"");
				}
			}
			else
			{
				sendMessage(Handler_What.onFail,url,"");
			}
		}
	}

	private class MyOnProgressLinstener implements NetworkUtil.OnProgressLinstener
	{
		@Override
		public void onProgress(String url, int length, int index) {
			float progress =(float)index/ (float)length;
			sendMessage(Handler_What.onProgress,url,progress);
		}
	}

	
	
	private class Handler_What
	{
		public static final int onCompleteFile = 0;
		public static final int onFail= 2;
		public static final int onProgress = 3;
		
	}
	@SuppressLint("HandlerLeak")
	private class DownloadHandler extends Handler
	{
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what)
			{
			case Handler_What.onCompleteFile:
				if(onFileDownloadListener!=null)
				{
					onFileDownloadListener.onComplete(msg.getData().getString("url"), (String)msg.obj);
				}
				break;
			case Handler_What.onFail:
				if(onFileDownloadListener!=null)
				{
					onFileDownloadListener.onFail(msg.getData().getString("url"));
				}
				break;
			case Handler_What.onProgress:
				String url = msg.getData().getString("url");
				float progress = (float) msg.obj;
				if(mOnFileDownloaderProgressListener!=null)
				{
					mOnFileDownloaderProgressListener.onProgress(url,progress);
				}
				break;
			}
			super.handleMessage(msg);
		}
	}
	private void sendMessage(int what,String url,Object obj)
	{
		Message msg = new Message();
		Bundle data = new Bundle();  
		data.putString("url", url); 
		msg.setData(data);
		msg.obj = obj;
		msg.what = what ;
		handler.sendMessage(msg);
	}
	
	public interface OnFileDownloaderListener
	{
		public void onComplete(String url, String path);
		public void onFail(String url);
	}
	public interface OnFileDownloaderProgressListener
	{
		public void onProgress(String url, float progress);

	}
	private OnFileDownloaderProgressListener mOnFileDownloaderProgressListener ;

	public OnFileDownloaderProgressListener getOnFileDownloaderProgressListener() {
		return mOnFileDownloaderProgressListener;
	}

	public void setOnFileDownloaderProgressListener(OnFileDownloaderProgressListener onFileDownloaderProgressListener) {
		mOnFileDownloaderProgressListener = onFileDownloaderProgressListener;
	}

	public OnFileDownloaderListener getOnFileDownloadListener() {
		return onFileDownloadListener;
	}
	public void setOnFileDownloadListener(OnFileDownloaderListener onFileDownloadListener) {
		this.onFileDownloadListener = onFileDownloadListener;
	}
	
	/**
	 * 获取视频文件的存储路径
	 * @param prefix 格式 mp4
	 * @return
	 */
	public static String getVideoFilePath(String prefix)
	{
		return ConstantUtil.getVideoCachePath()+UUID.randomUUID().toString()+"."+prefix;
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
		}
		return url;
	}

}

package com.tjbaobao.framework.utils;

import android.os.Environment;

import java.io.File;

/**
 * 常量数据组件
 * @author lyzyjoyo 
 */
public class ConstantUtil
{
	
	/**
	 * MAC地址
	 */
	public static String MAC="";
	
	public static int IMAGE_CACHE_TIME = 60*60*24*7;
	public static int VIDEO_CACHE_TIME = 60*60*24*3;
	public static int FILE_CACHE_TIME = 60*60*24*3 ;
	
	private static String APP_EXCEPTION_LOG_PATH="";
	/**
	 * 获取应用异常日志文件目录
	 * @return 应用异常日志文件目录
	 */
	public static String getAppExcepitionLogPath( )
	{
		if(APP_EXCEPTION_LOG_PATH.equals(""))
		{
			APP_EXCEPTION_LOG_PATH=getAppHomePath()+"log/";
		}
		FileUtil.createFolder(IMAGE_CACHE_PATH);
		return APP_EXCEPTION_LOG_PATH;
	}

	public static String getMyAppPath()
	{
		String homePath = Environment.getExternalStorageDirectory().getPath()+"/"+DeviceUtil.getAppName()+"/";
		return homePath;
	}

	public static String getImageAppPath()
	{
		String path = getMyAppPath()+"Image/";
		FileUtil.createFolder(path);
		return path;
	}

	private static String getAppHomePath()
	{
		String homePath = Environment.getExternalStorageDirectory().getPath()+"/Android/data/";
		File file = new File(homePath);
		if(file.exists())
		{
			return homePath +DeviceUtil.getPackageName()+"/";
		}
		else
		{
			return "/data/data/"+DeviceUtil.getPackageName()+"/";
		}
	}
	private static String getAppCachePath()
	{
		return getAppHomePath()+"cache/";
	}
	//图片缓存目录
	private static String IMAGE_CACHE_PATH="";
	public static String getImageCachePath()
	{
		if(IMAGE_CACHE_PATH.equals(""))
		{
			IMAGE_CACHE_PATH = getAppCachePath()+"image/";
		}
		FileUtil.createFolder(IMAGE_CACHE_PATH);
		return IMAGE_CACHE_PATH;
	}
	//视频缓存目录
	private static String VIDEO_CACHE_PATH = "";
	public static String getVideoCachePath()
	{
		if(VIDEO_CACHE_PATH.equals(""))
		{
			VIDEO_CACHE_PATH = getAppCachePath()+"video/";
		}
		FileUtil.createFolder(VIDEO_CACHE_PATH);
		return VIDEO_CACHE_PATH;
	}
	//文件缓存目录
	private static String FILE_CACHE_PATH="";
	public static String getFileCachePath()
	{
		if(FILE_CACHE_PATH.equals(""))
		{
			FILE_CACHE_PATH = getAppCachePath()+"file/";
		}
		FileUtil.createFolder(FILE_CACHE_PATH);
		return FILE_CACHE_PATH;
	}
	private static String FILE_PATH = "";
	public static String getFilePath()
	{
		if(FILE_PATH.equals(""))
		{
			FILE_PATH = getAppHomePath()+"file/";
		}
		FileUtil.createFolder(FILE_PATH);
		return FILE_PATH;
	}
	private static String FILE_IMAGE_PATH = "";
	public static String getFileImagePath()
	{
		if(FILE_IMAGE_PATH.equals(""))
		{
			FILE_IMAGE_PATH = getFilePath()+"image/";
		}
		FileUtil.createFolder(FILE_IMAGE_PATH);
		return FILE_IMAGE_PATH;
	}

	private static String FILE_TEMP_IMAGE_PATH = "";
	public static String getFileTempImagePath()
	{
		if(FILE_TEMP_IMAGE_PATH.equals(""))
		{
			FILE_TEMP_IMAGE_PATH = getFilePath()+"tempImage/";
		}
		FileUtil.createFolder(FILE_TEMP_IMAGE_PATH);
		return FILE_TEMP_IMAGE_PATH;
	}


	//音频缓存目录
	private static String REC_CACHE_PATH="";
	public static String getRecCachePath(String username)
	{
		if(REC_CACHE_PATH.equals(""))
		{
			REC_CACHE_PATH = getFileCachePath()+"voice/"+username+"/";
		}
		FileUtil.createFolder(IMAGE_CACHE_PATH);
		return REC_CACHE_PATH;
	}
	
	private static final String Voice_Home_Path = getFileCachePath()+"voice/";
	
	public static String getVoiceHomePathByAccountNumber(String accountNumber)
	{
		String path = Voice_Home_Path+accountNumber+"/";
		FileUtil.createFolder(path);
		return path;
	}
}



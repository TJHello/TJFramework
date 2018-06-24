package com.tjbaobao.framework.utils;

import android.os.Environment;

import com.tjbaobao.framework.base.BaseApplication;

import java.io.File;

/**
 * 常量工具类(获取常用路径)
 * @author tjbaobao
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class ConstantUtil
{
	private static final String SEPARATOR = File.separator;
	private static final String FILES_PATH = getFilesPath();
	private static final String CACHE_PATH = getCachePath();

	private static final String IMAGE_PATH = "image"+SEPARATOR;
	private static final String VIDEO_PATH = "video"+SEPARATOR;
	private static final String CONFIG_PATH = "config"+SEPARATOR;
	private static final String LOG_PATH = "log"+SEPARATOR;

	private static final String IMAGE_FILES_PATH = FILES_PATH +IMAGE_PATH ;
	private static final String VIDEO_FILES_PATH = FILES_PATH+VIDEO_PATH;
	private static final String CONFIG_FILES_PATH = FILES_PATH+CONFIG_PATH;
	private static final String LOG_FILES_PATH = FILES_PATH+LOG_PATH;
	private static final String IMAGE_CACHE_PATH = CACHE_PATH +IMAGE_PATH ;
	private static final String VIDEO_CACHE_PATH = CACHE_PATH+VIDEO_PATH;
	private static final String CONFIG_CACHE_PATH = CACHE_PATH+CONFIG_PATH;

	static {
		FileUtil.createFolder(IMAGE_FILES_PATH);
		FileUtil.createFolder(VIDEO_FILES_PATH);
		FileUtil.createFolder(CONFIG_FILES_PATH);
		FileUtil.createFolder(IMAGE_CACHE_PATH);
		FileUtil.createFolder(VIDEO_CACHE_PATH);
		FileUtil.createFolder(CONFIG_CACHE_PATH);
	}

	public static String getFilesPath()
	{
		return BaseApplication.getContext().getFilesDir().getPath()+SEPARATOR;
	}

	public static String getCachePath()
	{
		return BaseApplication.getContext().getCacheDir().getPath()+SEPARATOR;
	}

	public static String getMyAppPath()
	{
		String appName = DeviceUtil.getAppName().replace(".","_").replace("/","_");
		String homePath = Environment.getExternalStorageDirectory().getPath()+"/"+appName+"/";
		FileUtil.createFolder(homePath);
		return homePath;
	}

	public static String getImageFilesPath()
	{
		return IMAGE_FILES_PATH ;
	}

	public static String getVideoFilesPath()
	{
		return VIDEO_FILES_PATH;
	}

	public static String getConfigFilesPath() {
		return CONFIG_FILES_PATH;
	}

	public static String getImageCachePath() {
		return IMAGE_CACHE_PATH;
	}

	public static String getVideoCachePath() {
		return VIDEO_CACHE_PATH;
	}

	public static String getConfigCachePath() {
		return CONFIG_CACHE_PATH;
	}

	public static String getLogFilesPath() {
		return LOG_FILES_PATH;
	}

	public static String getImageAppPath()
	{
		String path = getMyAppPath()+"images/";
		FileUtil.createFolder(path);
		return path;
	}

	public static String getVideoAppPath()
	{
		String path = getMyAppPath()+"video/";
		FileUtil.createFolder(path);
		return path;
	}
}



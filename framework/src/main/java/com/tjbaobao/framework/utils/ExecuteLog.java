package com.tjbaobao.framework.utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * 日志本地存储工具类
 */
@SuppressWarnings("unused")
public class ExecuteLog {
	private static final String logPath = ConstantUtil.getLogFilesPath();
	private static final String TYPE_NET = "network";
	private static final String TYPE_NULL = "null";
	private static final String TYPE_ERROR = "error";
	public static void writeAppException(String log,String type)
	{
		String date = DateTimeUtil.getNowDate();
		String filePath = logPath+date+".log";
		JSONObject jsonObj = new JSONObject();
		try {
			jsonObj.put("type", type);
			jsonObj.put("date", DateTimeUtil.getNowTime());
			jsonObj.put("log", log);
			FileUtil.Writer.writeFileAtEnd(jsonObj.toString()+"\n", filePath,"UTF-8");

			String appPath = ConstantUtil.getMyAppPath()+ File.separator+"log"+File.separator+ date+".log";
			FileUtil.createFolder(ConstantUtil.getMyAppPath()+ File.separator+"log"+File.separator);
			FileUtil.copyFile(filePath, appPath);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	public static void writeNetException(String log)
	{
		writeAppException(log,TYPE_NET);
	}
	public static void writeNullException(String log)
	{
		writeAppException(log,TYPE_NULL);
	}
	public static void writeErrorException(String log)
	{
		writeAppException(log,TYPE_ERROR);
	}
}

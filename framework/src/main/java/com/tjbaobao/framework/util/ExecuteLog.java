package com.tjbaobao.framework.util;

import org.json.JSONException;
import org.json.JSONObject;

public class ExecuteLog {
	private static final String logPath = ConstantUtil.getAppExcepitionLogPath();
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
			FileUtil.Writer.writeFileAtEnd(jsonObj.toString()+"\n", filePath);
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

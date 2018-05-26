package com.tjbaobao.framework.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 日期时间组件
 * @author lyzyjoyo
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class DateTimeUtil
{
	/**
	 * 获取当前时间 
	 * @return 当前时间 0000-00-00 00:00:00
	 */
	public static String getNowTime()
	{
		return getNowTime("yyyy-MM-dd HH:mm:ss");
	}
	public static String getNowTime(String example)
	{
		Date dt = new Date();
		SimpleDateFormat sdf=(SimpleDateFormat)DateFormat.getInstance();
		sdf.applyPattern(example);
		return sdf.format(dt);
	}
	/**
	 * 转换指定毫秒数为当前时间 
	 * @param milliseconds 毫秒数
	 * @return 当前时间 0000-00-00 00:00:00
	 */
	public static String getNowTime(long milliseconds)
	{
		Date dt=new Date(milliseconds);
		SimpleDateFormat sdf=(SimpleDateFormat)DateFormat.getInstance();
		sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
		return sdf.format(dt);
	}

	/**
	 * 通过毫秒获取想要的格式的字符
	 * @param milliseconds milliseconds
	 * @param format format
	 * @return Str
	 */
	public static String getTimeFormat(long milliseconds,String format)
	{
		Date dt=new Date(milliseconds);
		SimpleDateFormat sdf=(SimpleDateFormat)DateFormat.getInstance();
		sdf.setTimeZone(TimeZone.getTimeZone("GMT+0"));
		sdf.applyPattern(format);
		return sdf.format(dt);
	}
	
	/**
	 * 获取当前时间，包含毫秒
	 * @return 当前时间 0000-00-00 00:00:00.000
	 */
	public static String getNowMsTime()
	{
		return getNowTime("yyyy-MM-dd HH:mm:ss.SSS");
	}
	
	/**
	 * 转换指定毫秒数为当前时间，包含毫秒
	 * @param milliseconds 毫秒数
	 * @return 当前时间 0000-00-00 00:00:00.000
	 */
	public static String getNowMsTime(long milliseconds)
	{
		Date dt=new Date(milliseconds);
		SimpleDateFormat sdf=(SimpleDateFormat)DateFormat.getInstance();
		sdf.applyPattern("yyyy-MM-dd HH:mm:ss.SSS");
		return sdf.format(dt);
	}
	
	/**
	 * 获取当前日期
	 * @return 当前日期 0000-00-00
	 */
	public static String getNowDate()
	{
		return getNowTime("yyyy-MM-dd");
	}
	
	/**
	 * 得到两个日期时间之间相差的天数
	 * @param dtFromString 起始日期时间
	 * @param dtToString 终止日期时间
	 * @return 成功返回相差的天数，否则返回-1
	 */
	public static Integer getDays(String dtFromString, String dtToString)
	{
		try
		{
			SimpleDateFormat sdf=(SimpleDateFormat)DateFormat.getInstance();
			sdf.applyPattern("yyyy-MM-dd");
			Date dtFrom = sdf.parse(dtFromString);
			Date dtTo = sdf.parse(dtToString);
			long between = dtFrom.getTime() - dtTo.getTime();
			return (int) Math.abs(between/1000/3600/24);
		}
		catch (Exception e)
		{
			return -1;
		}
	}
		
	/**
	 * 得到两个日期时间之间相差的秒数
	 * @param dtFromString 起始日期时间
	 * @param dtToString 终止日期时间
	 * @return 成功返回相差的秒数，否则返回-1
	 */
	public static Integer getSeconds(String dtFromString, String dtToString)
	{
		try
		{
			SimpleDateFormat sdf=(SimpleDateFormat)DateFormat.getInstance();
			sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
			Date dtFrom = sdf.parse(dtFromString);
			Date dtTo = sdf.parse(dtToString);
			long between = dtFrom.getTime() - dtTo.getTime();
			return (int) Math.abs(between/1000);
		}
		catch (Exception e)
		{
			return -1;
		}
	}
	
	/**
	 * 得到两个日期时间之间相差的毫秒数绝对值
	 * @param dtFromString 起始日期时间
	 * @param dtToString 终止日期时间
	 * @return 成功返回相差的毫秒数，否则返回-1
	 */
	public static Integer getMilliSeconds(String dtFromString, String dtToString)
	{
		try
		{
			SimpleDateFormat sdf=(SimpleDateFormat)DateFormat.getInstance();
			sdf.applyPattern("yyyy-MM-dd HH:mm:ss.SSS");
			Date dtFrom = sdf.parse(dtFromString);
			Date dtTo = sdf.parse(dtToString);
			long between = dtFrom.getTime() - dtTo.getTime();
			return (int) Math.abs(between);
		}
		catch (Exception e)
		{
			return -1;
		}
	}
	
	/**
	 * 将指定的秒数加到日期时间上
	 * @param dtString 原来的日期时间
	 * @param offset 指定的秒数(秒数可以为负数)
	 * @return 成功返回得到的日期时间，否则返回原来的日期时间 0000-00-00 00:00:00
	 */
    public static String addSeconds(String dtString, long offset)
    {
    	try
		{
    		SimpleDateFormat sdf=(SimpleDateFormat)DateFormat.getInstance();
    		sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
			Date dt = sdf.parse(dtString);
			dt=new Date(dt.getTime()+offset*1000);
			return sdf.format(dt);
		}
    	catch(Exception e)
    	{
    		return dtString;
    	}
    }
	
	/**
	 * 将指定的天数加到日期时间上
	 * @param dtString 原来的日期时间
	 * @param offset 指定的天数(天数可以为负数)
	 * @return 成功返回得到的日期时间，否则返回原来的日期时间 0000-00-00 00:00:00
	 */
    public static String addDays(String dtString, long offset) 
    {
    	try
		{
    		SimpleDateFormat sdf=(SimpleDateFormat)DateFormat.getInstance();
    		sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
			Date dt = sdf.parse(dtString);
			dt=new Date(dt.getTime()+offset*86400*1000);
			return sdf.format(dt);
		}
    	catch(Exception e)
    	{
    		return dtString;
    	}
    }
    

	/**
	 * 通过生日获取年龄
	 * @param birthday birthday
	 * @return String
	 */
	public static String getAge(String birthday)
	{
		SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
		  Date date1=new Date();
		  Date date2;
		try {
			date2 = myFormatter.parse(birthday);
			long day=(date1.getTime()-date2.getTime())/(24*60*60*1000) + 1;
			  return new java.text.DecimalFormat("#").format(day/365f);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return "";
	}
	/**
	 * 获取msec毫秒前的说法
	 * @return 返回msec毫秒前的说法
	 */
	public static String getTimeMinus(String dtStringLast)
	{
		try {
			int msec = DateTimeUtil.getMilliSeconds(DateTimeUtil.getNowMsTime(),dtStringLast);
			Date date = DateTimeUtil.getDateByStr(dtStringLast);
			int secAbs = Math.abs(msec)/1000;
			int minAbs = secAbs/60;
			int hourAbs = minAbs/60;
			int dayAbs = hourAbs/24;
			String nowYear = getNowDateStrByPattern("yyyy");
			String lastYear = getDateStrByPattern(date, "yyyy");
			if(secAbs<60)
			{
				return secAbs+"秒前";
			}
			else if(minAbs<60&&(minAbs!=30))
			{
				return minAbs+"分钟前";
			}
			else if(minAbs==30)
			{
				return "半小时前";
			}
			else if(hourAbs<24)
			{
				return hourAbs+"小时前";
			}
			else if(dayAbs==1)
			{
				return getDateStrByPattern(date,"昨天 HH:mm");
			}
			else if(dayAbs<7)
			{
				return getDateStrByPattern(date,"EEEE HH:mm");
			}
			else if(!nowYear.equals(lastYear))
			{
				return getDateStrByPattern(date,"yyyy-MM-dd HH:mm");
			}
			else
			{
				return getDateStrByPattern(date,"MM-dd HH:mm");
			}
		} catch (Exception e) {
			return "";
		}
	}
	
	
	public static String getNowDateStrByPattern(String pattern)
	{
		return getDateStrByPattern(new Date(),pattern);
	}
	public static String getDateStrByPattern(Date date ,String pattern)
	{
		SimpleDateFormat sdf = (SimpleDateFormat)DateFormat.getInstance();
		sdf.applyPattern(pattern);
		return sdf.format(date);
	}
	public static Date getDateByStr(String dateStr,String pattern)
	{
		SimpleDateFormat sdf = (SimpleDateFormat)DateFormat.getInstance();
		sdf.applyPattern(pattern);
		try {
			return sdf.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static Date getDateByStr(String dateStr)
	{
		return getDateByStr(dateStr,"yyyy-MM-dd HH:mm:ss.SSS");
	}

	public static String repair(int number,int length)
	{
		StringBuilder sNum = new StringBuilder(number + "");
		if(sNum.length()<length)
		{
			for(int i=0;i<length-sNum.length();i++)
			{
				sNum.insert(0, "0");
			}
		}
		return sNum.toString();
	}


	public static long getNowTimeMilliseconds()
	{
		return new Date().getTime();
	}

	public static long toTime(String dtFromString,String dateString)
	{
		SimpleDateFormat sdf=(SimpleDateFormat)DateFormat.getInstance();
		sdf.applyPattern(dtFromString);
		try {
			Date date = sdf.parse(dateString);
			return date.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return -1;
	}

	public static long toTime(String dateString)
	{
		return toTime("yyyy-MM-dd",dateString);
	}

}



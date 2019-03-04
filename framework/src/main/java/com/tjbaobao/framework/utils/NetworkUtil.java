package com.tjbaobao.framework.utils;

import android.util.Log;

import com.tjbaobao.framework.listener.OnProgressListener;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * 网络组件
 * @author lyzyjoyo
 *
 * @deprecated 已经弃用，建议使用 {@link OKHttpUtil}
 */
@Deprecated
public class NetworkUtil
{

	/**
	 * 发送post请求
	 * @param requestUrl 请求页面地址
	 * @param requestString 请求的XML数据
	 * @return 发送成功返回true，否则返回false
	 */
	public static boolean sendPostRequest(String requestUrl,String requestString)
	{
		return sendPostRequest(requestUrl,requestString,5000,10000);
	}
	
	/**
	 * 发送post请求
	 * @param requestUrl 请求页面地址
	 * @param requestString 请求的XML数据
	 * @param connectTime 连接时长
	 * @param readTime 读取时长
	 * @return 发送成功返回true，否则返回false
	 */
	public static boolean sendPostRequest(String requestUrl,String requestString,int connectTime,int readTime)
	{
		boolean bPostResult=false;;
		HttpURLConnection connect = null;
		try
		{
			//解决链接中有空格和中文问题
			requestUrl = URLEncoder.encode(requestUrl,"UTF-8").replaceAll("\\+", "%20");  
			requestUrl = requestUrl.replaceAll("%3A", ":").replaceAll("%2F", "/");  
			//创建连接
			URL url = new URL(requestUrl);
			connect = (HttpURLConnection) url.openConnection();
			//设置连接属性
			connect.setRequestMethod("POST");
			connect.setRequestProperty("Content-Type", "text/xml");
			connect.setConnectTimeout(connectTime);
			connect.setReadTimeout(readTime);
			connect.setDoOutput(true);
			//输出请求数据到此连接
			byte[] b = requestString.getBytes();
			connect.setRequestProperty("Content-length", String.valueOf(b.length));
			DataOutputStream dos = new DataOutputStream(connect.getOutputStream());
			dos.write(b);
			dos.flush();
			dos.close();
			//打开连接并发送请求
			InputStream in = connect.getInputStream();
			in.close();
			bPostResult=true;
		}
		catch (Exception e)
		{
			bPostResult=false;
			String sLog="sendPostRequest失败："+e.toString();
			StackTraceElement[] stElements=e.getStackTrace();
			for(int i=0;i<stElements.length;i++)
			{
				sLog+="\r\nat "+stElements[i].toString();
			}
			Log.e("Execute",sLog);
			ExecuteLog.writeNetException(sLog);
		}
		finally
		{
			//关闭连接
			if (connect != null)
			{
				connect.disconnect();
			}
		}
		return bPostResult;
	}
	
	/**
	 * 发送post请求
	 * @param requestUrl 请求页面地址
	 * @param buffer 请求数据的byte数组
	 * @return 发送成功返回true，否则返回false
	 */
	public static boolean sendPostRequest(String requestUrl,byte[] buffer)
	{
		return sendPostRequest(requestUrl,buffer,5000,10000);
	}
	
	/**
	 * 发送post请求
	 * @param requestUrl 请求页面地址
	 * @param buffer 请求数据的byte数组
	 * @param connectTime 连接时长
	 * @param readTime 读取时长
	 * @return 发送成功返回true，否则返回false
	 */
	public static boolean sendPostRequest(String requestUrl,byte[] buffer,int connectTime,int readTime)
	{
		boolean bPostResult=false;;
		HttpURLConnection connect = null;
		try
		{
			//解决链接中有空格和中文问题
			requestUrl = URLEncoder.encode(requestUrl,"UTF-8").replaceAll("\\+", "%20");  
			requestUrl = requestUrl.replaceAll("%3A", ":").replaceAll("%2F", "/");  
			//创建连接
			URL url = new URL(requestUrl);
			connect = (HttpURLConnection) url.openConnection();
			//设置连接属性
			connect.setRequestMethod("POST");
			connect.setRequestProperty("Content-Type", "text/xml");
			connect.setConnectTimeout(connectTime);
			connect.setReadTimeout(readTime);
			connect.setDoOutput(true);
			//输出请求数据到此连接
			connect.setRequestProperty("Content-length", String.valueOf(buffer.length));
			DataOutputStream dos = new DataOutputStream(connect.getOutputStream());
			dos.write(buffer);
			dos.flush();
			dos.close();
			//打开连接并发送请求
			InputStream in = connect.getInputStream();
			in.close();
			bPostResult=true;
		}
		catch (Exception e)
		{
			bPostResult=false;
			String sLog="sendPostRequest失败："+e.toString();
			StackTraceElement[] stElements=e.getStackTrace();
			for(int i=0;i<stElements.length;i++)
			{
				sLog+="\r\nat "+stElements[i].toString();
			}
			Log.e("Execute",sLog);
			ExecuteLog.writeNetException(sLog);
		}
		finally
		{
			//关闭连接
			if (connect != null)
			{
				connect.disconnect();
			}
		}
		return bPostResult;
	}
	
	/**
	 * 发送post请求并获取响应内容
	 * @param requestUrl 请求页面地址
	 * @param requestString 请求的XML数据
	 * @return 响应内容
	 */
 	public static String getPostResponse(String requestUrl,String requestString)
	{
		return getPostResponse(requestUrl,requestString,1500,10000, "text/xml");
	}
	
 	/**
	 * 发送post请求并获取响应内容
	 * @param requestUrl 请求页面地址
	 * @param requestString 请求的XML数据
	 * @param connectTime 连接时长
	 * @param readTime 读取时长
	 * @return 响应内容
	 */
 	public static String getPostResponse(String requestUrl,String requestString,int connectTime,int readTime)
	{
 		return getPostResponse(requestUrl,requestString,connectTime,readTime, "text/xml");
	}
 	
	/**
	 * 发送post请求并获取响应内容
	 
	 * @param requestUrl 请求页面地址
	 * @param requestString 请求的XML数据
	 * @param connectTime 连接时长
	 * @param readTime 读取时长
	 * @return 响应内容
	 */
	public static String getPostResponse(String requestUrl,String requestString,int connectTime,int readTime, String contentType)
	{
		String sResponseContent="";
		HttpURLConnection connect = null;
		try
		{
			//解决链接中有空格和中文问题
			requestUrl = URLEncoder.encode(requestUrl,"UTF-8").replaceAll("\\+", "%20");  
			requestUrl = requestUrl.replaceAll("%3A", ":").replaceAll("%2F", "/");  
			//创建连接
			URL url = new URL(requestUrl);
			connect = (HttpURLConnection) url.openConnection();
			//设置连接属性
			connect.setRequestMethod("POST");
			connect.setRequestProperty("Content-Type", contentType);
			connect.setConnectTimeout(connectTime);
			connect.setReadTimeout(readTime);
			connect.setDoOutput(true);
			//输出请求数据到此连接
			byte[] b = requestString.getBytes();
			connect.setRequestProperty("Content-length", String.valueOf(b.length));
			OutputStream outPutStream = connect.getOutputStream();
			DataOutputStream dos = new DataOutputStream(outPutStream);
			dos.write(b);
			dos.flush();
			dos.close();
			//打开连接并发送请求
			InputStream in = connect.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			String sTemp = "";
			//获取返回内容
			while ((sTemp=rd.readLine())!= null)
			{
				sResponseContent+=sTemp;
			}
			rd.close();
			in.close();
		}
		catch (Exception e)
		{
			sResponseContent="";
			String sLog="getPostResponse失败："+e.toString();
			StackTraceElement[] stElements=e.getStackTrace();
			for(int i=0;i<stElements.length;i++)
			{
				sLog+="\r\nat "+stElements[i].toString();
			}
			Log.e("Execute",sLog);
			ExecuteLog.writeNetException(sLog);
		}
		finally
		{
			//关闭连接
			if (connect != null)
			{
				connect.disconnect();
			}
		}
		return sResponseContent;
	}
	
	/**
	 * 发送post请求并获取响应内容
	 
	 * @param requestUrl 请求页面地址
	 * @param buffer 请求数据的byte数组
	 * @return 响应内容
	 */
	public static String getPostResponse(String requestUrl,byte[] buffer)
	{
		return getPostResponse(requestUrl,buffer,5000,10000);
	}
	
	/**
	 * 发送post请求并获取响应内容
	 * @param requestUrl 请求页面地址
	 * @param buffer 请求数据的byte数组
	 * @param connectTime 连接时长
	 * @param readTime 读取时长
	 * @return 响应内容
	 */
	public static String getPostResponse(String requestUrl,byte[] buffer,int connectTime,int readTime)
	{
		String sResponseContent="";
		HttpURLConnection connect = null;
		try
		{
			//解决链接中有空格和中文问题
			requestUrl = URLEncoder.encode(requestUrl,"UTF-8").replaceAll("\\+", "%20");  
			requestUrl = requestUrl.replaceAll("%3A", ":").replaceAll("%2F", "/");  
			//创建连接
			URL url = new URL(requestUrl);
			connect = (HttpURLConnection) url.openConnection();
			//设置连接属性
			connect.setRequestMethod("POST");
			connect.setRequestProperty("Content-Type", "text/xml");
			connect.setConnectTimeout(connectTime);
			connect.setReadTimeout(readTime);
			connect.setDoOutput(true);
			//输出请求数据到此连接
			connect.setRequestProperty("Content-length", String.valueOf(buffer.length));
			DataOutputStream dos = new DataOutputStream(connect.getOutputStream());
			dos.write(buffer);
			dos.flush();
			dos.close();
			//打开连接并发送请求
			InputStream in = connect.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			String sTemp = "";
			//获取返回内容
			while ((sTemp=rd.readLine())!= null)
			{
				sResponseContent+=sTemp;
			}
			rd.close();
			in.close();
		}
		catch (Exception e)
		{
			sResponseContent="";
			String sLog="getPostResponse失败："+e.toString();
			StackTraceElement[] stElements=e.getStackTrace();
			for(int i=0;i<stElements.length;i++)
			{
				sLog+="\r\nat "+stElements[i].toString();
			}
			Log.e("Execute",sLog);
			ExecuteLog.writeNetException(sLog);
		}
		finally
		{
			//关闭连接
			if (connect != null)
			{
				connect.disconnect();
			}
		}
		return sResponseContent;
	}
	
	 /** 
     * 下载文件
	 * @param downloadUrl 文件下载地址
	 * @param savePath 文件保存目录
	 * @return 下载成功返回true，否则返回false
	 */
	public static boolean downloadFile(String downloadUrl,String savePath)
	{
		boolean bDownloadResult=false;
		try
		{
			//解决链接中有空格和中文问题
			//downloadUrl = URLEncoder.encode(downloadUrl,"UTF-8").replaceAll("\\+", "%20");  
			//downloadUrl = downloadUrl.replaceAll("%3A", ":").replaceAll("%2F", "/");
			//连接下载地址
			URL url = new URL(downloadUrl); 
			HttpURLConnection connect = (HttpURLConnection) url.openConnection(); 
			connect.setConnectTimeout(5000);
			connect.setReadTimeout(10000); 
			connect.setRequestMethod("GET");
			InputStream is = connect.getInputStream(); 
			//创建下载目录
			FileUtil.createFolder(savePath);
			//删除旧的文件
			FileUtil.delFileIfExists(savePath);
			//开始下载
			FileOutputStream fos = new FileOutputStream(new File(savePath)); 
			BufferedInputStream bis = new BufferedInputStream(is); 
			byte[] buffer = new byte[1024]; 
			int nLength=0; 
			while((nLength =bis.read(buffer))!=-1)
			{ 
				fos.write(buffer, 0, nLength);
			}
			fos.close(); 
			bis.close(); 
			is.close(); 
			bDownloadResult=true;
		}
		//下载失败
		catch(Exception e)
		{
			e.printStackTrace();
			String sLog="下载失败："+e.toString();
			StackTraceElement[] stElements=e.getStackTrace();
			for(int i=0;i<stElements.length;i++)
			{
				sLog+="\r\nat "+stElements[i].toString();
			}
			Log.e("Execute",sLog);
			ExecuteLog.writeNetException(sLog);
		}
		return bDownloadResult;
	}

	public boolean downloadFileNew(String downloadUrl,String savePath)
	{
		boolean bDownloadResult=false;
		try
		{
			//解决链接中有空格和中文问题
			//downloadUrl = URLEncoder.encode(downloadUrl,"UTF-8").replaceAll("\\+", "%20");
			//downloadUrl = downloadUrl.replaceAll("%3A", ":").replaceAll("%2F", "/");
			//连接下载地址
			URL url = new URL(downloadUrl);
			HttpURLConnection connect = (HttpURLConnection) url.openConnection();
			connect.setConnectTimeout(5000);
			connect.setReadTimeout(10000);
			connect.setRequestMethod("GET");
			int fileLength = connect.getContentLength();
			InputStream is = connect.getInputStream();
			//创建下载目录
			FileUtil.createFolder(savePath);
			//删除旧的文件
			FileUtil.delFileIfExists(savePath);
			//开始下载
			FileOutputStream fos = new FileOutputStream(new File(savePath));
			BufferedInputStream bis = new BufferedInputStream(is);
			byte[] buffer = new byte[1024];
			int readLength = 0;
			int nLength=0;
			while((nLength =bis.read(buffer))!=-1)
			{
				fos.write(buffer, 0, nLength);
				readLength +=nLength ;
				if(onProgressListener!=null)
				{
					onProgressListener.onProgress(downloadUrl,fileLength,readLength);
				}
			}
			fos.close();
			bis.close();
			is.close();
			bDownloadResult=true;
		}
		//下载失败
		catch(Exception e)
		{
			e.printStackTrace();
			String sLog="下载失败："+e.toString();
			StackTraceElement[] stElements=e.getStackTrace();
			for(int i=0;i<stElements.length;i++)
			{
				sLog+="\r\nat "+stElements[i].toString();
			}
			Log.e("Execute",sLog);
			ExecuteLog.writeNetException(sLog);
		}
		return bDownloadResult;
	}


	public String uploadFile(String uploadUrl,String path)
	{
		return uploadFile(uploadUrl,new File(path));
	}
	private static  boolean bUploadRun = true ;
	public String uploadFile(String uploadUrl,File file)
	{
		String sResponseContent="";
		HttpURLConnection connect = null;
		bUploadRun = true ;
		try
		{
			//解决链接中有空格和中文问题
//			uploadUrl = URLEncoder.encode(uploadUrl,"UTF-8").replaceAll("\\+", "%20");
//			uploadUrl = uploadUrl.replaceAll("%3A", ":").replaceAll("%2F", "/");
			//创建连接
			URL url = new URL(uploadUrl);
			connect = (HttpURLConnection) url.openConnection();
			//设置连接属性
			connect.setRequestMethod("POST");
			connect.setRequestProperty("Content-Type", "text/xml");
			//connect.setConnectTimeout(10000);
			//connect.setReadTimeout(15000);
			connect.setDoOutput(true);
			connect.setChunkedStreamingMode(1024);
			//输出请求数据到此连接
			connect.setRequestProperty("Content-length", String.valueOf(file.length()));
			DataOutputStream dos = new DataOutputStream(connect.getOutputStream());
			FileInputStream fileInput = new FileInputStream(file);
			byte[] buffBytes = new byte[1024];
			int fileLength = (int) file.length();
			int sendLength = 0;
			int len = 0;
			while((len = fileInput.read(buffBytes))!=-1&&bUploadRun)
			{
				dos.write(buffBytes,0,len);
				sendLength +=len;
				if(onProgressListener!=null)
				{
					onProgressListener.onProgress(uploadUrl,fileLength,sendLength);
				}
			}
			fileInput.close();
			dos.flush();
			dos.close();
			//打开连接并发送请求
			if(!bUploadRun)
			{
				return sResponseContent;
			}
			InputStream in = connect.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			String sTemp = "";
			//获取返回内容
			while ((sTemp=rd.readLine())!= null)
			{
				sResponseContent+=sTemp;
			}

		}
		catch (Exception e)
		{
			sResponseContent="";
			String sLog="第"+(1)+"次上传失败："+e.toString();
			StackTraceElement[] stElements=e.getStackTrace();
			for(int j=0;j<stElements.length;j++)
			{
				sLog+="\r\nat "+stElements[j].toString();
			}
			Log.e("Execute",sLog);
			ExecuteLog.writeNetException(sLog);
		}
		finally
		{
			//关闭连接
			if (connect != null)
			{
				connect.disconnect();
			}
		}
		return sResponseContent;
	}
	public static void setBUploadRun(boolean bRun)
	{
		bUploadRun = bRun;
	}
	
	private OnProgressListener onProgressListener ;

}



package com.tjbaobao.framework.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.TrafficStats;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.tjbaobao.framework.R;
import com.tjbaobao.framework.base.BaseApplication;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.Reader;
import java.lang.reflect.Method;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.List;

import static android.Manifest.permission.READ_PHONE_STATE;


public class DeviceUtil {
	private static Context context;

	static {
		context = BaseApplication.getContext();
	}

	/**
	 * 获取运营商名称
	 * @return 运营商名称
	 */
	public static String getOperatorName() {
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String sOperatorName = "无法获取运营商";
		if (ActivityCompat.checkSelfPermission(BaseApplication.getContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

			return "";
		}
		String sIMSI = tm.getSubscriberId();
		if (sIMSI != null && !sIMSI.trim().equals("")) {
			if (sIMSI.startsWith("46000") || sIMSI.startsWith("46002") || sIMSI.startsWith("46007")) {
				sOperatorName = "中国移动";
			} else if (sIMSI.startsWith("46001")) {
				sOperatorName = "中国联通";
			} else if (sIMSI.startsWith("46003")) {
				sOperatorName = "中国电信";
			}
		}
		return sOperatorName;
	}

	/**
	 * 获取手机制式
	 * @return 手机制式
	 */
	public static String getPhoneType() {
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String sPhoneType = "";
		if (tm.getPhoneType() == TelephonyManager.PHONE_TYPE_GSM) {
			sPhoneType = "GSM";
		} else if (tm.getPhoneType() == TelephonyManager.PHONE_TYPE_CDMA) {
			sPhoneType = "CDMA";
		} else {
			sPhoneType = "未知手机制式";
		}
		return sPhoneType;
	}

	/**
	 * 获取屏幕宽度
	 * @return 屏幕宽度
	 */
	public static int getScreenWidth() {
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		if(wm!=null){
			wm.getDefaultDisplay().getMetrics(dm);
		}
		return dm.widthPixels;
	}

	/**
	 * 获取屏幕高度
	 * @return 屏幕高度
	 */
	public static int getScreenHeight() {
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dm);
		return dm.heightPixels;
	}

	/**
	 * 获取手机网络类型
	 * @return 网络类型
	 */
	public static String getNetworkType() {
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		return getNetworkType(tm.getNetworkType());
	}

	/**
	 * 获取手机网络类型
	 * @param networktype 网络类型常量
	 * @return 网络类型
	 */
	public static String getNetworkType(int networktype) {
		String sNetworkType = "";
		if (networktype == 1) {
			sNetworkType = "GPRS";
		} else if (networktype == 2) {
			sNetworkType = "EDGE";
		} else if (networktype == 3) {
			sNetworkType = "UMTS";
		} else if (networktype == 4) {
			sNetworkType = "CDMA";
		} else if (networktype == 5) {
			sNetworkType = "CDMA - EvDo rev. 0";
		} else if (networktype == 6) {
			sNetworkType = "CDMA - EvDo rev. A";
		} else if (networktype == 7) {
			sNetworkType = "CDMA - 1xRTT";
		} else if (networktype == 8) {
			sNetworkType = "HSDPA";
		} else if (networktype == 9) {
			sNetworkType = "HSUPA";
		} else if (networktype == 10) {
			sNetworkType = "HSPA";
		} else if (networktype == 11) {
			sNetworkType = "iDEN";
		} else if (networktype == 12) {
			sNetworkType = "CDMA - EvDo rev. B";
		} else if (networktype == 13) {
			sNetworkType = "LTE";
		} else if (networktype == 14) {
			sNetworkType = "CDMA - eHRPD";
		} else if (networktype == 15) {
			sNetworkType = "HSPA+";
		} else {
			sNetworkType = "未知网络类型";
		}
		return sNetworkType;
	}

	/**
	 * 获取手机通信技术
	 * @return 手机通信技术
	 */
	public static String getGeneration() {
		String sGeneration = "";
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		if (tm.getNetworkType() == 1 || tm.getNetworkType() == 2 || tm.getNetworkType() == 4 || tm.getNetworkType() == 7 || tm.getNetworkType() == 11) {
			sGeneration = "2G";
		} else if (tm.getNetworkType() == 3 || tm.getNetworkType() == 5 || tm.getNetworkType() == 6 || tm.getNetworkType() == 8 || tm.getNetworkType() == 9
				|| tm.getNetworkType() == 10 || tm.getNetworkType() == 12 || tm.getNetworkType() == 14 || tm.getNetworkType() == 15) {
			sGeneration = "3G";
		} else if (tm.getNetworkType() == 13) {
			sGeneration = "4G";
		} else {
			sGeneration = "未知通信技术";
		}
		return sGeneration;
	}

	/**
	 * 获取手机漫游状态
	 * @return 漫游状态
	 */
	public static String getRoamingStatus() {
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		if (tm.isNetworkRoaming()) {
			return "漫游";
		} else {
			return "非漫游";
		}
	}

	/**
	 * 获取手机厂商
	 * @return 手机厂商
	 */
	public static String getManufacturer() {
		return Build.MANUFACTURER;
	}

	/**
	 * 获取手机型号
	 * @return 手机型号
	 */
	public static String getPhoneModel() {
		return Build.MODEL;
	}

	/**
	 * 获取手机分辨率
	 * @return 手机分辨率
	 */
	public static String getResolution() {
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dm);
		return String.valueOf(dm.heightPixels) + "x" + String.valueOf(dm.widthPixels);
	}

	/**
	 * 获取手机IMSI
	 * @return IMSI
	 */
	@SuppressLint("HardwareIds")
	public static String getPhoneIMSI() {
		String sPhoneIMSI;
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		if (ActivityCompat.checkSelfPermission(BaseApplication.getContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

			return "";
		}
		sPhoneIMSI = tm.getSubscriberId();
		if (sPhoneIMSI == null || sPhoneIMSI.trim().equals("")) {
			sPhoneIMSI = "无法获取IMSI";
		}
		return sPhoneIMSI;
	}

	/**
	 * 获取手机MCCMNC
	 * @return MCCMNC
	 */
	public static String getMCCMNC() {
		String sPhoneMCCMNC;
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		sPhoneMCCMNC = tm.getSimOperator();
		if (sPhoneMCCMNC == null || sPhoneMCCMNC.equals("")) {
			sPhoneMCCMNC = "无法获取MCCMNC";
		}
		return sPhoneMCCMNC;
	}

	/**
	 * 获取手机IMEI
	 * @return IMEI
	 */
	public static String getPhoneIMEI() {
		String sPhoneIMEI;
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		if (ActivityCompat.checkSelfPermission(BaseApplication.getContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

			return "";
		}
		sPhoneIMEI = tm.getDeviceId();
		if (sPhoneIMEI == null || sPhoneIMEI.trim().equals(""))
		{
			sPhoneIMEI = "无法获取IMEI";
		}
		return sPhoneIMEI.toUpperCase();
	}

	/**
	 * 获取Android系统SDK版本
	 * @return SDK版本
	 */
	public static String getSDKVersion()
	{
		return Build.VERSION.RELEASE;
	}

	/**
	 * 获取Android系统的API Level
	 * @return API Level
	 */
	public static int getAPILevel()
	{
		return Build.VERSION.SDK_INT;
	}

	/**
	 * 获取客户端包名
	 * @return 客户端包名
	 */
	public static String getPackageName()
	{
		String sPackageName = context.getPackageName();
		return sPackageName;
	}

	/**
	 * 获取客户端名称
	 * @return 客户端名称
	 */
	public static String getAppName()
	{
		String sAppName = context.getResources().getString(R.string.app_name);
		return sAppName;
	}

	/**
	 * 获取应用版本
	 * @return 应用版本
	 */
	public static String getAppVersion()
	{
		return getAppVersion(context.getPackageName());
	}
	
	/**
	 * 获取应用版本
	 * @param packageName 应用包名
	 * @return 应用版本
	 */
	public static String getAppVersion(String packageName)
	{
		String sAppVersion = "";
		try 
		{
			PackageInfo packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
			if(packageInfo!=null)
			{
				sAppVersion=packageInfo.versionName;
			}
		} 
		catch (Exception e) 
		{
			sAppVersion = "";
		}
		return sAppVersion;
	}

	/**
	 * 获取MAC地址
	 * @return MAC地址
	 */
	public static String getMacAddress(){  
        String str="";  
        String macSerial="";  
        try {  
            Process pp = Runtime.getRuntime().exec(  
                    "cat /sys/class/net/wlan0/address ");  
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());  
            LineNumberReader input = new LineNumberReader(ir);  
  
            for (; null != str;) {  
                str = input.readLine();  
                if (str != null) {  
                    macSerial = str.trim();// 去空格  
                    break;  
                }  
            }  
        } catch (Exception ex) {  
            ex.printStackTrace();  
        }  
        if (macSerial == null || "".equals(macSerial)) {  
            try {  
                return loadFileAsString("/sys/class/net/eth0/address")  
                        .toUpperCase().substring(0, 17);  
            } catch (Exception e) {  
                e.printStackTrace();  
                  
            }  
              
        }  
        return macSerial;  
    }  
     
	public static String loadFileAsString(String fileName) throws Exception {  
            FileReader reader = new FileReader(fileName);    
            String text = loadReaderAsString(reader);  
            reader.close();  
            return text;  
        }  
     
	public static String loadReaderAsString(Reader reader) throws Exception {  
            StringBuilder builder = new StringBuilder();  
            char[] buffer = new char[4096];  
            int readLength = reader.read(buffer);  
            while (readLength >= 0) {  
                builder.append(buffer, 0, readLength);  
                readLength = reader.read(buffer);  
            }  
            return builder.toString();  
        } 
	/**
	 * 获取IP地址
	 * @return IP地址
	 */
	public static String getIpAddress()
	{
		String sIpAddress=null;
		//获取WiFi网络IP地址
		WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);      
		if(wm!=null)
		{
			WifiInfo wi = wm.getConnectionInfo();      
			if(wi!=null&&wi.getBSSID()!=null&&wi.getSSID()!=null)
			{
				int ip = wi.getIpAddress();      
				sIpAddress= (ip & 0xFF) + "." + ((ip >> 8) & 0xFF) + "." + ((ip >> 16) & 0xFF) + "." + (ip >> 24 & 0xFF); 
			}
		}
		//获取手机网络IP地址
		if(sIpAddress==null)
		{
			try
			{
				for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();)
				{
					NetworkInterface intf = en.nextElement();
					for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();)
					{
						InetAddress inetAddress = enumIpAddr.nextElement();
						if (!inetAddress.isLoopbackAddress()&& (inetAddress instanceof Inet4Address))
						{
							sIpAddress= inetAddress.getHostAddress().toString();
							break;
						}
					}
					//已获取IP地址，跳出循环
					if(sIpAddress!=null)
					{
						break;
					}
				}
			}
			catch (Exception e)
			{
			}
		}
		return sIpAddress;
	}

	/**
	 * 获取当前连接网络
	 * @return 当前连接网络
	 */
	public static String getConnectNetwork()
	{
		String sConnectNetwork = "未连接网络";
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm != null)
		{
			NetworkInfo network = cm.getActiveNetworkInfo();
			if (network != null && network.isConnected())
			{
				if (network.getType() == ConnectivityManager.TYPE_WIFI)
				{
					sConnectNetwork = "WiFi";
				}
				else
				{
					TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
					if (tm.getNetworkType() == 1 || tm.getNetworkType() == 2 || tm.getNetworkType() == 4 || tm.getNetworkType() == 7 || tm.getNetworkType() == 11)
					{
						sConnectNetwork = "2G";
					}
					else if (tm.getNetworkType() == 3 || tm.getNetworkType() == 5 || tm.getNetworkType() == 6 || tm.getNetworkType() == 8 || tm.getNetworkType() == 9
						|| tm.getNetworkType() == 10 || tm.getNetworkType() == 12 || tm.getNetworkType() == 14 || tm.getNetworkType() == 15)
					{
						sConnectNetwork = "3G";
					}
					else if (tm.getNetworkType() == 13)
					{
						sConnectNetwork = "4G";
					}
					else
					{
						sConnectNetwork = "未知网络";
					}
				}
			}
		}
		return sConnectNetwork;
	}

	/**
	 * 获取当前是否移动网络
	 * @return 当前是移动网络返回true，否则返回false
	 */
	public static boolean isMobileNetwork()
	{
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		if (tm.getNetworkType() == 1 || tm.getNetworkType() == 2 || tm.getNetworkType() == 8 || tm.getNetworkType() == 9 || tm.getNetworkType() == 10 || tm.getNetworkType() == 13
			|| tm.getNetworkType() == 15)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * 获取应用总流量
	 * @return 总流量
	 */
	public static long getAppTotalBytes()
	{
		// 下行流量
		long lRxBytes = TrafficStats.getUidRxBytes(android.os.Process.myUid());
		if (lRxBytes == TrafficStats.UNSUPPORTED)
		{
			lRxBytes = TrafficStats.getTotalRxBytes();
		}
		if (lRxBytes == TrafficStats.UNSUPPORTED)
		{
			lRxBytes = 0;
		}
		// 上行流量
		long lTxBytes = TrafficStats.getUidTxBytes(android.os.Process.myUid());
		if (lTxBytes == TrafficStats.UNSUPPORTED)
		{
			lTxBytes = TrafficStats.getTotalTxBytes();
		}
		if (lTxBytes == TrafficStats.UNSUPPORTED)
		{
			lTxBytes = 0;
		}
		return lRxBytes + lTxBytes;
	}
	/**
     *判断当前应用程序处于前台还是后台
     */
    public static boolean isAppRunInBackground() {
    	ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (RunningAppProcessInfo appProcess : appProcesses) {
             if (appProcess.processName.equals(context.getPackageName())) {
                    if (appProcess.importance == RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
                            Tools.printLog("在后台运行");  
                    		return true;
                    }else{
                    	Tools.printLog("不在后台运行");  
                              return false;
                    }
               }
        }
        Tools.printLog("不在后台运行");  
        return false;
    }


	public static boolean checkPermission(Context context, String permission) {
		boolean result = false;
		if (Build.VERSION.SDK_INT >= 23) {
			try {
				Class<?> clazz = Class.forName("android.content.Context");
				Method method = clazz.getMethod("checkSelfPermission", String.class);
				int rest = (Integer) method.invoke(context, permission);
				if (rest == PackageManager.PERMISSION_GRANTED) {
					result = true;
				} else {
					result = false;
				}
			} catch (Exception e) {
				result = false;
			}
		} else {
			PackageManager pm = context.getPackageManager();
			if (pm.checkPermission(permission, context.getPackageName()) == PackageManager.PERMISSION_GRANTED) {
				result = true;
			}
		}
		return result;
	}
	public static String getDeviceInfo(Context context) {
		try {
			org.json.JSONObject json = new org.json.JSONObject();
			TelephonyManager tm = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			String device_id = null;
			if (checkPermission(context, READ_PHONE_STATE)) {
				device_id = tm.getDeviceId();
			}
			String mac = null;
			FileReader fstream = null;
			try {
				fstream = new FileReader("/sys/class/net/wlan0/address");
			} catch (FileNotFoundException e) {
				fstream = new FileReader("/sys/class/net/eth0/address");
			}
			BufferedReader in = null;
			if (fstream != null) {
				try {
					in = new BufferedReader(fstream, 1024);
					mac = in.readLine();
				} catch (IOException e) {
				} finally {
					if (fstream != null) {
						try {
							fstream.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					if (in != null) {
						try {
							in.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
			json.put("mac", mac);
			if (TextUtils.isEmpty(device_id)) {
				device_id = mac;
			}
			if (TextUtils.isEmpty(device_id)) {
				device_id = android.provider.Settings.Secure.getString(context.getContentResolver(),
						android.provider.Settings.Secure.ANDROID_ID);
			}
			json.put("device_id", device_id);
			return json.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}



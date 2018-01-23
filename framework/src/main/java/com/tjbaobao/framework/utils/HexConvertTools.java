package com.tjbaobao.framework.utils;

import java.io.UnsupportedEncodingException;

public class HexConvertTools {
	private static final String DEF_CHARSET_NAME = "iso8859-1";
	/**
	 * 将字节数组转为Int
	 * @param bytes
	 * @param offset 
	 * @return
	 */
	public static int bytesToInt(byte[] bytes, int offset) {
		if(bytes==null)
		{
			return -1;
		}
        int value = 0;
        for (int i = 0; i < 4; i++) {
            int shift = (4 - 1 - i) * 8;
            value += (bytes[i + offset] & 0x000000FF) << shift;
        }
        return value;
	}
	public static long bytesToLong(byte[] bytes)
	{
		if(bytes==null)
		{
			return -1;
		}
        long value = 0;
        for (int i = 0; i < 8; i++) {
            int shift = (8 - 1 - i) * 8;
            value += (bytes[i] & 0x000000FF) << shift;
        }
        return value;
	}
	/**
	 * 将字节转换为Int
	 * @param bytes
	 * @return
	 */
	public static int bytesToInt(byte[] bytes) {
		return bytesToInt(bytes,0);
	}
	/**
	 * 字节数组转为16进制
	 * @param bytes
	 * @return
	 */
	public static String bytesToHex(byte[] bytes)
	{
		if(bytes!=null)
		{
			StringBuilder stringBuilder = new StringBuilder("");  
			for(byte b:bytes)
			{
				  String hex = Integer.toHexString(b & 0xFF);
		            if (hex.length() == 1) {
		                hex = '0' + hex;
		            }
		            stringBuilder.append(hex.toUpperCase());
			}
			return stringBuilder.toString();
		}
		return null;
	}
	public static String stringToHex(String str)
	{
		try {
			return bytesToHex(str.getBytes(DEF_CHARSET_NAME));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 字节串转iso8859-1编码的字符串
	 * @param bytes
	 * @return
	 */
	public static String bytesToStr(byte[] bytes)
	{
		if(bytes==null)
		{
			return null;
		}
		try {
			return new String(bytes,DEF_CHARSET_NAME);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null ;
		}
	}

	/** 
	 * 16进制的字符串转成字节数组 
	 * @param hexString  16进制格式的字符串 
	 * @param fillSize 如果不满，则填充0
	 * @return 转换后的字节数组 
	 **/  
	public static byte[] hexStrToBytes(String hexString,int fillSize) {  
		if(hexString==null)
		{
			return null;
		}
		hexString = addNullToStr(hexString,fillSize);
	    hexString = hexString.toLowerCase();  
	    final byte[] byteArray = new byte[hexString.length() / 2];  
	    int k = 0;  
	    for (int i = 0; i < byteArray.length; i++) {  
	        byte high = (byte) (Character.digit(hexString.charAt(k), 16) & 0xff);  
	        byte low = (byte) (Character.digit(hexString.charAt(k + 1), 16) & 0xff);  
	        byteArray[i] = (byte) (high << 4 | low);  
	        k += 2;  
	    }  
	    return byteArray;  
	} 
	/**
	 * 16进制字符串转换为字节数组
	 * @param hexString
	 * @return
	 */
	public static byte[] hexStrToBytes(String hexString) {
		return hexStrToBytes(hexString,8);
	}
	
	public static String hexToUtf8Str(String hexString)
	{
		if(hexString==null)
		{
			return null;
		}
		byte[] hexBytes = hexStrToBytes(hexString);
		if(hexBytes!=null)
		{
			return bytesToStr(hexBytes);
		}
		return null;
	}
	public static int hexToInt(String hexString)
	{
		if(hexString==null)
		{
			return -1;
		}
		byte[] hexBytes = hexStrToBytes(hexString);
		if(hexBytes!=null)
		{
			return bytesToInt(hexBytes);
		}
		return 0;
	}
	/**
	 * 在指定字符串面前，添�?"0"，填充够指定位数
	 * @param hexString �?要被填充的字符串
	 * @param num �?要填充后达到的长�?
	 * @return
	 */
	public static String addNullToStr(String hexString ,int num)
	{
		if(hexString==null)
		{
			return null;
		}
		for(int i=0;i<num;i++)
		{
			hexString = "0"+hexString;
			if(hexString.length()==num)
				return hexString;
		}
		return hexString;
	}
	public static byte[] intToByteArray(final int integer) {
		int byteNum = (40 -Integer.numberOfLeadingZeros (integer < 0 ? ~integer : integer))/ 8;
		byte[] byteArray = new byte[4];

		for (int n = 0; n < byteNum; n++)
		byteArray[3 - n] = (byte) (integer>>> (n * 8));

		return (byteArray);
	}
}

package com.tjbaobao.framework.utils;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.provider.MediaStore;
import androidx.annotation.Nullable;

import com.tjbaobao.framework.base.BaseApplication;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

/**
 * 磁盘资源获取器(内置获取图片、视频、以及所有文件的方法)
 *
 * 使用方法：
 *
 * 1、创建对象
 * ResourcesGetTools tools = new ResourcesGetTools（activity);
 * 2、设置接口
 * tools.setOnResourcesGetListener(...);
 * 3、重写Activity等的onActivityResult
 *  @Override
 *	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
 *	 super.onActivityResult(requestCode, resultCode, data);
 *	 tools.onActivityResult(requestCode,resultCode,data);
 * }
 * 4、调起对应的方法获取资源
 * tools.getImgByCamera();//启动系统相机获取图片
 * tools.getImgByGallery();//从系统图库获取图片
 * getVideoByCamera();//启动系统录像机获取视频
 * getVideoByFile();//通过系统文件管理器获取视频文件
 * getFileByFile();//从文件管理器获取文件
 *
 * @author TJbaobao
 */

@SuppressWarnings({"WeakerAccess", "unused"})
public class ResourcesGetTools {
	public class RequestCode
	{
		public static final int IMG_CAMERA_GET = 101;//从相机获取图像
		public static final int IMG_GALLERY_GET = 102;//从图库获取图像
		public static final int IMG_FILE_GET = 103 ;//从文件管理器获取图像
		public static final int IMG_CAMERA_CUT = 104;//从相机获取图像时使用的图片裁减
		public static final int IMG_GALLERY_CUT = 105;//从图库获取图像时使用的图片裁减
		public static final int VIDEO_CAMERA_GET = 201;//从相机获取视频
		public static final int VIDEO_GALLERY_GET = 202 ;//从图库获取视频
		public static final int VIDEO_FILE_GET = 203 ;//从文件管理器获取视频
		public static final int FILE_FILE_GET = 301;//从文件管理器获取文件
	}

	public static final String AUDIO = "audio/*";
	public static final String VIDEO = "video/*";
	public static final String IMAGE = "image/*";
	public static final String ALL = "*/*";


	private String pathRes = "";
	private Activity activity ;
	private OnResourcesGetListener onResourcesGetListener;
	
	public ResourcesGetTools(Activity activity)
	{
		this.activity = activity ;
	}
	
	/**
	 * 启动系统相机获取图片
	 */
	public void getImgByCamera()
	{
		pathRes = ConstantUtil.getImageCachePath()+UUID.randomUUID().toString()+".jpg";
		File file =new File(pathRes);
	    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra("output", Uri.fromFile(file));
        intent.putExtra("outputFormat", "JPEG");
        intent.putExtra("return-data", false);
        activity.startActivityForResult(intent, RequestCode.IMG_CAMERA_GET);
	}
	/**
	 * 从系统图库获取图片
	 */
	public void getImgByGallery()
	{
		pathRes = ConstantUtil.getImageCachePath()+UUID.randomUUID().toString()+".jpg";
		File file =new File(pathRes);
		Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		intent.putExtra("output", Uri.fromFile(file));
		intent.setType("image/*");  
		intent.putExtra("outputFormat", "JPEG");
		intent.putExtra("return-data", true);
		activity.startActivityForResult(intent, RequestCode.IMG_GALLERY_GET);
	}

	/**
	 * 启动系统录像机获取视频
	 */
	public void getVideoByCamera()
	{
		pathRes = ConstantUtil.getVideoCachePath()+UUID.randomUUID().toString()+".mp4";
		File file = new File(pathRes);
		Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
		//intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);//清晰图，0最低清，1最高清，默认中间值
		activity.startActivityForResult(intent, RequestCode.VIDEO_CAMERA_GET);
	}

	/**
	 * 通过系统文件管理器获取视频文件
	 */
	public void getVideoByFile()
	{
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("video/*");
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		activity.startActivityForResult(intent, RequestCode.VIDEO_FILE_GET);
	}

	/**
	 * 从文件管理器获取文件
	 */
	public void getFileByFile()
	{
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("*/*");
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		activity.startActivityForResult(intent, RequestCode.FILE_FILE_GET);
	}

	/**
	 * 从文件管理器获取指定类型的文件
	 * @param fileType 文件类型例如{@link #AUDIO },{@link #VIDEO},{@link #IMAGE},{@link #ALL}等等
	 */
	public void getFileByFile(String fileType)
	{
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType(fileType);
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		activity.startActivityForResult(intent, RequestCode.FILE_FILE_GET);
	}

	/**
	 * 从相机启动剪切图片
	 */
	public void startCutFromCamera()
	{
		File file = new File(pathRes);
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(Uri.fromFile(file), "image/*");
		intent.putExtra("output", Uri.fromFile(file));
		intent.putExtra("outputFormat", "JPEG");
		intent.putExtra("crop", "true");
		intent.putExtra("return-data", false);
		activity.startActivityForResult(intent, RequestCode.IMG_CAMERA_CUT);
	}
	/**
	 * 从图库启动剪切图片
	 */
	public void startCutFromGallery(Uri outUri)
	{
		File file = new File(pathRes);
		startCutFromGallery(Uri.fromFile(file),outUri);
	}

    public void startCutFromGallery(Uri inUri,Uri outUri)
    {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(inUri, "image/*");
        intent.putExtra("output", outUri);
        intent.putExtra("outputFormat", "JPEG");
        intent.putExtra("crop", "true");
        intent.putExtra("return-data", false);
		intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
		intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION );
		List<ResolveInfo> resInfoList = queryActivityByIntent(intent);
		if (resInfoList.size() == 0) {
			return;
		}
		for (ResolveInfo resolveInfo : resInfoList) {
			String packageName = resolveInfo.activityInfo.packageName;
			activity.grantUriPermission(packageName, outUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
		}
		activity.startActivityForResult(intent, RequestCode.IMG_GALLERY_CUT);
    }

	private List<ResolveInfo> queryActivityByIntent(Intent intent){
		return activity.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if(resultCode==Activity.RESULT_OK)
		{
			Uri uri = data.getData();
			switch(requestCode)
			{
			case RequestCode.IMG_CAMERA_GET:
				startCutFromCamera();
				break;
			case RequestCode.IMG_GALLERY_GET:
				if(onResourcesGetListener!=null)
				{
					onResourcesGetListener.onSuccess(requestCode, uri, data);
				}
				break;
			case RequestCode.IMG_FILE_GET:
				
				break;
			case RequestCode.IMG_CAMERA_CUT:
				if(onResourcesGetListener!=null)
				{
					onResourcesGetListener.onSuccess(requestCode, pathRes, data);
				}
				break;
			case RequestCode.IMG_GALLERY_CUT:
				if(onResourcesGetListener!=null)
				{
					onResourcesGetListener.onSuccess(requestCode, pathRes, data);
				}
				break;
			case RequestCode.VIDEO_CAMERA_GET:
				if(onResourcesGetListener!=null)
				{
					onResourcesGetListener.onSuccess(requestCode, pathRes, data);
				}
				break;
			case RequestCode.VIDEO_GALLERY_GET:
				
				break;
			case RequestCode.VIDEO_FILE_GET:
				if(onResourcesGetListener!=null)
				{

					onResourcesGetListener.onSuccess(requestCode,uri, data);
				}
				break;
			case RequestCode.FILE_FILE_GET:
				if(onResourcesGetListener!=null)
				{
					onResourcesGetListener.onSuccess(requestCode, uri, data);
				}
				break;
			}

		}
		else if(resultCode==Activity.RESULT_FIRST_USER)
		{
			if(onResourcesGetListener!=null)
			{
				onResourcesGetListener.onFail(requestCode,resultCode);
			}
		}
		else if(resultCode==Activity.RESULT_CANCELED)
		{
			if(onResourcesGetListener!=null)
			{
				onResourcesGetListener.onFail(requestCode,resultCode);
			}
		}
	}

	/**
	 * 通过Uri获取路径(并不能获取真正的路径，建议高版本API使用InputStream方法来获取文件
	 * @param uri uri
	 * @return 路径
	 */
	@Nullable
	private static String getPath(Uri uri){
		if(uri==null) {
			return null;
		}
		return uri.getPath();
	}

	@Nullable
	public static InputStream getInputStream(Uri uri)
	{
		InputStream inputStream = null;
		try {
			inputStream = BaseApplication.getContext().getContentResolver().openInputStream(uri);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return inputStream;
	}

	/**
	 * 获取资源返回监听器
	 * @author TJbaobao
	 * @time 2017年5月16日
	 */
	public interface OnResourcesGetListener
	{
		default void onSuccess(int requestCode, String path, Intent data){}

		default void onSuccess(int requestCode,Uri uri,Intent data)
		{
			onSuccess(requestCode,getInputStream(uri),data);
			onSuccess(requestCode, getPath(uri),data);
		}

		void onSuccess(int requestCode,InputStream inputStream, Intent data);

		void onFail(int requestCode, int resultCode);
	}

	public OnResourcesGetListener getOnResourcesGetListener() {
		return onResourcesGetListener;
	}

	public void setOnResourcesGetListener(OnResourcesGetListener onResourcesGetListener) {
		this.onResourcesGetListener = onResourcesGetListener;
	}

	public String getPathRes() {
		return pathRes;
	}
}
